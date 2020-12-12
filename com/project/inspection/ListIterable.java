package com.project.inspection;

import java.util.List;
import java.util.Map;

import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.HTTPController;
import com.project.interfacebuilder.http.actions.HTTPAction;

public interface ListIterable<ItemType> extends Iterable<ItemType> {

	public ItemType createItem(HTTPController controller,InformationPropertyInfo pInfo,Map<String, String[]> parameters, List<HTTPAction> actions) throws InterfaceException;
	public void addItem(ItemType item);
	
}
