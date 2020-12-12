package com.project.interfacebuilder.http.actions;

import com.project.interfacebuilder.ControllerSupport.FormContextItem;
import com.project.interfacebuilder.Form;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.HTTPInterfaceBuilder;
import com.project.interfacebuilder.menu.MenuItem;

public class HTTPSelectMenuItemAction extends HTTPSelectItemAction<MenuItem> {

	@Override
	public FormContextItem findTarget(Form sourceForm) throws InterfaceException {
		
		MenuItem item = getSelectedItem();
		HTTPInterfaceBuilder.getInterfaceBuilder().setCurrentContext(item.getContext());
		return super.findTarget(sourceForm);

	}
	
}
