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
package gov.redhawk.frontend.ui.internal;

import java.util.concurrent.Callable;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import CF.DataType;
import CF.DevicePackage.InvalidCapacity;
import CF.DevicePackage.InvalidState;
import gov.redhawk.frontend.ListenerAllocation;
import gov.redhawk.frontend.TunerContainer;
import gov.redhawk.frontend.TunerStatus;
import gov.redhawk.frontend.ui.FrontEndUIActivator;
import gov.redhawk.frontend.ui.internal.section.FrontendSection;
import gov.redhawk.frontend.util.TunerProperties.ListenerAllocationProperty;
import gov.redhawk.frontend.util.TunerProperties.TunerAllocationProperty;
import gov.redhawk.frontend.util.TunerUtils;
import gov.redhawk.model.sca.RefreshDepth;
import gov.redhawk.model.sca.ScaDevice;
import gov.redhawk.model.sca.commands.ScaModelCommand;
import mil.jpeojtrs.sca.prf.Struct;
import mil.jpeojtrs.sca.util.CorbaUtils;
import mil.jpeojtrs.sca.util.ScaEcoreUtils;

public class DeallocateHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getActiveMenuSelection(event);
		if (selection == null) {
			selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
		}

		if (selection == null) {
			return null;
		}
		boolean removeSelection = true;
		Object obj = selection.getFirstElement();
		if (obj instanceof TunerStatus) {
			TunerStatus tuner = (TunerStatus) obj;
			if (tuner.getTunerContainer() == null) {
				// already deallocated, probably still in a pinned properties view
				return null;
			}
			boolean proceed = true;
			if (tuner.getAllocationID() != null && !tuner.getAllocationID().isEmpty() 
					&& tuner.getAllocationID().contains(",")) {
				if (confirmDeallocate(tuner, event) == 0) {
					proceed = false;
					removeSelection = false;
				}
			}
			if (proceed) {
				deallocateTuner(tuner);
			}
		}
		if (obj instanceof TunerContainer) {
			TunerContainer container = (TunerContainer) obj;
			if (!confirmDeallocate(container, event)) {
				return null;
			}
			for (TunerStatus tuner : container.getTunerStatus().toArray(new TunerStatus[0])) {
				deallocateTuner(tuner);
			}
			removeSelection = false;
		}
		if (obj instanceof ScaDevice) {
			ScaDevice< ? > device = (ScaDevice< ? >) obj;
			TunerContainer container = TunerUtils.INSTANCE.getTunerContainer(device);
			if (!confirmDeallocate(container, event)) {
				return null;
			}
			for (TunerStatus tuner : container.getTunerStatus().toArray(new TunerStatus[0])) {
				deallocateTuner(tuner);
			}
		}
		if (obj instanceof ListenerAllocation) {
			final ListenerAllocation listener = (ListenerAllocation) obj;
			if (listener.getTunerStatus() == null) {
				// already deallocated, probably still in a pinned properties view
				return null;
			}
			final ScaDevice< ? > device = ScaEcoreUtils.getEContainerOfType(listener, ScaDevice.class);
			if (device == null) {
				return null;
			}

			final DataType[] props = new DataType[1];
			Struct allocProp = ListenerAllocationProperty.INSTANCE.createDeallocationStruct(listener);
			props[0] = new DataType(allocProp.getId(), allocProp.toAny());

			Job job = new Job("Deallocate FEI control") {

				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try {
						monitor.beginTask("Deallocating", IProgressMonitor.UNKNOWN);
						CorbaUtils.invoke(new Callable<Object>() {

							@Override
							public Object call() throws Exception {
								try {
									device.deallocateCapacity(props);
								} catch (InvalidCapacity e) {
									throw new CoreException(new Status(IStatus.ERROR, FrontEndUIActivator.PLUGIN_ID,
										"Invalid Capacity in control deallocation: " + e.msg, e));
								} catch (InvalidState e) {
									throw new CoreException(new Status(IStatus.ERROR, FrontEndUIActivator.PLUGIN_ID, "Invalid State in control deallocation: "
										+ e.msg, e));
								}
								return null;
							}

						}, monitor);

						device.refresh(null, RefreshDepth.SELF);
					} catch (InterruptedException e) {
						return new Status(IStatus.ERROR, FrontEndUIActivator.PLUGIN_ID, "Interrupted Exception during control deallocation", e);
					} catch (CoreException e) {
						return new Status(e.getStatus().getSeverity(), FrontEndUIActivator.PLUGIN_ID, "Failed to deallocate", e);
					}

					final TunerStatus tunerStatus = listener.getTunerStatus();
					if (tunerStatus != null) {
						ScaModelCommand.execute(tunerStatus, new ScaModelCommand() {
							@Override
							public void execute() {
								tunerStatus.getListenerAllocations().remove(listener);
							}
						});
					}
					return Status.OK_STATUS;
				}

			};
			job.setUser(true);
			job.schedule();
		}
		// If called from toolbar button, we must unset the property page's selection to clear it
		Object section = ((IEvaluationContext) event.getApplicationContext()).getVariable("gov.redhawk.frontend.propertySection");
		if (section instanceof FrontendSection && removeSelection) {
			FrontendSection feSection = (FrontendSection) section;
			feSection.unsetPageSelection();
		}
		return null;
	}

	private int confirmDeallocate(TunerStatus tuner, ExecutionEvent event) {
		MessageDialog warning = new MessageDialog(HandlerUtil.getActiveWorkbenchWindow(event).getShell(), "Deallocation Warning", null,
			"Some selected tuners have listeners.  Deallocating them will also deallocate all of their listeners.  Deallocate them anyway?", 
			MessageDialog.WARNING, new String[] { "Cancel", "Yes" }, 0);
		return warning.open();
	}
	
	private boolean confirmDeallocate(TunerContainer container, ExecutionEvent event) {
		for (TunerStatus tuner : container.getTunerStatus()) {
			if (tuner.getAllocationID() != null && !tuner.getAllocationID().isEmpty() 
					&& tuner.getAllocationID().contains(",")) {
				int response = confirmDeallocate(tuner, event);
				if (response == 0) {
					return false;
				}
				return true;
			}
		}
		return true;
	}
	
	private boolean deallocateTuner(TunerStatus tuner) {
		final ScaDevice< ? > device = ScaEcoreUtils.getEContainerOfType(tuner, ScaDevice.class);
		final DataType[] props = new DataType[1];
		Struct allocProp = TunerAllocationProperty.INSTANCE.createDeallocationStruct(tuner);
		props[0] = new DataType(allocProp.getId(), allocProp.toAny());

		Job job = new Job("Deallocate FEI control") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					monitor.beginTask("Deallocating", IProgressMonitor.UNKNOWN);
					CorbaUtils.invoke(new Callable<Object>() {

						@Override
						public Object call() throws Exception {
							try {
								device.deallocateCapacity(props);
								return null;
							} catch (InvalidCapacity e) {
								throw new CoreException(new Status(IStatus.ERROR, FrontEndUIActivator.PLUGIN_ID, "Invalid Capacity in control deallocation: "
									+ e.msg, e));
							} catch (InvalidState e) {
								throw new CoreException(new Status(IStatus.ERROR, FrontEndUIActivator.PLUGIN_ID, "Invalid State in control deallocation: "
									+ e.msg, e));
							}
						}

					}, monitor);
					device.refresh(monitor, RefreshDepth.SELF);
					return Status.OK_STATUS;
				} catch (InterruptedException e) {
					return new Status(IStatus.ERROR, FrontEndUIActivator.PLUGIN_ID, "Interrupted Exception during control deallocation", e);
				} catch (CoreException e) {
					return new Status(e.getStatus().getSeverity(), FrontEndUIActivator.PLUGIN_ID, "Failed to deallocate", e);
				}
			}

		};
		job.setUser(true);
		job.schedule();

		return true;
	}
}
