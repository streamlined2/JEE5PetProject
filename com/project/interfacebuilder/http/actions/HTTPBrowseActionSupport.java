package com.project.interfacebuilder.http.actions;

import com.project.Helpers;
import com.project.inspection.EntityInfo;
import com.project.inspection.EntityInspector;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.HTTPController;
import com.project.interfacebuilder.http.forms.HTTPDataRangeForm;
import com.project.queries.EntityDataSource;

public abstract class HTTPBrowseActionSupport extends HTTPSelectItemAction <EntityInfo> {

	public HTTPBrowseActionSupport(String name) {
		super(name);
	}

	@Override
	public void perform() throws InterfaceException {
		
		String selectedEntityName=controller.getParameter(HTTPController.SELECTED_ENTITY_ATTRIBUTE);
		if(selectedEntityName==null) throw new InterfaceException(HTTPController.SELECTED_ENTITY_ATTRIBUTE+" parameter must be set after HTTPEntitySelectionForm submission");

		controller.setAttribute(HTTPController.SELECTED_ENTITY_ATTRIBUTE,selectedEntityName);
	
		if(!(getTargetForm() instanceof HTTPDataRangeForm)) throw new InterfaceException("target form must be an instance of HTTPDataRangeForm");

		HTTPDataRangeForm dataRangeForm=(HTTPDataRangeForm)getTargetForm();
		try {
			
			Class<?> entityClass=Class.forName(Helpers.getEntityFullClassName(selectedEntityName));
			EntityInfo entityInfo=EntityInspector.getEntityInfo(entityClass);
			EntityDataSource dataSource = new EntityDataSource(entityInfo); 
			dataRangeForm.setDataSource(dataSource);

			controller.setAttribute(HTTPController.DATA_SOURCE_ATTRIBUTE, dataSource);
		
		} catch (ClassNotFoundException e) {
			throw new InterfaceException(e);
		}
		
		super.perform();
	}

}
