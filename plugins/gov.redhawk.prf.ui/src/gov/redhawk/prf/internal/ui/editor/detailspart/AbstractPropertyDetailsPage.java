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
package gov.redhawk.prf.internal.ui.editor.detailspart;

import gov.redhawk.prf.internal.ui.editor.PropertiesSection;
import gov.redhawk.prf.internal.ui.editor.composite.AbstractPropertyComposite;
import gov.redhawk.ui.editor.SCAFormEditor;
import gov.redhawk.ui.editor.ScaDetails;
import gov.redhawk.ui.util.EMFEmptyStringToNullUpdateValueStrategy;
import gov.redhawk.ui.util.SCAEditorUtil;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.emf.databinding.edit.EMFEditObservables;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * Provides functionality common to all Property types, including data binding of all common widgets.
 */
public abstract class AbstractPropertyDetailsPage extends ScaDetails {

	private final PropertiesSection section;
	private Property property;
	private EObject input;
	private AbstractPropertyComposite composite;
	private Binding nameBinding;

	public AbstractPropertyDetailsPage(final PropertiesSection section) {
		super(section.getPage());
		this.section = section;
	}

	@Override
	protected void createSpecificContent(final Composite parent) {
		final FormToolkit toolkit = getManagedForm().getToolkit();
		this.composite = createSection(parent, toolkit);
		this.addListeners();
	}

	protected void addListeners() {
//		if (this.composite.getIdEntry() != null) {
//			this.composite.getIdEntry().getText().addModifyListener(new ModifyListener() {
//
//				public void modifyText(ModifyEvent e) {
//					if (property != null && input != null) {
//						String propName = (String) input.eGet(property.getName());
//						String id = (String) input.eGet(property.getId());
//						if (PluginUtil.equals(propName, id)) {
//							String newValue = composite.getIdEntry().getText().getText();
//							if (composite.getNameEntry() != null) {
//								composite.getNameEntry().getText().setText(newValue);
//							}
//						}
//					}
//				}
//			});
//		}
	}

	@Override
	protected List<Binding> bind(final DataBindingContext dataBindingContext, final EObject input) {
		final List<Binding> retVal = new ArrayList<Binding>();
		final EditingDomain domain = getEditingDomain();
		this.input = input;
		this.property = getProperty(this.input);

		// ID
		if (getComposite().getIdEntry() != null) {
			retVal.add(dataBindingContext.bindValue(
				WidgetProperties.text(SWT.Modify).observeDelayed(SCAFormEditor.getFieldBindingDelay(), this.composite.getIdEntry().getText()),
				EMFEditObservables.observeValue(getEditingDomain(), input, this.property.getId()), new EMFEmptyStringToNullUpdateValueStrategy(), null));
		}

		// Name
		if (getComposite().getNameEntry() != null) {
			this.nameBinding = dataBindingContext.bindValue(
				WidgetProperties.text(SWT.Modify).observeDelayed(SCAFormEditor.getFieldBindingDelay(), this.composite.getNameEntry().getText()),
				EMFEditObservables.observeValue(domain, input, this.property.getName()), new EMFEmptyStringToNullUpdateValueStrategy(), null);
			retVal.add(this.nameBinding);
		}

		// Mode
		if (getComposite().getModeViewer() != null) {
			retVal.add(dataBindingContext.bindValue(ViewersObservables.observeSingleSelection(this.composite.getModeViewer()),
				EMFEditObservables.observeValue(domain, input, this.property.getMode()), null, null));
		}

		// Description
		if (getComposite().getDescriptionText() != null) {
			retVal.add(dataBindingContext.bindValue(
				WidgetProperties.text(SWT.Modify).observeDelayed(SCAFormEditor.getFieldBindingDelay(), this.composite.getDescriptionText()),
				EMFEditObservables.observeValue(domain, input, this.property.getDescription()), new EMFEmptyStringToNullUpdateValueStrategy(), null));
		}

		this.setEditable();
		return retVal;
	}

	protected PropertiesSection getSection() {
		return this.section;
	}

	protected abstract AbstractPropertyComposite createSection(Composite parent, FormToolkit toolkit);

	/**
	 * Returns the {@link Property} type based on the given input.
	 * 
	 * @param input the {@link EObject} to obtain a {@link Property} for
	 * @return the {@link Property} associated with the input if it exists, <code> null </code >otherwise
	 */
	protected Property getProperty(final EObject input) {
		return Property.getProperty(input);
	}

	protected boolean isEditable() {
		return SCAEditorUtil.isEditableResource(getPage(), this.input.eResource());
	}

	private void setEditable() {
		this.composite.setEditable(isEditable());
	}

	protected AbstractPropertyComposite getComposite() {
		return this.composite;
	}
}
