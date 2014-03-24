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
public class Win32NetworkAdapter extends SWbemObject {

	/**
	 * @param objectDispatcher
	 * @param service
	 */
	public Win32NetworkAdapter(IJIDispatch objectDispatcher, SWbemServices service) {
		super(objectDispatcher, service);
	}

	/**
	 * Only work for CMIV2
	 * 
	 * @param service
	 * @return
	 * @throws SWbemException 
	 */
	public static SWbemObjectSet<Win32NetworkAdapter> getWin32NetworkAdapters(SWbemServices service) throws SWbemException {

		// Get the management service.
		final String wql = "SELECT * FROM Win32_NetworkAdapter";
		final SWbemObjectSet<Win32NetworkAdapter> objSetMgmtSvc = service.execQuery(wql, Win32NetworkAdapter.class);

		return objSetMgmtSvc;
	}

}
