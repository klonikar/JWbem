package tw.howie.jwbem;

import tw.howie.jwbem.msvm.virtualsystem.MsvmComputerSystem;
import tw.howie.jwbem.msvm.virtualsystem.MsvmSummaryInformation;
import tw.howie.jwbem.msvm.virtualsystem.MsvmVirtualSystemSettingData;
import tw.howie.jwbem.msvm.virtualsystemmanagement.MsvmVirtualSystemManagementService;

// On the hyperv host, use give Full Control permissions to user IWFLABS\hyperadm 
// Ref: https://sourceforge.net/p/j-interop/discussion/840678/thread/3be49d48/
/*
1. Launch 'regedit.exe' as 'Administrator'
   2. Find the following registry key: 'HKEY_CLASSES_ROOT\CLSID\{76A64158-CB41-11D1-8B02-00600806D9B6}'
   3. Right click and select 'Permissions'
   4. Click the 'Advanced' button.
   5. Select the tab labeled 'Owner'
   6. Add the user you want to allow to connect to the owners list
   7. Click the 'Ok' button.
   8. Now highlight the user and grant Full Control
   9. Click 'Ok' 
 */
// Ref: https://wiki.opennms.org/wiki/WmiConfiguration#Registry_key_permissions_change_.28Windows_Server_2008_R2_or_later.2C_or_Windows_7_or_later_only.29
/*Launch "regedit.exe" using elevated permissions ("Run as Administrator")

Find the following registry key: "HKEY_CLASSES_ROOT\CLSID\{76A64158-CB41-11D1-8B02-00600806D9B6}" (this is the CLSID for "WBEM Scripting Locator")
First, take ownership of the key to be able to change permissions.

Right-click the key and select "Permissions"
Click the "Advanced" button
Select the tab labeled "Owner"
Change the owner from "TrustedInstaller" to the local machine "MACHINE_NAME\Administrators" group (it is not necessarily to replace ownership on subcontainers and objects)
Click the "OK" button

Now allow local administrators or a particular user like IWFLABS\hyperadm to have full control of this key.
highlight the local machine "Administrators" group and allow "Full Control" for this key
OR: click on Add in the Advanced permissions screen, and add the targeted user with "Full Control" permissions.
Click "OK"

Now change the owner back to the original owner.

Again, click the 'Advanced' button.
Again, select the tab labeled 'Owner'
Click the button labeled 'Other users or groups'
In the 'Enter the object name to select' text field, enter 'NT Service\TrustedInstaller'
Click the button labeled 'Check Names' and verify that the principal name validates
Click 'OK' (dismissing 'Select User or Group' dialog window)
Click 'Apply'
Click 'OK'
Close regedit.

The steps above are tested repeatable against Windows Server 2008 R2 Enterprise with SP1 on x86_64 platforms.
*/
// After giving permissions, create registry entries as follows:
/*
   1. Locate the key HKEY_CLASSES_ROOT\CLSID\{76A64158-CB41-11D1-8B02-00600806D9B6} (Create if does not exist).
   2. Under that key, create a String Value with name AppID and data {76A64158-CB41-11D1-8B02-00600806D9B6} (including braces).
   
   2. Create a key: HKEY_CLASSES_ROOT\AppID\{76A64158-CB41-11D1-8B02-00600806D9B6} (including braces).
      Modify data of (Default) Value to WBEM Scripting Locator.
      Create a String Value with name DllSurrogate and data empty string (no value)
      
      commands to do the above (run in Powershell as Administrator):
      reg add "HKCR\CLSID\{76A64158-CB41-11D1-8B02-00600806D9B6}" /v AppID /t REG_SZ /d "{76A64158-CB41-11D1-8B02-00600806D9B6}" /f
      reg add "HKCR\AppID\{76A64158-CB41-11D1-8B02-00600806D9B6}" /f
      reg add "HKCR\AppID\{76A64158-CB41-11D1-8B02-00600806D9B6}" /ve /t REG_SZ /d "WBEM Scripting Locator" /f
      reg add "HKCR\AppID\{76A64158-CB41-11D1-8B02-00600806D9B6}" /v DllSurrogate /t REG_SZ /f
      
      Or execute the following remote command in powershell:
      Invoke-Command -ComputerName 16.184.47.119 -ScriptBlock { reg add "HKCR\CLSID\{76A64158-CB41-11D1-8B02-00600806D9B6}" /v AppID /t REG_SZ /d "{76A64158-CB41-11D1-8B02-00600806D9B6}" /f; reg add "HKCR\AppID\{76A64158-CB41-11D1-8B02-00600806D9B6}" /f; reg add "HKCR\AppID\{76A64158-CB41-11D1-8B02-00600806D9B6}" /ve /t REG_SZ /d "WBEM Scripting Locator" /f; reg add "HKCR\AppID\{76A64158-CB41-11D1-8B02-00600806D9B6}" /v DllSurrogate /t REG_SZ /f } -credential IWFLABS\hyperadm

 */
