package com.leaseleap.userservice.exception;

public class UserCreationException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public UserCreationException() {
		super();
	}
	
	public UserCreationException(String message) {
        super(message);
    }

    public UserCreationException(Throwable cause) {
        super(cause);
    }

    public UserCreationException(String message, Throwable cause) {
        super(message, cause);
    }

}
