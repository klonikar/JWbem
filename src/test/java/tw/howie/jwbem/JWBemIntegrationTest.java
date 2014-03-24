/**
 * 
 */
package tw.howie.jwbem;

import java.net.UnknownHostException;

import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.impls.automation.JIAutomationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import tw.howie.jwbem.SWbemEventSource.EventType;
import tw.howie.jwbem.exception.SWbemException;
import tw.howie.jwbem.msvm.event.InstanceModificationEvent;
import tw.howie.jwbem.msvm.memory.MsvmMemory;
import tw.howie.jwbem.msvm.networking.MsvmSwitchPort;
import tw.howie.jwbem.msvm.networking.MsvmSyntheticEthernetPort;
import tw.howie.jwbem.msvm.networking.MsvmVirtualEthernetSwitch;
import tw.howie.jwbem.msvm.networking.MsvmVirtualSwitch;
import tw.howie.jwbem.msvm.networking.MsvmVirtualSwitchManagementService;
import tw.howie.jwbem.msvm.processor.MsvmProcessor;
import tw.howie.jwbem.msvm.virtualsystem.MsvmComputerSystem;
import tw.howie.jwbem.msvm.virtualsystem.MsvmSummaryInformation;
import tw.howie.jwbem.msvm.virtualsystem.MsvmVirtualSystemSettingData;
import tw.howie.jwbem.msvm.virtualsystemmanagement.MsvmVirtualSystemManagementService;
import tw.howie.jwbem.win32.Win32ComputerSystem;
import tw.howie.jwbem.win32.Win32NetworkAdapter;
import tw.howie.jwbem.win32.Win32Processor;

/**
 * @author howie_yu
 */
@RunWith(JUnit4.class)
public class JWBemIntegrationTest {

	private String			hostname		= HyperVTestInfo.hostname;

	private String			username		= HyperVTestInfo.username;

	private String			passphrase		= HyperVTestInfo.passphrase;

	private String			cimNamespace	= HyperVTestInfo.cimNamespace;

	private SWbemServices	svc;

	private SWbemServices	cimv2;

	private SWbemServices	v1;

	private SWbemServices createV1() throws UnknownHostException, JIException {

		// Create a locator object.
		SWbemLocator loc = new SWbemLocator();

		// Connect to the Windows server and return a services object.
		return loc.connect(hostname, hostname, "root\\virtualization", username, passphrase);
	}

	private SWbemServices createSWbemServices() throws UnknownHostException, JIException {

		// Create a locator object.
		SWbemLocator loc = new SWbemLocator();

		// Connect to the Windows server and return a services object.
		return loc.connect(hostname, hostname, cimNamespace, username, passphrase);
	}

	private SWbemServices createCIMV2() throws UnknownHostException, JIException {

		// Create a locator object.
		SWbemLocator loc = new SWbemLocator();

		// Connect to the Windows server and return a services object.
		return loc.connect(hostname, hostname, "ROOT\\CIMV2", username, passphrase);
	}

	@Before
	public void init() throws UnknownHostException, JIException {
		// Connect to the Windows server and return a services object.
		svc = createSWbemServices();
		cimv2 = createCIMV2();
		v1 = createV1();
	}

	@After
	public void close() {
		svc.getLocator().disconnect();
		cimv2.getLocator().disconnect();
		v1.getLocator().disconnect();
	}

	@Test
	public void testGetByUUID() throws Exception {

		String name = "140E4DD2-2537-40DF-A4AD-75E20FC79D24";
		MsvmComputerSystem cs = MsvmComputerSystem.getByName(svc, MsvmComputerSystem.class, name);

		System.out.println(cs.getElementName());
		System.out.println("EnabledState:" + cs.getEnabledState());
		System.out.println("HealthState:" + cs.getHealthState());
		System.out.println("Name:" + cs.getName()); // uuid
		System.out.println("Text:" + cs.getObjectText());

		System.out.println("---------------------MsvmVirtualSystemSettingData-------------------------");
		SWbemObjectSet<MsvmVirtualSystemSettingData> settings = cs.getSettingData();
		for (MsvmVirtualSystemSettingData msvmVirtualSystemSettingData : settings) {
			System.out.println("Description:" + msvmVirtualSystemSettingData.getDescription());

			SWbemPropertySet properties = msvmVirtualSystemSettingData.getProperties();
			for (SWbemProperty sWbemProperty : properties) {
				System.out.println(sWbemProperty.getName() + ":" + sWbemProperty.getValue());
			}

		}

		SWbemMethodSet method = cs.getMethods();
		System.out.println("-------------------Method---------------------------");
		for (SWbemMethod sWbemMethod : method) {
			System.out.println(sWbemMethod.getName());
		}
	}

