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
package gov.redhawk.sca.sad.diagram.providers;

import java.util.List;

import org.eclipse.draw2d.Label;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.diagram.ui.editparts.INodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.AbstractDecorator;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.CreateDecoratorsOperation;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorProvider;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorTarget;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import gov.redhawk.diagram.DiagramUtil;
import gov.redhawk.sca.sad.diagram.RedhawkSadDiagramPlugin;
import gov.redhawk.sca.sad.diagram.edit.parts.SadComponentInstantiationEditPart;
import mil.jpeojtrs.sca.diagram.figures.ComponentInstantiationFigure;
import mil.jpeojtrs.sca.partitioning.ComponentFile;
import mil.jpeojtrs.sca.sad.SadComponentInstantiation;
import mil.jpeojtrs.sca.sad.SadComponentPlacement;
import mil.jpeojtrs.sca.sad.SadPackage;
import mil.jpeojtrs.sca.sad.SoftwareAssembly;
import mil.jpeojtrs.sca.sad.diagram.edit.parts.ComponentPlacementEditPart;
import mil.jpeojtrs.sca.sad.diagram.part.SadVisualIDRegistry;

/**
 * @since 2.0
 */
public class MissingSoftPkgDecoratorProvider extends AbstractProvider implements IDecoratorProvider {

	private static class MissingSoftPkgProvider extends AbstractDecorator {

		private final Label toolTip = new Label("Component not found in the target SDR", //$NON-NLS-1$
			getImage());

		private final Adapter listener = new EContentAdapter() {
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void notifyChanged(final Notification msg) {
				super.notifyChanged(msg);
				if (msg.getEventType() == Notification.SET || msg.getEventType() == Notification.REMOVE) {
					if (msg.getNotifier() instanceof SadComponentPlacement) {
						switch (msg.getFeatureID(SadComponentPlacement.class)) {
						case SadPackage.SAD_PARTITIONING__COMPONENT_PLACEMENT:
							refresh();
							break;
						default:
							break;
						}
					}
				}
			}
		};

		public MissingSoftPkgProvider(final IDecoratorTarget decoratorTarget) {
			super(decoratorTarget);
		}

		@Override
		public void activate() {
			refresh();
		}

		private Image getImage() {
			return RedhawkSadDiagramPlugin.getDefault().getBundledImage("icons/obj16/error_x12.png");
		}

		private boolean refreshing;

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void refresh() {
			if (refreshing) {
				return;
			}
			refreshing = true;
			try {
				removeDecoration();
				final View view = (View) getDecoratorTarget().getAdapter(View.class);
				if (view == null || view.eResource() == null) {
					return;
				}
				final EditPart editPart = (EditPart) getDecoratorTarget().getAdapter(EditPart.class);
				if (editPart instanceof ComponentPlacementEditPart) {
					final ComponentPlacementEditPart compPart = (ComponentPlacementEditPart) editPart;

					if (compPart.getRoot() == null) {
						return;
					}

					if (view.getElement() instanceof SadComponentPlacement) {
						final SadComponentPlacement comp = (SadComponentPlacement) view.getElement();
						final SoftwareAssembly sad = SoftwareAssembly.Util.getSoftwareAssembly(comp.eResource());
						if (sad != null && DiagramUtil.isDiagramLocalSandbox(sad.eResource())) {
							return;
						}
						if (!comp.getComponentInstantiation().isEmpty()) {
							final SadComponentInstantiation inst = comp.getComponentInstantiation().get(0);
							final ComponentFile softPkgFile = comp.getComponentFileRef().getFile();

							if ((softPkgFile == null || softPkgFile.getSoftPkg() == null || softPkgFile.getSoftPkg().eIsProxy())
								&& compPart instanceof org.eclipse.gef.GraphicalEditPart) {
								int margin = -12; // SUPPRESS CHECKSTYLE MagicNumber
								if (compPart instanceof org.eclipse.gef.GraphicalEditPart) {
									margin = MapModeUtil.getMapMode(((org.eclipse.gef.GraphicalEditPart) compPart).getFigure()).DPtoLP(margin);
								}

								setDecoration(getDecoratorTarget().addShapeDecoration(getImage(), IDecoratorTarget.Direction.NORTH_WEST, margin - 30, false)); // SUPPRESS
																																								// CHECKSTYLE
																																								// MagicNumber
								getDecoration().setToolTip(this.toolTip);

								final EditPart foundPart = MissingSoftPkgDecoratorProvider.getEditPartFor(editPart, inst, INodeEditPart.class);

								if (foundPart != null) {
									final ComponentInstantiationFigure figure = (ComponentInstantiationFigure) ((SadComponentInstantiationEditPart) foundPart).getContentPane();
									figure.setLineStyle(SWT.LINE_DASH);
								}
							}
						}

						if (sad != null && !sad.eAdapters().contains(this.listener)) {
							sad.eAdapters().add(this.listener);
						}
					}
				}
			} finally {
				refreshing = false;
			}
		}
	}

	private static final String KEY = "missingSoftPkg"; //$NON-NLS-1$

	@SuppressWarnings("unchecked")
	private static EditPart getEditPartFor(final EditPart hostPart, final EObject element, @SuppressWarnings("rawtypes") final Class expectedClass) {
		if (element != null && !(element instanceof View)) {
			final EditPartViewer viewer = hostPart.getViewer();
			if (viewer instanceof IDiagramGraphicalViewer) {
				List<EditPart> parts = ((IDiagramGraphicalViewer) viewer).findEditPartsForElement(EMFCoreUtil.getProxyID(element), INodeEditPart.class);

				if (parts.isEmpty()) {
					// reach for the container's editpart instead and force it
					// to refresh
					final EObject container = element.eContainer();
					final EditPart containerEP = MissingSoftPkgDecoratorProvider.getEditPartFor(hostPart, container, INodeEditPart.class);
					if (Display.getCurrent() != null) {
						if (containerEP != null) {
							try {
								containerEP.refresh();
							} catch (NullPointerException e) {
								// PASS Ignore bug in GMF
							}
							parts = ((IDiagramGraphicalViewer) viewer).findEditPartsForElement(EMFCoreUtil.getProxyID(element), INodeEditPart.class);
						}
					}
				}

				if (!parts.isEmpty()) {
					return parts.get(0);
				}
			}
		}

		return (EditPart) hostPart.getViewer().getEditPartRegistry().get(element);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createDecorators(final IDecoratorTarget decoratorTarget) {
		return;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean provides(final IOperation operation) {
		if (!(operation instanceof CreateDecoratorsOperation)) {
			return false;
		}
		final IDecoratorTarget decoratorTarget = ((CreateDecoratorsOperation) operation).getDecoratorTarget();
		final View view = (View) decoratorTarget.getAdapter(View.class);
		return view != null && mil.jpeojtrs.sca.sad.diagram.edit.parts.SoftwareAssemblyEditPart.MODEL_ID.equals(SadVisualIDRegistry.getModelID(view));
	}
}
