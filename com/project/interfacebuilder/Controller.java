package com.project.interfacebuilder;

import com.project.interfacebuilder.ControllerSupport.FormChainElement;
import com.project.interfacebuilder.transition.Dispatcher.InterfaceContext;


public interface Controller {
	
	public void service() throws InterfaceException;
	public void push(Form target,InterfaceContext context);
	public FormChainElement pop();

}