	@Test
	public void testGetAll() throws Exception {

		SWbemSet<MsvmComputerSystem> compSysSet = MsvmComputerSystem.getAll(svc, MsvmComputerSystem.class);

		for (MsvmComputerSystem msvmComputerSystem : compSysSet) {
			System.out.println(msvmComputerSystem.getElementName());
			System.out.println("EnabledState:" + msvmComputerSystem.getEnabledState());
			System.out.println("HealthState:" + msvmComputerSystem.getHealthState());
			System.out.println("Name:" + msvmComputerSystem.getName()); // uuid
			System.out.println("Text:" + msvmComputerSystem.getObjectText());
		}

	}

	@Test
	public void testGetVMByUUID() throws Exception {
		String name = "140E4DD2-2537-40DF-A4AD-75E20FC79D24";

		// Define the WQL query that returns all of a Hyper-V's virtual machines.
		String wql = "SELECT * FROM Msvm_ComputerSystem WHERE Name ='" + name + "'";

		// Execute the query.
		SWbemObjectSet<MsvmComputerSystem> compSysSet = svc.execQuery(wql, MsvmComputerSystem.class);

		System.out.println("----------------------------------------------");
		System.out.println("compSysSet:" + compSysSet.getSize());
		// Print the names of the virtual machines.
		for (MsvmComputerSystem cs : compSysSet) {
			System.out.println(cs.getElementName());
			System.out.println("EnabledState:" + cs.getEnabledState());
			System.out.println("HealthState:" + cs.getHealthState());
			System.out.println("Name:" + cs.getName()); // uuid
			System.out.println("Text:" + cs.getObjectText());

			System.out.println("---------------------MsvmVirtualSystemSettingData-------------------------");
			SWbemObjectSet<MsvmVirtualSystemSettingData> settings = cs.getSettingData();
			for (MsvmVirtualSystemSettingData msvmVirtualSystemSettingData : settings) {
				System.out.println("Description:" + msvmVirtualSystemSettingData.getDescription());

				SWbemPropertySet properties = msvmVirtualSystemSettingData.getProperties();
				for (SWbemProperty sWbemProperty : properties) {
					System.out.println(sWbemProperty.getName() + ":" + sWbemProperty.getValue());
				}

			}

			SWbemMethodSet method = cs.getMethods();
			System.out.println("-------------------Method---------------------------");
			for (SWbemMethod sWbemMethod : method) {
				System.out.println(sWbemMethod.getName());
			}
		}

	}

	@Test
	public void testMsvm_ComputerSystem() throws Exception {

		// Define the WQL query that returns all of a Hyper-V's virtual machines.
		String wql = "SELECT * FROM Msvm_ComputerSystem WHERE Caption='Virtual Machine'";

		// Execute the query.
		SWbemObjectSet<MsvmComputerSystem> compSysSet = svc.execQuery(wql, MsvmComputerSystem.class);

		System.out.println("compSysSet:" + compSysSet.getSize());
		System.out.println("===============================================");
		// Print the names of the virtual machines.
		for (MsvmComputerSystem cs : compSysSet) {
			System.out.println(cs.getElementName());
			System.out.println("EnabledState:" + cs.getEnabledState());
			System.out.println("HealthState:" + cs.getHealthState());
			System.out.println("Name:" + cs.getName()); // uuid

			System.out.println(cs.getProperties().getItem("Description").getName() + ":" + cs.getProperties().getItem("Description").getValue());

			System.out.println("----------------------------------------------");
		}

	}

