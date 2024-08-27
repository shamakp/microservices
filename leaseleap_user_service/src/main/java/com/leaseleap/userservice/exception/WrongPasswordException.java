package com.leaseleap.userservice.exception;

public class WrongPasswordException extends RuntimeException{
	
	
	private static final long serialVersionUID = 1L;

	public WrongPasswordException() {
		super();
	}
	
	public WrongPasswordException(String message) {
        super(message);
    }

    public WrongPasswordException(Throwable cause) {
        super(cause);
    }

    public WrongPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

}
