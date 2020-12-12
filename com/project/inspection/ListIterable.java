package com.project.inspection;

import java.util.List;
import java.util.Map;

import com.project.inspection.property.InformationPropertyInfo;
import com.project.interfacebuilder.Action;
import com.project.interfacebuilder.Controller;
import com.project.interfacebuilder.InterfaceException;

public interface ListIterable<ItemType> extends Iterable<ItemType> {

	public ItemType createItem(
				Controller controller,
				InformationPropertyInfo pInfo,
				Map<String, String[]> parameters, 
				List<Action> actions) throws InterfaceException;
	
	public void addItem(ItemType item) throws InterfaceException;
	
}
