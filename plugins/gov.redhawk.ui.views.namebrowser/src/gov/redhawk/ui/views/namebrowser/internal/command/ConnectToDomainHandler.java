/**
 * This file is protected by Copyright.
 * Please refer to the COPYRIGHT file distributed with this source distribution.
 *
 * This file is part of REDHAWK IDE.
 *
 * All rights reserved.  This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 */
package gov.redhawk.ui.views.namebrowser.internal.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import gov.redhawk.sca.ui.DomainConnectionUtil;
import gov.redhawk.ui.views.namebrowser.view.BindingNode;

public class ConnectToDomainHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection sel = (IStructuredSelection) selection;
			for (final Object selected : sel.toList()) {
				if (selected instanceof BindingNode) {
					final BindingNode b = (BindingNode) selected;
					Shell shell = HandlerUtil.getActiveShell(event);
					DomainConnectionUtil.showDialog(shell.getDisplay(), b.getHost(), b.getPath().split("/")[1], null);
				}
			}
		}
		return null;
	}

}