	@Test
	public void testMsvm_VirtualSystemManagementService() throws Exception {

		MsvmVirtualSystemManagementService service = MsvmVirtualSystemManagementService.getManagementService(svc);

		System.out.println("Element Name:" + service.getElementName());
		System.out.println("Element Name:" + service.getName());
		System.out.println("Text:" + service.getObjectText());
		// service.getSummaryInformation(settingData, requestedInformation)
		MsvmSummaryInformation info = service.getSummaryInfo("TEST");

		System.out.println("os:" + info.getGuestOperatingSystem());
		System.out.println("memory:" + info.getMemoryUsage());

	}

	@Test
	public void testGetWin32ComputerSystem() throws JIException, SWbemException {

		String wql = "SELECT * FROM Win32_ComputerSystem ";

		// Execute the query.
		SWbemObjectSet<Win32ComputerSystem> compSysSet = cimv2.execQuery(wql, Win32ComputerSystem.class);

		System.out.println("compSysSet:" + compSysSet.getSize());

		Win32ComputerSystem host = compSysSet.iterator().next();

		System.out.println("host:" + host.getObjectText());

		// Manufacturer = "Dell Inc.";
		// Model = "OptiPlex 780       "
		// NumberOfLogicalProcessors = 4;
		// NumberOfProcessors = 1;
		// TotalPhysicalMemory = "4122591232";
	}

	@Test
	public void testGetWin32Processor() throws JIException, SWbemException {
		SWbemSet<Win32Processor> ps = Win32Processor.getWin32Processor(cimv2);

		System.out.println("size:" + ps.getSize());

		Win32Processor p = ps.iterator().next();

		System.out.println(p.getObjectText());
		// Caption = "Intel64 Family 6 Model 23 Stepping 10";
		// CurrentClockSpeed = 2660;
		// Description = "Intel64 Family 6 Model 23 Stepping 10";

		// Manufacturer = "GenuineIntel";
		// MaxClockSpeed = 2660;
		// Name = "Intel(R) Core(TM)2 Quad CPU    Q9400  @ 2.66GHz";
		// NumberOfCores = 4;
		// NumberOfLogicalProcessors = 4;
	}

	@Test
	public void testGetMsvmProcessor() throws UnknownHostException, JIException, SWbemException {
		// Define the WQL query that returns all of a Hyper-V's virtual machines.
		String wql = "SELECT * FROM MSVM_Processor ";

		// Execute the query.
		SWbemObjectSet<MsvmProcessor> compSysSet = svc.execQuery(wql, MsvmProcessor.class);

		System.out.println("compSysSet:" + compSysSet.getSize());
		// Print the names of the virtual machines.
		for (MsvmProcessor cs : compSysSet) {
			System.out.println(cs.getElementName());
			System.out.println("Name:" + cs.getName()); // uuid
			System.out.println("Text:" + cs.getObjectText());

			SWbemMethodSet method = cs.getMethods();
			System.out.println("-------------------Method---------------------------");
			for (SWbemMethod sWbemMethod : method) {
				System.out.println(sWbemMethod.getName());
			}
			System.out.println("=============================================");
		}
	}

	@Test
	public void testMsvm_Memory() throws JIException, SWbemException {

		SWbemObjectSet<MsvmMemory> compSysSet = MsvmMemory.getMsvmMemory(svc);

		System.out.println("size:" + compSysSet.getSize());

		MsvmMemory memory = compSysSet.iterator().next();

		System.out.println("memory:" + memory.getObjectText());

	}

