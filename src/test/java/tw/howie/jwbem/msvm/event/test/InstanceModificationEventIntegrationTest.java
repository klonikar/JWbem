/**
 * 
 */
package tw.howie.jwbem.msvm.event.test;

import java.net.UnknownHostException;

import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.impls.automation.JIAutomationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tw.howie.jwbem.SWbemEventSource;
import tw.howie.jwbem.SWbemEventSource.EventType;
import tw.howie.jwbem.SWbemServices;
import tw.howie.jwbem.SWbemServicesTestUtil;
import tw.howie.jwbem.exception.SWbemException;
import tw.howie.jwbem.msvm.event.InstanceModificationEvent;

/**
 * @author howie
 */
public class InstanceModificationEventIntegrationTest {

	private SWbemServices	svc;

	private SWbemServices	cimv2;

	private SWbemServices	v1;

	@Before
	public void init() throws UnknownHostException, JIException {
		// Connect to the Windows server and return a services object.
		svc = SWbemServicesTestUtil.createV2();
		cimv2 = SWbemServicesTestUtil.createCIMV2();
		v1 = SWbemServicesTestUtil.createV1();
	}

	@After
	public void close() {
		svc.getLocator().disconnect();
		cimv2.getLocator().disconnect();
		v1.getLocator().disconnect();
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
