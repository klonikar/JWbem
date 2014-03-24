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

package tw.howie.jwbem;

import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.IJIComObject;
import org.jinterop.dcom.core.JIVariant;
import org.jinterop.dcom.impls.JIObjectFactory;
import org.jinterop.dcom.impls.automation.IJIDispatch;

/**
 * An SWbemMethod.
 * 
 * @author akutz
 */
public class SWbemMethod extends SWbemDispatchObject implements SWbemSetItem {
	private String		name;
	private SWbemObject	inParams;
	private SWbemObject	outParams;

	/**
	 * Initializes a new instance of the SWbemMethod class.
	 * 
	 * @param objectDispatcher The underlying dispatch object used to communicate with the server.
	 * @param service The service connection.
	 */
	public SWbemMethod(IJIDispatch objectDispatcher, SWbemServices service) {
		super(objectDispatcher, service);
	}

	/**
	 * Gets the name of the method.
	 * 
	 * @return The name of the method.
	 * @throws JIException When an error occurs.
	 */
	public String getName() throws JIException {
		if (this.name != null) {
			return this.name;
		}

		this.name = super.objectDispatcher.get("Name").getObjectAsString2();
		return this.name;
	}

	/**
	 * Gets the InParameters object.
	 * 
	 * @return The InParameters object.
	 * @throws JIException When an error occurs.
	 */
	public SWbemObject getInParameters() throws JIException {
		if (this.inParams != null) {
			return this.inParams;
		}

		JIVariant variant = super.objectDispatcher.get("InParameters");
		IJIComObject co = variant.getObjectAsComObject();
		IJIDispatch dispatch = (IJIDispatch)JIObjectFactory.narrowObject(co);
		this.inParams = new SWbemObject(dispatch, super.service);
		return this.inParams;
	}

	/**
	 * Gets the OutParameters object.
	 * 
	 * @return The OutParameters object.
	 * @throws JIException When an error occurs.
	 */
	public SWbemObject getOutParameters() throws JIException {
		if (this.outParams != null) {
			return this.outParams;
		}

		JIVariant variant = super.objectDispatcher.get("OutParameters");
		IJIComObject co = variant.getObjectAsComObject();
		IJIDispatch dispatch = (IJIDispatch)JIObjectFactory.narrowObject(co);
		this.outParams = new SWbemObject(dispatch, super.service);
		return this.outParams;
	}
}