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
package gov.redhawk.prf.internal.ui.editor;

import gov.redhawk.prf.internal.ui.editor.detailspart.Property;
import gov.redhawk.prf.internal.ui.handlers.PropertyHandler;
import gov.redhawk.prf.ui.editor.page.PropertiesFormPage;
import gov.redhawk.prf.ui.wizard.BrowsePropertiesWizard;
import gov.redhawk.sca.ScaPlugin;
import gov.redhawk.sca.ui.parts.FormFilteredTree;
import gov.redhawk.ui.actions.SortAction;
import gov.redhawk.ui.editor.TreeSection;
import gov.redhawk.ui.parts.TreePart;
import gov.redhawk.ui.parts.UnwrappingLabelProvider;
import gov.redhawk.ui.util.SCAEditorUtil;
import gov.redhawk.ui.util.ViewerUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import mil.jpeojtrs.sca.prf.Properties;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

public class PropertiesSection extends TreeSection implements IPropertyChangeListener {

	private static final int BUTTON_REMOVE = 5;
	private static final int BUTTON_BROWSE = 4;
	private static final int BUTTON_ADD_STRUCT_SEQUENCE = 3;
	private static final int BUTTON_ADD_STRUCT = 2;
	private static final int BUTTON_ADD_SIMPLE_SEQUENCE = 1;
	private static final int BUTTON_ADD_SIMPLE = 0;

	private FormFilteredTree fFilteredTree;

	private TreeViewer fExtensionTree;

	private SortAction fSortAction;

	private Resource prfResource;
	private boolean editable;
	private Properties properties;
	private PropertiesBlock block;

	public PropertiesSection(PropertiesBlock block, final Composite parent) {
		super(block.getPage(), parent, Section.DESCRIPTION | ExpandableComposite.TITLE_BAR,
			new String[] { "Add Simple", "Add Sequence", "Add Struct", "Add StructSeq", "Browse...", "Remove" });
		this.fHandleDefaultButton = false;
		this.block = block;
	}

	@Override
	protected void buttonSelected(final int index) {
		final List<Object> selection = Arrays.asList(((IStructuredSelection) getViewerSelection()).toArray());
		switch (index) {
		case BUTTON_ADD_SIMPLE:
			PropertyHandler.addSimple(getAdapterFactory(), getEditingDomain(), getProperties());
			block.getDetailsPart().setFocus();
			break;
		case BUTTON_ADD_SIMPLE_SEQUENCE:
			// Always add a simplesequence to the root properties
			PropertyHandler.addSimpleSequence(getAdapterFactory(), getEditingDomain(), getProperties());
			block.getDetailsPart().setFocus();
			break;
		case BUTTON_ADD_STRUCT:
			PropertyHandler.addStruct(getAdapterFactory(), getEditingDomain(), getProperties());
			block.getDetailsPart().setFocus();
			break;
		case BUTTON_ADD_STRUCT_SEQUENCE:
			// Always add a structsequence to the root properties
			PropertyHandler.addStructSequence(getAdapterFactory(), getEditingDomain(), getProperties());
			block.getDetailsPart().setFocus();
			break;
		case BUTTON_BROWSE:
			handleBrowse();
			break;
		case BUTTON_REMOVE:
			EditingDomain editingDomain = getEditingDomain();
			Command remove = RemoveCommand.create(editingDomain, selection);
			editingDomain.getCommandStack().execute(remove);
			break;
		default:
			break;
		}
	}

