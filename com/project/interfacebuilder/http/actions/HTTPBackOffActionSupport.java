package com.project.interfacebuilder.http.actions;

import com.project.interfacebuilder.ControllerSupport.FormContextItem;
import com.project.interfacebuilder.Form;
import com.project.interfacebuilder.InterfaceException;

public abstract class HTTPBackOffActionSupport extends HTTPActionSupport {

	public HTTPBackOffActionSupport(String name) {
		super(name);
	}

	@Override
	public FormContextItem findTarget(Form sourceForm) throws InterfaceException {
		return controller.pop();
	}

}
