/*******************************************************************************
 * This file is protected by Copyright. 
 * Please refer to the COPYRIGHT file distributed with this source distribution.
 *
 * This file is part of REDHAWK IDE.
 *
 * All rights reserved.  This program and the accompanying materials are made available under 
 * the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package gov.redhawk.sca.ui;

import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

/**
 * @since 10.2
 * 
 * Creates a dialog that allows for selection of predefined connection IDs
 */
public class MultiOutConnectionWizard extends Dialog {

	// Possible connectionIds for a multi-out port
	private Map<String, Boolean> connectionIds;

	// SWT Widgets
	private Button selectIdRadioButton;
	private TreeViewer selectIdTreeViewer;
	private Button createIdRadioButton;
	private Text connectionIdText;

	private String selectedConnectionId;

	/** Content provider for the connection ID tree viewer **/
	private ITreeContentProvider treeViewerContentProvider = new ITreeContentProvider() {

		@Override
		public boolean hasChildren(Object element) {
			return false;
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return ArrayContentProvider.getInstance().getElements(inputElement);
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			return null;
		}
	};

	/** Content provider for the connection ID tree viewer **/
	private LabelProvider treeViewerLabelProvider = new LabelProvider() {
		@SuppressWarnings("unchecked")
		@Override
		public String getText(Object element) {
			if (element instanceof Entry) {
				Entry<String, Boolean> entry = (Entry<String, Boolean>) element;
				String id = entry.getKey().toString();

				if (!entry.getValue()) {
					id = id + " (IN USE) ";
				}
				return id;
			}
			return super.getText(element);
		}
	};

	public MultiOutConnectionWizard(Shell parentShell, Map<String, Boolean> connectionIds) {
		super(parentShell);
		this.connectionIds = connectionIds;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Multi-out port connection wizard");
	}

	@Override
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);

		// Need to validate here on creation, since we have to wait for the button bars to be created
		validate();

		return contents;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(2, false));
		createMessageArea(container);
		createRadioComposite(container);
		return container;
	}

	private void createMessageArea(Composite parent) {
		final Image image = parent.getDisplay().getSystemImage(SWT.ICON_WARNING);
		final Label imageLabel = new Label(parent, SWT.NONE);
		imageLabel.setImage(image);
		imageLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));

		final Label msg = new Label(parent, SWT.NONE);
		msg.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
		msg.setText("Attempting to perform a connection operation on a multi-out port with multiple data streams.");
	}

	private void createRadioComposite(Composite container) {
		Composite radioContainer = new Composite(container, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginLeft = 50;
		radioContainer.setLayout(layout);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		data.widthHint = 400;
		radioContainer.setLayoutData(data);

		createSelectExistingIdSection(radioContainer);
		createCreateNewIdSection(radioContainer);
		createListeners();

		// Set default to the first item in the tree viewer
		selectIdRadioButton.setSelection(true);
		TreeItem firstItem = selectIdTreeViewer.getTree().getItem(0);
		selectedConnectionId = firstItem.getText();
		selectIdTreeViewer.getTree().setSelection(firstItem);

		initDataBindings();
	}

	/** Data bindings to enable/disable sections depending on which radio button is selected **/
	private void initDataBindings() {
		DataBindingContext dbc = new DataBindingContext();
		ISWTObservableValue enabledObservable = WidgetProperties.enabled().observe(selectIdTreeViewer.getControl());
		ISWTObservableValue selectionObservable = WidgetProperties.selection().observe(selectIdRadioButton);
		dbc.bindValue(enabledObservable, selectionObservable);

		enabledObservable = WidgetProperties.enabled().observe(connectionIdText);
		selectionObservable = WidgetProperties.selection().observe(createIdRadioButton);
		dbc.bindValue(enabledObservable, selectionObservable);
	}

	private void createSelectExistingIdSection(Composite radioContainer) {
		selectIdRadioButton = new Button(radioContainer, SWT.RADIO);
		selectIdRadioButton.setText("Select an existing connection ID");
		Composite existingComposite = new Composite(radioContainer, SWT.NONE);
		GridLayout existingCompositeLayout = new GridLayout();
		existingCompositeLayout.marginLeft = 25;
		existingComposite.setLayout(existingCompositeLayout);
		existingComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		selectIdTreeViewer = new TreeViewer(existingComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE);
		selectIdTreeViewer.setContentProvider(treeViewerContentProvider);
		selectIdTreeViewer.setLabelProvider(treeViewerLabelProvider);
		selectIdTreeViewer.setInput(connectionIds.entrySet());
		GridData treeViewerData = new GridData(GridData.FILL, GridData.FILL, true, true);
		treeViewerData.heightHint = 50;
		selectIdTreeViewer.getControl().setLayoutData(treeViewerData);
	}

	private void createCreateNewIdSection(Composite radioContainer) {
		createIdRadioButton = new Button(radioContainer, SWT.RADIO);
		createIdRadioButton.setText("Input connection ID");
		Composite generateComposite = new Composite(radioContainer, SWT.NONE);
		GridLayout generateCompositeLayout = new GridLayout();
		generateCompositeLayout.marginLeft = 25;
		generateComposite.setLayout(generateCompositeLayout);
		generateComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Label warningLabel = new Label(generateComposite, SWT.NONE);
		warningLabel.setText("WARNING: Inputing a connection ID for a multi-out port is not recommended.\nThis may result in your port not suppling data.");
		connectionIdText = new Text(generateComposite, SWT.BORDER);
		connectionIdText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		connectionIdText.setText(ConnectPortWizard.generateDefaultConnectionID());
	}

	/** Various listeners to avoid completing the dialog while an invalid value is selected for the connection ID **/
	private void createListeners() {

		// Validate selection when changing between selectExistingId and createNewId sections
		selectIdRadioButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validate();
			}
		});
		createIdRadioButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validate();
			}
		});

		// Don't allow selection of "In Use" connection IDs
		selectIdTreeViewer.addSelectionChangedListener((event) -> {
			@SuppressWarnings("unchecked")
			Entry<String, Boolean> selection = (Entry<String, Boolean>) event.getStructuredSelection().getFirstElement();
			if (selection != null) {
				selectedConnectionId = selection.getKey();
				getButton(IDialogConstants.OK_ID).setEnabled(selection.getValue());
			}
		});

		// Don't allow an empty text field when manually entering the connection ID
		connectionIdText.addModifyListener((event) -> {
			selectedConnectionId = connectionIdText.getText();
			validate();
		});
	}

	@SuppressWarnings("unchecked")
	private void validate() {
		if (selectIdRadioButton.getSelection()) {
			Entry<String, Boolean> selection = (Entry<String, Boolean>) selectIdTreeViewer.getStructuredSelection().getFirstElement();
			if (selection != null) {
				selectedConnectionId = selection.getKey();
				getButton(IDialogConstants.OK_ID).setEnabled(selection.getValue());
			}
		} else if (createIdRadioButton.getSelection()) {
			String connId = connectionIdText.getText();
			selectedConnectionId = connId;
			getButton(IDialogConstants.OK_ID).setEnabled(connId != null && !"".equals(connId));
		}
	}

	public String getSelectedId() {
		return selectedConnectionId;
	}
}
