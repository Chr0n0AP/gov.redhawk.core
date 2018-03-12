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
package gov.redhawk.internal.ui.port.nxmplot.view;

import gov.redhawk.ui.port.nxmplot.FftNumAvgControls;
import gov.redhawk.ui.port.nxmplot.FrameSizeControls;
import gov.redhawk.ui.port.nxmplot.IPlotView;
import gov.redhawk.ui.port.nxmplot.PlotActivator;
import gov.redhawk.ui.port.nxmplot.PlotEventChannelForwarder;
import gov.redhawk.ui.port.nxmplot.PlotPageBook2;
import gov.redhawk.ui.port.nxmplot.PlotSettings;
import gov.redhawk.ui.port.nxmplot.PlotSource;
import gov.redhawk.ui.port.nxmplot.preferences.PlotPreferences;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.services.IDisposable;
import org.eclipse.ui.statushandlers.StatusManager;

/**
 * The spectral view provides view that displays spectral data in a plot.
 *
 * @since 4.2
 * @noreference This class is not intended to be referenced by clients
 */
public class PlotView2 extends ViewPart implements IPlotView {
	/** The ID of the view. */
	public static final String ID = "gov.redhawk.ui.port.nxmplot.PlotView2";

	public static final String ID_CHANGE_PLOT_TYPE_ACTION = "gov.redhawk.ChangePlotType";

	private static int secondardId;

	/** The private action for toggling raster enabled state. */
	private IAction plotTypeAction;

	/** The private action for creating a new plot connection */
	private IAction newPlotViewAction;

	private IMenuManager menu;

	private PlotFftMenuAction fftSizeMenu;
	private PlotModeMenuAction plotModeMenu;
	private Action frameSizeAction;

	private PlotPageBook2 plotPageBook;

	private DisposeListener disposeListener = new DisposeListener() {

		@Override
		public void widgetDisposed(DisposeEvent e) {
			if (!diposed && !PlatformUI.getWorkbench().isClosing()) {
				getSite().getPage().hideView(PlotView2.this);
			}
		}
	};

	private boolean diposed;

	private FftNumAvgControls fftControls;
	private FrameSizeControls frameSizeControls;

	private PlotSettingsAction plotSettingsAction;

	private Composite parent;

	@Override
	public void createPartControl(final Composite parent) {
		this.parent = parent;
		GridLayout layout = new GridLayout(3, false);
		layout.marginBottom = 0;
		layout.marginHeight = 0;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		layout.marginTop = 0;
		layout.marginWidth = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		parent.setLayout(layout);
		this.plotPageBook = new PlotPageBook2(parent, SWT.NONE);
		this.plotPageBook.addPropertyChangeListener(event -> {
			if (PlotPageBook2.PROP_SOURCES.equals(event.getPropertyName())) {
				final boolean hasFft = hasFft();
				if (parent == null || parent.isDisposed()) {
					return;
				}
				parent.getDisplay().asyncExec(() -> {
					updateFftSizeMenu(hasFft);
					updateFftControls(hasFft);
				});
				if (event.getNewValue() instanceof PlotSource) {
					PlotSource source = (PlotSource) event.getNewValue();
					PlotEventChannelForwarder.forwardEvents(plotPageBook, source.getInput(), PlotView2.this);
				}
			}
		});
		this.plotPageBook.setLayoutData(GridDataFactory.fillDefaults().indent(0, 0).grab(true, true).create());

		this.plotPageBook.addDisposeListener(disposeListener);
		this.plotPageBook.getSharedPlotBlockPreferences().addPropertyChangeListener(event -> {
			if (PlotPreferences.ENABLE_QUICK_CONTROLS.isEvent(event)) {
				final boolean hasFft = hasFft();
				if (parent == null || parent.isDisposed()) {
					return;
				}
				parent.getDisplay().asyncExec(() -> {
					updateFftControls(hasFft);
				});
			}
		});
		createActions();
		createToolBars();
		createMenu();
	}

	@Override
	public void dispose() {
		this.diposed = true;
		if (this.plotPageBook != null && !plotPageBook.isDisposed()) {
			this.plotPageBook.removeDisposeListener(disposeListener);
			this.plotPageBook = null;
		}
		super.dispose();
	}

	public IDisposable addPlotSource(@NonNull PlotSource plotSource) {
		return this.plotPageBook.addSource(plotSource);
	}

	@Override
	public void setFocus() {
		if (this.plotPageBook != null && !this.plotPageBook.isDisposed()) {
			this.plotPageBook.setFocus();
		}
	}

	private void createMenu() {
		final IActionBars bars = getViewSite().getActionBars();
		menu = bars.getMenuManager();
		menu.add(this.newPlotViewAction);
		menu.add(this.plotTypeAction);
		menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		menu.add(plotSettingsAction);
	}

