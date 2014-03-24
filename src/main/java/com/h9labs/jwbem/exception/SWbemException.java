/**
 * 
 */
package com.h9labs.jwbem.exception;

/**
 * @author howie_yu
 */
public class SWbemException extends Exception {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;


	public SWbemException(String message) {
		super(message);

	}

	public SWbemException(Throwable tb) {
		super(tb);
	}
}