	@Override
	protected void createClient(final Section section, final FormToolkit toolkit) {
		final Composite container = createClientContainer(section, 2, toolkit);
		final TreePart treePart = getTreePart();
		createViewerPartControl(container, SWT.MULTI, 2, toolkit);
		this.fExtensionTree = treePart.getTreeViewer();
		this.fExtensionTree.setContentProvider(new AdapterFactoryContentProvider(getAdapterFactory()));
		this.fExtensionTree.setLabelProvider(new DecoratingLabelProvider(new UnwrappingLabelProvider(new AdapterFactoryLabelProvider(getAdapterFactory())), PlatformUI.getWorkbench()
		        .getDecoratorManager().getLabelDecorator()));
		toolkit.paintBordersFor(container);
		section.setClient(container);
		section.setDescription("Define properties within the following section.");
		// See Bug # 160554: Set text before text client
		section.setText("All Properties");
		initialize();
		createSectionToolbar(section, toolkit);
		// Create the adapted listener for the filter entry field
		final Text filterText = this.fFilteredTree.getFilterControl();
		if (filterText != null) {
			filterText.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(final ModifyEvent e) {
					final StructuredViewer viewer = getStructuredViewerPart().getViewer();
					final IStructuredSelection ssel = (IStructuredSelection) viewer.getSelection();
					updateButtons((ssel.size() != 1) ? null : ssel); // SUPPRESS CHECKSTYLE AvoidInline
				}
			});
		}

		refresh(this.prfResource);
	}

	private void createSectionToolbar(final Section section, final FormToolkit toolkit) {
		final ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT);
		final ToolBar toolbar = toolBarManager.createControl(section);
		final Cursor handCursor = new Cursor(Display.getCurrent(), SWT.CURSOR_HAND);
		toolbar.setCursor(handCursor);
		// Cursor needs to be explicitly disposed
		toolbar.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(final DisposeEvent e) {
				if (!handCursor.isDisposed()) {
					handCursor.dispose();
				}
			}
		});
		// Add sort action to the tool bar
		this.fSortAction = new SortAction(this.fExtensionTree, "Sort the properties alpabetically.", new AbstractPropertyViewerComparator(), null, this);
		toolBarManager.add(this.fSortAction);

		toolBarManager.update(true);

		section.setTextClient(toolbar);
	}

	@Override
	protected TreeViewer createTreeViewer(final Composite parent, final int style) {
		this.fFilteredTree = new FormFilteredTree(parent, style, new PatternFilter());
		parent.setData("filtered", Boolean.TRUE); //$NON-NLS-1$
		return this.fFilteredTree.getViewer();
	}

	@Override
	public void dispose() {
		// Explicitly call the dispose method on the extensions tree
		if (this.fFilteredTree != null) {
			this.fFilteredTree.dispose();
		}
		super.dispose();
	}

	protected void fireSelection() {
		final ISelection selection = this.fExtensionTree.getSelection();
		if (selection.isEmpty()) {
			this.selectElement();
		} else {
			this.fExtensionTree.setSelection(this.fExtensionTree.getSelection());
		}
	}

	private AdapterFactory getAdapterFactory() {
		return getPage().getEditor().getAdapterFactory();
	}

	private EditingDomain getEditingDomain() {
		return getPage().getEditor().getEditingDomain();
	}

	@Override
	public PropertiesFormPage getPage() {
		return (PropertiesFormPage) super.getPage();
	}

	private Properties getProperties() {
		return Properties.Util.getProperties(this.prfResource);
	}

	/**
	 * Browse for already defined properties.
	 */
	private void handleBrowse() {
		final BrowsePropertiesWizard wizard = new BrowsePropertiesWizard(ScaPlugin.getPropertiesProviderRegistry().getPropertiesProvidersDescriptors());

		final WizardDialog dialog = new WizardDialog(getPage().getSite().getShell(), wizard);

		if (dialog.open() == Window.OK) {
			final List<EObject> myProps = wizard.getProperties();
			for (final EObject property : myProps) {
				final Property prop = Property.getProperty(property);
				final Command command = AddCommand.create(getEditingDomain(), getProperties(), prop.getPropertyFeature(), property);
				getEditingDomain().getCommandStack().execute(command);
			}
		}
	}

	private void initialize() {
		selectElement();
		final TreePart treePart = getTreePart();
		treePart.setButtonEnabled(PropertiesSection.BUTTON_ADD_SIMPLE, true);
		treePart.setButtonEnabled(PropertiesSection.BUTTON_ADD_SIMPLE_SEQUENCE, true);
		treePart.setButtonEnabled(PropertiesSection.BUTTON_ADD_STRUCT, true);
		treePart.setButtonEnabled(PropertiesSection.BUTTON_ADD_STRUCT_SEQUENCE, true);
		treePart.setButtonEnabled(PropertiesSection.BUTTON_REMOVE, false);
	}

	@Override
	public void propertyChange(final PropertyChangeEvent event) {
		if (this.fSortAction.equals(event.getSource()) && IAction.RESULT.equals(event.getProperty())) {
			final StructuredViewer viewer = getStructuredViewerPart().getViewer();
			final IStructuredSelection ssel = (IStructuredSelection) viewer.getSelection();
			updateButtons(ssel);
		}
	}

	@Override
	public void refresh(final Resource resource) {
		this.prfResource = resource;
		this.properties = getProperties();
		if (this.fExtensionTree != null) {
			this.fExtensionTree.setInput(this.properties);
		}
		this.fireSelection();
		this.setEditable();
		super.refresh(this.prfResource);
	}

	/**
	 * Select first element.
	 */
	private void selectElement() {
		final Tree tree = this.fExtensionTree.getTree();
		final TreeItem[] items = tree.getItems();
		if (items.length == 0) {
			return;
		}
		TreeItem item = items[0];

		this.fExtensionTree.setSelection(new StructuredSelection(item.getData()));
	}

	@Override
	protected void selectionChanged(final IStructuredSelection selection) {
		if (selection.isEmpty()) {
			selectElement();
		} else {
			getPage().setSelection(selection);
			updateButtons(selection);
		}
	}

	private void setEditable() {
		this.editable = SCAEditorUtil.isEditableResource(getPage(), this.prfResource);
		this.getTreePart().setButtonEnabled(PropertiesSection.BUTTON_ADD_SIMPLE, this.editable);
		this.getTreePart().setButtonEnabled(PropertiesSection.BUTTON_ADD_SIMPLE_SEQUENCE, this.editable);
		this.getTreePart().setButtonEnabled(PropertiesSection.BUTTON_ADD_STRUCT, this.editable);
		this.getTreePart().setButtonEnabled(PropertiesSection.BUTTON_ADD_STRUCT_SEQUENCE, this.editable);
		this.getTreePart().setButtonEnabled(PropertiesSection.BUTTON_BROWSE, this.editable);
		this.getTreePart().setButtonEnabled(PropertiesSection.BUTTON_REMOVE, this.editable);
	}

	@Override
	public boolean setFormInput(final Object object) {
		if (object != null) {
			// TODO: This may not be necessary, if all views use the same set of adapters
			ISelection selection = ViewerUtil.itemsToSelection(this.fExtensionTree, Collections.singleton(object));
			if (!selection.isEmpty()) {
				this.fExtensionTree.setSelection(selection, true);
				return true;
			}
			return false;
		} else {
			return false;
		}
	}

	private void updateButtons(final Object item) {
		final boolean sorted = this.fSortAction != null && this.fSortAction.isChecked();
		if (sorted) {
			return;
		}

		final boolean filtered = this.fFilteredTree.isFiltered();
		boolean addEnabled = true;
		boolean removeEnabled = false;

		if (item != null && this.editable) {
			removeEnabled = true;
		}
		if (filtered || !this.editable) {
			// Fix for bug 194529 and bug 194828
			addEnabled = false;
		}
		getTreePart().setButtonEnabled(PropertiesSection.BUTTON_ADD_SIMPLE, addEnabled);
		getTreePart().setButtonEnabled(PropertiesSection.BUTTON_ADD_SIMPLE_SEQUENCE, addEnabled);
		getTreePart().setButtonEnabled(PropertiesSection.BUTTON_ADD_STRUCT, addEnabled);
		getTreePart().setButtonEnabled(PropertiesSection.BUTTON_ADD_STRUCT_SEQUENCE, addEnabled);
		getTreePart().setButtonEnabled(PropertiesSection.BUTTON_REMOVE, removeEnabled);
	}

}
