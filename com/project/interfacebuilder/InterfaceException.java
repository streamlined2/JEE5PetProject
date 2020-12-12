package com.project.interfacebuilder;

// root application exception
public class InterfaceException extends Exception {
	
	private static final long serialVersionUID = 7441525840317817809L;

	private Exception wrappedException;
	
	public InterfaceException(Exception wrappedException){
		this.wrappedException = wrappedException;
	}
	
	public InterfaceException(String message) {
		super(message);
	}

	public InterfaceException(String message, Exception wrappedException) {
		super(message);
		this.wrappedException = wrappedException;
	}
	
	public Exception getWrappedException(){
		return wrappedException;
	}

}
