/**
 * 
 */
package com.h9labs.jwbem.msvm.virtualsystem;

/**
 * @author howie_yu
 */
public enum EnabledState {

	Unknow(0, "Unknown"),
	Other(1, "Other"),
	Enabled(2, "Enabled"), //The element is running.
	Disabled(3, "Disabled"), //The element is turned off.
	Shutting_Down(4, "Shutting Down"), //The element is in the process of going to a Disabled state.
	Not_Applicable(5, "Not Applicable"), //The element does not support being enabled or disabled.
	Offline(6, "Enabled but Offline"), //The element might be completing commands, and it will drop any new requests.
	Test(7, "In Test"), //The element is in a test state.
	Deferred(8, "Deferred"), //The element might be completing commands, but it will queue any new requests.
	Quiesce(9, "Quiesce"),
	Starting(10, "Starting");//The element is in the process of going to an Enabled state (2). New requests are queued.

	private final int		code;
	private final String	state;

	private EnabledState(int code, String state) {

		this.code = code;
		this.state = state;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	public static EnabledState valueOf(int code) {

		switch (code) {

		case 0:
			return EnabledState.Unknow;
		case 1:
			return EnabledState.Other;
		case 2:
			return EnabledState.Enabled;
		case 3:
			return EnabledState.Disabled;
		case 4:
			return EnabledState.Shutting_Down;
		case 5:
			return EnabledState.Not_Applicable;
		case 6:
			return EnabledState.Offline;
		case 7:
			return EnabledState.Test;
		case 8:
			return EnabledState.Deferred;
		case 9:
			return EnabledState.Quiesce;
		case 10:
			return EnabledState.Starting;
		default:
			return EnabledState.Unknow;
		}
	}
}
