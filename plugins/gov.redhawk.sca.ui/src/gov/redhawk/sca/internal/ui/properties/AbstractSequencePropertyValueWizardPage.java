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
package gov.redhawk.sca.internal.ui.properties;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import gov.redhawk.model.sca.ScaAbstractProperty;
import gov.redhawk.sca.ui.ScaUiPlugin;
import mil.jpeojtrs.sca.prf.PropertyValueType;
import mil.jpeojtrs.sca.util.math.ComplexBoolean;
import mil.jpeojtrs.sca.util.math.ComplexDouble;
import mil.jpeojtrs.sca.util.math.ComplexFloat;
import mil.jpeojtrs.sca.util.math.ComplexLong;
import mil.jpeojtrs.sca.util.math.ComplexLongLong;
import mil.jpeojtrs.sca.util.math.ComplexOctet;
import mil.jpeojtrs.sca.util.math.ComplexShort;
import mil.jpeojtrs.sca.util.math.ComplexULong;
import mil.jpeojtrs.sca.util.math.ComplexULongLong;
import mil.jpeojtrs.sca.util.math.ComplexUShort;

public abstract class AbstractSequencePropertyValueWizardPage extends WizardPage {

	protected final ScaAbstractProperty< ? > property;
	private StructuredViewer viewer;
	private Button removeButton;
	private Button downButton;
	private Button upButton;
	private Button resetButton;

	public AbstractSequencePropertyValueWizardPage(ScaAbstractProperty< ? > property) {
		super("valuePage", "Edit Value", null);
		this.property = property;
		String propDesc = property.getDescription();
		if (propDesc == null) {
			propDesc = "";
		}
		setDescription("Edit the values of the sequence property.\n" + propDesc);
	}

