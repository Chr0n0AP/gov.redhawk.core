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
package gov.redhawk.prf.internal.ui.editor.composite;

import gov.redhawk.common.ui.doc.HelpConstants;
import gov.redhawk.common.ui.parts.FormEntry;
import gov.redhawk.ui.doc.HelpUtil;
import gov.redhawk.ui.editor.IScaComposite;
import mil.jpeojtrs.sca.prf.AccessType;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * 
 */
public abstract class AbstractPropertyComposite extends Composite implements IScaComposite {

	public static final int NUM_COLUMNS = 3;

	protected static final GridDataFactory FACTORY = GridDataFactory.fillDefaults().span(2, 1).grab(true, false);
	private ComboViewer modeViewer;
	private Label modeLabel;
	private Label descriptionLabel;
	private Text descriptionText;
	private FormEntry idEntry;
	private FormEntry nameEntry;

	/**
	 * @param parent
	 * @param style
	 */
	public AbstractPropertyComposite(final Composite parent, final int style, final FormToolkit toolkit) {
		super(parent, style);
	}

	private void assignTooltip(final Control control, final String contextId) {
		HelpUtil.assignTooltip(control, contextId);
	}

	/**
	 * @param propertyComposite
	 * @param toolkit
	 */
	protected Text createDescription(final Composite parent, final FormToolkit toolkit) {
		// Description
		this.descriptionLabel = toolkit.createLabel(parent, "Description:");
		this.descriptionLabel.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
		this.descriptionLabel.setLayoutData(GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.BEGINNING).create());
		final Text text = toolkit.createText(parent, "", SWT.MULTI | SWT.WRAP);
		text.setLayoutData(AbstractPropertyComposite.FACTORY.copy().hint(SWT.DEFAULT, 75).create()); // SUPPRESS CHECKSTYLE MagicNumber
		assignTooltip(text, HelpConstants.prf_properties_simple_description);
		this.descriptionText = text;
		return this.descriptionText;
	}

	/**
	 * @param propertyComposite
	 * @param toolkit
	 */
	protected ComboViewer createModeViewer(final Composite parent, final FormToolkit toolkit) {
		// Mode
		this.modeLabel = toolkit.createLabel(parent, "Mode:");
		this.modeLabel.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
		this.modeLabel.setLayoutData(GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.BEGINNING).create());
		final ComboViewer viewer = new ComboViewer(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
		viewer.getCombo().addListener(SWT.MouseVerticalWheel, new Listener() {

			@Override
			public void handleEvent(Event event) {
				// Disable Mouse Wheel Combo Box Control
				event.doit = false;
			}

		});
		toolkit.adapt(viewer.getCombo());
		viewer.getControl().setLayoutData(AbstractPropertyComposite.FACTORY.create());
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(new LabelProvider());
		viewer.setInput(AccessType.values());
		assignTooltip(viewer.getControl(), HelpConstants.prf_properties_simple_mode);
		this.modeViewer = viewer;
		return this.modeViewer;
	}

	/**
	 * Creates the id entry field.
	 * 
	 * @param toolkit the toolkit
	 * @param client the client
	 */
	protected FormEntry createIDEntryField(final FormToolkit toolkit, final Composite parent) {
		final FormEntry id = new FormEntry(parent, toolkit, "ID*:", SWT.SINGLE);
		assignTooltip(id.getText(), HelpConstants.prf_properties_simple_id);
		this.idEntry = id;
		return id;
	}

	/**
	 * Creates the name entry field.
	 * 
	 * @param toolkit the toolkit
	 * @param client the client
	 */
	protected FormEntry createNameEntryField(final FormToolkit toolkit, final Composite client) {
		this.nameEntry = new FormEntry(client, toolkit, "Name:", SWT.SINGLE);
		assignTooltip(this.nameEntry.getText(), HelpConstants.prf_properties_simple_name);
		return this.nameEntry;
	}

	/**
	 * @return the modeLabel
	 */
	public Label getModeLabel() {
		return modeLabel;
	}

	/**
	 * @return the modeViewer
	 */
	public ComboViewer getModeViewer() {
		return this.modeViewer;
	}

	/**
	 * @return the descriptionText
	 */
	public Text getDescriptionText() {
		return this.descriptionText;
	}

	/**
	 * @return the idEntry
	 */
	public FormEntry getIdEntry() {
		return this.idEntry;
	}

	/**
	 * @return the nameEntry
	 */
	public FormEntry getNameEntry() {
		return this.nameEntry;
	}

	@Override
	public void setEditable(final boolean canEdit) {
		this.idEntry.setEditable(canEdit);
		this.nameEntry.setEditable(canEdit);
		if (this.modeViewer != null) {
			this.modeLabel.setEnabled(canEdit);
			this.modeViewer.getCombo().setEnabled(canEdit);
		}
		this.descriptionLabel.setEnabled(canEdit);
		this.descriptionText.setEditable(canEdit);
	}

}
