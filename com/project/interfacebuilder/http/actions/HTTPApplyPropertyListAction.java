package com.project.interfacebuilder.http.actions;

import java.util.Map;

import com.project.datasource.DataSource;
import com.project.inspection.PropertyListItem;
import com.project.interfacebuilder.Action;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.HTTPController;
import com.project.interfacebuilder.http.forms.HTTPDataRangeForm;
import com.project.interfacebuilder.http.forms.HTTPPropertyListForm;

public class HTTPApplyPropertyListAction extends HTTPApplyActionSupport<PropertyListItem> {

	public HTTPApplyPropertyListAction() {
		super("ApplyPropertyList");
	}

	@Override
	public void perform() throws InterfaceException {
		checkState();
		
		DataSource dataSource=(DataSource) controller.getAttribute(HTTPController.DATA_SOURCE_ATTRIBUTE);
		if(dataSource==null) throw new InterfaceException(HTTPController.DATA_SOURCE_ATTRIBUTE+" must be set before applying HTTPApplyPropertyListAction");
		
		@SuppressWarnings("unchecked")
		java.util.List<Action> actions=(java.util.List<Action>)controller.getAttribute(HTTPController.ACTIONS_ATTRIBUTE);
		if(actions==null) throw new InterfaceException(HTTPController.ACTIONS_ATTRIBUTE+" must be set after HTTPBrowseForm submission");

		Map<String,String[]> requestParametersMap=controller.getParameters();
		
		if(!(getSourceForm() instanceof HTTPPropertyListForm)) throw new InterfaceException("source form must be instance of HTTPPropertyListForm");
			
		if(!(getTargetForm() instanceof HTTPDataRangeForm)) throw new InterfaceException("target form must be instance of HTTPDataRangeForm");

		HTTPDataRangeForm dataRangeForm=(HTTPDataRangeForm)getTargetForm();

		dataRangeForm.setDataSource(dataSource);

		synchronizeValuesAndParameters(
				dataSource, dataSource.getPropertyList(),
				requestParametersMap,actions);


		super.perform();
	}
	
}
