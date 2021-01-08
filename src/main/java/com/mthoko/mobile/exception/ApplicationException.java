package com.mthoko.mobile.exception;

public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = -9167466006230372307L;

	public static final String GENERIC_ERROR_MESSAGE = "An error has occurred we are investigating it. We are sorry for the inconvenience, please try again later";

	private ErrorCode errorCode;

	public ApplicationException() {
	}

	public ApplicationException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public ApplicationException(String message) {
		super(message);
	}

	public ApplicationException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationException(String message, Throwable cause, ErrorCode errorCode) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public ApplicationException(Throwable cause) {
		super(cause);
	}

	public ApplicationException(Throwable cause, ErrorCode errorCode) {
		super(cause);
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
}