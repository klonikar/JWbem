/**
 * 
 */
package com.h9labs.jwbem.msvm.event;

import org.jinterop.dcom.core.IJIComObject;
import org.jinterop.dcom.impls.JIObjectFactory;
import org.jinterop.dcom.impls.automation.IJIDispatch;

import com.trendmicro.jwbem.SWbemObject;
import com.trendmicro.jwbem.SWbemServices;
import com.trendmicro.jwbem.exception.SWbemException;
import com.trendmicro.jwbem.msvm.virtualsystem.MsvmComputerSystem;

/**
 * The __InstanceModificationEvent system class reports an instance modification event, which is a type of intrinsic event generated
 * when an instance changes in the namespace. The following syntax is simplified from Managed Object Format (MOF) code and includes
 * all inherited properties. Properties are listed in alphabetic order, not MOF order.
 * 
 * @see <a href="http://msdn.microsoft.com/en-us/library/aa394651%28v=vs.85%29.aspx">__InstanceModificationEvent class</a>
 * @author howie_yu
 */
public class InstanceModificationEvent extends SWbemObject {

	private final MsvmComputerSystem	previousInstance;

	private final MsvmComputerSystem	targetInstance;

	public InstanceModificationEvent(IJIDispatch objectDispatcher, SWbemServices service) throws SWbemException {
		super(objectDispatcher, service);

		try {
			IJIComObject pre = (IJIComObject)super.getProperties().getItem("PreviousInstance").getValue();
			IJIDispatch pre_dispatch = (IJIDispatch)JIObjectFactory.narrowObject(pre);
			previousInstance = new MsvmComputerSystem(pre_dispatch, super.service);

			IJIComObject tar = (IJIComObject)super.getProperties().getItem("PreviousInstance").getValue();
			IJIDispatch tar_dispatch = (IJIDispatch)JIObjectFactory.narrowObject(tar);
			targetInstance = new MsvmComputerSystem(tar_dispatch, super.service);


		} catch (Exception e) {
			throw new SWbemException(e);
		}


	}

	/**
	 * @return the previousInstance
	 */
	public MsvmComputerSystem getPreviousInstance() {
		return previousInstance;
	}

	/**
	 * @return the targetInstance
	 */
	public MsvmComputerSystem getTargetInstance() {
		return targetInstance;
	}


}
