/**
 * 
 */
package tw.howie.jwbem.msvm.virtualsystem.test;

import java.net.UnknownHostException;

import org.jinterop.dcom.common.JIException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import tw.howie.jwbem.SWbemServices;
import tw.howie.jwbem.SWbemServicesTestUtil;
import tw.howie.jwbem.msvm.virtualsystem.MsvmComputerSystem;
import tw.howie.jwbem.msvm.virtualsystem.MsvmSummaryInformation;
import tw.howie.jwbem.msvm.virtualsystemmanagement.MsvmVirtualSystemManagementService;

/**
 * @author howie
 */
@RunWith(JUnit4.class)
public class MsvmVirtualSystemManagementServiceIntegrationTest {

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
}
