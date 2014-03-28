/**
 * 
 */
package tw.howie.jwbem;

import java.net.UnknownHostException;

import org.jinterop.dcom.common.JIException;

/**
 * @author howie
 */
public class SWbemServicesTestUtil {

	public static String	hostname	= HyperVTestInfo.hostname;

	public static String	username	= HyperVTestInfo.username;

	public static String	passphrase	= HyperVTestInfo.passphrase;


	public static SWbemServices createV1() throws UnknownHostException, JIException {

		// Create a locator object.
		SWbemLocator loc = new SWbemLocator();

		// Connect to the Windows server and return a services object.
		return loc.connect(hostname, hostname, "root\\virtualization", username, passphrase);
	}

	public static SWbemServices createV2() throws UnknownHostException, JIException {

		// Create a locator object.
		SWbemLocator loc = new SWbemLocator();

		// Connect to the Windows server and return a services object.
		return loc.connect(hostname, hostname, "root\\virtualization\\v2", username, passphrase);
	}

	public static SWbemServices createCIMV2() throws UnknownHostException, JIException {

		// Create a locator object.
		SWbemLocator loc = new SWbemLocator();

		// Connect to the Windows server and return a services object.
		return loc.connect(hostname, hostname, "ROOT\\CIMV2", username, passphrase);
	}
}
