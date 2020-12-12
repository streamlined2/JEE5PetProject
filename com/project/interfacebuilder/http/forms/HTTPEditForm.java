package com.project.interfacebuilder.http.forms;

import com.project.Startup;
import com.project.inspection.EntityInspector;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.HTTPController;
import com.project.interfacebuilder.http.actions.HTTPCancelAction;
import com.project.interfacebuilder.http.actions.HTTPSaveChangesAction;
import com.project.datasource.EntityDataSource;

public class HTTPEditForm extends HTTPSelectorSetForm {

	private Object primaryKey;
	private boolean createNew;
	
	public HTTPEditForm() throws InterfaceException{
		super();

		addAction(new HTTPSaveChangesAction());
		addAction(new HTTPCancelAction());
		
	}
	
	protected void placeContent() throws InterfaceException {
		
		addSelectorSet();
		
	}

	@Override
	public void activate() throws InterfaceException {
		
		checkState();
		
		if(createNew){
			entityData=EntityInspector.initializeEntityData(getDataSource());
		}else{
			if(primaryKey==null) throw new InterfaceException("primary key must be set before HTTPEditForm activation.");
			entityData=Startup.getAgent().fetchEntity((EntityDataSource)getDataSource(),primaryKey);
		}

		controller.setAttribute(HTTPController.CREATE_NEW_ATTRIBUTE, Boolean.valueOf(createNew));
		controller.setAttribute(HTTPController.PRIMARY_KEY_ATTRIBUTE, primaryKey);
		
		super.activate();
	}

	public void setPrimaryKey(Object primaryKey) {
		this.primaryKey = primaryKey;
	}

	public boolean isCreateNew() {
		return createNew;
	}

	public void setCreateNew(boolean createNew) {
		this.createNew = createNew;
	}

}