// Sample usage:
// java -cp j-interop.jar:j-interopdeps.jar:jWbem.jar:jcifs-1.3.18.jar:. -Dhost=16.184.47.119 -Dnamespace=root\\virtualization\\V2 -Duser=IWFLABS\\Administrator -Dpassword=1iso*help com.hp.jwbem.WbemTest

public class WbemTest {

	public static void main(String[] args) throws Exception {
		String serverName = System.getProperty("host", "16.184.47.119");
		 
		// The CIM namespace to connect to.
		String cimNamespace = System.getProperty("namespace", "root\\virtualization\\V2");
		 
		// The name of the user to connect as. The format of the user name supports
		// USERNAME, DOMAIN\\USERNAME, and USERNAME@DOMAIN.
		String userName = System.getProperty("user", "IWFLABS\\hyperadm");
		 
		// The passprase for the given user.
		String passphrase = System.getProperty("password", "1iso*help");
		System.out.format("Connecting to host: %s, namespace: %s, user: %s, password: %s.\n", 
				serverName, cimNamespace, userName, passphrase); 
		// Create a locator object.
		SWbemLocator loc = new SWbemLocator();
		 
		// Connect to the Windows server and return a services object.
		SWbemServices svc = loc.connect(serverName, "127.0.0.1", cimNamespace, userName, passphrase);
		System.out.format("*******Succesfully Connected to host: %s, namespace: %s, user: %s, password: %s!!!!!********\n", 
				serverName, cimNamespace, userName, passphrase);

		try {
			String clusterNamespace = "root\\mscluster";
			String clusterQuery = "select * from MSCluster_cluster";
	
			SWbemLocator clusterLoc = new SWbemLocator();
			SWbemServices clusterSvc = clusterLoc.connect(serverName, "127.0.0.1", clusterNamespace, userName, passphrase);
			System.out.format("*******Succesfully Connected to host: %s, namespace: %s, user: %s, password: %s!!!!!********\n", 
					serverName, clusterNamespace, userName, passphrase); 
			SWbemObjectSet<SWbemObject> clusters = clusterSvc.execQuery(clusterQuery);
			for(SWbemObject c : clusters) {
				SWbemPropertySet props = c.getProperties();
				for(SWbemProperty prop : props) {
					System.out.format("    property: %s, value: %s\n", prop.getName(), prop.getValue().toString());
				}
			}
			clusterLoc.disconnect();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			System.err.println("Error in executing cluster query: " + ex);
		}
		
		// Define the WQL query that returns all of a Hyper-V's virtual machines.
		String wql = "SELECT * FROM Msvm_ComputerSystem WHERE Caption='Virtual Machine'";
		// Execute the query.
		SWbemObjectSet<MsvmComputerSystem> compSysSet = svc.execQuery(wql, MsvmComputerSystem.class);
		 
		// Print the names of the virtual machines.
		System.out.format("Executed query %s Number of results: %d.\n", wql, compSysSet.getSize());
		for (MsvmComputerSystem cs : compSysSet)
		{
			System.out.format("element name: %s, name: %s\n", cs.getElementName(), cs.getName());
			SWbemPropertySet props = cs.getProperties();
			for(SWbemProperty prop : props) {
				System.out.format("    property: %s, value: %s\n", prop.getName(), prop.getValue().toString());
			}
		}
		
		MsvmVirtualSystemManagementService vmService = MsvmVirtualSystemManagementService.getManagementService(svc);
		System.out.format("Obtained VM service: %s - %s.\n", vmService.getName(), vmService.getElementName());
		MsvmSummaryInformation[] summaries = vmService.getSummaryInformation(new MsvmVirtualSystemSettingData[0], new Integer[] {0,1,4,5,6,7,100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113});
		System.out.format("Obtained summary information: %d.\n", summaries.length);
		for(MsvmSummaryInformation summary : summaries) {
			System.out.format("element name: %s, name: %s, processor load %d, memory: %d\n", summary.getElementName(), summary.getName(),
					summary.getProcessorLoad(), summary.getMemoryUsage());
			SWbemPropertySet props = summary.getProperties();
			for(SWbemProperty prop : props) {
				System.out.format("    property: %s, value: %s\n", prop.getName(), prop.getValue().toString());
			}
		}
		
		loc.disconnect();
	}

}
