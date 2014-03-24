/**
 * 
 */
package com.h9labs.jwbem;

import java.lang.reflect.Constructor;

import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.IJIComObject;
import org.jinterop.dcom.core.JIVariant;
import org.jinterop.dcom.impls.JIObjectFactory;
import org.jinterop.dcom.impls.automation.IJIDispatch;

import com.trendmicro.jwbem.exception.SWbemException;

/**
 * @see <a href="http://msdn.microsoft.com/en-us/library/aa393710%28v=vs.85%29.aspx">SWbemEventSource</a> The SWbemEventSource
 *      object retrieves events from an event query in conjunction with SWbemServices.ExecNotificationQuery. You get an
 *      SWbemEventSource object if you make a call to SWbemServices.ExecNotificationQuery to make an event query. You can then use
 *      the NextEvent method to retrieve events as they arrive. This object cannot be created by the VBScript CreateObject call.
 * @see <a href="http://msdn.microsoft.com/en-us/library/aa390355%28v=vs.85%29.aspx">Determining the Type of Event to Receive</a>
 * @author howie_yu
 */
public class SWbemEventSource extends SWbemObject {

	public static final String	IID	= "27d54d92-0ebe-11d2-8b22-00600806d9b6";


	public SWbemEventSource(IJIDispatch objectDispatcher, SWbemServices service) {
		super(objectDispatcher, service);
	}


	/**
	 * If an event is available, the NextEvent method of the SWbemEventSource object retrieves the event from an event query.
	 * 
	 * @see <a href="http://msdn.microsoft.com/en-us/library/aa393711%28v=vs.85%29.aspx">nextEvent</a>
	 * @param iTimeoutMs Number of milliseconds the call waits for an event before returning a time-out error. The default value for
	 *        this parameter is wbemTimeoutInfinite (-1), which directs the call to wait indefinitely.
	 * @return
	 * @throws JIException
	 */
	public <Event extends SWbemObject> Event nextEvent(int iTimeoutMs, Class<Event> clazz) throws SWbemException {

		Object[] params = new Object[] { new Integer(iTimeoutMs) };

		try {
			int idOfName = super.objectDispatcher.getIDsOfNames("NextEvent");
			JIVariant variant[] = super.objectDispatcher.callMethodA(idOfName, params);

			IJIComObject co = variant[0].getObjectAsComObject();
			IJIDispatch dispatch = (IJIDispatch)JIObjectFactory.narrowObject(co);


			// Create a new SWbemSetItem from the result.
			final Constructor<Event> ctor = clazz.getConstructor(IJIDispatch.class, SWbemServices.class);
			final Event item = ctor.newInstance(dispatch, super.getService());
			return item;
		} catch (Exception ex) {
			throw new SWbemException(ex);
		}
	}


	/**
	 * @param svc
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static SWbemEventSource getSWbemEventSource(SWbemServices svc, EventType type) throws Exception {

		// http://support.microsoft.com/kb/2482874

		// Receiving a WMI Event
		// http://msdn.microsoft.com/en-us/library/aa393013%28v=vs.85%29.aspx
		String wql = "SELECT * FROM  " + type.name() + " WITHIN 1 WHERE TargetInstance ISA 'Msvm_ComputerSystem'";

		return svc.execNotificationQuery(wql, SWbemEventSource.class);


	}

	public enum EventType {

		__InstanceModificationEvent;

	}

}
