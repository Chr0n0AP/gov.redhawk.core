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
package gov.redhawk.sca.internal.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import CF.ResourceOperations;
import gov.redhawk.model.sca.util.StartJob;
import gov.redhawk.sca.util.PluginUtil;

public class StartResouceOpHandler extends AbstractHandler {

	public Object execute(final ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getActiveMenuSelection(event);
		if (selection == null) {
			selection = HandlerUtil.getCurrentSelection(event);
		}
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection ss = (IStructuredSelection) selection;
			for (final Object obj : ss.toArray()) {
				start(obj);
			}
		} else {
			final IEditorPart editor = HandlerUtil.getActiveEditor(event);
			start(editor);
		}
		return null;
	}

	private void start(final Object obj) {
		final ResourceOperations resource = PluginUtil.adapt(ResourceOperations.class, obj);
		if (resource != null) {
			final Job job = new StartJob(resource.identifier(), resource);
			job.setUser(true);
			job.schedule();
		}
	}
}
