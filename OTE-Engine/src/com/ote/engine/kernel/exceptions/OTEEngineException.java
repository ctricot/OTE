package com.ote.engine.kernel.exceptions;

import java.io.Serializable;

public class OTEEngineException extends Exception implements Serializable {
	private static final long serialVersionUID = 1L;

	public OTEEngineException(Exception e) {
		super(e);
	}
	public OTEEngineException(String error, Throwable cause) {
		super(error, cause);
	}
	
	public OTEEngineException(String error) {
		super(error);
	}
	public OTEEngineException() {
		super();
	}


}