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

// BEGIN GENERATED CODE
package gov.redhawk.model.sca.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMap.ValueListIterator;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.IntHolder;
import org.omg.CORBA.SystemException;

import CF.Application;
import CF.ApplicationHelper;
import CF.ApplicationOperations;
import CF.ComponentType;
import CF.DataType;
import CF.DeviceAssignmentType;
import CF.LifeCycleOperations;
import CF.LogEvent;
import CF.PortSupplierOperations;
import CF.PropertiesHolder;
import CF.ResourceOperations;
import CF.TestableObjectOperations;
import CF.UnknownIdentifier;
import CF.UnknownProperties;
import CF.ApplicationPackage.ComponentElementType;
import CF.ApplicationPackage.ComponentProcessIdType;
import CF.LifeCyclePackage.InitializeError;
import CF.LifeCyclePackage.ReleaseError;
import CF.PortSetPackage.PortInfoType;
import CF.PortSupplierPackage.UnknownPort;
import CF.PropertyEmitterPackage.AlreadyInitialized;
import CF.PropertySetPackage.InvalidConfiguration;
import CF.PropertySetPackage.PartialConfiguration;
import CF.ResourcePackage.StartError;
import CF.ResourcePackage.StopError;
import CF.TestableObjectPackage.UnknownTest;
import gov.redhawk.model.sca.IRefreshable;
import gov.redhawk.model.sca.ProfileObjectWrapper;
import gov.redhawk.model.sca.RefreshDepth;
import gov.redhawk.model.sca.ScaComponent;
import gov.redhawk.model.sca.ScaDomainManager;
import gov.redhawk.model.sca.ScaDomainManagerFileSystem;
import gov.redhawk.model.sca.ScaModelPlugin;
import gov.redhawk.model.sca.ScaPackage;
import gov.redhawk.model.sca.ScaPort;
import gov.redhawk.model.sca.ScaPortContainer;
import gov.redhawk.model.sca.ScaWaveform;
import gov.redhawk.model.sca.commands.MergePortsCommand;
import gov.redhawk.model.sca.commands.MergePortsCommand.PortData;
import gov.redhawk.model.sca.commands.ScaModelCommand;
import gov.redhawk.model.sca.commands.ScaModelCommandWithResult;
import gov.redhawk.model.sca.commands.ScaWaveformMergeComponentsCommand;
import gov.redhawk.model.sca.commands.SetLocalAttributeCommand;
import gov.redhawk.model.sca.commands.UnsetLocalAttributeCommand;
import gov.redhawk.model.sca.commands.VersionedFeature;
import gov.redhawk.model.sca.commands.VersionedFeature.Transaction;
import gov.redhawk.model.sca.util.ModelUtil;
import gov.redhawk.sca.model.internal.ExternalPropertiesUtil;
import gov.redhawk.sca.util.PluginUtil;
import mil.jpeojtrs.sca.partitioning.ComponentProperties;
import mil.jpeojtrs.sca.partitioning.PartitioningPackage;
import mil.jpeojtrs.sca.prf.AbstractProperty;
import mil.jpeojtrs.sca.sad.AssemblyController;
import mil.jpeojtrs.sca.sad.ExternalProperty;
import mil.jpeojtrs.sca.sad.Port;
import mil.jpeojtrs.sca.sad.SadComponentInstantiation;
import mil.jpeojtrs.sca.sad.SadComponentInstantiationRef;
import mil.jpeojtrs.sca.sad.SadPackage;
import mil.jpeojtrs.sca.sad.SoftwareAssembly;
import mil.jpeojtrs.sca.scd.AbstractPort;
import mil.jpeojtrs.sca.scd.Ports;
import mil.jpeojtrs.sca.scd.ScdPackage;
import mil.jpeojtrs.sca.spd.SpdPackage;
import mil.jpeojtrs.sca.util.ScaEcoreUtils;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Waveform</b></em>'.
 * 
 * @since 20.1
 *        <!-- end-user-doc -->
 *        <p>
 *        The following features are implemented:
 *        </p>
 *        <ul>
 *        <li>{@link gov.redhawk.model.sca.impl.ScaWaveformImpl#getPorts <em>Ports</em>}</li>
 *        <li>{@link gov.redhawk.model.sca.impl.ScaWaveformImpl#getComponents <em>Components</em>}</li>
 *        <li>{@link gov.redhawk.model.sca.impl.ScaWaveformImpl#getAssemblyController <em>Assembly Controller</em>}</li>
 *        <li>{@link gov.redhawk.model.sca.impl.ScaWaveformImpl#getDomMgr <em>Dom Mgr</em>}</li>
 *        <li>{@link gov.redhawk.model.sca.impl.ScaWaveformImpl#getIdentifier <em>Identifier</em>}</li>
 *        <li>{@link gov.redhawk.model.sca.impl.ScaWaveformImpl#getName <em>Name</em>}</li>
 *        <li>{@link gov.redhawk.model.sca.impl.ScaWaveformImpl#getStarted <em>Started</em>}</li>
 *        <li>{@link gov.redhawk.model.sca.impl.ScaWaveformImpl#getProfile <em>Profile</em>}</li>
 *        </ul>
 *
 * @generated
 */
public class ScaWaveformImpl extends ScaPropertyContainerImpl<Application, SoftwareAssembly> implements ScaWaveform {
	/**
	 * The cached value of the '{@link #getPorts() <em>Ports</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getPorts()
	 * @generated
	 * @ordered
	 */
	protected EList<ScaPort< ? , ? >> ports;

	/**
	 * The cached value of the '{@link #getComponents() <em>Components</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getComponents()
	 * @generated
	 * @ordered
	 */
	protected EList<ScaComponent> components;

	/**
	 * The cached value of the '{@link #getAssemblyController() <em>Assembly Controller</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getAssemblyController()
	 * @generated
	 * @ordered
	 */
	protected ScaComponent assemblyController;

	/**
	 * This is true if the Assembly Controller reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean assemblyControllerESet;

	/**
	 * The default value of the '{@link #getIdentifier() <em>Identifier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getIdentifier()
	 * @generated
	 * @ordered
	 */
	protected static final String IDENTIFIER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getIdentifier() <em>Identifier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getIdentifier()
	 * @generated
	 * @ordered
	 */
	protected String identifier = IDENTIFIER_EDEFAULT;

	/**
	 * This is true if the Identifier attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean identifierESet;

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * This is true if the Name attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean nameESet;

	/**
	 * The default value of the '{@link #getStarted() <em>Started</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getStarted()
	 * @generated
	 * @ordered
	 */
	protected static final Boolean STARTED_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStarted() <em>Started</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getStarted()
	 * @generated
	 * @ordered
	 */
	protected Boolean started = STARTED_EDEFAULT;

	/**
	 * This is true if the Started attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean startedESet;

	/**
	 * The default value of the '{@link #getProfile() <em>Profile</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getProfile()
	 * @generated
	 * @ordered
	 */
	protected static final String PROFILE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getProfile() <em>Profile</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getProfile()
	 * @generated
	 * @ordered
	 */
	protected String profile = PROFILE_EDEFAULT;

	/**
	 * This is true if the Profile attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean profileESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ScaWaveformImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ScaPackage.Literals.SCA_WAVEFORM;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @since 18.0
	 *        <!-- end-user-doc -->
	 *        This is specialized for the more specific type known in this context.
	 * @generated
	 */
	@Override
	public void setProfileObj(SoftwareAssembly newProfileObj) {
		super.setProfileObj(newProfileObj);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EList<ScaPort< ? , ? >> getPorts() {
		if (ports == null) {
			ports = new EObjectContainmentWithInverseEList.Unsettable<ScaPort< ? , ? >>(ScaPort.class, this, ScaPackage.SCA_WAVEFORM__PORTS,
				ScaPackage.SCA_PORT__PORT_CONTAINER);
		}
		return ports;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void unsetPorts() {
		if (ports != null)
			((InternalEList.Unsettable< ? >) ports).unset();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean isSetPorts() {
		return ports != null && ((InternalEList.Unsettable< ? >) ports).isSet();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EList<ScaComponent> getComponents() {
		if (components == null) {
			components = new EObjectContainmentWithInverseEList.Unsettable<ScaComponent>(ScaComponent.class, this, ScaPackage.SCA_WAVEFORM__COMPONENTS,
				ScaPackage.SCA_COMPONENT__WAVEFORM);
		}
		return components;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void unsetComponents() {
		if (components != null)
			((InternalEList.Unsettable< ? >) components).unset();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean isSetComponents() {
		return components != null && ((InternalEList.Unsettable< ? >) components).isSet();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public ScaComponent getAssemblyController() {
		if (assemblyController != null && assemblyController.eIsProxy()) {
			InternalEObject oldAssemblyController = (InternalEObject) assemblyController;
			assemblyController = (ScaComponent) eResolveProxy(oldAssemblyController);
			if (assemblyController != oldAssemblyController) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ScaPackage.SCA_WAVEFORM__ASSEMBLY_CONTROLLER, oldAssemblyController,
						assemblyController));
			}
		}
		return assemblyController;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ScaComponent basicGetAssemblyController() {
		return assemblyController;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setAssemblyController(ScaComponent newAssemblyController) {
		ScaComponent oldAssemblyController = assemblyController;
		assemblyController = newAssemblyController;
		boolean oldAssemblyControllerESet = assemblyControllerESet;
		assemblyControllerESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScaPackage.SCA_WAVEFORM__ASSEMBLY_CONTROLLER, oldAssemblyController, assemblyController,
				!oldAssemblyControllerESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void unsetAssemblyController() {
		ScaComponent oldAssemblyController = assemblyController;
		boolean oldAssemblyControllerESet = assemblyControllerESet;
		assemblyController = null;
		assemblyControllerESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ScaPackage.SCA_WAVEFORM__ASSEMBLY_CONTROLLER, oldAssemblyController, null,
				oldAssemblyControllerESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean isSetAssemblyController() {
		return assemblyControllerESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public ScaDomainManager getDomMgr() {
		if (eContainerFeatureID() != ScaPackage.SCA_WAVEFORM__DOM_MGR)
			return null;
		return (ScaDomainManager) eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ScaDomainManager basicGetDomMgr() {
		if (eContainerFeatureID() != ScaPackage.SCA_WAVEFORM__DOM_MGR)
			return null;
		return (ScaDomainManager) eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetDomMgr(ScaDomainManager newDomMgr, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject) newDomMgr, ScaPackage.SCA_WAVEFORM__DOM_MGR, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setDomMgr(ScaDomainManager newDomMgr) {
		if (newDomMgr != eInternalContainer() || (eContainerFeatureID() != ScaPackage.SCA_WAVEFORM__DOM_MGR && newDomMgr != null)) {
			if (EcoreUtil.isAncestor(this, newDomMgr))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newDomMgr != null)
				msgs = ((InternalEObject) newDomMgr).eInverseAdd(this, ScaPackage.SCA_DOMAIN_MANAGER__WAVEFORMS, ScaDomainManager.class, msgs);
			msgs = basicSetDomMgr(newDomMgr, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScaPackage.SCA_WAVEFORM__DOM_MGR, newDomMgr, newDomMgr));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setIdentifier(String newIdentifier) {
		String oldIdentifier = identifier;
		identifier = newIdentifier;
		boolean oldIdentifierESet = identifierESet;
		identifierESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScaPackage.SCA_WAVEFORM__IDENTIFIER, oldIdentifier, identifier, !oldIdentifierESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void unsetIdentifier() {
		String oldIdentifier = identifier;
		boolean oldIdentifierESet = identifierESet;
		identifier = IDENTIFIER_EDEFAULT;
		identifierESet = false;
		if (eNotificationRequired())
			eNotify(
				new ENotificationImpl(this, Notification.UNSET, ScaPackage.SCA_WAVEFORM__IDENTIFIER, oldIdentifier, IDENTIFIER_EDEFAULT, oldIdentifierESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean isSetIdentifier() {
		return identifierESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		boolean oldNameESet = nameESet;
		nameESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScaPackage.SCA_WAVEFORM__NAME, oldName, name, !oldNameESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void unsetName() {
		String oldName = name;
		boolean oldNameESet = nameESet;
		name = NAME_EDEFAULT;
		nameESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ScaPackage.SCA_WAVEFORM__NAME, oldName, NAME_EDEFAULT, oldNameESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean isSetName() {
		return nameESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Boolean getStarted() {
		return started;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setStarted(Boolean newStarted) {
		Boolean oldStarted = started;
		started = newStarted;
		boolean oldStartedESet = startedESet;
		startedESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScaPackage.SCA_WAVEFORM__STARTED, oldStarted, started, !oldStartedESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void unsetStarted() {
		Boolean oldStarted = started;
		boolean oldStartedESet = startedESet;
		started = STARTED_EDEFAULT;
		startedESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ScaPackage.SCA_WAVEFORM__STARTED, oldStarted, STARTED_EDEFAULT, oldStartedESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean isSetStarted() {
		return startedESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getProfile() {
		return profile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setProfile(String newProfile) {
		String oldProfile = profile;
		profile = newProfile;
		boolean oldProfileESet = profileESet;
		profileESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScaPackage.SCA_WAVEFORM__PROFILE, oldProfile, profile, !oldProfileESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void unsetProfile() {
		String oldProfile = profile;
		boolean oldProfileESet = profileESet;
		profile = PROFILE_EDEFAULT;
		profileESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ScaPackage.SCA_WAVEFORM__PROFILE, oldProfile, PROFILE_EDEFAULT, oldProfileESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean isSetProfile() {
		return profileESet;
	}

	/**
	 * The default value of the '{@link #getProfileContentType() <em>Profile Content Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * 
	 * @since 9.0
	 *        <!-- end-user-doc -->
	 * @see #getProfileContentType()
	 * @generated NOT
	 * @ordered
	 */
	protected static final String PROFILE_CONTENT_TYPE_EDEFAULT = SadPackage.eCONTENT_TYPE;
	private static final ComponentProcessIdType[] NO_COMPONENT_PROCESS_ID = new ComponentProcessIdType[0];
	private static final ComponentElementType[] NO_COMPONENT_ELEMENT_TYPE = new ComponentElementType[0];
	private static final DeviceAssignmentType[] NO_DEVICE_ASSIGNMENT_TYPE = new DeviceAssignmentType[0];;

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @deprecated Use {@link #fetchComponents(IProgressMonitor, RefreshDepth)}
	 * @since 14.0
	 *        <!-- end-user-doc -->
	 * @throws InterruptedException
	 * @generated NOT
	 */
	@Override
	public EList<ScaComponent> fetchComponents(IProgressMonitor monitor) {
		return fetchComponents(monitor, RefreshDepth.SELF);
	}

	private final VersionedFeature identifierFeature = new VersionedFeature(this, ScaPackage.Literals.SCA_WAVEFORM__IDENTIFIER);

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @since 14.0
	 *        <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String fetchIdentifier(IProgressMonitor monitor) {
		// END GENERATED CODE
		if (isDisposed()) {
			return null;
		}
		if (isSetIdentifier()) {
			return getIdentifier();
		}

		SubMonitor subMonitor = SubMonitor.convert(monitor, "Fetch identifier", 3);
		if (subMonitor.isCanceled()) {
			throw new OperationCanceledException();
		}
		Application localObj = fetchNarrowedObject(subMonitor.newChild(1));

		Transaction transaction = identifierFeature.createTransaction();
		if (localObj != null) {
			if (subMonitor.isCanceled()) {
				throw new OperationCanceledException();
			}
			try {
				String id = localObj.identifier();
				transaction.append(new SetLocalAttributeCommand(this, id, ScaPackage.Literals.SCA_WAVEFORM__IDENTIFIER));
			} catch (SystemException e) {
				Status status = new Status(Status.ERROR, ScaModelPlugin.ID, "Failed to fetch identifier.", e);
				transaction.append(new UnsetLocalAttributeCommand(this, status, ScaPackage.Literals.SCA_WAVEFORM__IDENTIFIER));
			}
			subMonitor.worked(1);
		} else {
			transaction.addCommand(new UnsetLocalAttributeCommand(this, Status.OK_STATUS, ScaPackage.Literals.SCA_WAVEFORM__IDENTIFIER));
		}

		subMonitor.setWorkRemaining(1);
		transaction.commit();
		subMonitor.worked(1);
		subMonitor.done();
		return getIdentifier();
		// BEGIN GENERATED CODE
	}

	private final VersionedFeature nameFeature = new VersionedFeature(this, ScaPackage.Literals.SCA_WAVEFORM__NAME);

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @since 14.0
	 *        <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String fetchName(IProgressMonitor monitor) {
		// END GENERATED CODE
		if (isDisposed()) {
			return null;
		}
		if (isSetName()) {
			return getName();
		}

		SubMonitor subMonitor = SubMonitor.convert(monitor, "Fetch Name", 3);
		if (subMonitor.isCanceled()) {
			throw new OperationCanceledException();
		}
		Application localObj = fetchNarrowedObject(subMonitor.newChild(1));

		Transaction transaction = nameFeature.createTransaction();
		if (localObj != null) {
			if (subMonitor.isCanceled()) {
				throw new OperationCanceledException();
			}
			try {
				String newName = localObj.name();
				transaction.append(new SetLocalAttributeCommand(this, newName, ScaPackage.Literals.SCA_WAVEFORM__NAME));
			} catch (SystemException e) {
				Status status = new Status(Status.ERROR, ScaModelPlugin.ID, "Failed to fetch name.", e);
				transaction.append(new UnsetLocalAttributeCommand(this, status, ScaPackage.Literals.SCA_WAVEFORM__NAME));
			}
			subMonitor.worked(1);
		} else {
			transaction.addCommand(new UnsetLocalAttributeCommand(this, Status.OK_STATUS, ScaPackage.Literals.SCA_WAVEFORM__NAME));
		}

		subMonitor.setWorkRemaining(1);
		transaction.commit();
		subMonitor.worked(1);
		subMonitor.done();
		return getName();
		// BEGIN GENERATED CODE
	}

	private final VersionedFeature startedFeature = new VersionedFeature(this, ScaPackage.Literals.SCA_WAVEFORM__STARTED);

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @since 14.0
	 *        <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public Boolean fetchStarted(IProgressMonitor monitor) {
		// END GENERATED CODE
		if (isDisposed()) {
			return null;
		}

		SubMonitor subMonitor = SubMonitor.convert(monitor, "Fetching started", 4);
		if (subMonitor.isCanceled()) {
			throw new OperationCanceledException();
		}
		Application localObj = fetchNarrowedObject(subMonitor.newChild(1));

		Transaction transaction = startedFeature.createTransaction();
		if (localObj != null) {
			if (subMonitor.isCanceled()) {
				throw new OperationCanceledException();
			}
			try {
				boolean newStarted = localObj.started();
				transaction.append(new SetLocalAttributeCommand(this, newStarted, ScaPackage.Literals.SCA_WAVEFORM__STARTED));
			} catch (BAD_OPERATION e) {
				IStatus startedStatus = new Status(Status.WARNING, ScaModelPlugin.ID, "Does not support 'started' extension.");
				transaction.append(new UnsetLocalAttributeCommand(this, startedStatus, ScaPackage.Literals.SCA_WAVEFORM__STARTED));
			} catch (SystemException e) {
				Status status = new Status(Status.ERROR, ScaModelPlugin.ID, "Failed to fetch started.", e);
				transaction.append(new UnsetLocalAttributeCommand(this, status, ScaPackage.Literals.SCA_WAVEFORM__STARTED));
			}
			subMonitor.worked(1);
		} else {
			transaction.addCommand(new UnsetLocalAttributeCommand(this, Status.OK_STATUS, ScaPackage.Literals.SCA_WAVEFORM__STARTED));
		}

		subMonitor.setWorkRemaining(1);
		transaction.commit();
		subMonitor.worked(1);
		subMonitor.done();
		return getStarted();
		// BEGIN GENERATED CODE
	}

	private final VersionedFeature componentsFeature = new VersionedFeature(this, ScaPackage.Literals.SCA_WAVEFORM__COMPONENTS);

	/**
	 * @since 14.0
	 */
	protected void internalFetchComponents(IProgressMonitor monitor) {
		// END GENERATED CODE
		if (isSetComponents() || isDisposed()) {
			return;
		}

		SubMonitor subMonitor = SubMonitor.convert(monitor, 6);
		if (subMonitor.isCanceled()) {
			throw new OperationCanceledException();
		}
		final Application app = fetchNarrowedObject(subMonitor.newChild(1));

		Transaction transaction = componentsFeature.createTransaction();
		if (app != null) {
			if (subMonitor.isCanceled()) {
				throw new OperationCanceledException();
			}
			final SoftwareAssembly localSad = fetchProfileObject(subMonitor.newChild(1));
			if (localSad != null) {
				// New Components
				ComponentType[] compTypes = null;
				IStatus status = null;
				if (subMonitor.isCanceled()) {
					throw new OperationCanceledException();
				}
				try {
					compTypes = app.registeredComponents();
				} catch (SystemException e) {
					status = new Status(Status.ERROR, ScaModelPlugin.ID, "Failed to fetch components.", e);
				}
				subMonitor.worked(1);
				transaction.addCommand(createMergeComponentsCommand(compTypes, status));
			} else {
				transaction.addCommand(new UnsetLocalAttributeCommand(this, Status.OK_STATUS, ScaPackage.Literals.SCA_WAVEFORM__ASSEMBLY_CONTROLLER));
				transaction.addCommand(new UnsetLocalAttributeCommand(this, Status.OK_STATUS, ScaPackage.Literals.SCA_WAVEFORM__COMPONENTS));
			}
		} else {
			transaction.addCommand(new UnsetLocalAttributeCommand(this, Status.OK_STATUS, ScaPackage.Literals.SCA_WAVEFORM__ASSEMBLY_CONTROLLER));
			transaction.addCommand(new UnsetLocalAttributeCommand(this, Status.OK_STATUS, ScaPackage.Literals.SCA_WAVEFORM__COMPONENTS));
		}

		subMonitor.setWorkRemaining(1);
		transaction.commit();
		subMonitor.worked(1);
		subMonitor.done();
		// BEGIN GENERATED CODE
	}

	/**
	 * @since 15.0
	 */
	protected Command createMergeComponentsCommand(ComponentType[] compTypes, IStatus status) {
		return new ScaWaveformMergeComponentsCommand(this, compTypes, status);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public ScaComponent findComponent(String instantiationId) {
		// END GENERATED CODE
		return getScaComponent(instantiationId);
		// BEGIN GENERATED CODE
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @since 14.0
	 *        <!-- end-user-doc -->
	 * @throws InterruptedException
	 * @generated NOT
	 */
	@Override
	public EList<ScaPort< ? , ? >> fetchPorts(IProgressMonitor monitor) {
		// END GENERATED CODE
		if (isDisposed()) {
			return ECollections.emptyEList();
		}

		SubMonitor subMonitor = SubMonitor.convert(monitor, "Fetching ports", 2);
		if (subMonitor.isCanceled()) {
			throw new OperationCanceledException();
		}
		internalFetchPorts(subMonitor.newChild(1));

		List<ScaPort< ? , ? >> portsCopy = null;
		try {
			portsCopy = ScaModelCommand.runExclusive(this, new RunnableWithResult.Impl<List<ScaPort< ? , ? >>>() {
				@Override
				public void run() {
					setResult(new ArrayList<>(getPorts()));
				}
			});
		} catch (InterruptedException e) {
			throw new OperationCanceledException();
		}
		if (portsCopy != null) {
			SubMonitor portRefresh = subMonitor.newChild(1).setWorkRemaining(portsCopy.size());
			for (ScaPort< ? , ? > port : portsCopy) {
				try {
					if (subMonitor.isCanceled()) {
						throw new OperationCanceledException();
					}
					port.refresh(portRefresh.newChild(1), RefreshDepth.SELF);
				} catch (InterruptedException e) {
					throw new OperationCanceledException();
				}
			}
		}

		subMonitor.done();
		if (portsCopy != null) {
			return ECollections.unmodifiableEList(new BasicEList<ScaPort< ? , ? >>(portsCopy));
		} else {
			return ECollections.emptyEList();
		}
		// BEGIN GENERATED CODE
	}

	private static final EStructuralFeature[] EXTERNAL_PORTS_PATH = { SadPackage.Literals.SOFTWARE_ASSEMBLY__EXTERNAL_PORTS,
		SadPackage.Literals.EXTERNAL_PORTS__PORT };

	/**
	 * EMF feature path from a SAD external port to the ports in the corresponding component's SCD.
	 */
	private static final EStructuralFeature[] EXT_PORT_TO_SCD_PORTS_PATH = { SadPackage.Literals.PORT__COMPONENT_INSTANTIATION_REF,
		PartitioningPackage.Literals.COMPONENT_INSTANTIATION_REF__INSTANTIATION, PartitioningPackage.Literals.COMPONENT_INSTANTIATION__PLACEMENT,
		PartitioningPackage.Literals.COMPONENT_PLACEMENT__COMPONENT_FILE_REF, PartitioningPackage.Literals.COMPONENT_FILE_REF__FILE,
		PartitioningPackage.Literals.COMPONENT_FILE__SOFT_PKG, SpdPackage.Literals.SOFT_PKG__DESCRIPTOR, SpdPackage.Literals.DESCRIPTOR__COMPONENT,
		ScdPackage.Literals.SOFTWARE_COMPONENT__COMPONENT_FEATURES, ScdPackage.Literals.COMPONENT_FEATURES__PORTS};

	private final VersionedFeature portsFeature = new VersionedFeature(this, ScaPackage.Literals.SCA_PORT_CONTAINER__PORTS);

	/**
	 * @since 14.0
	 */
	protected void internalFetchPorts(IProgressMonitor monitor) {
		// END GENERATED CODE
		if (isSetPorts() || isDisposed()) {
			return;
		}

		SubMonitor subMonitor = SubMonitor.convert(monitor, 4);
		if (subMonitor.isCanceled()) {
			throw new OperationCanceledException();
		}
		final Application currentObj = fetchNarrowedObject(subMonitor.newChild(1));

		if (subMonitor.isCanceled()) {
			throw new OperationCanceledException();
		}
		final SoftwareAssembly localProfileObj = fetchProfileObject(subMonitor.newChild(1));

		List<MergePortsCommand.PortData> newPorts = new ArrayList<MergePortsCommand.PortData>();
		final MultiStatus fetchPortsStatus = new MultiStatus(ScaModelPlugin.ID, Status.OK, "Fetch ports status.", null);

		Transaction transaction = portsFeature.createTransaction();
		// Load all of the ports
		EList<Port> externalPorts = ScaEcoreUtils.getFeature(localProfileObj, EXTERNAL_PORTS_PATH);
		if (externalPorts != null) {
			SubMonitor externalPortProgress = subMonitor.newChild(1).setWorkRemaining(externalPorts.size());
			for (final Port port : externalPorts) {
				String id = port.getProvidesIdentifier();
				if (id == null) {
					id = port.getUsesIdentifier();
				}
				if (id == null) {
					id = port.getSupportedIdentifier();
				}
				if (id == null) {
					fetchPortsStatus.add(new Status(IStatus.WARNING, ScaModelPlugin.ID, "External port without a valid identifier cannot be located"));
					externalPortProgress.worked(1);
					continue;
				}

				Ports componentSCDPorts = ScaEcoreUtils.getFeature(port, EXT_PORT_TO_SCD_PORTS_PATH);
				if (componentSCDPorts == null) {
					String msg = String.format("Cannot locate ports for the external port with ID '%s'", id);
					fetchPortsStatus.add(new Status(IStatus.WARNING, ScaModelPlugin.ID, msg));
					externalPortProgress.worked(1);
					continue;
				}

				for (AbstractPort abstractPort : componentSCDPorts.getAllPorts()) {
					if (!id.equals(abstractPort.getName())) {
						continue;
					}

					String portName = port.getExternalName();
					if (portName == null) {
						portName = id;
					}

					if (subMonitor.isCanceled()) {
						throw new OperationCanceledException();
					}
					try {
						org.omg.CORBA.Object portCorbaObj = currentObj.getPort(portName);
						newPorts.add(new PortData(portName, abstractPort, portCorbaObj));
					} catch (UnknownPort e) {
						fetchPortsStatus.add(new Status(Status.ERROR, ScaModelPlugin.ID, "Failed to fetch port '" + portName + "'", e));
					} catch (SystemException e) {
						fetchPortsStatus.add(new Status(Status.ERROR, ScaModelPlugin.ID, "Failed to fetch port '" + portName + "'", e));
					}

					break;
				}

				externalPortProgress.worked(1);
			}
			transaction.addCommand(new MergePortsCommand(this, newPorts, fetchPortsStatus));
		} else {
			transaction.addCommand(new UnsetLocalAttributeCommand(this, null, ScaPackage.Literals.SCA_PORT_CONTAINER__PORTS));
		}

		// Perform the actions
		subMonitor.setWorkRemaining(1);
		transaction.commit();
		subMonitor.done();
		// BEGIN GENERATED CODE
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public ScaComponent getScaComponent(String instantiationId) {
		// END GENERATED CODE
		if (instantiationId == null) {
			return null;
		}
		for (ScaComponent component : getComponents()) {
			if (instantiationId.equals(component.getInstantiationIdentifier())) {
				return component;
			}
		}
		return null;
		// BEGIN GENERATED CODE
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public ScaPort< ? , ? > getScaPort(String name) {
		// END GENERATED CODE
		for (ScaPort< ? , ? > port : getPorts()) {
			if (port.getName().equals(name)) {
				return port;
			}
		}
		return null;
		// BEGIN GENERATED CODE
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @throws InterruptedException
	 * @generated NOT
	 */
	@Override
	public void fetchAttributes(IProgressMonitor monitor) {
		// END GENERATED CODE
		if (isDisposed()) {
			return;
		}

		SubMonitor subMonitor = SubMonitor.convert(monitor, 4);

		if (subMonitor.isCanceled()) {
			throw new OperationCanceledException();
		}
		super.fetchAttributes(subMonitor.newChild(1));

		if (subMonitor.isCanceled()) {
			throw new OperationCanceledException();
		}
		fetchLocalAttributes(subMonitor.newChild(1));

		if (subMonitor.isCanceled()) {
			throw new OperationCanceledException();
		}
		fetchProfileObject(subMonitor.newChild(1));

		if (subMonitor.isCanceled()) {
			throw new OperationCanceledException();
		}
		fetchProperties(subMonitor.newChild(1));

		subMonitor.done();
		// BEGIN GENERATED CODE
	}

	// END GENERATED CODE

	private void fetchLocalAttributes(IProgressMonitor monitor) {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 5);

		if (subMonitor.isCanceled()) {
			throw new OperationCanceledException();
		}
		fetchIdentifier(subMonitor.newChild(1));

		if (subMonitor.isCanceled()) {
			throw new OperationCanceledException();
		}
		fetchStarted(subMonitor.newChild(1));

		if (subMonitor.isCanceled()) {
			throw new OperationCanceledException();
		}
		fetchProfile(subMonitor.newChild(1));

		if (subMonitor.isCanceled()) {
			throw new OperationCanceledException();
		}
		fetchName(subMonitor.newChild(1));

		subMonitor.done();
	}

	@Override
	protected void internalFetchChildren(IProgressMonitor monitor) throws InterruptedException {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 2);

		if (subMonitor.isCanceled()) {
			throw new OperationCanceledException();
		}
		internalFetchComponents(subMonitor.newChild(1));

		if (subMonitor.isCanceled()) {
			throw new OperationCanceledException();
		}
		internalFetchPorts(subMonitor.newChild(1));

		subMonitor.done();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void notifyChanged(Notification msg) {
		super.notifyChanged(msg);
		if (msg.isTouch()) {
			return;
		}
		switch (msg.getFeatureID(ScaWaveform.class)) {
		case ScaPackage.SCA_WAVEFORM__DISPOSED:
		case ScaPackage.SCA_WAVEFORM__OBJ:
			unsetProfile();
			unsetIdentifier();
			unsetName();
			unsetStarted();
			unsetPorts();
			unsetComponents();
			unsetAssemblyController();
			break;
		case ScaPackage.SCA_WAVEFORM__PROFILE:
			if (!PluginUtil.equals(msg.getOldValue(), msg.getNewValue())) {
				unsetProfileURI();
			}
			break;
		case ScaPackage.SCA_WAVEFORM__COMPONENTS:
			switch (msg.getEventType()) {
			case Notification.ADD:
				addedComponent((ScaComponent) msg.getNewValue());
				break;
			case Notification.ADD_MANY:
				for (ScaComponent comp : ((Collection<ScaComponent>) msg.getNewValue())) {
					addedComponent(comp);
				}
				break;
			case Notification.REMOVE:
				removedComponent((ScaComponent) msg.getOldValue());
				break;
			case Notification.REMOVE_MANY:
				for (ScaComponent comp : ((Collection<ScaComponent>) msg.getOldValue())) {
					removedComponent(comp);
				}
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}

	private void removedComponent(ScaComponent oldValue) {
		if (getAssemblyController() == oldValue) {
			setAssemblyController(null);
		}
	}

	private void addedComponent(ScaComponent newValue) {
		SoftwareAssembly sad = getProfileObj();
		if (sad != null) {
			AssemblyController assem = sad.getAssemblyController();
			if (assem != null) {
				SadComponentInstantiationRef ref = assem.getComponentInstantiationRef();
				if (ref != null) {
					String refId = ref.getRefid();
					if (refId != null && refId.equals(newValue.getInstantiationIdentifier())) {
						setAssemblyController(newValue);
					}
				}
			}
		}
	}

	/**
	 * @since 14.0
	 */
	@Override
	protected Application narrow(final org.omg.CORBA.Object obj) {
		return ApplicationHelper.narrow(obj);
	}

	@Override
	protected Class<Application> getCorbaType() {
		return Application.class;
	}

	/**
	 * @since 14.0
	 */
	@Override
	public DeviceAssignmentType[] componentDevices() {
		Application waveform = fetchNarrowedObject(null);
		if (waveform == null) {
			return NO_DEVICE_ASSIGNMENT_TYPE;
		}
		return waveform.componentDevices();
	}

	/**
	 * @since 14.0
	 */
	@Override
	public ComponentElementType[] componentImplementations() {
		Application waveform = fetchNarrowedObject(null);
		if (waveform == null) {
			return NO_COMPONENT_ELEMENT_TYPE;
		}
		return waveform.componentImplementations();
	}

	/**
	 * @since 14.0
	 */
	@Override
	public ComponentElementType[] componentNamingContexts() {
		Application waveform = fetchNarrowedObject(null);
		if (waveform == null) {
			return NO_COMPONENT_ELEMENT_TYPE;
		}
		return waveform.componentNamingContexts();
	}

	/**
	 * @since 14.0
	 */
	@Override
	public ComponentProcessIdType[] componentProcessIds() {
		Application waveform = fetchNarrowedObject(null);
		if (waveform == null) {
			return NO_COMPONENT_PROCESS_ID;
		}
		return waveform.componentProcessIds();
	}

	@Override
	public String name() {
		return getName();
	}

	@Override
	public String profile() {
		return getProfile();
	}

	@Override
	public String identifier() {
		return getIdentifier();
	}

	@Override
	public boolean started() {
		Boolean retVal = getStarted();
		if (retVal == null) {
			return false;
		}
		return retVal;
	}

	@Override
	public void start() throws StartError {
		Application waveform = fetchNarrowedObject(null);
		if (waveform == null) {
			throw new IllegalStateException("CORBA Object is Null");
		}
		waveform.start();
		fetchStarted(null);
		for (Object c : ModelUtil.getAsImmutableList(this, ScaPackage.Literals.SCA_WAVEFORM__COMPONENTS)) {
			((ScaComponent) c).fetchStarted(null);
		}
	}

	@Override
	public void stop() throws StopError {
		Application waveform = fetchNarrowedObject(null);
		if (waveform == null) {
			throw new IllegalStateException("CORBA Object is Null");
		}
		waveform.stop();
		fetchStarted(null);
		for (Object c : ModelUtil.getAsImmutableList(this, ScaPackage.Literals.SCA_WAVEFORM__COMPONENTS)) {
			((ScaComponent) c).fetchStarted(null);
		}
	}

	@Override
	public void initialize() throws InitializeError {
		Application waveform = fetchNarrowedObject(null);
		if (waveform == null) {
			throw new IllegalStateException("CORBA Object is Null");
		}
		waveform.initialize();
		try {
			refresh(null, RefreshDepth.SELF);
		} catch (InterruptedException e) {
			// PASS
		}
	}

	private boolean released;

	@Override
	public void releaseObject() throws ReleaseError {
		// Ensure we only call releaseObject() once (assuming no exception thrown) 
		if (released) {
			return;
		}
		final Application waveform = fetchNarrowedObject(null);
		if (waveform != null) {
			waveform.releaseObject();
		}
		released = true;

		TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(this);
		Command command = new ScaModelCommand() {

			@Override
			public void execute() {
				EcoreUtil.delete(ScaWaveformImpl.this);
			}
		};

		// The domain may be null if the waveform has already been released.
		if (domain != null) {
			domain.getCommandStack().execute(command);
		} else {
			ScaModelCommand.execute(this, command);
		}
	}

	// BEGIN GENERATED CODE

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ScaPackage.SCA_WAVEFORM__PORTS:
			return ((InternalEList<InternalEObject>) (InternalEList< ? >) getPorts()).basicAdd(otherEnd, msgs);
		case ScaPackage.SCA_WAVEFORM__COMPONENTS:
			return ((InternalEList<InternalEObject>) (InternalEList< ? >) getComponents()).basicAdd(otherEnd, msgs);
		case ScaPackage.SCA_WAVEFORM__DOM_MGR:
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			return basicSetDomMgr((ScaDomainManager) otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ScaPackage.SCA_WAVEFORM__PORTS:
			return ((InternalEList< ? >) getPorts()).basicRemove(otherEnd, msgs);
		case ScaPackage.SCA_WAVEFORM__COMPONENTS:
			return ((InternalEList< ? >) getComponents()).basicRemove(otherEnd, msgs);
		case ScaPackage.SCA_WAVEFORM__DOM_MGR:
			return basicSetDomMgr(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
		case ScaPackage.SCA_WAVEFORM__DOM_MGR:
			return eInternalContainer().eInverseRemove(this, ScaPackage.SCA_DOMAIN_MANAGER__WAVEFORMS, ScaDomainManager.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case ScaPackage.SCA_WAVEFORM__PORTS:
			return getPorts();
		case ScaPackage.SCA_WAVEFORM__COMPONENTS:
			return getComponents();
		case ScaPackage.SCA_WAVEFORM__ASSEMBLY_CONTROLLER:
			if (resolve)
				return getAssemblyController();
			return basicGetAssemblyController();
		case ScaPackage.SCA_WAVEFORM__DOM_MGR:
			if (resolve)
				return getDomMgr();
			return basicGetDomMgr();
		case ScaPackage.SCA_WAVEFORM__IDENTIFIER:
			return getIdentifier();
		case ScaPackage.SCA_WAVEFORM__NAME:
			return getName();
		case ScaPackage.SCA_WAVEFORM__STARTED:
			return getStarted();
		case ScaPackage.SCA_WAVEFORM__PROFILE:
			return getProfile();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case ScaPackage.SCA_WAVEFORM__PORTS:
			getPorts().clear();
			getPorts().addAll((Collection< ? extends ScaPort< ? , ? >>) newValue);
			return;
		case ScaPackage.SCA_WAVEFORM__COMPONENTS:
			getComponents().clear();
			getComponents().addAll((Collection< ? extends ScaComponent>) newValue);
			return;
		case ScaPackage.SCA_WAVEFORM__ASSEMBLY_CONTROLLER:
			setAssemblyController((ScaComponent) newValue);
			return;
		case ScaPackage.SCA_WAVEFORM__DOM_MGR:
			setDomMgr((ScaDomainManager) newValue);
			return;
		case ScaPackage.SCA_WAVEFORM__IDENTIFIER:
			setIdentifier((String) newValue);
			return;
		case ScaPackage.SCA_WAVEFORM__NAME:
			setName((String) newValue);
			return;
		case ScaPackage.SCA_WAVEFORM__STARTED:
			setStarted((Boolean) newValue);
			return;
		case ScaPackage.SCA_WAVEFORM__PROFILE:
			setProfile((String) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case ScaPackage.SCA_WAVEFORM__PORTS:
			unsetPorts();
			return;
		case ScaPackage.SCA_WAVEFORM__COMPONENTS:
			unsetComponents();
			return;
		case ScaPackage.SCA_WAVEFORM__ASSEMBLY_CONTROLLER:
			unsetAssemblyController();
			return;
		case ScaPackage.SCA_WAVEFORM__DOM_MGR:
			setDomMgr((ScaDomainManager) null);
			return;
		case ScaPackage.SCA_WAVEFORM__IDENTIFIER:
			unsetIdentifier();
			return;
		case ScaPackage.SCA_WAVEFORM__NAME:
			unsetName();
			return;
		case ScaPackage.SCA_WAVEFORM__STARTED:
			unsetStarted();
			return;
		case ScaPackage.SCA_WAVEFORM__PROFILE:
			unsetProfile();
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case ScaPackage.SCA_WAVEFORM__PORTS:
			return isSetPorts();
		case ScaPackage.SCA_WAVEFORM__COMPONENTS:
			return isSetComponents();
		case ScaPackage.SCA_WAVEFORM__ASSEMBLY_CONTROLLER:
			return isSetAssemblyController();
		case ScaPackage.SCA_WAVEFORM__DOM_MGR:
			return basicGetDomMgr() != null;
		case ScaPackage.SCA_WAVEFORM__IDENTIFIER:
			return isSetIdentifier();
		case ScaPackage.SCA_WAVEFORM__NAME:
			return isSetName();
		case ScaPackage.SCA_WAVEFORM__STARTED:
			return isSetStarted();
		case ScaPackage.SCA_WAVEFORM__PROFILE:
			return isSetProfile();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class< ? > baseClass) {
		if (baseClass == LifeCycleOperations.class) {
			switch (derivedFeatureID) {
			default:
				return -1;
			}
		}
		if (baseClass == TestableObjectOperations.class) {
			switch (derivedFeatureID) {
			default:
				return -1;
			}
		}
		if (baseClass == PortSupplierOperations.class) {
			switch (derivedFeatureID) {
			default:
				return -1;
			}
		}
		if (baseClass == ResourceOperations.class) {
			switch (derivedFeatureID) {
			default:
				return -1;
			}
		}
		if (baseClass == ApplicationOperations.class) {
			switch (derivedFeatureID) {
			default:
				return -1;
			}
		}
		if (baseClass == ScaPortContainer.class) {
			switch (derivedFeatureID) {
			case ScaPackage.SCA_WAVEFORM__PORTS:
				return ScaPackage.SCA_PORT_CONTAINER__PORTS;
			default:
				return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class< ? > baseClass) {
		if (baseClass == LifeCycleOperations.class) {
			switch (baseFeatureID) {
			default:
				return -1;
			}
		}
		if (baseClass == TestableObjectOperations.class) {
			switch (baseFeatureID) {
			default:
				return -1;
			}
		}
		if (baseClass == PortSupplierOperations.class) {
			switch (baseFeatureID) {
			default:
				return -1;
			}
		}
		if (baseClass == ResourceOperations.class) {
			switch (baseFeatureID) {
			default:
				return -1;
			}
		}
		if (baseClass == ApplicationOperations.class) {
			switch (baseFeatureID) {
			default:
				return -1;
			}
		}
		if (baseClass == ScaPortContainer.class) {
			switch (baseFeatureID) {
			case ScaPackage.SCA_PORT_CONTAINER__PORTS:
				return ScaPackage.SCA_WAVEFORM__PORTS;
			default:
				return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (identifier: ");
		if (identifierESet)
			result.append(identifier);
		else
			result.append("<unset>");
		result.append(", name: ");
		if (nameESet)
			result.append(name);
		else
			result.append("<unset>");
		result.append(", started: ");
		if (startedESet)
			result.append(started);
		else
			result.append("<unset>");
		result.append(", profile: ");
		if (profileESet)
			result.append(profile);
		else
			result.append("<unset>");
		result.append(')');
		return result.toString();
	}

	// END GENERATED CODE

	/**
	 * @since 14.0
	 */
	@Override
	public void runTest(final int testid, final PropertiesHolder testValues) throws UnknownTest, UnknownProperties {
		Application waveform = fetchNarrowedObject(null);
		if (waveform == null) {
			throw new IllegalStateException("CORBA Object is Null");
		}
		waveform.runTest(testid, testValues);
	}

	/**
	 * @since 20.0
	 */
	@Override
	public void initializeProperties(final DataType[] configProperties) throws AlreadyInitialized, InvalidConfiguration, PartialConfiguration {
		Application waveform = fetchNarrowedObject(null);
		if (waveform == null) {
			throw new IllegalStateException("CORBA Object is null");
		}
		waveform.initializeProperties(configProperties);
	}

	@Override
	public void configure(final DataType[] configProperties) throws InvalidConfiguration, PartialConfiguration {
		Application waveform = fetchNarrowedObject(null);
		if (waveform == null) {
			return;
		}
		waveform.configure(configProperties);
		fetchProperties(null);
	}

	@Override
	public void query(final PropertiesHolder configProperties) throws UnknownProperties {
		Application waveform = fetchNarrowedObject(null);
		if (waveform != null) {
			waveform.query(configProperties);
		}
	}

	@Override
	public org.omg.CORBA.Object getPort(final String name) throws UnknownPort {
		ScaPort< ? , ? > scaPort = getScaPort(name);
		if (scaPort != null) {
			return scaPort.fetchNarrowedObject(null);
		}
		return null;
	}

	/**
	 * @since 20.0
	 */
	public PortInfoType[] getPortSet() {
		Application waveform = fetchNarrowedObject(null);
		if (waveform == null) {
			throw new IllegalStateException("CORBA Object is null");
		}
		return waveform.getPortSet();
	}

	private static final EStructuralFeature[] PRF_PATH = { SadPackage.Literals.SOFTWARE_ASSEMBLY__ASSEMBLY_CONTROLLER,
		SadPackage.Literals.ASSEMBLY_CONTROLLER__COMPONENT_INSTANTIATION_REF, PartitioningPackage.Literals.COMPONENT_INSTANTIATION_REF__INSTANTIATION,
		PartitioningPackage.Literals.COMPONENT_INSTANTIATION__PLACEMENT, PartitioningPackage.Literals.COMPONENT_PLACEMENT__COMPONENT_FILE_REF,
		PartitioningPackage.Literals.COMPONENT_FILE_REF__FILE, PartitioningPackage.Literals.COMPONENT_FILE__SOFT_PKG,
		SpdPackage.Literals.SOFT_PKG__PROPERTY_FILE, SpdPackage.Literals.PROPERTY_FILE__PROPERTIES };

	private static final EStructuralFeature[] INST_PRF_PATH = { PartitioningPackage.Literals.COMPONENT_INSTANTIATION__PLACEMENT,
		PartitioningPackage.Literals.COMPONENT_PLACEMENT__COMPONENT_FILE_REF, PartitioningPackage.Literals.COMPONENT_FILE_REF__FILE,
		PartitioningPackage.Literals.COMPONENT_FILE__SOFT_PKG, SpdPackage.Literals.SOFT_PKG__PROPERTY_FILE, SpdPackage.Literals.PROPERTY_FILE__PROPERTIES };

	private static final EStructuralFeature[] ASMBLY_CTL_COMP_PROPS_PATH = { SadPackage.Literals.SOFTWARE_ASSEMBLY__ASSEMBLY_CONTROLLER,
		SadPackage.Literals.ASSEMBLY_CONTROLLER__COMPONENT_INSTANTIATION_REF, PartitioningPackage.Literals.COMPONENT_INSTANTIATION_REF__INSTANTIATION,
		PartitioningPackage.Literals.COMPONENT_INSTANTIATION__COMPONENT_PROPERTIES };

	@Override
	protected List<AbstractProperty> fetchPropertyDefinitions(IProgressMonitor monitor) {
		SoftwareAssembly localProfile = fetchProfileObject(monitor);
		if (localProfile == null) {
			return Collections.emptyList();
		}

		List<AbstractProperty> retVal = new ArrayList<AbstractProperty>();
		retVal.addAll(getAssemblyControllerProps(localProfile));
		retVal.addAll(getExternalControllerProps(localProfile));
		return retVal;
	}

	private Collection< ? extends AbstractProperty> getAssemblyControllerProps(SoftwareAssembly localProfile) {
		List<AbstractProperty> values = new ArrayList<AbstractProperty>();

		// Create a map of any properties whose values have been overridden in the SAD.XML
		HashMap<String, EObject> overriddenPropsMap = new HashMap<String, EObject>();
		if (localProfile.getAssemblyController() != null) {
			ComponentProperties componentProps = ScaEcoreUtils.getFeature(localProfile, ASMBLY_CTL_COMP_PROPS_PATH);
			componentProps = localProfile.getAssemblyController().getComponentInstantiationRef().getInstantiation().getComponentProperties();
			if (componentProps != null) {
				overriddenPropsMap = ExternalPropertiesUtil.getOverriddenProperties(componentProps);
			}
		}

		// All Assembly Controller properties are external, so add those here.  Check for any that were overridden in the SAD.XML 
		mil.jpeojtrs.sca.prf.Properties propDefs = ScaEcoreUtils.getFeature(localProfile, PRF_PATH);
		if (propDefs != null) {
			for (ValueListIterator<Object> i = propDefs.getProperties().valueListIterator(); i.hasNext();) {
				Object propDef = i.next();
				if (propDef instanceof AbstractProperty) {
					AbstractProperty property = (AbstractProperty) propDef;
					if (overriddenPropsMap.containsKey(property.getId())) {
						property = ExternalPropertiesUtil.copyProperty(property, overriddenPropsMap);
						values.add(property);
					} else {
						values.add(property);
					}
				}
			}
		}

		return values;
	}

	private Collection< ? extends AbstractProperty> getExternalControllerProps(SoftwareAssembly localProfile) {
		List<AbstractProperty> values = new ArrayList<AbstractProperty>();

		// Add any external properties from components other than the Assembly Controller. Check for any that were overridden in the SAD.XML
		Map<String, EObject> overriddenPropsMap = new HashMap<String, EObject>();
		if (localProfile.getExternalProperties() != null) {
			EList<SadComponentInstantiation> insts = localProfile.getAllComponentInstantiations();
			for (ExternalProperty externalProp : localProfile.getExternalProperties().getProperties()) {
				for (SadComponentInstantiation inst : insts) {
					if (inst.getId().equals(externalProp.getCompRefID())) {
						mil.jpeojtrs.sca.prf.Properties instProperties = ScaEcoreUtils.getFeature(inst, INST_PRF_PATH);
						overriddenPropsMap = ExternalPropertiesUtil.getOverriddenProperties(inst.getComponentProperties());
						if (instProperties != null) {
							AbstractProperty prop = instProperties.getProperty(externalProp.getPropID());
							if (prop != null) {
								if (externalProp.getExternalPropID() != null) {
									prop = ExternalPropertiesUtil.copyProperty(prop, overriddenPropsMap);
									prop.setId(externalProp.getExternalPropID());
								}
								values.add(prop);
							}
						}
						break; // Check the next external property
					}
				}
			}
		}

		return values;
	}

	private final VersionedFeature profileObjectFeature = new VersionedFeature(this, ScaPackage.Literals.PROFILE_OBJECT_WRAPPER__PROFILE_OBJ);

	// BEGIN GENERATED CODE

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @since 14.0
	 *        <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public SoftwareAssembly fetchProfileObject(IProgressMonitor monitor) {
		if (isDisposed()) {
			return null;
		}
		if (isSetProfileObj()) {
			return getProfileObj();
		}

		Transaction transaction = profileObjectFeature.createTransaction();
		transaction.addCommand(ProfileObjectWrapper.Util.fetchProfileObject(monitor, this, SoftwareAssembly.class, SoftwareAssembly.EOBJECT_PATH));
		transaction.commit();
		return getProfileObj();
	}

	/**
	 * @since 14.0
	 */
	@Override
	public ComponentType[] registeredComponents() {
		Application localObj = fetchNarrowedObject(null);
		if (localObj == null) {
			throw new IllegalStateException();
		}
		return localObj.registeredComponents();
	}

	private final VersionedFeature profileFeature = new VersionedFeature(this, ScaPackage.Literals.SCA_WAVEFORM__PROFILE);

	/**
	 * @since 14.0
	 * @generated NOT
	 */
	@Override
	public String fetchProfile(IProgressMonitor monitor) {
		if (isDisposed()) {
			return null;
		}
		if (isSetProfile()) {
			return getProfile();
		}

		SubMonitor subMonitor = SubMonitor.convert(monitor, "Fetch profile", 3);
		if (subMonitor.isCanceled()) {
			throw new OperationCanceledException();
		}
		Application localObj = fetchNarrowedObject(subMonitor.newChild(1));

		Transaction transaction = profileFeature.createTransaction();
		if (localObj != null) {
			if (subMonitor.isCanceled()) {
				throw new OperationCanceledException();
			}
			try {
				String newName = localObj.profile();
				transaction.append(new SetLocalAttributeCommand(this, newName, ScaPackage.Literals.SCA_WAVEFORM__PROFILE));
			} catch (SystemException e) {
				Status status = new Status(Status.ERROR, ScaModelPlugin.ID, "Failed to fetch profile.", e);
				transaction.append(new UnsetLocalAttributeCommand(this, status, ScaPackage.Literals.SCA_WAVEFORM__PROFILE));
			}
			subMonitor.worked(1);
		} else {
			transaction.addCommand(new UnsetLocalAttributeCommand(this, null, ScaPackage.Literals.SCA_WAVEFORM__PROFILE));
		}

		subMonitor.setWorkRemaining(1);
		transaction.commit();
		subMonitor.worked(1);
		subMonitor.done();
		return getProfile();
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @since 20.0
	 *        <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<ScaComponent> getComponentsCopy() {
		try {
			return ScaModelCommandWithResult.runExclusive(this, new RunnableWithResult.Impl<EList<ScaComponent>>() {
				@Override
				public void run() {
					setResult(new BasicEList<ScaComponent>(getComponents()));
				}
			});
		} catch (InterruptedException e) {
			ScaModelPlugin.logError("Interrupted while reading model", e);
			return new BasicEList<ScaComponent>();
		}
	}

	private final VersionedFeature profileURIFeature = new VersionedFeature(this, ScaPackage.Literals.PROFILE_OBJECT_WRAPPER__PROFILE_URI);

	@Override
	public URI fetchProfileURI(IProgressMonitor monitor) {
		if (isDisposed()) {
			return null;
		}
		if (isSetProfileURI()) {
			return getProfileURI();
		}

		SubMonitor progress = SubMonitor.convert(monitor, 3);
		ScaDomainManager domMgr = getDomMgr();
		Transaction transaction = profileURIFeature.createTransaction();
		if (progress.isCanceled()) {
			throw new OperationCanceledException();
		}
		String profile = fetchProfile(progress.newChild(1));

		if (profile != null) {
			if (domMgr != null) {
				if (progress.isCanceled()) {
					throw new OperationCanceledException();
				}
				ScaDomainManagerFileSystem fileMgr = domMgr.fetchFileManager(progress.newChild(1), RefreshDepth.SELF);

				if (fileMgr != null) {
					final URI newURI = fileMgr.createURI(profile);
					transaction.addCommand(new ScaModelCommand() {

						@Override
						public void execute() {
							setProfileURI(newURI);
						}
					});
					transaction.commit();
					progress.worked(1);
				}
			} else {
				progress.setWorkRemaining(1);

				final URI newURI = URI.createURI(profile);
				transaction.addCommand(new ScaModelCommand() {

					@Override
					public void execute() {
						setProfileURI(newURI);
					}
				});
				transaction.commit();
				progress.worked(1);
			}
		}

		progress.done();
		return getProfileURI();
	}

	/**
	 * @since 19.0
	 */
	@Override
	public String softwareProfile() {
		return profile();
	}

	/**
	 * @since 19.0
	 */
	@Override
	public LogEvent[] retrieve_records(IntHolder howMany, int startingRecord) {
		return getObj().retrieve_records(howMany, startingRecord);
	}

	/**
	 * @since 19.0
	 */
	@Override
	public LogEvent[] retrieve_records_by_date(IntHolder howMany, long to_timeStamp) {
		return getObj().retrieve_records_by_date(howMany, to_timeStamp);
	}

	/**
	 * @since 19.0
	 */
	@Override
	public LogEvent[] retrieve_records_from_date(IntHolder howMany, long from_timeStamp) {
		return getObj().retrieve_records_from_date(howMany, from_timeStamp);
	}

	/**
	 * @since 19.0
	 */
	@Override
	public int log_level() {
		return getObj().log_level();
	}

	/**
	 * @since 19.0
	 */
	@Override
	public void log_level(int newLog_level) {
		getObj().log_level(newLog_level);
	}

	/**
	 * @since 19.0
	 */
	@Override
	public void setLogLevel(String logger_id, int newLevel) throws UnknownIdentifier {
		getObj().setLogLevel(logger_id, newLevel);
	}

	/**
	 * @since 19.0
	 */
	@Override
	public String getLogConfig() {
		return getObj().getLogConfig();
	}

	/**
	 * @since 19.0
	 */
	@Override
	public void setLogConfig(String config_contents) {
		getObj().setLogConfig(config_contents);
	}

	/**
	 * @since 19.0
	 */
	@Override
	public void setLogConfigURL(String config_url) {
		getObj().setLogConfigURL(config_url);
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @since 20.0
	 *        <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public EList<ScaComponent> fetchComponents(IProgressMonitor monitor, RefreshDepth depth) {
		// END GENERATED CODE
		if (isDisposed()) {
			return ECollections.emptyEList();
		}

		SubMonitor subMonitor = SubMonitor.convert(monitor, "Fetching components", 2);
		if (subMonitor.isCanceled()) {
			throw new OperationCanceledException();
		}
		internalFetchComponents(subMonitor.newChild(1));

		List<ScaComponent> componentsCopy = ScaModelCommandWithResult.execute(this, new ScaModelCommandWithResult<List<ScaComponent>>() {

			@Override
			public void execute() {
				setResult(new ArrayList<>(getComponents()));
			}
		});
		if (componentsCopy != null && depth != RefreshDepth.NONE) {
			SubMonitor portRefresh = subMonitor.newChild(1).setWorkRemaining(componentsCopy.size());
			for (IRefreshable element : componentsCopy) {
				try {
					if (subMonitor.isCanceled()) {
						throw new OperationCanceledException();
					}
					element.refresh(portRefresh.newChild(1), depth);
				} catch (InterruptedException e) {
					throw new OperationCanceledException();
				}
			}
		}

		subMonitor.done();
		return getComponents();
		// BEGIN GENERATED CODE
	}

	/**
	 * @since 20.0
	 */
	@Override
	public boolean aware() {
		return getObj().aware();
	}

} // ScaWaveformImpl
