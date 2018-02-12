/**
 * This file is protected by Copyright.
 * Please refer to the COPYRIGHT file distributed with this source distribution.
 *
 * This file is part of REDHAWK IDE.
 *
 * All rights reserved.  This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 *
 */
package gov.redhawk.bulkio.ui.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.progress.WorkbenchJob;

import BULKIO.PrecisionUTCTime;
import BULKIO.StreamSRI;
import gov.redhawk.bulkio.ui.views.SriDataView;
import gov.redhawk.bulkio.util.AbstractUberBulkIOPort;
import gov.redhawk.bulkio.util.BulkIOType;
import gov.redhawk.bulkio.util.BulkIOUtilActivator;
import gov.redhawk.model.sca.ScaUsesPort;

public class SriDataViewReceiver extends AbstractUberBulkIOPort {
	private ScaUsesPort port;
	private TreeViewer viewer;
	private SriDataView sriDataView;
	private String activeSriStreamID;
	private String connectionId;
	private Map<String, SriWrapper> modelStreamMap = new HashMap<String, SriWrapper>(); // contains real time stream data
	private Map<String, SriWrapper> viewStreamMap = modelStreamMap; // contains stream data available to the view UI
	private Object[] expandedItems;
	private boolean inputSet;
	private final Job refreshView;

