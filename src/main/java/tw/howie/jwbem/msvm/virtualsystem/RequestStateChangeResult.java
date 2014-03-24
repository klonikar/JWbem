/**
 * 
 */
package tw.howie.jwbem.msvm.virtualsystem;

/**
 * @author howie_yu
 */
public enum RequestStateChangeResult {

	Unknow(-1),
	Success(0),
	Transition_Started(4096),
	Access_Denied(32769),
	Invalid_state(32775);

	private final int	code;

	private RequestStateChangeResult(int code) {
		this.code = code;

	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	public static RequestStateChangeResult valueOf(int code) {

		switch (code) {

		case 0:
			return RequestStateChangeResult.Success;
		case 4096:
			return RequestStateChangeResult.Transition_Started;
		case 32769:
			return RequestStateChangeResult.Access_Denied;
		case 32775:
			return RequestStateChangeResult.Invalid_state;
		default:
			return RequestStateChangeResult.Unknow;
		}
	}
}
