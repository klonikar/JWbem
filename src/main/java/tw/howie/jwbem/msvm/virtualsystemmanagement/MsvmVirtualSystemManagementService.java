/*
 * Copyright (c) 2009, Hyper9 All rights reserved. Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met: Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the
 * distribution. Neither the name of Hyper9 nor the names of its contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package tw.howie.jwbem.msvm.virtualsystemmanagement;

import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.IJIComObject;
import org.jinterop.dcom.core.JIArray;
import org.jinterop.dcom.core.JIString;
import org.jinterop.dcom.core.JIVariant;
import org.jinterop.dcom.impls.JIObjectFactory;
import org.jinterop.dcom.impls.automation.IJIDispatch;

import tw.howie.jwbem.SWbemMethod;
import tw.howie.jwbem.SWbemObject;
import tw.howie.jwbem.SWbemObjectSet;
import tw.howie.jwbem.SWbemServices;
import tw.howie.jwbem.exception.SWbemException;
import tw.howie.jwbem.msvm.MsvmObject;
import tw.howie.jwbem.msvm.virtualsystem.MsvmSummaryInformation;
import tw.howie.jwbem.msvm.virtualsystem.MsvmVirtualSystemSettingData;

/**
 * This class represents the Msvm_VirtualSystemManagementService class.
 * 
 * @author akutz
 */
public class MsvmVirtualSystemManagementService extends MsvmObject {

	private SWbemMethod	getSummaryInformation;

	/**
	 * Initializes a new instance of the MsvmVirtualSystemManagementService class.
	 * 
	 * @param dispatch The dispatch object.
	 * @param service The service connection.
	 */
	public MsvmVirtualSystemManagementService(IJIDispatch dispatch, SWbemServices service) {
		super(dispatch, service);
	}

	/**
	 * Gets the hosts Msvm_VirtualSystemManagementService.
	 * 
	 * @param service The service connection.
	 * @return The hosts Msvm_VirtualSystemManagementService.
	 * @throws Exception When an error occurs.
	 */
	static public MsvmVirtualSystemManagementService getManagementService(final SWbemServices service) throws Exception {
		// Get the management service.
		final String wql = "SELECT * FROM Msvm_VirtualSystemManagementService";
		final SWbemObjectSet<MsvmVirtualSystemManagementService> objSetMgmtSvc = service.execQuery(wql, MsvmVirtualSystemManagementService.class);
		final int size = objSetMgmtSvc.getSize();
		if (size != 1) {
			throw new Exception("There should be exactly 1 Msvm_VirtualSystemManagementService");
		}
		final MsvmVirtualSystemManagementService mgmtSvc = objSetMgmtSvc.iterator().next();
		return mgmtSvc;
	}

	/**
	 * Returns virtual system summary information.
	 * 
	 * @param settingData An array of CIM_VirtualSystemSettingData instances that specifies the virtual machines and/or snapshots
	 *        for which information is to be retrieved. If this parameter is NULL, information for all virtual machines is
	 *        retrieved.
	 * @param requestedInformation An array of enumeration values (which correspond to the properties in the Msvm_SummaryInformation
	 *        class) that specifies the data to retrieve for the virtual machines and/or snapshots specified in the SettingData
	 *        array. Values in the 0-99 range apply to both virtual machines and snapshots. Values in the 100-199 range apply to
	 *        virtual machines only, and will be ignored for elements of SettingData which represent snapshots. Values in the
	 *        200-299 range apply to snapshots only, and will be ignored for elements of SettingData which represent virtual
	 *        machines.
	 * @return Virtual system summary information.
	 * @throws Exception When an error occurs.
	 */
	public MsvmSummaryInformation[] getSummaryInformation(MsvmVirtualSystemSettingData[] settingData, Integer[] requestedInformation) throws Exception {
		if (this.getSummaryInformation == null) {
			for (final SWbemMethod m : super.getMethods()) {
				if (m.getName().equals("GetSummaryInformation")) {
					this.getSummaryInformation = m;
				}
			}
		}

		JIString[] sdpaths = new JIString[settingData.length];
		for (int x = 0; x < sdpaths.length; ++x) {
			String path = settingData[x].getObjectPath().getPath();
			sdpaths[x] = new JIString(path);
		}

		// Get the IN parameters.
		SWbemObject inParams = this.getSummaryInformation.getInParameters();
		inParams.getObjectDispatcher().put("SettingData", new JIVariant(new JIArray(sdpaths)));
		inParams.getObjectDispatcher().put("RequestedInformation", new JIVariant(new JIArray(requestedInformation)));

		Object[] methodParams = new Object[] { new JIString("GetSummaryInformation"), new JIVariant(inParams.getObjectDispatcher()), new Integer(0), JIVariant.NULL(), };

		// Execute the method.
		JIVariant[] results = super.objectDispatcher.callMethodA("ExecMethod_", methodParams);

		// Get the out parameters.
		JIVariant outParamsVar = results[0];
		IJIComObject co = outParamsVar.getObjectAsComObject();
		IJIDispatch outParamsDisp = (IJIDispatch)JIObjectFactory.narrowObject(co);

		// Get the out parameter SummaryInformation and convert it into an
		// array of JIVariants.
		JIVariant summInfoVars = outParamsDisp.get("SummaryInformation");
		JIArray summInfoJIArr = summInfoVars.getObjectAsArray();
		JIVariant[] summInfoJIVarArr = (JIVariant[])summInfoJIArr.getArrayInstance();

        MsvmSummaryInformation[] ret = new MsvmSummaryInformation[summInfoJIVarArr.length];

        int i = 0;
        for(JIVariant summInfo: summInfoJIVarArr) {
	        IJIComObject summInfoCo = summInfo.getObjectAsComObject();
	        IJIDispatch summInfoDisp =
	            (IJIDispatch) JIObjectFactory.narrowObject(summInfoCo);
	        ret[i++] = new MsvmSummaryInformation(summInfoDisp, this.service);
        }

        return ret;
	}

