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
package gov.redhawk.prf.ui.editor.page;

import gov.redhawk.common.ui.doc.HelpConstants;
import gov.redhawk.prf.internal.ui.editor.PropertiesBlock;
import gov.redhawk.ui.editor.SCAFormEditor;
import gov.redhawk.ui.editor.ScaFormPage;
import mil.jpeojtrs.sca.prf.AbstractProperty;
import mil.jpeojtrs.sca.util.ScaEcoreUtils;

import org.eclipse.emf.common.ui.viewer.IViewerProvider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.menus.IMenuService;

/**
 * This class is the form page that allows graphical editing of properties in a PRF file.
 */
public class PropertiesFormPage extends ScaFormPage implements IViewerProvider {

	/** The Constant PAGE_ID. */
	public static final String PAGE_ID = "properties"; //$NON-NLS-1$

	/**
	 * The toolbar contribution ID
	 * @since 2.0
	 */
	public static final String TOOLBAR_ID = "gov.redhawk.prf.ui.editor.page.toolbar";

	private final PropertiesBlock fBlock;

	/**
	 * Instantiates a new properties form page.
	 * 
	 * @param editor the editor
	 * @param propertiesResource the properties resource
	 */
	public PropertiesFormPage(final SCAFormEditor editor) {
		super(editor, PropertiesFormPage.PAGE_ID, "Properties");
		this.fBlock = new PropertiesBlock(this);
	}

	@Override
	protected String getHelpResource() {
		return HelpConstants.reference_editors_prf_propertiesPage;
	}

	@Override
	protected void createFormContent(final IManagedForm managedForm) {
		final ScrolledForm form = managedForm.getForm();
		form.setText("Properties");
		// TODO
		// form.setImage(PDEPlugin.getDefault().getLabelProvider().get(PDEPluginImages.DESC_EXTENSIONS_OBJ));
		this.fBlock.createContent(managedForm);

		PlatformUI.getWorkbench().getHelpSystem().setHelp(form.getBody(), HelpConstants.reference_editors_prf_propertiesPage);
		
		super.createFormContent(managedForm);
		final ToolBarManager manager = (ToolBarManager) form.getToolBarManager();
		final IMenuService service = (IMenuService) getSite().getService(IMenuService.class);
		service.populateContributionManager(manager, "toolbar:" + PropertiesFormPage.TOOLBAR_ID);
		manager.update(true);
	}

	@Override
	public Viewer getViewer() {
		return this.fBlock.getSection().getStructuredViewerPart().getViewer();
	}

	@Override
	protected void refresh(final Resource resource) {
		this.fBlock.refresh(resource);
	}

	@Override
	public boolean selectReveal(final Object object) {
		if (object instanceof EObject) {
			EObject eObj = (EObject) object;
			if (eObj instanceof AbstractProperty) {
				return super.selectReveal(object);
			}

			// Try to find a property that is the parent of the object. Reveal it instead.
			AbstractProperty absProp = ScaEcoreUtils.getEContainerOfType(eObj, AbstractProperty.class);
			if (absProp != null) {
				return super.selectReveal(absProp);
			}
		}

		return super.selectReveal(object);
	}
}
