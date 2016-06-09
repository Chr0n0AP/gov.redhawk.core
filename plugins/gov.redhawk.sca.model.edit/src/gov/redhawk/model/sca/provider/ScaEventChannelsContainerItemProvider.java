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
package gov.redhawk.model.sca.provider;

import gov.redhawk.model.sca.ScaDomainManager;
import gov.redhawk.model.sca.ScaPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITableItemLabelProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link gov.redhawk.model.sca.ScaWaveformsContainer} object.
 * <!-- begin-user-doc -->
 * @since 12.2
 * <!-- end-user-doc -->
 * @generated
 */
public class ScaEventChannelsContainerItemProvider extends TransientItemProvider implements IEditingDomainItemProvider, IStructuredItemContentProvider,
		ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource, ITableItemLabelProvider {

	/**
	 * @since 11.0
	 */
	public ScaEventChannelsContainerItemProvider(AdapterFactory adapterFactory, EObject object) {
		super(adapterFactory, object);
	}

	@Override
	public Collection< ? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(ScaPackage.Literals.SCA_DOMAIN_MANAGER__EVENT_CHANNELS);
		}
		return childrenFeatures;
	}

	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/ScaEventChannelsContainer"));
	}

	@Override
	public String getText(Object object) {
		return "Event Channels";
	}

	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(ScaDomainManager.class)) {
		case ScaPackage.SCA_DOMAIN_MANAGER__EVENT_CHANNELS:
			fireNotifyChanged(new ViewerNotification(notification, this, true, false));
			return;
		default:
		}
		super.notifyChanged(notification);
	}

	@Override
	public Object getParent(Object object) {
		return target;
	}

}