	@Override
	public void createControl(final Composite parent) {
		final Composite root = new Composite(parent, SWT.None);
		root.setLayout(new GridLayout(2, false));

		final Composite tableComposite = new Composite(root, SWT.NO_FOCUS);
		viewer = createViewer(tableComposite);
		viewer.setInput(this.property);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(final SelectionChangedEvent event) {
				updateButtonState();
			}
		});
		tableComposite.setLayoutData(GridDataFactory.fillDefaults().hint(500, 300).span(2, 1).create());

		// Create spacer
		new Label(root, SWT.None).setLayoutData(GridDataFactory.fillDefaults().span(1, 1).grab(true, false).create());

		final Composite buttonComposite = createButtons(root);
		buttonComposite.setLayoutData(GridDataFactory.fillDefaults().span(1, 1).grab(false, false).create());

		setControl(root);
	}

	protected Object getDefaultValue(final PropertyValueType type, boolean isComplex) {
		if (isComplex) {
			switch (type) {
			case BOOLEAN:
				return new ComplexBoolean();
			case DOUBLE:
				return new ComplexDouble();
			case FLOAT:
				return new ComplexFloat();
			case LONG:
				return new ComplexLong();
			case LONGLONG:
				return new ComplexLongLong();
			case OCTET:
				return new ComplexOctet();
			case SHORT:
				return new ComplexShort();
			case ULONG:
				return new ComplexULong();
			case USHORT:
				return new ComplexUShort();
			case ULONGLONG:
				return new ComplexULongLong();
			case STRING:
			case OBJREF:
			case CHAR:
			default:
				throw new IllegalArgumentException("Unhandled property type");
			}
		} else {
			switch (type) {
			case BOOLEAN:
				return Boolean.FALSE;
			case CHAR:
				return 'a';
			case DOUBLE:
				return 0.00d;
			case FLOAT:
				return 0.00f;
			case LONG:
				return 0;
			case LONGLONG:
				return 0L;
			case OBJREF:
				return null;
			case OCTET:
				return (short) 0;
			case SHORT:
				return (short) 0;
			case STRING:
				return "newString";
			case ULONG:
				return 0L;
			case USHORT:
				return 0;
			case ULONGLONG:
				return BigInteger.ZERO;
			default:
				throw new IllegalArgumentException("Unhandled property type");
			}
		}
	}

	protected abstract EList< ? > getList();

	protected abstract void handleAddValue();

	private void handleRemoveValue() {
		IStructuredSelection selection = viewer.getStructuredSelection();
		if (!selection.isEmpty()) {
			EList< ? > list = getList();
			int index = indexOf(list, selection.getFirstElement());
			for (Object item : selection.toList()) {
				list.remove(indexOf(list, item));
			}
			if (!list.isEmpty()) {
				index = Math.min(index, list.size() - 1);
				setSelection(index);
			}
		}
	}

	private void handleMoveUp() {
		if (viewer.getSelection().isEmpty()) {
			return;
		}

		// Move the items up in ascending order to ensure that they do not overlap, and adjust the selection
		// indices while we're at it
		EList< ? > list = getList();
		int[] indices = getSelectionIndices();
		for (int ii = 0; ii < indices.length; ++ii) {
			int index = indices[ii];
			list.move(index - 1, index);
			indices[ii] -= 1;
		}

		setSelection(indices);
	}

	private void handleMoveDown() {
		if (viewer.getSelection().isEmpty()) {
			return;
		}

		// Move the items down in descending order to ensure that they do not overlap, and adjust the selection
		// indices while we're at it
		int[] indices = getSelectionIndices();
		EList< ? > list = getList();
		for (int ii = indices.length - 1; ii >= 0; --ii) {
			int index = indices[ii];
			list.move(index + 1, index);
			indices[ii] += 1;
		}

		setSelection(indices);
	}

	private void setSelection(int index) {
		setSelection(new int[] { index });
	}

	private void setSelection(int[] indices) {
		// Fetch the model objects as seen from the viewer; these will differ from the list's elements if they are
		// wrapped (such as in a simple sequence)
		IStructuredContentProvider provider = (IStructuredContentProvider) viewer.getContentProvider();
		Object[] elements = provider.getElements(viewer.getInput());

		// Convert the indices into the list of model objects to ensure that the selection succeeds
		List<Object> selection = new ArrayList<Object>(indices.length);
		for (int index : indices) {
			selection.add(elements[index]);
		}
		viewer.setSelection(new StructuredSelection(selection));
	}

	private int[] getSelectionIndices() {
		IStructuredSelection selection = viewer.getStructuredSelection();
		int[] indices = new int[selection.size()];
		int offset = 0;
		for (Object item : selection.toList()) {
			indices[offset++] = indexOf(getList(), item);
		}
		Arrays.sort(indices);
		return indices;
	}

	protected abstract int indexOf(List< ? > list, Object object);

	private Composite createButtons(final Composite root) {
		final Composite buttonRoot = new Composite(root, SWT.None);
		buttonRoot.setLayout(new GridLayout(5, false));
		final ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();

		final Button addButton = new Button(buttonRoot, SWT.PUSH);
		addButton.setToolTipText("Add");
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				handleAddValue();
			}
		});
		addButton.setImage(sharedImages.getImage(ISharedImages.IMG_OBJ_ADD));
		this.removeButton = new Button(buttonRoot, SWT.PUSH);
		this.removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				handleRemoveValue();
			}
		});
		this.removeButton.setToolTipText("Remove");
		this.removeButton.setImage(sharedImages.getImage(ISharedImages.IMG_ETOOL_DELETE));

		this.upButton = new Button(buttonRoot, SWT.PUSH);
		this.upButton.setImage(ScaUiPlugin.getDefault().getImage("icons/clcl16/up.png"));
		this.upButton.setToolTipText("Move value up");
		this.upButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				handleMoveUp();
			}
		});
		this.downButton = new Button(buttonRoot, SWT.PUSH);
		this.downButton.setImage(ScaUiPlugin.getDefault().getImage("icons/clcl16/down.png"));
		this.downButton.setToolTipText("Move value down");
		this.downButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				handleMoveDown();
			}
		});
		this.resetButton = new Button(buttonRoot, SWT.PUSH);
		resetButton.setText("Reset");
		this.resetButton.setToolTipText("Reset to default value");
		this.resetButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				handleReset();
			}
		});

		updateButtonState();

		return buttonRoot;
	}

	protected void handleReset() {
		property.restoreDefaultValue();
	}

	protected abstract StructuredViewer createViewer(Composite parent);

	/**
	 * Setter to allow us to enable / disable the remove button
	 *
	 * @param enabled Boolean representing whether we should or should not enable the remove button
	 */
	private void updateButtonState() {
		IStructuredSelection selection = viewer.getStructuredSelection();
		if (selection.isEmpty()) {
			removeButton.setEnabled(false);
			upButton.setEnabled(false);
			downButton.setEnabled(false);
		} else {
			// Remove is always enabled because there must be at least one selection
			removeButton.setEnabled(true);

			// Find the lowest and highest selection indices
			List< ? > list = getList();
			int minIndex = Integer.MAX_VALUE;
			int maxIndex = -1;
			for (Object item : selection.toList()) {
				int index = indexOf(list, item);
				minIndex = Math.min(minIndex, index);
				maxIndex = Math.max(maxIndex, index);
			}

			// The up and down buttons are enabled only if there is somewhere to go (i.e., not the first or last item,
			// respectively)
			upButton.setEnabled(minIndex > 0);
			downButton.setEnabled(maxIndex < (list.size() - 1));
		}
	}

}