	// Return an array of JIVariant representing SummaryIformation of the VMs
	public MsvmSummaryInformation getSummaryInfo(final String elementName) throws JIException {


		if (this.getSummaryInformation == null) {
			for (final SWbemMethod m : super.getMethods()) {
				if (m.getName().equals("GetSummaryInformation")) {
					this.getSummaryInformation = m;
				}
			}
		}
		SWbemObject inParams = getSummaryInformation.getInParameters();
		//http://msdn.microsoft.com/en-us/library/hh850062%28v=vs.85%29.aspx
		inParams.getObjectDispatcher().put("RequestedInformation", new JIVariant(new JIArray(new Integer[] { 0, //Name
				1, // Element Name
				2,// Creation Time 
				3,//Notes
				4,//Number of Processors
				100,//EnabledState
				101,//ProcessorLoad
				102,//ProcessorLoadHistory
				103,//MemoryUsage
				104,//Heartbeat
				105,//Uptime
				106,//GuestOperatingSystem
				107,//Snapshots
				108,//AsynchronousTasks
				109,//HealthState
				110,//OperationalStatus
				111,//StatusDescriptions
				112 //MemoryAvailable
			}, true)));

		Object[] methodParams = new Object[] { new JIString("GetSummaryInformation"), new JIVariant(inParams.getObjectDispatcher()), new Integer(0), JIVariant.NULL(), };

		JIVariant outParamsVar = this.getObjectDispatcher().callMethodA("ExecMethod_", methodParams)[0];
		IJIDispatch outParamsDisp = (IJIDispatch)JIObjectFactory.narrowObject(outParamsVar.getObjectAsComObject());
		JIVariant summaryInformationValue = outParamsDisp.get("SummaryInformation");
		JIArray summInfoJIArr = summaryInformationValue.getObjectAsArray();
		JIVariant[] summInfoJIVarArr = (JIVariant[])summInfoJIArr.getArrayInstance();

		IJIComObject summInfoCo = summInfoJIVarArr[0].getObjectAsComObject();
		IJIDispatch summInfoDisp = (IJIDispatch)JIObjectFactory.narrowObject(summInfoCo);

		return new MsvmSummaryInformation(summInfoDisp, this.service);
	}

	/**
	 * @param elementName
	 * @return
	 * @throws JIException
	 * @throws SWbemException
	 */
	public MsvmVirtualSystemSettingData getMsvmVirtualSystemSettingData(final String elementName) throws JIException, SWbemException {

		String wql;
		if (elementName == null) {
			wql = "SELECT * FROM Msvm_VirtualSystemSettingData";
		} else {
			wql = "SELECT * FROM Msvm_VirtualSystemSettingData WHERE ElementName = '" + elementName + "'";
		}
		SWbemObjectSet<MsvmVirtualSystemSettingData> settingDataSet = this.service.execQuery(wql, MsvmVirtualSystemSettingData.class);

		JIVariant[] VirtualSystemVariantArray = new JIVariant[settingDataSet.getSize()];
		int i = 0;
		for (MsvmVirtualSystemSettingData vssd : settingDataSet) {
			VirtualSystemVariantArray[i] = new JIVariant(vssd.getObjectPath().getPath());
			i++;
		}


		return null;
	}
}
