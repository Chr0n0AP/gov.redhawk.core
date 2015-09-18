package CF.jni;

public class _DomainManagerStub extends omnijni.ObjectImpl implements CF.DomainManager
{
  public _DomainManagerStub ()
  {
  }

  protected _DomainManagerStub (long ref)
  {
    super(ref);
  }

  public void configure (CF.DataType[] configProperties)
  {
    configure(this.ref_, configProperties);
  }
  private static native void configure (long __ref__, CF.DataType[] configProperties);

  public void query (CF.PropertiesHolder configProperties)
  {
    query(this.ref_, configProperties);
  }
  private static native void query (long __ref__, CF.PropertiesHolder configProperties);

  public void initializeProperties (CF.DataType[] initialProperties)
  {
    initializeProperties(this.ref_, initialProperties);
  }
  private static native void initializeProperties (long __ref__, CF.DataType[] initialProperties);

  public String registerPropertyListener (org.omg.CORBA.Object obj, String[] prop_ids, float interval)
  {
    return registerPropertyListener(this.ref_, obj, prop_ids, interval);
  }
  private static native String registerPropertyListener (long __ref__, org.omg.CORBA.Object obj, String[] prop_ids, float interval);

  public void unregisterPropertyListener (String id)
  {
    unregisterPropertyListener(this.ref_, id);
  }
  private static native void unregisterPropertyListener (long __ref__, String id);

  public String domainManagerProfile ()
  {
    return _get_domainManagerProfile(this.ref_);
  }
  private static native String _get_domainManagerProfile (long __ref__);

  public CF.DeviceManager[] deviceManagers ()
  {
    return _get_deviceManagers(this.ref_);
  }
  private static native CF.DeviceManager[] _get_deviceManagers (long __ref__);

  public CF.Application[] applications ()
  {
    return _get_applications(this.ref_);
  }
  private static native CF.Application[] _get_applications (long __ref__);

  public CF.ApplicationFactory[] applicationFactories ()
  {
    return _get_applicationFactories(this.ref_);
  }
  private static native CF.ApplicationFactory[] _get_applicationFactories (long __ref__);

  public CF.FileManager fileMgr ()
  {
    return _get_fileMgr(this.ref_);
  }
  private static native CF.FileManager _get_fileMgr (long __ref__);

  public CF.AllocationManager allocationMgr ()
  {
    return _get_allocationMgr(this.ref_);
  }
  private static native CF.AllocationManager _get_allocationMgr (long __ref__);

  public CF.ConnectionManager connectionMgr ()
  {
    return _get_connectionMgr(this.ref_);
  }
  private static native CF.ConnectionManager _get_connectionMgr (long __ref__);

  public CF.EventChannelManager eventChannelMgr ()
  {
    return _get_eventChannelMgr(this.ref_);
  }
  private static native CF.EventChannelManager _get_eventChannelMgr (long __ref__);

  public String identifier ()
  {
    return _get_identifier(this.ref_);
  }
  private static native String _get_identifier (long __ref__);

  public String name ()
  {
    return _get_name(this.ref_);
  }
  private static native String _get_name (long __ref__);

  public CF.DomainManager[] remoteDomainManagers ()
  {
    return _get_remoteDomainManagers(this.ref_);
  }
  private static native CF.DomainManager[] _get_remoteDomainManagers (long __ref__);

  public void registerDevice (CF.Device registeringDevice, CF.DeviceManager registeredDeviceMgr)
  {
    registerDevice(this.ref_, registeringDevice, registeredDeviceMgr);
  }
  private static native void registerDevice (long __ref__, CF.Device registeringDevice, CF.DeviceManager registeredDeviceMgr);

  public void registerDeviceManager (CF.DeviceManager deviceMgr)
  {
    registerDeviceManager(this.ref_, deviceMgr);
  }
  private static native void registerDeviceManager (long __ref__, CF.DeviceManager deviceMgr);

  public void unregisterDeviceManager (CF.DeviceManager deviceMgr)
  {
    unregisterDeviceManager(this.ref_, deviceMgr);
  }
  private static native void unregisterDeviceManager (long __ref__, CF.DeviceManager deviceMgr);

  public void unregisterDevice (CF.Device unregisteringDevice)
  {
    unregisterDevice(this.ref_, unregisteringDevice);
  }
  private static native void unregisterDevice (long __ref__, CF.Device unregisteringDevice);

  public CF.Application createApplication (String profileFileName, String name, CF.DataType[] initConfiguration, CF.DeviceAssignmentType[] deviceAssignments)
  {
    return createApplication(this.ref_, profileFileName, name, initConfiguration, deviceAssignments);
  }
  private static native CF.Application createApplication (long __ref__, String profileFileName, String name, CF.DataType[] initConfiguration, CF.DeviceAssignmentType[] deviceAssignments);

  public void installApplication (String profileFileName)
  {
    installApplication(this.ref_, profileFileName);
  }
  private static native void installApplication (long __ref__, String profileFileName);

  public void uninstallApplication (String applicationId)
  {
    uninstallApplication(this.ref_, applicationId);
  }
  private static native void uninstallApplication (long __ref__, String applicationId);

  public void registerService (org.omg.CORBA.Object registeringService, CF.DeviceManager registeredDeviceMgr, String name)
  {
    registerService(this.ref_, registeringService, registeredDeviceMgr, name);
  }
  private static native void registerService (long __ref__, org.omg.CORBA.Object registeringService, CF.DeviceManager registeredDeviceMgr, String name);

  public void unregisterService (org.omg.CORBA.Object unregisteringService, String name)
  {
    unregisterService(this.ref_, unregisteringService, name);
  }
  private static native void unregisterService (long __ref__, org.omg.CORBA.Object unregisteringService, String name);

  public void registerWithEventChannel (org.omg.CORBA.Object registeringObject, String registeringId, String eventChannelName)
  {
    registerWithEventChannel(this.ref_, registeringObject, registeringId, eventChannelName);
  }
  private static native void registerWithEventChannel (long __ref__, org.omg.CORBA.Object registeringObject, String registeringId, String eventChannelName);

  public void unregisterFromEventChannel (String unregisteringId, String eventChannelName)
  {
    unregisterFromEventChannel(this.ref_, unregisteringId, eventChannelName);
  }
  private static native void unregisterFromEventChannel (long __ref__, String unregisteringId, String eventChannelName);

  public void registerRemoteDomainManager (CF.DomainManager registeringDomainManager)
  {
    registerRemoteDomainManager(this.ref_, registeringDomainManager);
  }
  private static native void registerRemoteDomainManager (long __ref__, CF.DomainManager registeringDomainManager);

  public void unregisterRemoteDomainManager (CF.DomainManager unregisteringDomainManager)
  {
    unregisterRemoteDomainManager(this.ref_, unregisteringDomainManager);
  }
  private static native void unregisterRemoteDomainManager (long __ref__, CF.DomainManager unregisteringDomainManager);

  private static String __ids[] = {
    "IDL:CF/DomainManager:1.0",
    "IDL:CF/PropertyEmitter:1.0",
  };

  public String[] _ids ()
  {
    return (String[])__ids.clone();
  }

  static {
    System.loadLibrary("ossiecfjni");
  }

  protected native long _get_object_ref(long ref);
  protected native long _narrow_object_ref(long ref);
}
