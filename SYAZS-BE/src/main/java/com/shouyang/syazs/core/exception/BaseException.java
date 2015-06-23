package com.shouyang.syazs.core.exception;

/**
 * BaseException
 * @author Roderick
 * @version 2014/3/11
 */
public class BaseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 486181199114838448L;

	private String errorCode;
	
	private String[] messages;
	
	public BaseException() {
		super();
	}
	
	public BaseException(Throwable cause) {
		super(cause);
	}
	
	public BaseException(Throwable cause, String errorCode, String[] messages) {
		super(cause);
		this.errorCode = errorCode;
		this.messages = messages;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String[] getMessages() {
		return messages;
	}

	public void setMessages(String[] messages) {
		this.messages = messages;
	}
	
}
