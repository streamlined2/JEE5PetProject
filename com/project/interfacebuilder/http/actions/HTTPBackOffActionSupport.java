package com.project.interfacebuilder.http.actions;

import com.project.interfacebuilder.ControllerSupport.FormChainElement;
import com.project.interfacebuilder.Form;
import com.project.interfacebuilder.InterfaceException;

public abstract class HTTPBackOffActionSupport extends HTTPActionSupport {

	public HTTPBackOffActionSupport(String name) {
		super(name);
	}

	@Override // fetch previous visited form from form chain
	public FormChainElement findTarget(Form sourceForm) throws InterfaceException {
		return controller.pop();
	}

}