	@Test
	public void testMsvm_VirtualSystemSettingData() throws JIException, UnknownHostException, SWbemException {

		// Define the WQL query that returns all of a Hyper-V's virtual machines.
		String wql = "SELECT * FROM Msvm_VirtualSystemSettingData";

		// Execute the query.
		SWbemObjectSet<MsvmVirtualSystemSettingData> compSysSet = svc.execQuery(wql, MsvmVirtualSystemSettingData.class);

		System.out.println("compSysSet:" + compSysSet.getSize());
		// Print the names of the virtual machines.
		for (MsvmVirtualSystemSettingData cs : compSysSet) {
			System.out.println("ElementName:" + cs.getElementName());
			System.out.println("Name:" + cs.getName()); // uuid
			System.out.println("Text:" + cs.getObjectText());

			SWbemPropertySet properties = cs.getProperties();
			for (SWbemProperty sWbemProperty : properties) {
				System.out.println(sWbemProperty.getName() + ":" + sWbemProperty.getValue());
			}

			SWbemMethodSet method = cs.getMethods();
			System.out.println("-------------------Method---------------------------");
			for (SWbemMethod sWbemMethod : method) {
				System.out.println(sWbemMethod.getName());
			}
			System.out.println("=============================================");
		}
	}

	// ===================================================================

	@Test
	public void testMsvmVirtualSwitchManagementService() throws Exception {
		MsvmVirtualSwitchManagementService ms = MsvmVirtualSwitchManagementService.getManagementService(svc);

		System.out.println(ms.getObjectText());

	}

	@Test
	public void testGetMsvmSwitchPort() throws Exception {

		SWbemSet<MsvmSwitchPort> ports = MsvmSwitchPort.getInternalSwitchPorts(svc);

		System.out.println("Size:" + ports.getSize());

		// no data ?
	}

	@Test
	public void testGetMsvm_VirtualSwitch() throws Exception {

		// Execute the query.
		SWbemSet<MsvmVirtualSwitch> compSysSet = MsvmVirtualSwitch.getVirtualSwitches(svc);

		System.out.println("compSysSet:" + compSysSet.getSize());

		// no data ?
	}

	@Test
	public void testGetMsvm_VirtualEthernetSwitch() throws UnknownHostException, JIException, SWbemException {

		// Define the WQL query that returns all of a Hyper-V's virtual machines.
		String wql = "SELECT * FROM Msvm_VirtualEthernetSwitch";

		// Execute the query.
		SWbemObjectSet<MsvmVirtualEthernetSwitch> compSysSet = svc.execQuery(wql, MsvmVirtualEthernetSwitch.class);

		System.out.println("compSysSet:" + compSysSet.getSize());
		// Print the names of the virtual machines.
		for (MsvmVirtualEthernetSwitch cs : compSysSet) {
			System.out.println(cs.getElementName());
			System.out.println("Name:" + cs.getName()); // uuid
			System.out.println("Text:" + cs.getObjectText());

			SWbemPropertySet properties = cs.getProperties();
			for (SWbemProperty sWbemProperty : properties) {
				System.out.println(sWbemProperty.getName() + ":" + sWbemProperty.getValue());
			}

			SWbemMethodSet method = cs.getMethods();
			System.out.println("-------------------Method---------------------------");
			for (SWbemMethod sWbemMethod : method) {
				System.out.println(sWbemMethod.getName());
			}
			System.out.println("=============================================");
		}
	}

	@Test
	public void testGetMsvm_SyntheticEthernetPort() throws UnknownHostException, JIException, SWbemException {

		// no data ?

		// Define the WQL query that returns all of a Hyper-V's virtual machines.
		String wql = "SELECT * FROM Msvm_SyntheticEthernetPort";

		// Execute the query.
		SWbemObjectSet<MsvmSyntheticEthernetPort> compSysSet = svc.execQuery(wql, MsvmSyntheticEthernetPort.class);

		System.out.println("compSysSet:" + compSysSet.getSize());
		// Print the names of the virtual machines.
		for (MsvmSyntheticEthernetPort cs : compSysSet) {
			System.out.println(cs.getElementName());
			System.out.println("Name:" + cs.getName()); // uuid
			System.out.println("Text:" + cs.getObjectText());

			SWbemPropertySet properties = cs.getProperties();
			for (SWbemProperty sWbemProperty : properties) {
				System.out.println(sWbemProperty.getName() + ":" + sWbemProperty.getValue());
			}

			SWbemMethodSet method = cs.getMethods();
			System.out.println("-------------------Method---------------------------");
			for (SWbemMethod sWbemMethod : method) {
				System.out.println(sWbemMethod.getName());
			}
			System.out.println("=============================================");
		}

	}

