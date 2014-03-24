/**
 * 
 */
package tw.howie.jwbem.msvm.virtualsystem;

/**
 * @see {@link http://msdn.microsoft.com/en-us/library/hh850279%28v=vs.85%29.aspx}
 * @author howie_yu
 */
public enum RequestStateChange {

	Unknow(0),
	Other(1),
	Running(2),
	Off(3),
	Stopping(4),
	Saved(6),
	Paused(9),
	Starting(10),
	Reset(11),
	Saving(32773),
	Pausing(32776),
	Resuming(32779),
	FastSaving(32780);

	private final int	code;

	private RequestStateChange(int code) {
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	public static RequestStateChange valueOf(int code) {

		switch (code) {

		case 0:
			return RequestStateChange.Unknow;
		case 1:
			return RequestStateChange.Other;
		case 2:
			return RequestStateChange.Running;
		case 3:
			return RequestStateChange.Off;
		case 4:
			return RequestStateChange.Stopping;
		case 6:
			return RequestStateChange.Saved;
		case 9:
			return RequestStateChange.Paused;
		case 10:
			return RequestStateChange.Starting;
		case 11:
			return RequestStateChange.Reset;
		case 32773:
			return RequestStateChange.Saving;
		case 32776:
			return RequestStateChange.Pausing;
		case 32779:
			return RequestStateChange.Resuming;
		case 32780:
			return RequestStateChange.FastSaving;
		default:
			return RequestStateChange.Unknow;
		}
	}
}