	public SriDataViewReceiver(BulkIOType type, TreeViewer treeViewer, SriDataView view) {
		super(type);
		this.viewer = treeViewer;
		this.sriDataView = view;
		this.refreshView = new WorkbenchJob(treeViewer.getControl().getDisplay(), "Updating SRI view...") {

			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				if (viewer != null & !viewer.getControl().isDisposed()) {

					// Capture expanded state to persist after setInput
					setExpandedItems(viewer.getExpandedElements());
					viewer.getControl().setRedraw(false);

					// Send input to content provider
					if (!inputSet) {
						viewer.setInput(viewStreamMap);
						inputSet = true;
					} else {
						viewer.refresh();
					}

					// Rebuild expanded state
					viewer.setExpandedElements(getExpandedItems());
					viewer.getControl().setRedraw(true);

					// Bold tabs of views with modified content, rebuild menus and toolbars
					sriDataView.contentChanged();
				}
				return Status.OK_STATUS;
			}
		};
		refreshView.setUser(false);
		refreshView.setSystem(true);
	}

	public void setPort(@NonNull ScaUsesPort port) {
		setBulkIOType(BulkIOType.getType(port.getRepid()));
		this.port = port;
	}

	@NonNull
	public Map<String, SriWrapper> getStreamMap() {
		return viewStreamMap;
	}

	public void setActiveStreamID(@NonNull String newStreamID) {
		activeSriStreamID = newStreamID;
		refreshView.schedule();
	}

	@Override
	protected void handleStreamSRIChanged(@NonNull String streamID, @Nullable final StreamSRI oldSri, @NonNull final StreamSRI newSri) {
		super.handleStreamSRIChanged(streamID, oldSri, newSri);

		// store the incoming SRI data in the modelStreamMap
		if (modelStreamMap.containsKey(streamID)) {
			SriWrapper stream = modelStreamMap.get(streamID);
			stream.setSri(newSri);
			long timeMillis = System.currentTimeMillis();
			stream.setPushSriDate(new PrecisionUTCTime(BULKIO.TCM_CPU.value, BULKIO.TCS_VALID.value, 0.0, timeMillis / 1000, timeMillis % 1000 / 1000.0));
		} else {
			long timeMillis = System.currentTimeMillis();
			SriWrapper sriWrapper = new SriWrapper(newSri, new PrecisionUTCTime(BULKIO.TCM_CPU.value, BULKIO.TCS_VALID.value, 0.0, timeMillis / 1000, timeMillis % 1000 / 1000.0));
			modelStreamMap.put(streamID, sriWrapper);
		}
		if (activeSriStreamID == null) {
			activeSriStreamID = streamID;
		}

		if (activeSriStreamID.equals(streamID)) {
			refreshView.schedule(100);
		}
	}

	// updates the viewStreamMap from modelStreamMap when the pause button is selected
	public void updateViewStreamMap() {
		if (sriDataView.isPaused()) {
			viewStreamMap = deepcopy(modelStreamMap);
			inputSet = false;
		} else {
			viewStreamMap = modelStreamMap;
			inputSet = false;
		}
		refreshView.schedule();
	}

	private Map<String, SriWrapper> deepcopy(Map<String, SriWrapper> modelStreamMap) {
		Map<String, SriWrapper> copy = new HashMap<String, SriWrapper>();
		for (Entry<String, SriWrapper> entry : modelStreamMap.entrySet()) {
			copy.put(entry.getKey(), new SriWrapper(entry.getValue()));
		}
		return copy;
	}

	public void setConnectionID(String connectionId) {
		this.connectionId = connectionId;
	}

	public void connect() throws CoreException {
		if (port == null) {
			throw new IllegalStateException("Port must not be null");
		}
		if (connectionId == null) {
			BulkIOUtilActivator.getBulkIOPortConnectionManager().connect(port.getIor(), getBulkIOType(), this);
		} else {
			BulkIOUtilActivator.getBulkIOPortConnectionManager().connect(port.getIor(), getBulkIOType(), this, connectionId);
		}
	}

	public void disconnect() {
		final SriDataViewReceiver receiver = this;
		Job disconnectJob = new Job("Disconnecting SRI Receiver...") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				BulkIOUtilActivator.getBulkIOPortConnectionManager().disconnect(port.getIor(), getBulkIOType(), receiver, connectionId);
				return Status.OK_STATUS;
			}
		};
		disconnectJob.setUser(false);
		disconnectJob.setSystem(true);
		disconnectJob.schedule();
	}

	private void setPrecisionTime(@NonNull PrecisionUTCTime time, String streamID) {
		// sets precision time to latest pushPacket
		SriWrapper stream = modelStreamMap.get(streamID);
		if (stream == null) {
			return;
		}
		if (time != null) {
			// Assign to SriWrapper object
			modelStreamMap.get(streamID).setPrecisionTime(time);

			if (!sriDataView.isPaused()) {
				viewStreamMap.get(streamID).setPrecisionTime(time);
				refreshView.schedule(250);
			}
		}
	}

	private void checkForEOS(boolean eos, String streamID) {
		SriWrapper stream = modelStreamMap.get(streamID);
		if (stream == null || !eos) {
			return;
		} else {
			stream.setEOS(true);
			sriDataView.setTerminatedStreams(true);
			refreshView.schedule(100);
		}
	}

	public String getActiveStreamID() {
		return activeSriStreamID;
	}

	@Override
	public void pushPacket(short[] data, PrecisionUTCTime time, boolean eos, String streamID) {
		setPrecisionTime(time, streamID);
		checkForEOS(eos, streamID);
	}

	@Override
	public void pushPacket(char[] data, PrecisionUTCTime time, boolean eos, String streamID) {
		setPrecisionTime(time, streamID);
		checkForEOS(eos, streamID);
	}

	@Override
	public void pushPacket(double[] data, PrecisionUTCTime time, boolean eos, String streamID) {
		setPrecisionTime(time, streamID);
		checkForEOS(eos, streamID);
	}

	@Override
	public void pushPacket(float[] data, PrecisionUTCTime time, boolean eos, String streamID) {
		setPrecisionTime(time, streamID);
		checkForEOS(eos, streamID);
	}

	@Override
	public void pushPacket(long[] data, PrecisionUTCTime time, boolean eos, String streamID) {
		setPrecisionTime(time, streamID);
		checkForEOS(eos, streamID);
	}

	@Override
	public void pushPacket(int[] data, PrecisionUTCTime time, boolean eos, String streamID) {
		setPrecisionTime(time, streamID);
		checkForEOS(eos, streamID);
	}

	@Override
	public void pushPacket(byte[] data, PrecisionUTCTime time, boolean eos, String streamID) {
		setPrecisionTime(time, streamID);
		checkForEOS(eos, streamID);
	}

	public Object[] getExpandedItems() {
		return expandedItems;
	}

	public void setExpandedItems(Object[] expandedItems) {
		this.expandedItems = expandedItems;
	}
}
