package com.project.interfacebuilder.http.actions;

import com.project.inspection.EntityInspector;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.HTTPController;
import com.project.interfacebuilder.http.forms.HTTPEditForm;
import com.project.queries.DataSource;
import com.project.queries.EntityDataSource;

public class HTTPEditActionSupport extends HTTPActionSupport /*HTTPSelectItemAction <EntityInfo>*/ {

	public HTTPEditActionSupport(String name) {
		super(name);
	}
	
	public boolean createNew(){
		return false;
	}
	
	@Override
	public void perform() throws InterfaceException {
		
		checkState();
		
		String pkParameter=controller.getParameter(HTTPController.PRIMARY_KEY_ATTRIBUTE);
		if(pkParameter==null) throw new InterfaceException("PRIMARY_KEY_ATTRIBUTE parameter must be set before applying HTTPEditActionSupport");
	
		DataSource dataSource=(DataSource) controller.getAttribute(HTTPController.DATA_SOURCE_ATTRIBUTE);
		if(dataSource==null) throw new InterfaceException(HTTPController.DATA_SOURCE_ATTRIBUTE+" must be set after HTTPBrowseForm submission");
		
		if(!(dataSource instanceof EntityDataSource)) throw new InterfaceException("dataSource must be instance of EntityDataSource for HTTPEditActionSupport"); 
		
		Object primaryKey=EntityInspector.convertFromString(pkParameter, ((EntityDataSource)dataSource).getPrimaryKeyPropertyInfo().getType());
		
		if(!(getTargetForm() instanceof HTTPEditForm)) throw new InterfaceException("target form must be an instance of HTTPEditForm");

		HTTPEditForm editForm=(HTTPEditForm)getTargetForm();
		editForm.setCreateNew(createNew());
		editForm.setPrimaryKey(primaryKey);
		editForm.setDataSource(dataSource);
		
		super.perform();
	}

}
