package com.project.interfacebuilder.http.actions;


public class HTTPAddNewAction extends HTTPEditActionSupport {

	public HTTPAddNewAction() {
		super("AddNew");
	}

	@Override
	public boolean createNew() {
		return true;
	}

}
