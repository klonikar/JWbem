/**
 * 
 */
package tw.howie.jwbem.win32;

import org.jinterop.dcom.impls.automation.IJIDispatch;

import tw.howie.jwbem.SWbemObject;
import tw.howie.jwbem.SWbemObjectSet;
import tw.howie.jwbem.SWbemServices;
import tw.howie.jwbem.exception.SWbemException;

/**
 * @author howie_yu
 */
public class Win32ComputerSystem extends SWbemObject {

	/**
	 * Initializes a new instance of the Win32ComputerSystem class.
	 * 
	 * @param objectDispatcher The underlying dispatch object used to communicate with the server.
	 * @param service The service connection.
	 */
	public Win32ComputerSystem(IJIDispatch objectDispatcher, SWbemServices service) {
		super(objectDispatcher, service);
	}

	/**
	 * @param cimv2
	 * @return
	 * @throws SWbemException 
	 */
	public static SWbemObjectSet<Win32ComputerSystem> getWin32ComputerSystem(SWbemServices cimv2) throws SWbemException {

		String wql = "SELECT * FROM Win32_ComputerSystem ";

		// Execute the query.
		SWbemObjectSet<Win32ComputerSystem> compSysSet = cimv2.execQuery(wql, Win32ComputerSystem.class);


		return compSysSet;

	}
}
