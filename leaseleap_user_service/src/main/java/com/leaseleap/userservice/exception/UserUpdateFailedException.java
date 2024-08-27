package com.leaseleap.userservice.exception;

public class UserUpdateFailedException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public UserUpdateFailedException() {
		super();
	}
	
	public UserUpdateFailedException(String message) {
        super(message);
    }

    public UserUpdateFailedException(Throwable cause) {
        super(cause);
    }

    public UserUpdateFailedException(String message, Throwable cause) {
        super(message, cause);
    }


}
