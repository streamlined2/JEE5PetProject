package com.project.interfacebuilder.http.forms;

import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.SelectionViewItem;

public abstract class HTTPBrowseViewForm<ItemType extends SelectionViewItem> extends HTTPOptionForm<ItemType> {

	public HTTPBrowseViewForm() throws InterfaceException {
		super();
	}

	@Override
	protected abstract String formListTitle() throws InterfaceException;
	
	@Override
	protected abstract String formListValue(ItemType e);

}
