package com.project.interfacebuilder.http.forms;

import java.io.PrintWriter;

import javax.servlet.http.HttpSession;

import com.project.interfacebuilder.FormSupport;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.Helpers;

public abstract class HTTPFormSupport extends FormSupport {
	
	public HTTPFormSupport() throws InterfaceException{
		super();
	}
	
	protected PrintWriter out;
	
	public PrintWriter getOut() {
		return out;
	}
	
	public void setOut(PrintWriter out){
		this.out = out;
	}
	
	protected HttpSession session;
	
	public void setSession(HttpSession session){
		this.session = session;
	}
	
	protected void checkState() throws InterfaceException{
		super.checkState();
		if(out==null) throw new IllegalStateException("out reference must be set");
		if(session==null) throw new IllegalStateException("session reference must be set");
	}
	
	@Override
	public abstract void activate() throws InterfaceException;
	
	public String getStyle(){
		return Helpers.getStyle(getRenderFont());
	}

}