	/**
	 * Create the view toolbars.
	 */
	private void createToolBars() {
		final IActionBars bars = getViewSite().getActionBars();

		final IToolBarManager toolBarManager = bars.getToolBarManager();
		toolBarManager.add(plotModeMenu);
		toolBarManager.add(this.fftSizeMenu);
		toolBarManager.add(this.plotTypeAction);
		toolBarManager.add(this.frameSizeAction);
		toolBarManager.add(new Separator());
		toolBarManager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	public static String createSecondaryId() {
		return String.valueOf(PlotView2.secondardId++);
	}

	/** Creates the actions. **/
	private void createActions() {
		// Toolbar actions
		this.plotModeMenu = new PlotModeMenuAction(plotPageBook);
		this.fftSizeMenu = new PlotFftMenuAction(plotPageBook);
		this.plotTypeAction = new PlotTypeMenuAction(plotPageBook);
		this.frameSizeAction = new Action("Frame Size", IAction.AS_CHECK_BOX) {

			{
				ImageDescriptor img = AbstractUIPlugin.imageDescriptorFromPlugin(PlotActivator.PLUGIN_ID, "icons/elcl16/frameSize.png"); //$NON-NLS-1$
				setImageDescriptor(img);
			}

			@Override
			public void run() {
				updateFrameSizeControls(isChecked());
			}
		};

		// Menu actions
		this.newPlotViewAction = createNewPlotViewAction();
		this.plotSettingsAction = new PlotSettingsAction(plotPageBook);

		boolean hasFft = hasFft();
		updateFftSizeMenu(hasFft);
		updateFftControls(hasFft);
	}

	private boolean hasFft() {
		for (PlotSource s : plotPageBook.getSources()) {
			if (s.getFftBlockSettings() != null) {
				return true;
			}
		}
		return false;
	}

	private void updateFftSizeMenu(boolean hasFft) {
		if (fftSizeMenu == null) {
			return;
		}
		fftSizeMenu.setEnabled(hasFft);
	}

	private void updateFftControls(boolean hasFft) {
		if (parent == null || parent.isDisposed()) {
			return;
		}
		boolean controlsEnabled = PlotPreferences.ENABLE_QUICK_CONTROLS.getValue(plotPageBook.getSharedPlotBlockPreferences());
		if (!hasFft || !controlsEnabled) {
			if (this.fftControls != null) {
				this.fftControls.dispose();
				this.fftControls = null;
				this.parent.layout(true, true);
			}
			return;
		}

		if (this.fftControls != null) {
			return;
		}
		this.fftControls = new FftNumAvgControls(plotPageBook, parent);
		this.fftControls.setLayoutData(GridDataFactory.fillDefaults().grab(false, true).create());
		this.parent.layout(true, true);
		return;
	}

	private void updateFrameSizeControls(boolean visible) {
		if (parent == null || parent.isDisposed()) {
			return;
		}
		if (!visible) {
			if (this.frameSizeControls != null) {
				this.frameSizeControls.dispose();
				this.frameSizeControls = null;
				this.parent.layout(true, true);
			}
			return;
		}

		if (this.frameSizeControls != null) {
			return;
		}
		this.frameSizeControls = new FrameSizeControls(plotPageBook, parent);
		this.frameSizeControls.setLayoutData(GridDataFactory.fillDefaults().grab(false, true).create());
		this.parent.layout(true, true);
		return;
	}

	private IAction createNewPlotViewAction() {
		IAction action = new Action() {
			@Override
			public void run() {
				try {
					final IViewPart newView = getSite().getPage().showView(getSite().getId(), PlotView2.createSecondaryId(), IWorkbenchPage.VIEW_ACTIVATE);
					if (newView instanceof PlotView2) {
						final PlotView2 newPlotView = (PlotView2) newView;
						newPlotView.setPartName(getPartName());
						newPlotView.setTitleToolTip(getTitleToolTip());
						newPlotView.getPlotPageBook().showPlot(plotPageBook.getCurrentType());
						for (PlotSource source : plotPageBook.getSources()) {
							newPlotView.addPlotSource(source);
						}
						PlotSettings settings = plotPageBook.getActivePlotWidget().getPlotSettings();
						settings.setPlotType(null);
						newPlotView.getPlotPageBook().getActivePlotWidget().applySettings(settings);
					}
				} catch (final PartInitException e) {
					StatusManager.getManager().handle(new Status(IStatus.ERROR, PlotActivator.PLUGIN_ID, "Failed to open new Plot View", e),
						StatusManager.SHOW | StatusManager.LOG);
				}
			} // end method
		};
		action.setEnabled(true);
		action.setText("New Plot View");
		action.setToolTipText("Open a new Plot View with all the same plots.");
		return action;
	}

	@Override
	public PlotPageBook2 getPlotPageBook() {
		return plotPageBook;
	}

	@Override
	public void setPartName(String partName) {
		super.setPartName(partName);
	}

	@Override
	public void setTitleToolTip(String toolTip) {
		super.setTitleToolTip(toolTip);
	}
}
