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
package gov.redhawk.ui.editor;

import gov.redhawk.ui.parts.StructuredViewerPart;
import gov.redhawk.ui.parts.TreePart;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

public abstract class TreeSection extends StructuredViewerSection {

	protected boolean fHandleDefaultButton = true;

	class PartAdapter extends TreePart {
		public PartAdapter(String[] buttonLabels) {
			super(buttonLabels);
		}

		public void selectionChanged(IStructuredSelection selection) {
			getManagedForm().fireSelectionChanged(TreeSection.this, selection);
			TreeSection.this.selectionChanged(selection);
		}

		public void handleDoubleClick(IStructuredSelection selection) {
			TreeSection.this.handleDoubleClick(selection);
		}

		public void buttonSelected(Button button, int index) {
			TreeSection.this.buttonSelected(index);
			if (fHandleDefaultButton) {
				button.getShell().setDefaultButton(null);
			}
		}

		protected void createButtons(Composite parent, FormToolkit toolkit) {
			super.createButtons(parent, toolkit);
			if (fButtonContainer == null) {
				return;
			}
			enableButtons();
			if (parent.getData("filtered") != null) { //$NON-NLS-1$
				GridLayout layout = (GridLayout) fButtonContainer.getLayout();
				layout.marginHeight = 28;
			}
		}

		protected TreeViewer createTreeViewer(final Composite parent, final int style) {
			final TreeViewer viewer = TreeSection.this.createTreeViewer(parent, style);
			getPage().getEditor().createContextMenuFor(viewer);
			return viewer;
		}

	}

	/**
	 * Constructor for TableSection.
	 * @param formPage
	 */
	public TreeSection(ScaFormPage formPage, Composite parent, int style, String[] buttonLabels) {
		super(formPage, parent, style, buttonLabels);
	}

	protected StructuredViewerPart createViewerPart(String[] buttonLabels) {
		return new PartAdapter(buttonLabels);
	}

	protected TreePart getTreePart() {
		return (TreePart) fViewerPart;
	}

	protected TreeViewer createTreeViewer(final Composite parent, final int style) {
		final TreeViewer viewer = new TreeViewer(parent, style);
		return viewer;
	}

	protected void selectionChanged(IStructuredSelection selection) {
	}

	/**
	 * Expands or collapsed selected node according to its current state
	 * @param selection
	 */
	protected void handleDoubleClick(IStructuredSelection selection) {
		TreeViewer viewer = (TreeViewer) fViewerPart.getViewer();
		boolean expandedState = viewer.getExpandedState(selection.getFirstElement());
		viewer.setExpandedState(selection.getFirstElement(), !expandedState);
	}

	protected void enableButtons() {
	}
}
