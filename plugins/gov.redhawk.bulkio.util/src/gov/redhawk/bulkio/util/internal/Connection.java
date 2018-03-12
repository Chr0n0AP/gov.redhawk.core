/*******************************************************************************
 * This file is protected by Copyright.
 * Please refer to the COPYRIGHT file distributed with this source distribution.
 *
 * This file is part of REDHAWK IDE.
 *
 * All rights reserved.  This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package gov.redhawk.bulkio.util.internal;

import gov.redhawk.bulkio.util.AbstractUberBulkIOPort;
import gov.redhawk.bulkio.util.BulkIOType;
import gov.redhawk.bulkio.util.BulkIOUtilActivator;
import gov.redhawk.bulkio.util.IPortFactory;
import gov.redhawk.bulkio.util.PortReference;
import gov.redhawk.sca.util.Debug;
import gov.redhawk.sca.util.OrbSession;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang.RandomStringUtils;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import BULKIO.BitSequence;
import BULKIO.PrecisionUTCTime;
import BULKIO.StreamSRI;
import BULKIO.dataBitOperations;
import BULKIO.dataCharOperations;
import BULKIO.dataDoubleOperations;
import BULKIO.dataFloatOperations;
import BULKIO.dataLongLongOperations;
import BULKIO.dataLongOperations;
import BULKIO.dataOctetOperations;
import BULKIO.dataShortOperations;
import BULKIO.dataUlongLongOperations;
import BULKIO.dataUlongOperations;
import BULKIO.dataUshortOperations;
import BULKIO.updateSRIOperations;

/**
 * @since 1.1
 */
public class Connection extends AbstractUberBulkIOPort {
	private static final Debug DEBUG_PUSHPACKET = new Debug(BulkIOUtilActivator.PLUGIN_ID, Connection.class.getSimpleName());
	private static final String FORMAT_STR = "MMdd'_'HHmmss";

	private OrbSession orbSession = OrbSession.createSession();
	private final String ior;
	private final String connectionId;
	private final boolean generatedID;
	private PortReference ref;
	private final List<updateSRIOperations> children = Collections.synchronizedList(new ArrayList<updateSRIOperations>());
	private LinkedBlockingQueue<StreamSRI> sriChanges = new LinkedBlockingQueue<>();
	private final Thread workerThread;

	private final Runnable handleStreamRunnable = new Runnable() {

		@Override
		public void run() {
			while (!Thread.currentThread().isInterrupted() || disposed) {
				final StreamSRI sri;
				try {
					sri = sriChanges.take();
				} catch (InterruptedException e) {
					break;
				}
				for (final Object child : getSafeChildren()) {
					SafeRunner.run(new ISafeRunnable() {

						@Override
						public void handleException(Throwable exception) {
						}

						@Override
						public void run() throws Exception {
							((updateSRIOperations) child).pushSRI(sri);
						}
					});

				}
			}
		}

	};

	private boolean disposed;
	private boolean warnedPushPacketError = false;

	public Connection(String ior, BulkIOType type, String connectionId) {
		super(type);
		Assert.isNotNull(ior);
		Assert.isNotNull(type);
		this.ior = ior;
		if (connectionId == null || connectionId.isEmpty()) {
			this.connectionId = Connection.createConnectionID();
			this.generatedID = true;
		} else {
			this.connectionId = connectionId;
			this.generatedID = false;
		}
		workerThread = new Thread(handleStreamRunnable, Connection.class.getName() + ":" + connectionId);
		workerThread.setDaemon(true);
		workerThread.start();
	}

	public boolean isGeneratedID() {
		return generatedID;
	}

	public void connectPort() throws CoreException {
		if (disposed) {
			// Connection is disposed, do nothing
			return;
		}
		IPortFactory factory = BulkIOUtilActivator.getDefault().getPortFactory();
		ref = factory.connect(connectionId, ior, getBulkIOType(), this);
	}

	// SRI has changed for specified streamID
	@Override
	protected void handleStreamSRIChanged(String streamID, StreamSRI oldSri, final StreamSRI newSri) {
		if (disposed) {
			return;
		}
		if (!sriChanges.offer(newSri)) {
			warnedPushPacketError = true;
			log(IStatus.ERROR, "This will only be logged once per Port Connection, handleStreamSRIChanged queue full, event dropped!", null);
		}
	}

	public String getConnectionId() {
		return connectionId;
	}

	private static String createConnectionID() {
		SimpleDateFormat formater = new SimpleDateFormat(FORMAT_STR);
		String user = System.getProperty("user.name", "user");
		String randomAlphanumeric = RandomStringUtils.random(3, true, true); // 62^3 = 238,328
		String dateTime = formater.format(Calendar.getInstance().getTime());
		return "IDE_" + user + "_" + randomAlphanumeric + "_" + dateTime;
	}

	public synchronized void dispose() {
		if (disposed) {
			return;
		}
		disposed = true;
		if (ref != null) {
			ref.dispose();
			ref = null;
		}
		children.clear();
		workerThread.interrupt();
		sriChanges.clear();

		orbSession.dispose();
	}

