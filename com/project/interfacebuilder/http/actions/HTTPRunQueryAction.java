package com.project.interfacebuilder.http.actions;

import com.project.interfacebuilder.Form;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.HTTPController;
import com.project.interfacebuilder.http.forms.HTTPDataRangeForm;
import com.project.interfacebuilder.http.forms.HTTPQuerySelectionForm;
import com.project.interfacebuilder.menu.MenuItem;
import com.project.datasource.QueryDataSource;
import com.project.queries.QueryDefinition;

public class HTTPRunQueryAction extends HTTPSelectItemAction<MenuItem> {

	public HTTPRunQueryAction() {
		super("RunQuery");
	}

	@Override
	public void perform() throws InterfaceException {
		
		Form sourceForm = getSourceForm();
		if(!(sourceForm instanceof HTTPQuerySelectionForm)) throw new InterfaceException("target form must be an instance of HTTPQuerySelectionForm");
		HTTPQuerySelectionForm querySelectionForm=(HTTPQuerySelectionForm)getSourceForm();
		
		Form targetForm = getTargetForm();
		if(!(targetForm instanceof HTTPDataRangeForm)) throw new InterfaceException("target form must be an instance of HTTPQueryForm");
		HTTPDataRangeForm resultForm=(HTTPDataRangeForm)getTargetForm();
		
		QueryDefinition queryDef = querySelectionForm.getSelectedItem();
		
		QueryDataSource dataSource = new QueryDataSource(resultForm,queryDef);
		
		resultForm.setDataSource(dataSource);

		controller.setAttribute(HTTPController.DATA_SOURCE_ATTRIBUTE, dataSource);
		
		super.perform();
	}

}
