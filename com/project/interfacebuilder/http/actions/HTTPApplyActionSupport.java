package com.project.interfacebuilder.http.actions;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.project.inspection.ListItem;
import com.project.inspection.ListIterable;
import com.project.inspection.PropertyInfo;
import com.project.inspection.property.InformationPropertyInfo;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.HTTPControllerSupport;
import com.project.interfacebuilder.http.forms.HTTPForm;
import com.project.datasource.DataSource;

public abstract class HTTPApplyActionSupport<ItemType extends ListItem> extends HTTPActionSupport {

	protected HTTPApplyActionSupport(String name) {
		super(name);
	}

	protected final void syncValuesWithParameters(
			DataSource dataSource,ListIterable<ItemType> list,
			Map<String, String[]> parameters, List<HTTPAction> actions) throws InterfaceException {
				
		addValuesFromParameters(dataSource, list, parameters, actions);
		
		removeAbsentValues(list, parameters, actions);
			
	}

	private void removeAbsentValues(
			Iterable<ItemType> list, Map<String, String[]> parameters,
			List<HTTPAction> actions) throws InterfaceException {

		for(Iterator<ItemType> i=list.iterator();i.hasNext();){
			PropertyInfo pInfo=i.next().getPropertyInfo();
			String propertyKey=HTTPForm.getUseFlagName(pInfo);
			if(!parameters.containsKey(propertyKey)){
				i.remove();
			}
		}
	}

	private void addValuesFromParameters(
			DataSource dataSource, ListIterable<ItemType> list, 
			Map<String, String[]> parameters,
			List<HTTPAction> actions) throws InterfaceException {
				
		for(InformationPropertyInfo pInfo:dataSource.getSelectedInformationProperties()){
			
			if(HTTPControllerSupport.parameterIsNotEmpty(parameters,HTTPForm.getUseFlagName(pInfo))){
	
				ItemType item=list.createItem(controller, pInfo, parameters, actions);
				list.addItem(item);
				
			}
	
		}
	
	}

}
