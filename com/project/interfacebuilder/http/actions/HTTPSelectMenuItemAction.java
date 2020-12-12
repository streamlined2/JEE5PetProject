package com.project.interfacebuilder.http.actions;

import com.project.interfacebuilder.ControllerSupport.FormChainElement;
import com.project.interfacebuilder.Form;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.menu.MenuItem;
import com.project.interfacebuilder.transition.Dispatcher;

public class HTTPSelectMenuItemAction extends HTTPSelectItemAction<MenuItem> {

	public HTTPSelectMenuItemAction(String actionName) {
		super(actionName);
	}

	public HTTPSelectMenuItemAction() {
		super("MenuSelect");
	}

	@Override
	public FormChainElement findTarget(Form sourceForm) throws InterfaceException {
		
		MenuItem item = getSelectedItem();
		Dispatcher.getDispatcher().setCurrentContext(item.getContext());
		return super.findTarget(sourceForm);

	}
	
}