	@Test
	public void GetMsvmSyntheticEthernetPortByVMUUID() throws UnknownHostException, JIException, SWbemException {
		String name = "140E4DD2-2537-40DF-A4AD-75E20FC79D24";

		// Define the WQL query that returns all of a Hyper-V's virtual machines.
		String wql = "SELECT * FROM Msvm_SyntheticEthernetPort where SystemName='" + name + "' ";

		// Execute the query.
		SWbemObjectSet<MsvmSyntheticEthernetPort> compSysSet = svc.execQuery(wql, MsvmSyntheticEthernetPort.class);

		System.out.println("compSysSet:" + compSysSet.getSize());
		// Print the names of the virtual machines.
		for (MsvmSyntheticEthernetPort cs : compSysSet) {
			System.out.println(cs.getElementName());
			System.out.println("Name:" + cs.getName()); // uuid
			System.out.println("Text:" + cs.getObjectText());

			SWbemPropertySet properties = cs.getProperties();
			for (SWbemProperty sWbemProperty : properties) {
				System.out.println(sWbemProperty.getName() + ":" + sWbemProperty.getValue());
			}

			SWbemMethodSet method = cs.getMethods();
			System.out.println("-------------------Method---------------------------");
			for (SWbemMethod sWbemMethod : method) {
				System.out.println(sWbemMethod.getName());
			}
			System.out.println("=============================================");
		}
	}

	@Test
	public void getMsvmVirtualSystemManagementService() throws Exception {

		MsvmVirtualSystemManagementService mss = MsvmVirtualSystemManagementService.getManagementService(svc);

		System.out.println("" + mss.getObjectText());

		MsvmSummaryInformation info = mss.getSummaryInfo("test");

		System.out.println("OS:" + info.getGuestOperatingSystem());
		System.out.println("info:" + info.getObjectText());

		MsvmComputerSystem vm = MsvmVirtualSystemManagementService.getByName(svc, MsvmComputerSystem.class, "140E4DD2-2537-40DF-A4AD-75E20FC79D24");

		System.out.println("port size:" + vm.getSyntheticEthernetPorts().getSize());

		System.out.println("vm:" + vm.getObjectText());
	}

	@Test
	public void getMsvmVirtualSystemSettingData() throws SWbemException {

		// Define the WQL query that returns all of a Hyper-V's virtual machines.
		String wql = "SELECT * FROM Msvm_VirtualSystemSettingData' ";

		// Execute the query.
		SWbemObjectSet<MsvmVirtualSystemSettingData> compSysSet = svc.execQuery(wql, MsvmVirtualSystemSettingData.class);

		System.out.println("compSysSet:" + compSysSet.getSize());

		// no data ?

	}

	@Test
	public void getWin32NetworkAdapter() throws Exception {

		SWbemLocator locator = new SWbemLocator();

		SWbemServices cimv2 = locator.connect(this.hostname, this.hostname, "ROOT\\CIMV2", this.username, this.passphrase);

		SWbemObjectSet<Win32NetworkAdapter> nics = Win32NetworkAdapter.getWin32NetworkAdapters(cimv2);

		System.out.println("size:" + nics.getSize());

		for (Win32NetworkAdapter adaptor : nics) {

			System.out.println("" + adaptor.getObjectText());
			// SWbemProperty property = adaptor.getProperties().getItem("PhysicalAdapter");
			// System.out.println(property.getName() + ":" + property.getValue());

			// for (SWbemProperty property : adaptor.getProperties()) {
			//
			// System.out.println(property.getName());
			// }
			System.out.println("-----------------------------------");
		}

	}

