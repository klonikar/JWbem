/**
 * 
 */
package tw.howie.jwbem.msvm.networking;

import org.jinterop.dcom.impls.automation.IJIDispatch;

import tw.howie.jwbem.SWbemObjectSet;
import tw.howie.jwbem.SWbemServices;
import tw.howie.jwbem.exception.SWbemException;
import tw.howie.jwbem.msvm.MsvmObject;

/**
 * @author howie_yu
 */
public class MsvmVirtualEthernetSwitch extends MsvmObject {

	/**
	 * Initializes a new instance of the MsvmVirtualEthernetSwitch class.
	 * 
	 * @param objectDispatcher The underlying dispatch object used to communicate with the server.
	 * @param service The service connection.
	 */
	public MsvmVirtualEthernetSwitch(IJIDispatch objectDispatcher, SWbemServices service) {
		super(objectDispatcher, service);
	}


	/**
	 * @param service
	 * @return
	 * @throws SWbemException 
	 */
	public static SWbemObjectSet<MsvmVirtualEthernetSwitch> getMsvmVirtualEthernetSwitch(SWbemServices service) throws SWbemException {

		String wql = "SELECT * FROM Msvm_VirtualEthernetSwitch";

		// Execute the query.
		SWbemObjectSet<MsvmVirtualEthernetSwitch> compSysSet = service.execQuery(wql, MsvmVirtualEthernetSwitch.class);

		return compSysSet;
	}
}
