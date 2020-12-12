package com.project.interfacebuilder.http.actions;

import java.util.Map;

import com.project.datasource.DataSource;
import com.project.inspection.FilterItem;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.HTTPController;
import com.project.interfacebuilder.http.forms.HTTPDataRangeForm;
import com.project.interfacebuilder.http.forms.HTTPFilterForm;

public class HTTPApplyFilterAction extends HTTPApplyActionSupport<FilterItem> {

	public HTTPApplyFilterAction() {
		super("ApplyFilter");
	}

	@Override
	public void perform() throws InterfaceException {
		
		checkState(); // check if parameters are valid
		
		DataSource dataSource=(DataSource) controller.getAttribute(HTTPController.DATA_SOURCE_ATTRIBUTE);
		if(dataSource==null) throw new InterfaceException(HTTPController.DATA_SOURCE_ATTRIBUTE+" must be set before applying HTTPApplyFilterAction");
		
		@SuppressWarnings("unchecked")
		java.util.List<HTTPAction> actions=(java.util.List<HTTPAction>)controller.getAttribute(HTTPController.ACTIONS_ATTRIBUTE);
		if(actions==null) throw new InterfaceException(HTTPController.ACTIONS_ATTRIBUTE+" must be set after HTTPBrowseForm submission");

		Map<String,String[]> requestParametersMap=controller.getParameters();
		
		if(!(getSourceForm() instanceof HTTPFilterForm)) throw new InterfaceException("source form must be an instance of HTTPFilterForm");
			
		if(!(getTargetForm() instanceof HTTPDataRangeForm)) throw new InterfaceException("target form must be an instance of HTTPDataRangeForm");

		HTTPDataRangeForm dataRangeForm=(HTTPDataRangeForm)getTargetForm();

		dataRangeForm.setDataSource(dataSource);

		synchronizeValuesAndParameters(
				dataSource, dataSource.getFilter(),
				requestParametersMap,actions);

		dataRangeForm.setStart(HTTPDataRangeForm.RANGE_UNDEFINED);
		dataRangeForm.setFinish(HTTPDataRangeForm.RANGE_UNDEFINED);
	
		super.perform();
	}

}