	@Test
	public void getMsvm_SyntheticEthernetPort() throws JIException, SWbemException {

		// Reference:
		// http://blogs.msdn.com/b/virtual_pc_guy/archive/2009/03/12/scripting-hyper-v-using-associations.aspx

		String name = "140E4DD2-2537-40DF-A4AD-75E20FC79D24";

		// Define the WQL query that returns all of a Hyper-V's virtual machines.
		String wql = "SELECT * FROM Msvm_ComputerSystem WHERE Name ='" + name + "'";

		// Execute the query.
		SWbemObjectSet<MsvmComputerSystem> compSysSet1 = svc.execQuery(wql, MsvmComputerSystem.class);
		MsvmComputerSystem vm = compSysSet1.iterator().next();

		// String path = vm.getObjectPath().getPath();
		// String format = "Associators of {%s} where ResultClass = Msvm_SyntheticEthernetPort";
		//
		// String query = String.format(format, path);
		//
		// System.out.println("query:" + query);
		// Execute the query.
		SWbemObjectSet<MsvmSyntheticEthernetPort> compSysSet2 = MsvmSyntheticEthernetPort.getMsvmSyntheticEthernetPort(svc, vm);

		System.out.println("size:" + compSysSet2.getSize());

		MsvmSyntheticEthernetPort port = compSysSet2.iterator().next();

		System.out.println("nic:" + port.getObjectText());

		// PermanentAddress = "00155DC03600";

	}