	public void registerDataReceiver(final updateSRIOperations receiver) {
		getBulkIOType().getPortType().cast(receiver);

		children.add(receiver);

		if (activeSRIs().length > 0) {
			Job job = new Job("pushSRI to new receiver") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					StreamSRI[] currentSri = activeSRIs();
					for (StreamSRI sri : currentSri) {
						receiver.pushSRI(sri);
					}
					return Status.OK_STATUS;
				}
			};
			job.setSystem(true); // hide from progress monitor
			job.setUser(false);
			job.schedule(0);
		}
	}

	/**
	 * 
	 * Disconnect an internal connection
	 */
	public void deregisterDataReceiver(updateSRIOperations receiver) {
		children.remove(receiver);
	}

	/**
	 * @return True if there are no internal connections
	 */
	public boolean isEmpty() {
		return children.isEmpty();
	}

	@Override
	public void pushPacket(BitSequence data, PrecisionUTCTime time, boolean eos, String streamID) {
		if (!super.pushPacket(data.bits, time, eos, streamID)) {
			return;
		}
		for (updateSRIOperations child : getSafeChildren()) {
			try {
				((dataBitOperations) child).pushPacket(data, time, eos, streamID);
			} catch (Exception e) { // SUPPRESS CHECKSTYLE IllegalCatch
				logPushException(data, child, e);
			}
		}
	}

	@Override
	public void pushPacket(char[] data, PrecisionUTCTime time, boolean eos, String streamID) {
		if (!super.pushPacket(data.length, time, eos, streamID)) {
			return;
		}
		for (updateSRIOperations child : getSafeChildren()) {
			try {
				((dataCharOperations) child).pushPacket(data, time, eos, streamID);
			} catch (Exception e) { // SUPPRESS CHECKSTYLE IllegalCatch
				logPushException(data, child, e);
			}
		}
	}

	@Override
	public void pushPacket(double[] data, PrecisionUTCTime time, boolean eos, String streamID) {
		if (!super.pushPacket(data.length, time, eos, streamID)) {
			return;
		}
		for (updateSRIOperations child : getSafeChildren()) {
			try {
				((dataDoubleOperations) child).pushPacket(data, time, eos, streamID);
			} catch (Exception e) { // SUPPRESS CHECKSTYLE IllegalCatch
				logPushException(data, child, e);
			}
		}
	}

	@Override
	public void pushPacket(float[] data, PrecisionUTCTime time, boolean eos, String streamID) {
		if (!super.pushPacket(data.length, time, eos, streamID)) {
			return;
		}
		for (updateSRIOperations child : getSafeChildren()) {
			try {
				((dataFloatOperations) child).pushPacket(data, time, eos, streamID);
			} catch (Exception e) { // SUPPRESS CHECKSTYLE IllegalCatch
				logPushException(data, child, e);
			}
		}
	}

	@Override
	public void pushPacket(byte[] data, PrecisionUTCTime time, boolean eos, String streamID) {
		if (!super.pushPacket(data.length, time, eos, streamID)) {
			return;
		}
		for (updateSRIOperations child : getSafeChildren()) {
			try {
				((dataOctetOperations) child).pushPacket(data, time, eos, streamID);
			} catch (Exception e) { // SUPPRESS CHECKSTYLE IllegalCatch
				logPushException(data, child, e);
			}
		}
	}

	@Override
	public void pushPacket(final short[] data, final PrecisionUTCTime time, final boolean eos, final String streamID) {
		if (!super.pushPacket(data.length, time, eos, streamID)) {
			return;
		}
		for (final updateSRIOperations child : getSafeChildren()) {
			try {
				if (getBulkIOType().isUnsigned()) {
					((dataUshortOperations) child).pushPacket(data, time, eos, streamID);
				} else {
					((dataShortOperations) child).pushPacket(data, time, eos, streamID);
				}
			} catch (Exception e) { // SUPPRESS CHECKSTYLE IllegalCatch
				logPushException(data, child, e);
			}
		}
	}

	@Override
	public void pushPacket(int[] data, PrecisionUTCTime time, boolean eos, String streamID) {
		if (!super.pushPacket(data.length, time, eos, streamID)) {
			return;
		}
		for (updateSRIOperations child : getSafeChildren()) {
			try {
				if (getBulkIOType().isUnsigned()) {
					((dataUlongOperations) child).pushPacket(data, time, eos, streamID);
				} else {
					((dataLongOperations) child).pushPacket(data, time, eos, streamID);
				}
			} catch (Exception e) { // SUPPRESS CHECKSTYLE IllegalCatch
				logPushException(data, child, e);
			}
		}
	}

	@Override
	public void pushPacket(long[] data, PrecisionUTCTime time, boolean eos, String streamID) {
		if (!super.pushPacket(data.length, time, eos, streamID)) {
			return;
		}
		for (updateSRIOperations child : getSafeChildren()) {
			try {
				if (getBulkIOType().isUnsigned()) {
					((dataUlongLongOperations) child).pushPacket(data, time, eos, streamID);
				} else {
					((dataLongLongOperations) child).pushPacket(data, time, eos, streamID);
				}
			} catch (Exception e) { // SUPPRESS CHECKSTYLE IllegalCatch
				logPushException(data, child, e);
			}
		}
	}

	private updateSRIOperations[] getSafeChildren() {
		synchronized (children) {
			return children.toArray(new updateSRIOperations[children.size()]);
		}
	}

	private void logPushException(Object data, updateSRIOperations child, Exception e) {
		if (!warnedPushPacketError) {
			warnedPushPacketError = true;
			log(IStatus.ERROR, "This will only be logged once per Port Connection, got exception calling pushPacket("
				+ data.getClass().getCanonicalName() + "...) on " + child, e);
		}
		if (Connection.DEBUG_PUSHPACKET.enabled) {
			Connection.DEBUG_PUSHPACKET.message("Got exception calling pushPacket({0}...) on {1}. Exception={2}. IOR={3}",
				data.getClass().getCanonicalName(), child, e, ior);
		}
	}

	private void log(int severity, String msg, Throwable t) {
		BulkIOUtilActivator.getDefault().getLog().log(new Status(severity, BulkIOUtilActivator.PLUGIN_ID, msg, t));
	}

	public String getIor() {
		return ior;
	}
}
