package com.leaseleap.userservice.exception;

public class InvalidUserIdException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public InvalidUserIdException() {
		super();
	}
	
	public InvalidUserIdException(String message) {
        super(message);
    }

    public InvalidUserIdException(Throwable cause) {
        super(cause);
    }

    public InvalidUserIdException(String message, Throwable cause) {
        super(message, cause);
    }

}
