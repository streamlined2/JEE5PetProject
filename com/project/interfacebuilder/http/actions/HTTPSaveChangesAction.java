package com.project.interfacebuilder.http.actions;

import com.project.interfacebuilder.ConfirmAction;
import com.project.interfacebuilder.InterfaceException;

public class HTTPSaveChangesAction extends HTTPActionSupport implements ConfirmAction {
	
	public HTTPSaveChangesAction(){
		super("SaveChanges");
	}
	
	public void perform() throws InterfaceException{

		controller.saveChanges();
		
	}

}
