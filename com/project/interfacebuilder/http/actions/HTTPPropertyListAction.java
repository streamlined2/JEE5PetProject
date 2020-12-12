package com.project.interfacebuilder.http.actions;

import com.project.interfacebuilder.Form;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.forms.HTTPDataRangeForm;
import com.project.interfacebuilder.http.forms.HTTPQueryAwareForm;
import com.project.interfacebuilder.http.forms.HTTPPropertyListForm;

public class HTTPPropertyListAction extends HTTPActionSupport {
	
	public HTTPPropertyListAction(){
		super("PropertyList");
	}

	@Override
	public void perform() throws InterfaceException {
		
		Form sourceForm=getSourceForm();
		Form targetForm=getTargetForm();
		
		if(!(sourceForm instanceof HTTPDataRangeForm)) throw new InterfaceException("source form must be an instance of HTTPDataRangeForm");
		
		HTTPDataRangeForm src = (HTTPDataRangeForm)sourceForm;

		if(!(targetForm instanceof HTTPPropertyListForm)) throw new InterfaceException("target form must be an instance of HTTPPropertyListForm");
		
		HTTPQueryAwareForm dst = (HTTPQueryAwareForm)targetForm;
		
		dst.setDataSource(src.getDataSource());
		
		super.perform();
	}

}