	@Test
	public void testInstanceModificationEvent() throws Exception {

		// Execute the query.
		SWbemEventSource eventSource = SWbemEventSource.getSWbemEventSource(svc, EventType.__InstanceModificationEvent);

		while (true) {
			try {


				System.out.println("Wait for next Event....");
				InstanceModificationEvent object = eventSource.nextEvent(-1, InstanceModificationEvent.class);


				System.out.println("PreviousInstance:" + object.getPreviousInstance().getEnabledState());

				System.out.println("TargetInstance:" + object.getTargetInstance().getEnabledState());

				Thread.sleep(1 * 1000);

			} catch (SWbemException e) {
				System.out.println("-------------------------------------------");
				if (e.getCause() instanceof JIAutomationException) {
					System.err.println("JIAutomationException ERROR=" + ((JIAutomationException)e.getCause()).getErrorCode());
					//System.err.println("ERROR=" + e.getMessage());
					//System.err.println("ERROR=" + e.getStackTrace());
				} else {
					System.err.println("ERROR=" + e.getMessage());
					e.printStackTrace();
				}
				Thread.sleep(1 * 1000);
			}
		}//while
	}
	//	instance of __InstanceModificationEvent
	//	{
	//		PreviousInstance = 
	//	instance of Msvm_ComputerSystem
	//	{
	//		AvailableRequestedStates = NULL;
	//		Caption = "Virtual Machine";
	//		CommunicationStatus = NULL;
	//		CreationClassName = "Msvm_ComputerSystem";
	//		Dedicated = NULL;
	//		Description = "Microsoft Virtual Machine";
	//		DetailedStatus = NULL;
	//		ElementName = "Test";
	//		EnabledDefault = 3;
	//		EnabledState = 4;
	//		EnhancedSessionModeState = 3;
	//		FailedOverReplicationType = 0;
	//		HealthState = 5;
	//		IdentifyingDescriptions = NULL;
	//		InstallDate = "20131226084925.497185-000";
	//		InstanceID = NULL;
	//		LastApplicationConsistentReplicationTime = "16010101000000.000000-000";
	//		LastReplicationTime = "16010101000000.000000-000";
	//		LastReplicationType = 0;
	//		LastSuccessfulBackupTime = NULL;
	//		Name = "140E4DD2-2537-40DF-A4AD-75E20FC79D24";
	//		NameFormat = NULL;
	//		NumberOfNumaNodes = 1;
	//		OnTimeInMilliseconds = "7690869";
	//		OperatingStatus = NULL;
	//		OperationalStatus = {11};
	//		OtherDedicatedDescriptions = NULL;
	//		OtherEnabledState = NULL;
	//		OtherIdentifyingInfo = NULL;
	//		PowerManagementCapabilities = NULL;
	//		PrimaryOwnerContact = NULL;
	//		PrimaryOwnerName = NULL;
	//		PrimaryStatus = NULL;
	//		ProcessID = 33856;
	//		ReplicationHealth = 0;
	//		ReplicationMode = 0;
	//		ReplicationState = 0;
	//		RequestedState = 3;
	//		ResetCapability = 1;
	//		Roles = NULL;
	//		Status = "Service";
	//		StatusDescriptions = {"In service"};
	//		TimeOfLastConfigurationChange = "20140124061515.360523-000";
	//		TimeOfLastStateChange = "20140124061515.342572-000";
	//		TransitioningToState = NULL;
	//	};
	//		SECURITY_DESCRIPTOR = {1, 0, 4, 128, 148, 0, 0, 0, 164, 0, 0, 0, 0, 0, 0, 0, 20, 0, 0, 0, 2, 0, 128, 0, 4, 0, 0, 0, 0, 0, 36, 0, 64, 0, 0, 0, 1, 5, 0, 0, 0, 0, 0, 5, 21, 0, 0, 0, 233, 48, 79, 46, 222, 215, 133, 159, 179, 3, 104, 88, 244, 1, 0, 0, 0, 0, 36, 0, 64, 0, 0, 0, 1, 5, 0, 0, 0, 0, 0, 5, 21, 0, 0, 0, 233, 48, 79, 46, 222, 215, 133, 159, 179, 3, 104, 88, 233, 3, 0, 0, 0, 0, 24, 0, 64, 0, 0, 0, 1, 2, 0, 0, 0, 0, 0, 5, 32, 0, 0, 0, 66, 2, 0, 0, 0, 0, 24, 0, 64, 0, 0, 0, 1, 2, 0, 0, 0, 0, 0, 5, 32, 0, 0, 0, 32, 2, 0, 0, 1, 2, 0, 0, 0, 0, 0, 5, 32, 0, 0, 0, 32, 2, 0, 0, 1, 2, 0, 0, 0, 0, 0, 5, 32, 0, 0, 0, 32, 2, 0, 0};
	//		TargetInstance = 
	//	instance of Msvm_ComputerSystem
	//	{
	//		AvailableRequestedStates = NULL;
	//		Caption = "Virtual Machine";
	//		CommunicationStatus = NULL;
	//		CreationClassName = "Msvm_ComputerSystem";
	//		Dedicated = NULL;
	//		Description = "Microsoft Virtual Machine";
	//		DetailedStatus = NULL;
	//		ElementName = "Test";
	//		EnabledDefault = 3;
	//		EnabledState = 4;
	//		EnhancedSessionModeState = 3;
	//		FailedOverReplicationType = 0;
	//		HealthState = 5;
	//		IdentifyingDescriptions = NULL;
	//		InstallDate = "20131226084925.497185-000";
	//		InstanceID = NULL;
	//		LastApplicationConsistentReplicationTime = "16010101000000.000000-000";
	//		LastReplicationTime = "16010101000000.000000-000";
	//		LastReplicationType = 0;
	//		LastSuccessfulBackupTime = NULL;
	//		Name = "140E4DD2-2537-40DF-A4AD-75E20FC79D24";
	//		NameFormat = NULL;
	//		NumberOfNumaNodes = 1;
	//		OnTimeInMilliseconds = "7690869";
	//		OperatingStatus = NULL;
	//		OperationalStatus = {11};
	//		OtherDedicatedDescriptions = NULL;
	//		OtherEnabledState = NULL;
	//		OtherIdentifyingInfo = NULL;
	//		PowerManagementCapabilities = NULL;
	//		PrimaryOwnerContact = NULL;
	//		PrimaryOwnerName = NULL;
	//		PrimaryStatus = NULL;
	//		ProcessID = 33856;
	//		ReplicationHealth = 0;
	//		ReplicationMode = 0;
	//		ReplicationState = 0;
	//		RequestedState = 3;
	//		ResetCapability = 1;
	//		Roles = NULL;
	//		Status = "Service";
	//		StatusDescriptions = {"In service"};
	//		TimeOfLastConfigurationChange = "20140124061515.420734-000";
	//		TimeOfLastStateChange = "20140124061515.342572-000";
	//		TransitioningToState = NULL;
	//	};
	//		TIME_CREATED = "130350177154519495";
	//	};

}
