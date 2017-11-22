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
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMap.ValueListIterator;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.jacorb.naming.Name;
import org.omg.CORBA.IntHolder;
import org.omg.CORBA.ORB;
import org.omg.CORBA.SystemException;
import org.omg.CosEventChannelAdmin.EventChannelHelper;
import org.omg.CosNaming.Binding;
import org.omg.CosNaming.BindingIteratorHolder;
import org.omg.CosNaming.BindingListHolder;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import CF.AllocationManager;
import CF.Application;
import CF.ApplicationFactory;
import CF.ConnectionManager;
import CF.DataType;
import CF.Device;
import CF.DeviceAssignmentType;
import CF.DeviceManager;
import CF.DomainManager;
import CF.DomainManagerHelper;
import CF.EventChannelManager;
import CF.FileManager;
import CF.InvalidFileName;
import CF.InvalidObjectReference;
import CF.InvalidProfile;
import CF.LogEvent;
import CF.PropertiesHolder;
import CF.UnknownIdentifier;
import CF.UnknownProperties;
import CF.ApplicationFactoryPackage.CreateApplicationError;
import CF.ApplicationFactoryPackage.CreateApplicationInsufficientCapacityError;
import CF.ApplicationFactoryPackage.CreateApplicationRequestError;
import CF.ApplicationFactoryPackage.InvalidInitConfiguration;
import CF.DomainManagerPackage.AlreadyConnected;
import CF.DomainManagerPackage.ApplicationAlreadyInstalled;
import CF.DomainManagerPackage.ApplicationInstallationError;
import CF.DomainManagerPackage.ApplicationUninstallationError;
import CF.DomainManagerPackage.DeviceManagerNotRegistered;
import CF.DomainManagerPackage.InvalidEventChannelName;
import CF.DomainManagerPackage.InvalidIdentifier;
import CF.DomainManagerPackage.NotConnected;
import CF.DomainManagerPackage.RegisterError;
import CF.DomainManagerPackage.UnregisterError;
import CF.PropertyEmitterPackage.AlreadyInitialized;
import CF.PropertySetPackage.InvalidConfiguration;
import CF.PropertySetPackage.PartialConfiguration;
import gov.redhawk.model.sca.DomainConnectionException;
import gov.redhawk.model.sca.DomainConnectionState;
import gov.redhawk.model.sca.ProfileObjectWrapper;
import gov.redhawk.model.sca.Properties;
import gov.redhawk.model.sca.RefreshDepth;
import gov.redhawk.model.sca.ScaDevice;
import gov.redhawk.model.sca.ScaDeviceManager;
import gov.redhawk.model.sca.ScaDomainManager;
import gov.redhawk.model.sca.ScaDomainManagerFileSystem;
import gov.redhawk.model.sca.ScaEventChannel;
import gov.redhawk.model.sca.ScaFactory;
import gov.redhawk.model.sca.ScaModelPlugin;
import gov.redhawk.model.sca.ScaPackage;
import gov.redhawk.model.sca.ScaWaveform;
import gov.redhawk.model.sca.ScaWaveformFactory;
import gov.redhawk.model.sca.commands.ScaDomainManagerMergeDeviceManagersCommand;
import gov.redhawk.model.sca.commands.ScaDomainManagerMergeEventChannelsCommand;
import gov.redhawk.model.sca.commands.ScaDomainManagerMergeWaveformFactoriesCommand;
import gov.redhawk.model.sca.commands.ScaDomainManagerMergeWaveformsCommand;
import gov.redhawk.model.sca.commands.ScaModelCommand;
import gov.redhawk.model.sca.commands.ScaModelCommandWithResult;
import gov.redhawk.model.sca.commands.SetLocalAttributeCommand;
import gov.redhawk.model.sca.commands.UnsetLocalAttributeCommand;
import gov.redhawk.model.sca.commands.VersionedFeature;
import gov.redhawk.model.sca.commands.VersionedFeature.Transaction;
import gov.redhawk.sca.util.Debug;
import gov.redhawk.sca.util.OrbSession;
import gov.redhawk.sca.util.PluginUtil;
import gov.redhawk.sca.util.SilentJob;
import mil.jpeojtrs.sca.dmd.DmdPackage;
import mil.jpeojtrs.sca.dmd.DomainManagerConfiguration;
import mil.jpeojtrs.sca.prf.AbstractProperty;
import mil.jpeojtrs.sca.spd.SpdPackage;
import mil.jpeojtrs.sca.util.CorbaUtils;
import mil.jpeojtrs.sca.util.NamedThreadFactory;
import mil.jpeojtrs.sca.util.ScaEcoreUtils;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object ' <em><b>Domain Manager</b></em>'.
 * 
 * @since 12.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link gov.redhawk.model.sca.impl.ScaDomainManagerImpl#getWaveformFactories <em>Waveform Factories</em>}</li>
 * <li>{@link gov.redhawk.model.sca.impl.ScaDomainManagerImpl#getWaveforms <em>Waveforms</em>}</li>
 * <li>{@link gov.redhawk.model.sca.impl.ScaDomainManagerImpl#getDeviceManagers <em>Device Managers</em>}</li>
 * <li>{@link gov.redhawk.model.sca.impl.ScaDomainManagerImpl#getFileManager <em>File Manager</em>}</li>
 * <li>{@link gov.redhawk.model.sca.impl.ScaDomainManagerImpl#getConnectionPropertiesContainer <em>Connection Properties
 * Container</em>}</li>
 * <li>{@link gov.redhawk.model.sca.impl.ScaDomainManagerImpl#getConnectionProperties <em>Connection
 * Properties</em>}</li>
 * <li>{@link gov.redhawk.model.sca.impl.ScaDomainManagerImpl#isAutoConnect <em>Auto Connect</em>}</li>
 * <li>{@link gov.redhawk.model.sca.impl.ScaDomainManagerImpl#isConnected <em>Connected</em>}</li>
 * <li>{@link gov.redhawk.model.sca.impl.ScaDomainManagerImpl#getIdentifier <em>Identifier</em>}</li>
 * <li>{@link gov.redhawk.model.sca.impl.ScaDomainManagerImpl#getName <em>Name</em>}</li>
 * <li>{@link gov.redhawk.model.sca.impl.ScaDomainManagerImpl#getRootContext <em>Root Context</em>}</li>
 * <li>{@link gov.redhawk.model.sca.impl.ScaDomainManagerImpl#getState <em>State</em>}</li>
 * <li>{@link gov.redhawk.model.sca.impl.ScaDomainManagerImpl#getProfile <em>Profile</em>}</li>
 * <li>{@link gov.redhawk.model.sca.impl.ScaDomainManagerImpl#getEventChannels <em>Event Channels</em>}</li>
 * <li>{@link gov.redhawk.model.sca.impl.ScaDomainManagerImpl#getLocalName <em>Local Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ScaDomainManagerImpl extends ScaPropertyContainerImpl<DomainManager, DomainManagerConfiguration> implements ScaDomainManager {
	/**
	 * The cached value of the '{@link #getWaveformFactories() <em>Waveform Factories</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWaveformFactories()
	 * @generated
	 * @ordered
	 */
	protected EList<ScaWaveformFactory> waveformFactories;
	/**
	 * The cached value of the '{@link #getWaveforms() <em>Waveforms</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWaveforms()
	 * @generated
	 * @ordered
	 */
	protected EList<ScaWaveform> waveforms;
	/**
	 * The cached value of the '{@link #getDeviceManagers() <em>Device Managers</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDeviceManagers()
	 * @generated
	 * @ordered
	 */
	protected EList<ScaDeviceManager> deviceManagers;
	/**
	 * The cached value of the '{@link #getFileManager() <em>File Manager</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFileManager()
	 * @generated
	 * @ordered
	 */
	protected ScaDomainManagerFileSystem fileManager;
	/**
	 * This is true if the File Manager containment reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean fileManagerESet;
	/**
	 * The cached value of the '{@link #getConnectionPropertiesContainer() <em>Connection Properties Container</em>}'
	 * containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConnectionPropertiesContainer()
	 * @generated
	 * @ordered
	 */
	protected Properties connectionPropertiesContainer;
	/**
	 * The default value of the '{@link #isAutoConnect() <em>Auto Connect</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAutoConnect()
	 * @generated
	 * @ordered
	 */
	protected static final boolean AUTO_CONNECT_EDEFAULT = false;
	/**
	 * The cached value of the '{@link #isAutoConnect() <em>Auto Connect</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAutoConnect()
	 * @generated
	 * @ordered
	 */
	protected boolean autoConnect = AUTO_CONNECT_EDEFAULT;
	/**
	 * The default value of the '{@link #isConnected() <em>Connected</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isConnected()
	 * @generated
	 * @ordered
	 */
	protected static final boolean CONNECTED_EDEFAULT = false;
	/**
	 * The default value of the '{@link #getIdentifier() <em>Identifier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIdentifier()
	 * @generated
	 * @ordered
	 */
	protected static final String IDENTIFIER_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getIdentifier() <em>Identifier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIdentifier()
	 * @generated
	 * @ordered
	 */
	protected String identifier = IDENTIFIER_EDEFAULT;
	/**
	 * This is true if the Identifier attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean identifierESet;
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;
	/**
	 * The default value of the '{@link #getRootContext() <em>Root Context</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRootContext()
	 * @generated
	 * @ordered
	 */
	protected static final NamingContextExt ROOT_CONTEXT_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getRootContext() <em>Root Context</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRootContext()
	 * @generated
	 * @ordered
	 */
	protected NamingContextExt rootContext = ROOT_CONTEXT_EDEFAULT;
	/**
	 * This is true if the Root Context attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean rootContextESet;
	/**
	 * The default value of the '{@link #getState() <em>State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getState()
	 * @generated
	 * @ordered
	 */
	protected static final DomainConnectionState STATE_EDEFAULT = DomainConnectionState.DISCONNECTED;
	/**
	 * The cached value of the '{@link #getState() <em>State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getState()
	 * @generated
	 * @ordered
	 */
	protected DomainConnectionState state = STATE_EDEFAULT;
	/**
	 * The default value of the '{@link #getProfile() <em>Profile</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProfile()
	 * @generated
	 * @ordered
	 */
	protected static final String PROFILE_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getProfile() <em>Profile</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProfile()
	 * @generated
	 * @ordered
	 */
	protected String profile = PROFILE_EDEFAULT;
	/**
	 * This is true if the Profile attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean profileESet;
	/**
	 * The cached value of the '{@link #getEventChannels() <em>Event Channels</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * 
	 * @since 19.0
	 * <!-- end-user-doc -->
	 * @see #getEventChannels()
	 * @generated
	 * @ordered
	 */
	protected EList<ScaEventChannel> eventChannels;
	/**
	 * The default value of the '{@link #getLocalName() <em>Local Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * 
	 * @since 20.0
	 * <!-- end-user-doc -->
	 * @see #getLocalName()
	 * @generated
	 * @ordered
	 */
	protected static final String LOCAL_NAME_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getLocalName() <em>Local Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * 
	 * @since 20.0
	 * <!-- end-user-doc -->
	 * @see #getLocalName()
	 * @generated
	 * @ordered
	 */
	protected String localName = LOCAL_NAME_EDEFAULT;
	private static final Debug DEBUG = new Debug(ScaModelPlugin.ID, "scaDomainManager/connect");
	private static final Debug DEBUG_KEEP_ALIVE_ERRORS = new Debug(ScaModelPlugin.ID, "scaDomainManager/keepAliveErrors");

	private static final DeviceManager[] EMPTY_DEVICE_MANAGERS = new DeviceManager[0];

	private static final Application[] EMPTY_APPLICATIONS = new Application[0];

	private static final ApplicationFactory[] EMPTY_APPLICATION_FACTORIES = new ApplicationFactory[0];

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	protected ScaDomainManagerImpl() {
		super();
		this.setConnectionPropertiesContainer(ScaFactory.eINSTANCE.createProperties());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ScaPackage.Literals.SCA_DOMAIN_MANAGER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @since 18.0
	 * <!-- end-user-doc -->
	 * This is specialized for the more specific type known in this context.
	 * @generated
	 */
	@Override
	public void setProfileObj(DomainManagerConfiguration newProfileObj) {
		super.setProfileObj(newProfileObj);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<ScaWaveformFactory> getWaveformFactories() {
		if (waveformFactories == null) {
			waveformFactories = new EObjectContainmentWithInverseEList.Unsettable<ScaWaveformFactory>(ScaWaveformFactory.class, this,
				ScaPackage.SCA_DOMAIN_MANAGER__WAVEFORM_FACTORIES, ScaPackage.SCA_WAVEFORM_FACTORY__DOM_MGR);
		}
		return waveformFactories;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetWaveformFactories() {
		if (waveformFactories != null)
			((InternalEList.Unsettable< ? >) waveformFactories).unset();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetWaveformFactories() {
		return waveformFactories != null && ((InternalEList.Unsettable< ? >) waveformFactories).isSet();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<ScaWaveform> getWaveforms() {
		if (waveforms == null) {
			waveforms = new EObjectContainmentWithInverseEList.Unsettable<ScaWaveform>(ScaWaveform.class, this, ScaPackage.SCA_DOMAIN_MANAGER__WAVEFORMS,
				ScaPackage.SCA_WAVEFORM__DOM_MGR);
		}
		return waveforms;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetWaveforms() {
		if (waveforms != null)
			((InternalEList.Unsettable< ? >) waveforms).unset();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetWaveforms() {
		return waveforms != null && ((InternalEList.Unsettable< ? >) waveforms).isSet();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<ScaDeviceManager> getDeviceManagers() {
		if (deviceManagers == null) {
			deviceManagers = new EObjectContainmentWithInverseEList.Unsettable<ScaDeviceManager>(ScaDeviceManager.class, this,
				ScaPackage.SCA_DOMAIN_MANAGER__DEVICE_MANAGERS, ScaPackage.SCA_DEVICE_MANAGER__DOM_MGR);
		}
		return deviceManagers;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetDeviceManagers() {
		if (deviceManagers != null)
			((InternalEList.Unsettable< ? >) deviceManagers).unset();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetDeviceManagers() {
		return deviceManagers != null && ((InternalEList.Unsettable< ? >) deviceManagers).isSet();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ScaDomainManagerFileSystem getFileManager() {
		return fileManager;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetFileManager(ScaDomainManagerFileSystem newFileManager, NotificationChain msgs) {
		ScaDomainManagerFileSystem oldFileManager = fileManager;
		fileManager = newFileManager;
		boolean oldFileManagerESet = fileManagerESet;
		fileManagerESet = true;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ScaPackage.SCA_DOMAIN_MANAGER__FILE_MANAGER, oldFileManager,
				newFileManager, !oldFileManagerESet);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFileManager(ScaDomainManagerFileSystem newFileManager) {
		if (newFileManager != fileManager) {
			NotificationChain msgs = null;
			if (fileManager != null)
				msgs = ((InternalEObject) fileManager).eInverseRemove(this, ScaPackage.SCA_DOMAIN_MANAGER_FILE_SYSTEM__DOM_MGR,
					ScaDomainManagerFileSystem.class, msgs);
			if (newFileManager != null)
				msgs = ((InternalEObject) newFileManager).eInverseAdd(this, ScaPackage.SCA_DOMAIN_MANAGER_FILE_SYSTEM__DOM_MGR,
					ScaDomainManagerFileSystem.class, msgs);
			msgs = basicSetFileManager(newFileManager, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else {
			boolean oldFileManagerESet = fileManagerESet;
			fileManagerESet = true;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.SET, ScaPackage.SCA_DOMAIN_MANAGER__FILE_MANAGER, newFileManager, newFileManager,
					!oldFileManagerESet));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicUnsetFileManager(NotificationChain msgs) {
		ScaDomainManagerFileSystem oldFileManager = fileManager;
		fileManager = null;
		boolean oldFileManagerESet = fileManagerESet;
		fileManagerESet = false;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.UNSET, ScaPackage.SCA_DOMAIN_MANAGER__FILE_MANAGER, oldFileManager, null,
				oldFileManagerESet);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetFileManager() {
		if (fileManager != null) {
			NotificationChain msgs = null;
			msgs = ((InternalEObject) fileManager).eInverseRemove(this, ScaPackage.SCA_DOMAIN_MANAGER_FILE_SYSTEM__DOM_MGR, ScaDomainManagerFileSystem.class,
				msgs);
			msgs = basicUnsetFileManager(msgs);
			if (msgs != null)
				msgs.dispatch();
		} else {
			boolean oldFileManagerESet = fileManagerESet;
			fileManagerESet = false;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.UNSET, ScaPackage.SCA_DOMAIN_MANAGER__FILE_MANAGER, null, null, oldFileManagerESet));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetFileManager() {
		return fileManagerESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Properties getConnectionPropertiesContainer() {
		if (connectionPropertiesContainer != null && connectionPropertiesContainer.eIsProxy()) {
			InternalEObject oldConnectionPropertiesContainer = (InternalEObject) connectionPropertiesContainer;
			connectionPropertiesContainer = (Properties) eResolveProxy(oldConnectionPropertiesContainer);
			if (connectionPropertiesContainer != oldConnectionPropertiesContainer) {
				InternalEObject newConnectionPropertiesContainer = (InternalEObject) connectionPropertiesContainer;
				NotificationChain msgs = oldConnectionPropertiesContainer.eInverseRemove(this,
					EOPPOSITE_FEATURE_BASE - ScaPackage.SCA_DOMAIN_MANAGER__CONNECTION_PROPERTIES_CONTAINER, null, null);
				if (newConnectionPropertiesContainer.eInternalContainer() == null) {
					msgs = newConnectionPropertiesContainer.eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ScaPackage.SCA_DOMAIN_MANAGER__CONNECTION_PROPERTIES_CONTAINER, null, msgs);
				}
				if (msgs != null)
					msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ScaPackage.SCA_DOMAIN_MANAGER__CONNECTION_PROPERTIES_CONTAINER,
						oldConnectionPropertiesContainer, connectionPropertiesContainer));
			}
		}
		return connectionPropertiesContainer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Properties basicGetConnectionPropertiesContainer() {
		return connectionPropertiesContainer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetConnectionPropertiesContainer(Properties newConnectionPropertiesContainer, NotificationChain msgs) {
		Properties oldConnectionPropertiesContainer = connectionPropertiesContainer;
		connectionPropertiesContainer = newConnectionPropertiesContainer;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ScaPackage.SCA_DOMAIN_MANAGER__CONNECTION_PROPERTIES_CONTAINER,
				oldConnectionPropertiesContainer, newConnectionPropertiesContainer);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setConnectionPropertiesContainer(Properties newConnectionPropertiesContainer) {
		if (newConnectionPropertiesContainer != connectionPropertiesContainer) {
			NotificationChain msgs = null;
			if (connectionPropertiesContainer != null)
				msgs = ((InternalEObject) connectionPropertiesContainer).eInverseRemove(this,
					EOPPOSITE_FEATURE_BASE - ScaPackage.SCA_DOMAIN_MANAGER__CONNECTION_PROPERTIES_CONTAINER, null, msgs);
			if (newConnectionPropertiesContainer != null)
				msgs = ((InternalEObject) newConnectionPropertiesContainer).eInverseAdd(this,
					EOPPOSITE_FEATURE_BASE - ScaPackage.SCA_DOMAIN_MANAGER__CONNECTION_PROPERTIES_CONTAINER, null, msgs);
			msgs = basicSetConnectionPropertiesContainer(newConnectionPropertiesContainer, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScaPackage.SCA_DOMAIN_MANAGER__CONNECTION_PROPERTIES_CONTAINER,
				newConnectionPropertiesContainer, newConnectionPropertiesContainer));
	}

	private final ExecutorService disposeOrbExecutor = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory(ScaDomainManagerImpl.class.getName()));

	/**
	 * @since 14.0
	 */
	@Override
	protected void notifyChanged(Notification msg) {
		// END GENERATED CODE
		super.notifyChanged(msg);
		if (msg.isTouch()) {
			return;
		}
		switch (msg.getFeatureID(ScaDomainManager.class)) {
		case ScaPackage.SCA_DOMAIN_MANAGER__OBJ:
			unsetWaveforms();
			unsetWaveformFactories();
			unsetDeviceManagers();
			unsetFileManager();
			unsetIdentifier();
			unsetProfile();
			break;
		case ScaPackage.SCA_DOMAIN_MANAGER__PROFILE:
			if (!PluginUtil.equals(msg.getOldValue(), msg.getNewValue())) {
				unsetProfileURI();
			}
			break;
		default:
			break;
		}
		// BEGIN GENERATED CODE
	}

	private void destroyOrbSession(final OrbSession session) {
		// END GENERATED CODE
		if (session == null) {
			return;
		}
		disposeOrbExecutor.submit(new Runnable() {

			@Override
			public void run() {
				try {
					session.dispose();
				} catch (SystemException e) {
					// PASS Ignore issues during shutdown
				}
			}

		});
		// BEGIN GENERATED CODE
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @since 8.0
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public EMap<String, String> getConnectionProperties() {
		// END GENERATED CODE
		return getConnectionPropertiesContainer().getProperty();
		// BEGIN GENERATED CODE
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isAutoConnect() {
		return autoConnect;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setAutoConnect(boolean newAutoConnect) {
		boolean oldAutoConnect = autoConnect;
		autoConnect = newAutoConnect;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScaPackage.SCA_DOMAIN_MANAGER__AUTO_CONNECT, oldAutoConnect, autoConnect));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public boolean isConnected() {
		// END GENERATED CODE
		return getState() == DomainConnectionState.CONNECTED;
		// BEGIN GENERATED CODE
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setIdentifier(String newIdentifier) {
		String oldIdentifier = identifier;
		identifier = newIdentifier;
		boolean oldIdentifierESet = identifierESet;
		identifierESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScaPackage.SCA_DOMAIN_MANAGER__IDENTIFIER, oldIdentifier, identifier, !oldIdentifierESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetIdentifier() {
		String oldIdentifier = identifier;
		boolean oldIdentifierESet = identifierESet;
		identifier = IDENTIFIER_EDEFAULT;
		identifierESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ScaPackage.SCA_DOMAIN_MANAGER__IDENTIFIER, oldIdentifier, IDENTIFIER_EDEFAULT,
				oldIdentifierESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetIdentifier() {
		return identifierESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScaPackage.SCA_DOMAIN_MANAGER__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NamingContextExt getRootContext() {
		return rootContext;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRootContext(NamingContextExt newRootContext) {
		NamingContextExt oldRootContext = rootContext;
		rootContext = newRootContext;
		boolean oldRootContextESet = rootContextESet;
		rootContextESet = true;
		if (eNotificationRequired())
			eNotify(
				new ENotificationImpl(this, Notification.SET, ScaPackage.SCA_DOMAIN_MANAGER__ROOT_CONTEXT, oldRootContext, rootContext, !oldRootContextESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetRootContext() {
		NamingContextExt oldRootContext = rootContext;
		boolean oldRootContextESet = rootContextESet;
		rootContext = ROOT_CONTEXT_EDEFAULT;
		rootContextESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ScaPackage.SCA_DOMAIN_MANAGER__ROOT_CONTEXT, oldRootContext, ROOT_CONTEXT_EDEFAULT,
				oldRootContextESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetRootContext() {
		return rootContextESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DomainConnectionState getState() {
		return state;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @since 14.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStateGen(DomainConnectionState newState) {
		DomainConnectionState oldState = state;
		state = newState == null ? STATE_EDEFAULT : newState;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScaPackage.SCA_DOMAIN_MANAGER__STATE, oldState, state));
	}

	private SilentJob notifyAllJob = new SilentJob("Notify All") {

		@Override
		protected IStatus runSilent(IProgressMonitor monitor) {
			synchronized (ScaDomainManagerImpl.this) {
				ScaDomainManagerImpl.this.notifyAll();
			}
			return Status.OK_STATUS;
		}

	};
	private OrbSession session;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public void setState(final DomainConnectionState newState) {
		// END GENERATED CODE
		final DomainConnectionState oldState = this.state;
		setStateGen(newState);
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ScaPackage.SCA_DOMAIN_MANAGER__CONNECTED, oldState == DomainConnectionState.CONNECTED,
				this.state == DomainConnectionState.CONNECTED));
		}
		notifyAllJob.schedule();
		// BEGIN GENERATED CODE
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getProfile() {
		return profile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setProfile(String newProfile) {
		String oldProfile = profile;
		profile = newProfile;
		boolean oldProfileESet = profileESet;
		profileESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScaPackage.SCA_DOMAIN_MANAGER__PROFILE, oldProfile, profile, !oldProfileESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetProfile() {
		String oldProfile = profile;
		boolean oldProfileESet = profileESet;
		profile = PROFILE_EDEFAULT;
		profileESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ScaPackage.SCA_DOMAIN_MANAGER__PROFILE, oldProfile, PROFILE_EDEFAULT, oldProfileESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetProfile() {
		return profileESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @since 19.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ScaEventChannel> getEventChannels() {
		if (eventChannels == null) {
			eventChannels = new EObjectContainmentEList.Unsettable.Resolving<ScaEventChannel>(ScaEventChannel.class, this,
				ScaPackage.SCA_DOMAIN_MANAGER__EVENT_CHANNELS);
		}
		return eventChannels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @since 19.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetEventChannels() {
		if (eventChannels != null)
			((InternalEList.Unsettable< ? >) eventChannels).unset();
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @since 19.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetEventChannels() {
		return eventChannels != null && ((InternalEList.Unsettable< ? >) eventChannels).isSet();
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @since 20.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLocalName() {
		return localName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @since 20.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLocalName(String newLocalName) {
		String oldLocalName = localName;
		localName = newLocalName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScaPackage.SCA_DOMAIN_MANAGER__LOCAL_NAME, oldLocalName, localName));
	}

	/**
	 * Waits for a connect that is in progress to complete. This method assumes it is running in the protected context
	 * that can access the model.
	 * 
	 * @param monitor the progress monitor to use for reporting progress to the user. It is the caller's responsibility
	 * to call done() on the given monitor. Accepts null, indicating that no progress should be
	 * reported and that the operation cannot be canceled.
	 * @throws InterruptedException
	 */
	private void waitOnConnect(final IProgressMonitor monitor) throws InterruptedException {
		SubMonitor progress = SubMonitor.convert(monitor, SubMonitor.UNKNOWN);

		final int SLEEP_TIME_MILLIS = 1000;
		while (getState() == DomainConnectionState.CONNECTING && !progress.isCanceled()) {
			synchronized (this) {
				this.wait(SLEEP_TIME_MILLIS);
			}
			progress.worked(1);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @since 14.0
	 * <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public void connect(IProgressMonitor parentMonitor, RefreshDepth refreshDepth) throws DomainConnectionException {
		// END GENERATED CODE
		try {
			if (refreshDepth == null) {
				refreshDepth = RefreshDepth.SELF;
			}

			DomainConnectionState localState = getState();

			if (localState == DomainConnectionState.CONNECTED) {
				// Silly rabbit - you're already connected
				return;
			} else if (localState == DomainConnectionState.CONNECTING) {
				// Someone is already connecting. Wait on them to finish.
				waitOnConnect(parentMonitor);

				localState = getState();
				// If the other thread connected us, or we were canceled, we're done
				if (localState == DomainConnectionState.CONNECTED || (parentMonitor != null && parentMonitor.isCanceled())) {
					return;
				}

				// Report back failure
				throw new DomainConnectionException("Unable to connect");
			} else if ((localState != DomainConnectionState.DISCONNECTED) && (localState != DomainConnectionState.FAILED)) {
				// Disconnected / Failed are the only other states connect can be called from
				throw new DomainConnectionException("Invalid state for connect (" + getState().getName() + ")");
			}

			ScaModelCommand.execute(this, new ScaModelCommand() {
				@Override
				public void execute() {
					setState(DomainConnectionState.CONNECTING);
				}
			});

			final int INIT_ORB_WORK = 5;
			final int RESOLVE_NAMINGCONTEXT_WORK = 5;
			final int SET_NAMINGCONTEXT_WORK = 5;
			final int DOMAIN_MGR_WORK = 5;
			final int REFRESH_DOMAIN_WORK = 75;
			final SubMonitor monitor = SubMonitor.convert(parentMonitor, "Connecting to domain: " + getName(), 100);
			monitor.subTask("Initializing ORB...");
			java.util.Properties orbProperties = createProperties();
			java.util.Properties systemProps = new java.util.Properties(System.getProperties());
			systemProps.putAll(orbProperties);

			String tmpName = getLabel();
			if (tmpName == null) {
				tmpName = "";
			}
			final OrbSession orbSession = OrbSession.createSession(tmpName, Platform.getApplicationArgs(), systemProps);
			setOrbSession(orbSession);
			CompoundCommand command = new CompoundCommand();

			command.append(new ScaModelCommand() {

				@Override
				public void execute() {
					clearAllStatus();
				}

			});
			monitor.worked(INIT_ORB_WORK);

			// Allow the user to specify the DomainManager. If there is no '/'
			// then the user did not specify it (use the SCA 2.2.2 specific
			// implementation which is <name>/<name>)
			final String domMgrName = getDomMgrName();

			monitor.subTask("Resolving Naming Service...");
			// String nameService = orbProperties.getProperty("ORBInitRef.NameService");
			final ORB orb = orbSession.getOrb();
			final org.omg.CORBA.Object objRef = CorbaUtils.invoke(new Callable<org.omg.CORBA.Object>() {

				@Override
				public org.omg.CORBA.Object call() throws Exception {
					return orb.resolve_initial_references("NameService");
				}

			}, monitor.newChild(RESOLVE_NAMINGCONTEXT_WORK));

			final NamingContextExt newNamingContext = CorbaUtils.invoke(new Callable<NamingContextExt>() {

				@Override
				public NamingContextExt call() throws Exception {
					return NamingContextExtHelper.narrow(objRef);
				}

			}, monitor.newChild(SET_NAMINGCONTEXT_WORK));
			command.append(new ScaModelCommand() {

				@Override
				public void execute() {
					setRootContext(newNamingContext);
				}

			});

			monitor.subTask("Resolving Domain " + domMgrName);
			final org.omg.CORBA.Object newCorbaObj = CorbaUtils.resolve_str(newNamingContext, domMgrName, monitor.newChild(DOMAIN_MGR_WORK));
			command.append(new ScaModelCommand() {

				@Override
				public void execute() {
					setCorbaObj(newCorbaObj);
					setState(DomainConnectionState.CONNECTED);
				}

			});

			ScaModelCommand.execute(this, command);

			monitor.subTask("Refreshing Domain...");
			try {
				this.refresh(monitor.newChild(REFRESH_DOMAIN_WORK), refreshDepth);
			} catch (Exception e) { // SUPPRESS CHECKSTYLE Logged Catch all exception
				if (DEBUG.enabled) {
					DEBUG.message("Errors during refresh in connect.");
					DEBUG.catching(e);
				}
			}

			monitor.done();
		} catch (final DomainConnectionException e) {
			// Failure occurred in another thread and we're reporting that failure to this thread
			throw e;
		} catch (InterruptedException e) {
			// PASS
		} catch (final CoreException e) { // SUPPRESS CHECKSTYLE Logged Catch all exception
			ScaModelCommand.execute(this, new ScaModelCommand() {

				@Override
				public void execute() {
					setState(DomainConnectionState.FAILED);
					reset();
				}

			});
			throw new DomainConnectionException(e);
		} finally {
			if (parentMonitor != null) {
				parentMonitor.done();
			}
		}
		// BEGIN GENERATED CODE
	}

	/**
	 * @since 18.0
	 */
	public void setOrbSession(OrbSession session) {
		destroyOrbSession(this.session);
		this.session = session;
	}

	private java.util.Properties createProperties() {
		// END GENERATED CODE
		final java.util.Properties props = new java.util.Properties();
		for (final Map.Entry<String, String> entry : this.getConnectionProperties().entrySet()) {
			props.put(entry.getKey(), entry.getValue());
		}
		return props;
		// BEGIN GENERATED CODE
	}

	private String getDomMgrName() {
		// END GENERATED CODE
		String tmpString = this.name;
		if (tmpString != null && tmpString.indexOf('/') == -1) {
			return tmpString + "/" + tmpString;
		} else {
			return tmpString;
		}
		// BEGIN GENERATED CODE
	}

	private void internalDisconnect() {
		setState(DomainConnectionState.DISCONNECTING);
		reset();
		setState(DomainConnectionState.DISCONNECTED);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public void disconnect() {
		// END GENERATED CODE
		ScaModelCommand.execute(this, new ScaModelCommand() {

			@Override
			protected boolean prepare() {
				return super.prepare() && getState() == DomainConnectionState.CONNECTED;
			}

			@Override
			public void execute() {
				internalDisconnect();
			}
		});
		// BEGIN GENERATED CODE
	}

	private final VersionedFeature devicemanagers = new VersionedFeature(this, ScaPackage.Literals.SCA_DOMAIN_MANAGER__DEVICE_MANAGERS);

	/**
	 * @since 14.0
	 */
	protected void internalFetchDeviceManagers(IProgressMonitor monitor) {
		// END GENERATED CODE
		if (isDisposed()) {
			return;
		}

		final SubMonitor subMonitor = SubMonitor.convert(monitor, 3);
		final DomainManager domMgr = fetchNarrowedObject(subMonitor.split(1));

		Transaction transaction = devicemanagers.createTransaction();
		if (domMgr != null) {
			// Setup New Device Managers Map
			DeviceManager[] deviceMgrs = null;
			try {
				if (subMonitor.isCanceled()) {
					throw new OperationCanceledException();
				}
				deviceMgrs = domMgr.deviceManagers();
				transaction.addCommand(new ScaDomainManagerMergeDeviceManagersCommand(this, deviceMgrs));
			} catch (SystemException e) {
				Status status = new Status(Status.ERROR, ScaModelPlugin.ID, "Failed to fetch Device Managers.", e);
				transaction.addCommand(new UnsetLocalAttributeCommand(this, status, ScaPackage.Literals.SCA_DOMAIN_MANAGER__DEVICE_MANAGERS));
			}
			subMonitor.worked(1);
		} else {
			transaction.addCommand(new UnsetLocalAttributeCommand(this, null, ScaPackage.Literals.SCA_DOMAIN_MANAGER__DEVICE_MANAGERS));
		}

		// Perform Actions
		subMonitor.setWorkRemaining(1);
		transaction.commit();
		subMonitor.done();
		// BEGIN GENERATED CODE
	}

	private final VersionedFeature waveformFactoriesFeature = new VersionedFeature(this, ScaPackage.Literals.SCA_DOMAIN_MANAGER__WAVEFORM_FACTORIES);

	/**
	 * @since 14.0
	 */
	protected void internalFetchWaveformFactories(IProgressMonitor monitor) {
		// END GENERATED CODE
		if (isDisposed()) {
			return;
		}

		SubMonitor subMonitor = SubMonitor.convert(monitor, 3);
		final DomainManager domMgr = fetchNarrowedObject(subMonitor.split(1));

		Transaction transaction = waveformFactoriesFeature.createTransaction();
		if (domMgr != null) {
			// Setup new Factory Map
			ApplicationFactory[] factories = null;
			try {
				if (subMonitor.isCanceled()) {
					throw new OperationCanceledException();
				}
				factories = domMgr.applicationFactories();
				transaction.addCommand(new ScaDomainManagerMergeWaveformFactoriesCommand(this, factories));
			} catch (SystemException e) {
				Status status = new Status(Status.ERROR, ScaModelPlugin.ID, "Failed to fetch waveforms factories.", e);
				transaction.addCommand(new UnsetLocalAttributeCommand(this, status, ScaPackage.Literals.SCA_DOMAIN_MANAGER__WAVEFORM_FACTORIES));
			}
			subMonitor.worked(1);
		} else {
			transaction.addCommand(new UnsetLocalAttributeCommand(this, null, ScaPackage.Literals.SCA_DOMAIN_MANAGER__WAVEFORM_FACTORIES));
		}

		subMonitor.setWorkRemaining(1);
		transaction.commit();
		subMonitor.done();
		// BEGIN GENERATED CODE
	}

	private final VersionedFeature waveformsFeature = new VersionedFeature(this, ScaPackage.Literals.SCA_DOMAIN_MANAGER__WAVEFORMS);

	/**
	 * @since 14.0
	 */
	protected void internalFetchWaveforms(IProgressMonitor monitor) {
		// END GENERATED CODE
		if (isDisposed()) {
			return;
		}

		SubMonitor subMonitor = SubMonitor.convert(monitor, 3);
		final DomainManager domMgr = fetchNarrowedObject(subMonitor.split(1));

		Transaction transaction = waveformsFeature.createTransaction();

		if (domMgr != null) {
			// Fetch Applications
			Application[] applications = null;
			try {
				if (subMonitor.isCanceled()) {
					throw new OperationCanceledException();
				}
				applications = domMgr.applications();
				transaction.addCommand(new ScaDomainManagerMergeWaveformsCommand(this, applications));
			} catch (SystemException e) {
				Status status = new Status(Status.ERROR, ScaModelPlugin.ID, "Failed to fetch waveforms.", e);
				transaction.addCommand(new UnsetLocalAttributeCommand(this, status, ScaPackage.Literals.SCA_DOMAIN_MANAGER__WAVEFORMS));
			}
			subMonitor.worked(1);
		} else {
			transaction.addCommand(new UnsetLocalAttributeCommand(this, null, ScaPackage.Literals.SCA_DOMAIN_MANAGER__WAVEFORMS));
		}

		// Perform Actions
		subMonitor.setWorkRemaining(1);
		transaction.commit();
		subMonitor.done();
		// BEGIN GENERATED CODE
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @since 14.0
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public ScaWaveformFactory installScaWaveformFactory(final String profilePath)
		throws InvalidProfile, InvalidFileName, ApplicationInstallationError, ApplicationAlreadyInstalled {
		// END GENERATED CODE
		if (profilePath == null) {
			throw new InvalidProfile("Null path");
		}
		installApplication(profilePath);
		try {
			return ScaModelCommand.runExclusive(this, new RunnableWithResult.Impl<ScaWaveformFactory>() {

				@Override
				public void run() {
					for (ScaWaveformFactory factory : getWaveformFactories()) {
						if (profilePath.equals(factory.getProfile())) {
							setResult(factory);
						}
					}
				}

			});
		} catch (InterruptedException e) {
			return null;
		}

		// BEGIN GENERATED CODE
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @since 14.0
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void uninstallScaWaveformFactory(final ScaWaveformFactory factory) throws ApplicationUninstallationError, InvalidIdentifier {
		// END GENERATED CODE
		DomainManager localObj = fetchNarrowedObject(null);
		if (localObj == null) {
			throw new IllegalStateException("Corba Obj is null");
		}
		if (factory == null) {
			return;
		}
		localObj.uninstallApplication(factory.getIdentifier());
		ScaModelCommand.execute(this, new ScaModelCommand() {

			@Override
			public void execute() {
				EcoreUtil.delete(factory);
			}
		});
		// BEGIN GENERATED CODE
	}

	@Override
	public void dispose() {
		// END GENERATED CODE
		internalDisconnect();
		super.dispose();
		// BEGIN GENERATED CODE
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @since 2.0 <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public ScaDevice< ? > getDevice(final String deviceId) {
		// END GENERATED CODE
		ScaDevice< ? > result = null;
		for (final ScaDeviceManager devMgr : getDeviceManagers()) {
			result = devMgr.getDevice(deviceId);
			if (result != null) {
				return result;
			}
		}
		return null;
		// BEGIN GENERATED CODE
	}

	private void reset() {
		// END GENERATED CODE
		unsetCorbaObj();
		unsetRootContext();
		setOrbSession(null);
		clearAllStatus();
		// BEGIN GENERATED CODE
	}

	@Override
	public IStatus getStatus() {
		if (getState() == DomainConnectionState.DISCONNECTED) {
			return Status.OK_STATUS;
		}
		return super.getStatus();
	}

	/**
	 * @since 14.0
	 */
	@Override
	public ApplicationFactory[] applicationFactories() {
		// END GENERATED CODE
		DomainManager domMgr = fetchNarrowedObject(null);
		if (domMgr == null) {
			return ScaDomainManagerImpl.EMPTY_APPLICATION_FACTORIES;
		}
		return domMgr.applicationFactories();
		// BEGIN GENERATED CODE
	}

	/**
	 * @since 14.0
	 */
	@Override
	public Application[] applications() {
		// END GENERATED CODE
		DomainManager domMgr = fetchNarrowedObject(null);
		if (domMgr == null) {
			return ScaDomainManagerImpl.EMPTY_APPLICATIONS;
		}
		return domMgr.applications();
		// BEGIN GENERATED CODE
	}

	/**
	 * @since 20.0
	 */
	@Override
	public ConnectionManager connectionMgr() {
		// END GENERATED CODE
		DomainManager domMgr = fetchNarrowedObject(null);
		if (domMgr == null) {
			throw new IllegalStateException("CORBA Object is Null");
		}
		return domMgr.connectionMgr();
		// BEGIN GENERATED CODE
	}

	/**
	 * @since 20.0
	 */
	@Override
	public Application createApplication(String profileFileName, String name, DataType[] initConfiguration, DeviceAssignmentType[] deviceAssignments)
		throws InvalidProfile, InvalidFileName, ApplicationInstallationError, CreateApplicationError, CreateApplicationRequestError,
		CreateApplicationInsufficientCapacityError, InvalidInitConfiguration {
		// END GENERATED CODE
		DomainManager domMgr = fetchNarrowedObject(null);
		if (domMgr == null) {
			throw new IllegalStateException("CORBA Object is Null");
		}
		return domMgr.createApplication(profileFileName, name, initConfiguration, deviceAssignments);
		// BEGIN GENERATED CODE
	}

	/**
	 * @since 14.0
	 */
	@Override
	public DeviceManager[] deviceManagers() {
		// END GENERATED CODE
		DomainManager domMgr = fetchNarrowedObject(null);
		if (domMgr == null) {
			return ScaDomainManagerImpl.EMPTY_DEVICE_MANAGERS;
		}
		return domMgr.deviceManagers();
		// BEGIN GENERATED CODE
	}

	@Override
	public String domainManagerProfile() {
		// END GENERATED CODE
		return this.getProfile();
		// BEGIN GENERATED CODE
	}

	/**
	 * @since 20.0
	 */
	@Override
	public EventChannelManager eventChannelMgr() {
		// END GENERATED CODE
		DomainManager domMgr = fetchNarrowedObject(null);
		if (domMgr == null) {
			return null;
		}
		return domMgr.eventChannelMgr();
		// BEGIN GENERATED CODE
	}

	/**
	 * @since 14.0
	 */
	@Override
	public FileManager fileMgr() {
		// END GENERATED CODE
		return getFileManager().fetchNarrowedObject(null);
		// BEGIN GENERATED CODE
	}

	@Override
	public String identifier() {
		// END GENERATED CODE
		return getIdentifier();
		// BEGIN GENERATED CODE
	}

	@Override
	public void installApplication(final String profileFileName)
		throws InvalidProfile, InvalidFileName, ApplicationInstallationError, ApplicationAlreadyInstalled {
		// END GENERATED CODE
		DomainManager domMgr = fetchNarrowedObject(null);
		if (domMgr == null) {
			throw new IllegalStateException("CORBA Object is Null");
		}
		domMgr.installApplication(profileFileName);
		fetchWaveformFactories(new NullProgressMonitor(), RefreshDepth.SELF);
		// BEGIN GENERATED CODE
	}

	/**
	 * @since 14.0
	 */
	@Override
	public void registerDevice(final Device registeringDevice, final DeviceManager registeredDeviceMgr)
		throws InvalidObjectReference, InvalidProfile, DeviceManagerNotRegistered, RegisterError {
		// END GENERATED CODE
		DomainManager domMgr = fetchNarrowedObject(null);
		if (domMgr == null) {
			throw new IllegalStateException("CORBA Object is Null");
		}
		domMgr.registerDevice(registeringDevice, registeredDeviceMgr);
		// BEGIN GENERATED CODE
	}

	/**
	 * @since 14.0
	 */
	@Override
	public void registerDeviceManager(final DeviceManager deviceMgr) throws InvalidObjectReference, InvalidProfile, RegisterError {
		// END GENERATED CODE
		DomainManager domMgr = fetchNarrowedObject(null);
		if (domMgr == null) {
			throw new IllegalStateException("CORBA Object is Null");
		}
		domMgr.registerDeviceManager(deviceMgr);

		fetchDeviceManagers(new NullProgressMonitor(), RefreshDepth.SELF);
		// BEGIN GENERATED CODE
	}

	/**
	 * @since 14.0
	 */
	@Override
	public void registerService(final org.omg.CORBA.Object registeringService, final DeviceManager registeredDeviceMgr, final String name)
		throws InvalidObjectReference, DeviceManagerNotRegistered, RegisterError {
		// END GENERATED CODE
		DomainManager domMgr = fetchNarrowedObject(null);
		if (domMgr == null) {
			throw new IllegalStateException("CORBA Object is Null");
		}
		domMgr.registerService(registeringService, registeredDeviceMgr, name);
		// BEGIN GENERATED CODE
	}

	@Override
	public void registerWithEventChannel(final org.omg.CORBA.Object registeringObject, final String registeringId, final String eventChannelName)
		throws InvalidObjectReference, InvalidEventChannelName, AlreadyConnected {
		// END GENERATED CODE
		DomainManager domMgr = fetchNarrowedObject(null);
		if (domMgr == null) {
			throw new IllegalStateException("CORBA Object is Null");
		}
		domMgr.registerWithEventChannel(registeringObject, registeringId, eventChannelName);
		// BEGIN GENERATED CODE
	}

	@Override
	public void uninstallApplication(final String applicationId) throws InvalidIdentifier, ApplicationUninstallationError {
		// END GENERATED CODE
		ScaWaveformFactory factory = null;
		try {
			factory = ScaModelCommand.runExclusive(this, new RunnableWithResult.Impl<ScaWaveformFactory>() {

				@Override
				public void run() {
					for (ScaWaveformFactory factory : getWaveformFactories()) {
						if (factory.getIdentifier().equals(applicationId)) {
							setResult(factory);
						}
					}
				}

			});
		} catch (InterruptedException e) {
			// PASS
		}
		if (factory != null) {
			uninstallScaWaveformFactory(factory);
		} else {
			DomainManager domMgr = fetchNarrowedObject(null);
			if (domMgr == null) {
				throw new IllegalStateException("CORBA Object is Null");
			}
			domMgr.uninstallApplication(applicationId);
		}
		// BEGIN GENERATED CODE
	}

	/**
	 * @since 14.0
	 */
	@Override
	public void unregisterDevice(final Device unregisteringDevice) throws InvalidObjectReference, UnregisterError {
		// END GENERATED CODE
		DomainManager domMgr = fetchNarrowedObject(null);
		if (domMgr == null) {
			throw new IllegalStateException("CORBA Object is Null");
		}
		domMgr.unregisterDevice(unregisteringDevice);
		// BEGIN GENERATED CODE
	}

	/**
	 * @since 14.0
	 */
	@Override
	public void unregisterDeviceManager(final DeviceManager deviceMgr) throws InvalidObjectReference, UnregisterError {
		// END GENERATED CODE
		DomainManager domMgr = fetchNarrowedObject(null);
		if (domMgr == null) {
			throw new IllegalStateException("CORBA Object is Null");
		}
		domMgr.unregisterDeviceManager(deviceMgr);
		// BEGIN GENERATED CODE
	}

	@Override
	public void unregisterFromEventChannel(final String unregisteringId, final String eventChannelName) throws InvalidEventChannelName, NotConnected {
		// END GENERATED CODE
		DomainManager domMgr = fetchNarrowedObject(null);
		if (domMgr == null) {
			throw new IllegalStateException("CORBA Object is Null");
		}
		domMgr.unregisterFromEventChannel(unregisteringId, eventChannelName);
		// BEGIN GENERATED CODE
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ScaPackage.SCA_DOMAIN_MANAGER__WAVEFORM_FACTORIES:
			return ((InternalEList<InternalEObject>) (InternalEList< ? >) getWaveformFactories()).basicAdd(otherEnd, msgs);
		case ScaPackage.SCA_DOMAIN_MANAGER__WAVEFORMS:
			return ((InternalEList<InternalEObject>) (InternalEList< ? >) getWaveforms()).basicAdd(otherEnd, msgs);
		case ScaPackage.SCA_DOMAIN_MANAGER__DEVICE_MANAGERS:
			return ((InternalEList<InternalEObject>) (InternalEList< ? >) getDeviceManagers()).basicAdd(otherEnd, msgs);
		case ScaPackage.SCA_DOMAIN_MANAGER__FILE_MANAGER:
			if (fileManager != null)
				msgs = ((InternalEObject) fileManager).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ScaPackage.SCA_DOMAIN_MANAGER__FILE_MANAGER, null, msgs);
			return basicSetFileManager((ScaDomainManagerFileSystem) otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ScaPackage.SCA_DOMAIN_MANAGER__WAVEFORM_FACTORIES:
			return ((InternalEList< ? >) getWaveformFactories()).basicRemove(otherEnd, msgs);
		case ScaPackage.SCA_DOMAIN_MANAGER__WAVEFORMS:
			return ((InternalEList< ? >) getWaveforms()).basicRemove(otherEnd, msgs);
		case ScaPackage.SCA_DOMAIN_MANAGER__DEVICE_MANAGERS:
			return ((InternalEList< ? >) getDeviceManagers()).basicRemove(otherEnd, msgs);
		case ScaPackage.SCA_DOMAIN_MANAGER__FILE_MANAGER:
			return basicUnsetFileManager(msgs);
		case ScaPackage.SCA_DOMAIN_MANAGER__CONNECTION_PROPERTIES_CONTAINER:
			return basicSetConnectionPropertiesContainer(null, msgs);
		case ScaPackage.SCA_DOMAIN_MANAGER__CONNECTION_PROPERTIES:
			return ((InternalEList< ? >) getConnectionProperties()).basicRemove(otherEnd, msgs);
		case ScaPackage.SCA_DOMAIN_MANAGER__EVENT_CHANNELS:
			return ((InternalEList< ? >) getEventChannels()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case ScaPackage.SCA_DOMAIN_MANAGER__WAVEFORM_FACTORIES:
			return getWaveformFactories();
		case ScaPackage.SCA_DOMAIN_MANAGER__WAVEFORMS:
			return getWaveforms();
		case ScaPackage.SCA_DOMAIN_MANAGER__DEVICE_MANAGERS:
			return getDeviceManagers();
		case ScaPackage.SCA_DOMAIN_MANAGER__FILE_MANAGER:
			return getFileManager();
		case ScaPackage.SCA_DOMAIN_MANAGER__CONNECTION_PROPERTIES_CONTAINER:
			if (resolve)
				return getConnectionPropertiesContainer();
			return basicGetConnectionPropertiesContainer();
		case ScaPackage.SCA_DOMAIN_MANAGER__CONNECTION_PROPERTIES:
			if (coreType)
				return getConnectionProperties();
			else
				return getConnectionProperties().map();
		case ScaPackage.SCA_DOMAIN_MANAGER__AUTO_CONNECT:
			return isAutoConnect();
		case ScaPackage.SCA_DOMAIN_MANAGER__CONNECTED:
			return isConnected();
		case ScaPackage.SCA_DOMAIN_MANAGER__IDENTIFIER:
			return getIdentifier();
		case ScaPackage.SCA_DOMAIN_MANAGER__NAME:
			return getName();
		case ScaPackage.SCA_DOMAIN_MANAGER__ROOT_CONTEXT:
			return getRootContext();
		case ScaPackage.SCA_DOMAIN_MANAGER__STATE:
			return getState();
		case ScaPackage.SCA_DOMAIN_MANAGER__PROFILE:
			return getProfile();
		case ScaPackage.SCA_DOMAIN_MANAGER__EVENT_CHANNELS:
			return getEventChannels();
		case ScaPackage.SCA_DOMAIN_MANAGER__LOCAL_NAME:
			return getLocalName();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case ScaPackage.SCA_DOMAIN_MANAGER__WAVEFORM_FACTORIES:
			getWaveformFactories().clear();
			getWaveformFactories().addAll((Collection< ? extends ScaWaveformFactory>) newValue);
			return;
		case ScaPackage.SCA_DOMAIN_MANAGER__WAVEFORMS:
			getWaveforms().clear();
			getWaveforms().addAll((Collection< ? extends ScaWaveform>) newValue);
			return;
		case ScaPackage.SCA_DOMAIN_MANAGER__DEVICE_MANAGERS:
			getDeviceManagers().clear();
			getDeviceManagers().addAll((Collection< ? extends ScaDeviceManager>) newValue);
			return;
		case ScaPackage.SCA_DOMAIN_MANAGER__FILE_MANAGER:
			setFileManager((ScaDomainManagerFileSystem) newValue);
			return;
		case ScaPackage.SCA_DOMAIN_MANAGER__CONNECTION_PROPERTIES_CONTAINER:
			setConnectionPropertiesContainer((Properties) newValue);
			return;
		case ScaPackage.SCA_DOMAIN_MANAGER__CONNECTION_PROPERTIES:
			((EStructuralFeature.Setting) getConnectionProperties()).set(newValue);
			return;
		case ScaPackage.SCA_DOMAIN_MANAGER__AUTO_CONNECT:
			setAutoConnect((Boolean) newValue);
			return;
		case ScaPackage.SCA_DOMAIN_MANAGER__IDENTIFIER:
			setIdentifier((String) newValue);
			return;
		case ScaPackage.SCA_DOMAIN_MANAGER__NAME:
			setName((String) newValue);
			return;
		case ScaPackage.SCA_DOMAIN_MANAGER__ROOT_CONTEXT:
			setRootContext((NamingContextExt) newValue);
			return;
		case ScaPackage.SCA_DOMAIN_MANAGER__STATE:
			setState((DomainConnectionState) newValue);
			return;
		case ScaPackage.SCA_DOMAIN_MANAGER__PROFILE:
			setProfile((String) newValue);
			return;
		case ScaPackage.SCA_DOMAIN_MANAGER__EVENT_CHANNELS:
			getEventChannels().clear();
			getEventChannels().addAll((Collection< ? extends ScaEventChannel>) newValue);
			return;
		case ScaPackage.SCA_DOMAIN_MANAGER__LOCAL_NAME:
			setLocalName((String) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case ScaPackage.SCA_DOMAIN_MANAGER__WAVEFORM_FACTORIES:
			unsetWaveformFactories();
			return;
		case ScaPackage.SCA_DOMAIN_MANAGER__WAVEFORMS:
			unsetWaveforms();
			return;
		case ScaPackage.SCA_DOMAIN_MANAGER__DEVICE_MANAGERS:
			unsetDeviceManagers();
			return;
		case ScaPackage.SCA_DOMAIN_MANAGER__FILE_MANAGER:
			unsetFileManager();
			return;
		case ScaPackage.SCA_DOMAIN_MANAGER__CONNECTION_PROPERTIES_CONTAINER:
			setConnectionPropertiesContainer((Properties) null);
			return;
		case ScaPackage.SCA_DOMAIN_MANAGER__CONNECTION_PROPERTIES:
			getConnectionProperties().clear();
			return;
		case ScaPackage.SCA_DOMAIN_MANAGER__AUTO_CONNECT:
			setAutoConnect(AUTO_CONNECT_EDEFAULT);
			return;
		case ScaPackage.SCA_DOMAIN_MANAGER__IDENTIFIER:
			unsetIdentifier();
			return;
		case ScaPackage.SCA_DOMAIN_MANAGER__NAME:
			setName(NAME_EDEFAULT);
			return;
		case ScaPackage.SCA_DOMAIN_MANAGER__ROOT_CONTEXT:
			unsetRootContext();
			return;
		case ScaPackage.SCA_DOMAIN_MANAGER__STATE:
			setState(STATE_EDEFAULT);
			return;
		case ScaPackage.SCA_DOMAIN_MANAGER__PROFILE:
			unsetProfile();
			return;
		case ScaPackage.SCA_DOMAIN_MANAGER__EVENT_CHANNELS:
			unsetEventChannels();
			return;
		case ScaPackage.SCA_DOMAIN_MANAGER__LOCAL_NAME:
			setLocalName(LOCAL_NAME_EDEFAULT);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case ScaPackage.SCA_DOMAIN_MANAGER__WAVEFORM_FACTORIES:
			return isSetWaveformFactories();
		case ScaPackage.SCA_DOMAIN_MANAGER__WAVEFORMS:
			return isSetWaveforms();
		case ScaPackage.SCA_DOMAIN_MANAGER__DEVICE_MANAGERS:
			return isSetDeviceManagers();
		case ScaPackage.SCA_DOMAIN_MANAGER__FILE_MANAGER:
			return isSetFileManager();
		case ScaPackage.SCA_DOMAIN_MANAGER__CONNECTION_PROPERTIES_CONTAINER:
			return connectionPropertiesContainer != null;
		case ScaPackage.SCA_DOMAIN_MANAGER__CONNECTION_PROPERTIES:
			return !getConnectionProperties().isEmpty();
		case ScaPackage.SCA_DOMAIN_MANAGER__AUTO_CONNECT:
			return autoConnect != AUTO_CONNECT_EDEFAULT;
		case ScaPackage.SCA_DOMAIN_MANAGER__CONNECTED:
			return isConnected() != CONNECTED_EDEFAULT;
		case ScaPackage.SCA_DOMAIN_MANAGER__IDENTIFIER:
			return isSetIdentifier();
		case ScaPackage.SCA_DOMAIN_MANAGER__NAME:
			return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
		case ScaPackage.SCA_DOMAIN_MANAGER__ROOT_CONTEXT:
			return isSetRootContext();
		case ScaPackage.SCA_DOMAIN_MANAGER__STATE:
			return state != STATE_EDEFAULT;
		case ScaPackage.SCA_DOMAIN_MANAGER__PROFILE:
			return isSetProfile();
		case ScaPackage.SCA_DOMAIN_MANAGER__EVENT_CHANNELS:
			return isSetEventChannels();
		case ScaPackage.SCA_DOMAIN_MANAGER__LOCAL_NAME:
			return LOCAL_NAME_EDEFAULT == null ? localName != null : !LOCAL_NAME_EDEFAULT.equals(localName);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public String toString() {
		// END GENERATED CODE
		if (eIsProxy()) {
			return super.toString();
		}

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (autoConnect: ");
		result.append(autoConnect);
		result.append(", identifier: ");
		if (identifierESet) {
			result.append(identifier);
		} else {
			result.append("<unset>");
		}
		result.append(", name: ");
		result.append(name);
		result.append(", rootContext: ");
		// NOTE: DO NOT DO TO STRING on a CORBA Object, this is a potentially blocking operation. Just return if the
		// value is set
		if (rootContextESet) {
			result.append("<set>");
		} else {
			result.append("<unset>");
		}
		result.append(", state: ");
		result.append(state);
		result.append(", profile: ");
		if (profileESet) {
			result.append(profile);
		} else {
			result.append("<unset>");
		}
		result.append(", localName: ");
		result.append(localName);
		result.append(')');
		return result.toString();
		// BEGIN GENERATED CODE
	}

	@Override
	public void unregisterService(final org.omg.CORBA.Object unregisteringService, final String name) throws InvalidObjectReference, UnregisterError {
		// END GENERATED CODE
		DomainManager domMgr = fetchNarrowedObject(null);
		if (domMgr == null) {
			throw new IllegalStateException("CORBA Object is Null");
		}
		domMgr.unregisterService(unregisteringService, name);
		// BEGIN GENERATED CODE
	}

	/**
	 * @since 20.0
	 */
	@Override
	public void initializeProperties(final DataType[] configProperties) throws AlreadyInitialized, InvalidConfiguration, PartialConfiguration {
		// END GENERATED CODE
		DomainManager domMgr = fetchNarrowedObject(null);
		if (domMgr == null) {
			throw new IllegalStateException("CORBA Object is null");
		}
		domMgr.initializeProperties(configProperties);
		// BEGIN GENERATED CODE
	}

	@Override
	public void configure(final DataType[] configProperties) throws InvalidConfiguration, PartialConfiguration {
		// END GENERATED CODE
		DomainManager domMgr = fetchNarrowedObject(null);
		if (domMgr == null) {
			throw new IllegalStateException("CORBA Object is Null");
		}
		domMgr.configure(configProperties);
		fetchProperties(null);
		// BEGIN GENERATED CODE
	}

	@Override
	public void query(final PropertiesHolder configProperties) throws UnknownProperties {
		// END GENERATED CODE
		DomainManager domMgr = fetchNarrowedObject(null);
		if (domMgr != null) {
			domMgr.query(configProperties);
		}
		// BEGIN GENERATED CODE
	}

	/**
	 * @since 14.0
	 */
	@Override
	protected DomainManager narrow(final org.omg.CORBA.Object obj) {
		// END GENERATED CODE
		return DomainManagerHelper.narrow(obj);
		// BEGIN GENERATED CODE
	}

	@Override
	protected Class<DomainManager> getCorbaType() {
		return DomainManager.class;
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
		SubMonitor subMonitor = SubMonitor.convert(monitor, 7);
		fetchKeepAlive(subMonitor.split(1));
		super.fetchAttributes(subMonitor.split(1));
		fetchIdentifier(subMonitor.split(1));
		fetchProfile(subMonitor.split(1));
		fetchFileManager(subMonitor.split(1), RefreshDepth.SELF);
		fetchProfileObject(subMonitor.split(1));
		fetchProperties(subMonitor.split(1));
		subMonitor.done();
		// BEGIN GENERATED CODE
	}

	// END GENERATED CODE

	@Override
	protected void internalFetchChildren(IProgressMonitor monitor) throws InterruptedException {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 4);
		internalFetchDeviceManagers(subMonitor.split(1));
		internalFetchWaveformFactories(subMonitor.split(1));
		internalFetchWaveforms(subMonitor.split(1));
		internalFetchEventChannels(subMonitor.split(1));
		subMonitor.done();
	}

	private final VersionedFeature keepAliveFeature = new VersionedFeature(this, ScaPackage.Literals.CORBA_OBJ_WRAPPER__CORBA_OBJ);

	private void fetchKeepAlive(IProgressMonitor monitor) {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 2);
		if (subMonitor.isCanceled()) {
			throw new OperationCanceledException();
		}
		final boolean tmpExists = exists();
		subMonitor.worked(1);

		final NamingContextExt namingContext = rootContext;
		final String domMgrname = getDomMgrName();
		Boolean shouldProceed;
		try {
			shouldProceed = ScaModelCommand.runExclusive(this, new RunnableWithResult.Impl<Boolean>() {

				@Override
				public void run() {
					setResult(isConnected() && !tmpExists && namingContext != null && domMgrname != null);
				}

			});
		} catch (InterruptedException e1) {
			return;
		}
		if (shouldProceed != null && shouldProceed) {
			try {
				Transaction transaction = keepAliveFeature.createTransaction();
				if (subMonitor.isCanceled()) {
					throw new OperationCanceledException();
				}
				final org.omg.CORBA.Object newRootContext = CorbaUtils.resolve_str(namingContext, domMgrname, monitor);
				subMonitor.worked(1);

				transaction.addCommand(new ScaModelCommand() {

					@Override
					public void execute() {
						if (namingContext != getRootContext() || !domMgrname.equals(getDomMgrName())) {
							return;
						}
						setCorbaObj(newRootContext);
					}

				});
				transaction.commit();
			} catch (CoreException e) {
				if (DEBUG_KEEP_ALIVE_ERRORS.enabled) {
					DEBUG_KEEP_ALIVE_ERRORS.message("Errors durring fetch keep alive.");
					DEBUG_KEEP_ALIVE_ERRORS.catching(e);
				}
			} catch (InterruptedException e) {
				throw new OperationCanceledException();
			}
		}

		subMonitor.done();
	}

	private VersionedFeature fileManagerFeature = new VersionedFeature(this, ScaPackage.Literals.SCA_DOMAIN_MANAGER__FILE_MANAGER);

	private final VersionedFeature identifierRevision = new VersionedFeature(this, ScaPackage.Literals.SCA_DOMAIN_MANAGER__IDENTIFIER);

	// BEGIN GENERATED CODE

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @since 14.0
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String fetchIdentifier(IProgressMonitor monitor) {
		// END GENERATED CODE
		if (isSetIdentifier()) {
			return getIdentifier();
		}

		SubMonitor subMonitor = SubMonitor.convert(monitor, "Fetch Identifier", 3);
		DomainManager resource = fetchNarrowedObject(subMonitor.split(1));

		Transaction transaction = identifierRevision.createTransaction();
		if (resource != null) {
			try {
				if (subMonitor.isCanceled()) {
					throw new OperationCanceledException();
				}
				String newValue = resource.identifier();
				subMonitor.worked(1);
				transaction.append(new SetLocalAttributeCommand(this, newValue, ScaPackage.Literals.SCA_DOMAIN_MANAGER__IDENTIFIER));
			} catch (final SystemException e) {
				IStatus startedStatus = new Status(Status.ERROR, ScaModelPlugin.ID, "Failed to fetch identifier.", e);
				transaction.append(new UnsetLocalAttributeCommand(this, startedStatus, ScaPackage.Literals.SCA_DOMAIN_MANAGER__IDENTIFIER));
			}
		} else {
			transaction.append(new UnsetLocalAttributeCommand(this, null, ScaPackage.Literals.SCA_DOMAIN_MANAGER__IDENTIFIER));
			subMonitor.setWorkRemaining(1);
		}

		transaction.commit();
		subMonitor.worked(1);
		subMonitor.done();
		return getIdentifier();
		// BEGIN GENERATED CODE
	}

	// END GENERATED CODE

	private static final EStructuralFeature[] PRF_PATH = { DmdPackage.Literals.DOMAIN_MANAGER_CONFIGURATION__DOMAIN_MANAGER_SOFT_PKG,
		DmdPackage.Literals.DOMAIN_MANAGER_SOFT_PKG__SOFT_PKG, SpdPackage.Literals.SOFT_PKG__PROPERTY_FILE, SpdPackage.Literals.PROPERTY_FILE__PROPERTIES };

	@Override
	protected List<AbstractProperty> fetchPropertyDefinitions(IProgressMonitor monitor) {
		if (isDisposed()) {
			return Collections.emptyList();
		}

		DomainManagerConfiguration dmd = fetchProfileObject(monitor);
		mil.jpeojtrs.sca.prf.Properties propDefintions = ScaEcoreUtils.getFeature(dmd, PRF_PATH);
		List<AbstractProperty> retVal = new ArrayList<AbstractProperty>();
		if (propDefintions != null) {
			for (ValueListIterator<Object> i = propDefintions.getProperties().valueListIterator(); i.hasNext();) {
				Object propDef = i.next();
				if (propDef instanceof AbstractProperty) {
					retVal.add((AbstractProperty) propDef);
				}
			}
		}
		return retVal;
	}

	private final VersionedFeature profileObjectFeature = new VersionedFeature(this, ScaPackage.Literals.PROFILE_OBJECT_WRAPPER__PROFILE_OBJ);

	// BEGIN GENERATED CODE

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @since 14.0
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public DomainManagerConfiguration fetchProfileObject(IProgressMonitor monitor) {
		// END GENERATED CODE
		if (isDisposed()) {
			return null;
		}
		if (isSetProfileObj()) {
			return getProfileObj();
		}

		Transaction transaction = profileObjectFeature.createTransaction();
		transaction.addCommand(
			ProfileObjectWrapper.Util.fetchProfileObject(monitor, this, DomainManagerConfiguration.class, DomainManagerConfiguration.EOBJECT_PATH));
		transaction.commit();
		return getProfileObj();
		// BEGIN GENERATED CODE
	}

	private final VersionedFeature profileRevision = new VersionedFeature(this, ScaPackage.Literals.SCA_DOMAIN_MANAGER__PROFILE);

	/**
	 * @since 14.0
	 * @generated NOT
	 */
	@Override
	public String fetchProfile(IProgressMonitor monitor) {
		// END GENERATED CODE
		if (isDisposed()) {
			return null;
		}
		if (isSetProfile()) {
			return getProfile();
		}

		SubMonitor subMonitor = SubMonitor.convert(monitor, "Fetching profile", 3);
		DomainManager resource = fetchNarrowedObject(subMonitor.split(1));

		Transaction transaction = profileRevision.createTransaction();
		if (resource != null) {
			try {
				if (subMonitor.isCanceled()) {
					throw new OperationCanceledException();
				}
				String newValue = resource.domainManagerProfile();
				subMonitor.worked(1);
				transaction.append(new SetLocalAttributeCommand(this, newValue, ScaPackage.Literals.SCA_DOMAIN_MANAGER__PROFILE));
			} catch (final SystemException e) {
				IStatus startedStatus = new Status(Status.ERROR, ScaModelPlugin.ID, "Failed to fetch profile.", e);
				transaction.append(new UnsetLocalAttributeCommand(this, startedStatus, ScaPackage.Literals.SCA_DOMAIN_MANAGER__PROFILE));
			}
		} else {
			subMonitor.setWorkRemaining(1);
			transaction.append(new UnsetLocalAttributeCommand(this, null, ScaPackage.Literals.SCA_DOMAIN_MANAGER__PROFILE));
		}

		transaction.commit();
		subMonitor.worked(1);
		subMonitor.done();
		return getProfile();
		// BEGIN GENERATED CODE
	}

	private final VersionedFeature eventChannelFeature = new VersionedFeature(this, ScaPackage.Literals.SCA_DOMAIN_MANAGER__EVENT_CHANNELS);

	/**
	 * @since 19.0
	 */
	protected void internalFetchEventChannels(IProgressMonitor monitor) {
		// END GENERATED CODE
		if (isDisposed()) {
			return;
		}

		SubMonitor subMonitor = SubMonitor.convert(monitor, 4);
		NamingContextExt localRootContext = getRootContext();

		Transaction transaction = eventChannelFeature.createTransaction();
		if (localRootContext != null) {
			try {
				// Resolve the naming context
				if (subMonitor.isCanceled()) {
					throw new OperationCanceledException();
				}
				NamingContextExt context = NamingContextExtHelper.narrow(localRootContext.resolve_str(getName()));
				subMonitor.worked(1);

				// List children
				BindingIteratorHolder bi = new BindingIteratorHolder();
				BindingListHolder bl = new BindingListHolder();
				if (subMonitor.isCanceled()) {
					throw new OperationCanceledException();
				}
				context.list(-1, bl, bi);
				subMonitor.worked(1);

				// Iterate children looking for EventChannel's
				SubMonitor checkChildProgress = subMonitor.newChild(1).setWorkRemaining(bl.value.length);
				List<ScaEventChannel> newChannels = new ArrayList<ScaEventChannel>();
				for (Binding b : bl.value) {
					if (subMonitor.isCanceled()) {
						throw new OperationCanceledException();
					}
					org.omg.CORBA.Object objC = context.resolve(b.binding_name);
					if (objC._is_a(EventChannelHelper.id())) {
						ScaEventChannel channel = ScaFactory.eINSTANCE.createScaEventChannel();
						channel.setName(Name.toString(b.binding_name));
						channel.setCorbaObj(objC);
						newChannels.add(channel);
					}
					checkChildProgress.worked(1);
				}
				transaction.addCommand(new ScaDomainManagerMergeEventChannelsCommand(this, newChannels));
			} catch (SystemException e) {
				Status status = new Status(Status.ERROR, ScaModelPlugin.ID, "Failed to fetch Event Channels.", e);
				transaction.addCommand(new UnsetLocalAttributeCommand(this, status, ScaPackage.Literals.SCA_DOMAIN_MANAGER__EVENT_CHANNELS));
			} catch (NotFound e) {
				Status status = new Status(Status.ERROR, ScaModelPlugin.ID, "Failed to fetch Event Channels.", e);
				transaction.addCommand(new UnsetLocalAttributeCommand(this, status, ScaPackage.Literals.SCA_DOMAIN_MANAGER__EVENT_CHANNELS));
			} catch (CannotProceed e) {
				Status status = new Status(Status.ERROR, ScaModelPlugin.ID, "Failed to fetch Event Channels.", e);
				transaction.addCommand(new UnsetLocalAttributeCommand(this, status, ScaPackage.Literals.SCA_DOMAIN_MANAGER__EVENT_CHANNELS));
			} catch (InvalidName e) {
				Status status = new Status(Status.ERROR, ScaModelPlugin.ID, "Failed to fetch Event Channels.", e);
				transaction.addCommand(new UnsetLocalAttributeCommand(this, status, ScaPackage.Literals.SCA_DOMAIN_MANAGER__EVENT_CHANNELS));
			}
		} else {
			transaction.addCommand(new UnsetLocalAttributeCommand(this, null, ScaPackage.Literals.SCA_DOMAIN_MANAGER__EVENT_CHANNELS));
		}

		// Perform Actions
		subMonitor.setWorkRemaining(1);
		transaction.commit();
		subMonitor.done();
		// BEGIN GENERATED CODE
	}

	private final VersionedFeature profileURIFeature = new VersionedFeature(this, ScaPackage.Literals.PROFILE_OBJECT_WRAPPER__PROFILE_URI);

	@Override
	public URI fetchProfileURI(IProgressMonitor monitor) {
		// END GENERATED CODE
		if (isDisposed()) {
			return null;
		}
		if (isSetProfileURI()) {
			return getProfileURI();
		}

		SubMonitor subMonitor = SubMonitor.convert(monitor, "Fetch Profile URI", 2);
		ScaDomainManagerFileSystem fileSystem = fetchFileManager(subMonitor.split(1), RefreshDepth.SELF);

		if (fileSystem != null) {
			Transaction transaction = profileURIFeature.createTransaction();
			final URI newURI = fileSystem.createURI(fetchProfile(subMonitor.split(1)));

			transaction.addCommand(new ScaModelCommand() {

				@Override
				public void execute() {
					setProfileURI(newURI);
				}
			});
			transaction.commit();
		}

		subMonitor.done();
		return getProfileURI();
		// BEGIN GENERATED CODE
	}

	/**
	 * @since 19.0
	 */
	@Override
	public AllocationManager allocationMgr() {
		return getObj().allocationMgr();
	}

	/**
	 * @since 19.0
	 */
	@Override
	public String name() {
		return getName();
	}

	/**
	 * @since 19.0
	 */
	@Override
	public DomainManager[] remoteDomainManagers() {
		return getObj().remoteDomainManagers();
	}

	/**
	 * @since 19.0
	 */
	@Override
	public void registerRemoteDomainManager(DomainManager registeringDomainManager) throws InvalidObjectReference, RegisterError {
		getObj().registerRemoteDomainManager(registeringDomainManager);
	}

	/**
	 * @since 19.0
	 */
	@Override
	public void unregisterRemoteDomainManager(DomainManager unregisteringDomainManager) throws InvalidObjectReference, UnregisterError {
		getObj().unregisterRemoteDomainManager(unregisteringDomainManager);
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @since 20.0
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public EList<ScaDeviceManager> fetchDeviceManagers(IProgressMonitor monitor, RefreshDepth depth) {
		// END GENERATED CODE
		SubMonitor subMonitor = SubMonitor.convert(monitor, "Fetch Device Managers", 2);
		internalFetchDeviceManagers(subMonitor.split(1));

		List<ScaDeviceManager> deviceManagersCopy = ScaModelCommandWithResult.execute(this, new ScaModelCommandWithResult<List<ScaDeviceManager>>() {

			@Override
			public void execute() {
				setResult(new ArrayList<>(getDeviceManagers()));
			}
		});
		if (deviceManagersCopy != null && depth != RefreshDepth.NONE) {
			SubMonitor deviceMonitor = subMonitor.newChild(1).setWorkRemaining(deviceManagersCopy.size());
			for (ScaDeviceManager element : deviceManagersCopy) {
				try {
					element.refresh(deviceMonitor.split(1), depth);
				} catch (InterruptedException e) {
					throw new OperationCanceledException();
				}
			}
		}

		subMonitor.done();
		if (deviceManagersCopy != null) {
			return ECollections.unmodifiableEList(new BasicEList<ScaDeviceManager>(deviceManagersCopy));
		} else {
			return ECollections.emptyEList();
		}
		// BEGIN GENERATED CODE
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @since 20.0
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public EList<ScaWaveformFactory> fetchWaveformFactories(IProgressMonitor monitor, RefreshDepth depth) {
		if (isDisposed()) {
			return ECollections.emptyEList();
		}

		SubMonitor subMonitor = SubMonitor.convert(monitor, "Fetch Waveform Factories", 2);
		internalFetchWaveformFactories(subMonitor.split(1));

		List<ScaWaveformFactory> waveformFactoriesCopy = ScaModelCommandWithResult.execute(this, new ScaModelCommandWithResult<List<ScaWaveformFactory>>() {

			@Override
			public void execute() {
				setResult(new ArrayList<>(getWaveformFactories()));
			}
		});
		if (waveformFactoriesCopy != null && depth != RefreshDepth.NONE) {
			SubMonitor childMonitor = subMonitor.newChild(1).setWorkRemaining(waveformFactoriesCopy.size());
			for (ScaWaveformFactory element : waveformFactoriesCopy) {
				try {
					element.refresh(childMonitor.split(1), depth);
				} catch (InterruptedException e) {
					throw new OperationCanceledException();
				}
			}
		}

		subMonitor.done();
		if (waveformFactoriesCopy != null) {
			return ECollections.unmodifiableEList(new BasicEList<ScaWaveformFactory>(waveformFactoriesCopy));
		} else {
			return ECollections.emptyEList();
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @since 20.0
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public EList<ScaWaveform> fetchWaveforms(IProgressMonitor monitor, RefreshDepth depth) {
		// END GENERATED CODE
		if (isDisposed()) {
			return ECollections.emptyEList();
		}

		SubMonitor subMonitor = SubMonitor.convert(monitor, "Fetch Waveforms", 2);
		internalFetchWaveforms(subMonitor.split(1));

		List<ScaWaveform> waveformsCopy = ScaModelCommandWithResult.execute(this, new ScaModelCommandWithResult<List<ScaWaveform>>() {

			@Override
			public void execute() {
				setResult(new ArrayList<>(getWaveforms()));
			}
		});
		if (waveformsCopy != null && depth != RefreshDepth.NONE) {
			SubMonitor childMonitor = subMonitor.newChild(1).setWorkRemaining(waveformsCopy.size());
			for (ScaWaveform element : waveformsCopy) {
				try {
					element.refresh(childMonitor.newChild(1), depth);
				} catch (InterruptedException e) {
					throw new OperationCanceledException();
				}
			}
		}

		subMonitor.done();
		if (waveformsCopy != null) {
			return ECollections.unmodifiableEList(new BasicEList<ScaWaveform>(waveformsCopy));
		} else {
			return ECollections.emptyEList();
		}
		// BEGIN GENERATED CODE
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @since 20.0
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public ScaDomainManagerFileSystem fetchFileManager(IProgressMonitor monitor, RefreshDepth depth) {
		// END GENERATED CODE
		if (isSetFileManager()) {
			return getFileManager();
		}

		SubMonitor subMonitor = SubMonitor.convert(monitor, 3);
		final DomainManager localObj = fetchNarrowedObject(subMonitor.split(1));

		Transaction transaction = fileManagerFeature.createTransaction();
		if (localObj != null) {
			try {
				if (subMonitor.isCanceled()) {
					throw new OperationCanceledException();
				}
				final FileManager newFileMgr = localObj.fileMgr();
				subMonitor.worked(1);

				transaction.addCommand(new ScaModelCommand() {

					@Override
					public void execute() {
						if (fileManager == null) {
							setFileManager(ScaFactory.eINSTANCE.createScaDomainManagerFileSystem());
						}
						fileManager.setCorbaObj(newFileMgr);

						setStatus(ScaPackage.Literals.SCA_DOMAIN_MANAGER__FILE_MANAGER, null);
					}

				});
			} catch (SystemException e) {
				transaction.addCommand(new UnsetLocalAttributeCommand(this, new Status(Status.ERROR, ScaModelPlugin.ID, "Failed to fetch file manager.", e),
					ScaPackage.Literals.SCA_DOMAIN_MANAGER__FILE_MANAGER));
			}
		} else {
			transaction.addCommand(new UnsetLocalAttributeCommand(this, null, ScaPackage.Literals.SCA_DOMAIN_MANAGER__FILE_MANAGER));
		}

		subMonitor.setWorkRemaining(1);
		transaction.commit();
		ScaDomainManagerFileSystem localFileManager = getFileManager();
		if (localFileManager != null && depth != RefreshDepth.NONE) {
			try {
				localFileManager.refresh(subMonitor.split(1), depth);
			} catch (InterruptedException e) {
				throw new OperationCanceledException();
			}
		}

		subMonitor.done();
		return getFileManager();
		// BEGIN GENERATED CODE
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @since 20.0
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public EList<ScaEventChannel> fetchEventChannels(IProgressMonitor monitor, RefreshDepth depth) {
		SubMonitor subMonitor = SubMonitor.convert(monitor, "Fetch Event Channels", 2);
		internalFetchEventChannels(subMonitor.split(1));

		List<ScaEventChannel> eventChannelsCopy = ScaModelCommandWithResult.execute(this, new ScaModelCommandWithResult<List<ScaEventChannel>>() {

			@Override
			public void execute() {
				setResult(new ArrayList<>(getEventChannels()));
			}
		});
		if (eventChannelsCopy != null && depth != RefreshDepth.NONE) {
			SubMonitor childMonitor = subMonitor.newChild(1).setWorkRemaining(eventChannelsCopy.size());
			for (ScaEventChannel element : eventChannelsCopy) {
				try {
					element.refresh(childMonitor.split(1), depth);
				} catch (InterruptedException e) {
					throw new OperationCanceledException();
				}
			}
		}

		subMonitor.done();
		if (eventChannelsCopy != null) {
			return ECollections.unmodifiableEList(new BasicEList<ScaEventChannel>(eventChannelsCopy));
		} else {
			return ECollections.emptyEList();
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @since 20.0
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String getLabel() {
		if (getLocalName() != null) {
			return getLocalName();
		} else {
			return getName();
		}
	}

	/**
	 * @since 20.4
	 */
	@Override
	public LogEvent[] retrieve_records(IntHolder howMany, int startingRecord) {
		return getObj().retrieve_records(howMany, startingRecord);
	}

	/**
	 * @since 20.4
	 */
	@Override
	public LogEvent[] retrieve_records_by_date(IntHolder howMany, long to_timeStamp) {
		return getObj().retrieve_records_by_date(howMany, to_timeStamp);
	}

	/**
	 * @since 20.4
	 */
	@Override
	public LogEvent[] retrieve_records_from_date(IntHolder howMany, long from_timeStamp) {
		return getObj().retrieve_records_from_date(howMany, from_timeStamp);
	}

	/**
	 * @since 20.4
	 */
	@Override
	public int log_level() {
		DomainManager domMgr = fetchNarrowedObject(null);
		if (domMgr == null) {
			throw new IllegalStateException("CORBA Object is Null");
		}
		return domMgr.log_level();
	}

	/**
	 * @since 20.4
	 */
	@Override
	public void log_level(int newLog_level) {
		DomainManager domMgr = fetchNarrowedObject(null);
		if (domMgr == null) {
			throw new IllegalStateException("CORBA Object is Null");
		}
		domMgr.log_level(newLog_level);
	}

	/**
	 * @since 20.4
	 */
	@Override
	public void setLogLevel(String logger_id, int newLevel) throws UnknownIdentifier {
		DomainManager domMgr = fetchNarrowedObject(null);
		if (domMgr == null) {
			throw new IllegalStateException("CORBA Object is Null");
		}
		domMgr.setLogLevel(logger_id, newLevel);
	}

	/**
	 * @since 20.4
	 */
	@Override
	public String getLogConfig() {
		DomainManager domMgr = fetchNarrowedObject(null);
		if (domMgr == null) {
			throw new IllegalStateException("CORBA Object is Null");
		}
		return domMgr.getLogConfig();
	}

	/**
	 * @since 20.4
	 */
	@Override
	public void setLogConfig(String config_contents) {
		DomainManager domMgr = fetchNarrowedObject(null);
		if (domMgr == null) {
			throw new IllegalStateException("CORBA Object is Null");
		}
		domMgr.setLogConfig(config_contents);
	}

	/**
	 * @since 20.4
	 */
	@Override
	public void setLogConfigURL(String config_url) {
		DomainManager domMgr = fetchNarrowedObject(null);
		if (domMgr == null) {
			throw new IllegalStateException("CORBA Object is Null");
		}
		domMgr.setLogConfigURL(config_url);
	}

} // ScaDomainManagerImpl
