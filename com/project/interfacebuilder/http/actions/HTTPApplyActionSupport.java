package com.project.interfacebuilder.http.actions;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.project.datasource.DataSource;
import com.project.inspection.ListItem;
import com.project.inspection.ListIterable;
import com.project.inspection.property.InformationPropertyInfo;
import com.project.inspection.property.PropertyInfo;
import com.project.interfacebuilder.Action;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.HTTPControllerSupport;
import com.project.interfacebuilder.http.forms.HTTPForm;

public abstract class HTTPApplyActionSupport<ItemType extends ListItem> extends HTTPActionSupport {

	protected HTTPApplyActionSupport(String name) {
		super(name);
	}

	// add & remove property values for passed parameters
	protected final void synchronizeValuesAndParameters(
			DataSource dataSource,ListIterable<ItemType> list,
			Map<String, String[]> parameters, List<Action> actions) throws InterfaceException {
				
		addValuesFromParameters(dataSource, list, parameters, actions);
		
		removeAbsentValues(list, parameters, actions);
			
	}

	// remove parameter value if appropriate property is absent in request
	private void removeAbsentValues(
			Iterable<ItemType> list, Map<String, String[]> parameters,
			List<Action> actions) throws InterfaceException {

		for(Iterator<ItemType> i=list.iterator();i.hasNext();){
			PropertyInfo pInfo=i.next().getPropertyInfo();
			String propertyKey=HTTPForm.getUseFlagName(pInfo);
			if(!parameters.containsKey(propertyKey)){
				i.remove();
			}
		}
	}

	// add parameter value for each property that is present in request
	private void addValuesFromParameters(
			DataSource dataSource, ListIterable<ItemType> list, 
			Map<String, String[]> parameters,
			List<Action> actions) throws InterfaceException {
				
		for(InformationPropertyInfo pInfo:dataSource.getSelectedInformationProperties()){
			
			if(HTTPControllerSupport.parameterIsNotEmpty(parameters,HTTPForm.getUseFlagName(pInfo))){
	
				list.addItem(
						list.createItem(controller, pInfo, parameters, actions));
				
			}
	
		}
	
	}

}
