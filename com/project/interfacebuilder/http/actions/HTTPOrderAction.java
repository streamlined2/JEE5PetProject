package com.project.interfacebuilder.http.actions;

import com.project.interfacebuilder.Form;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.forms.HTTPDataRangeForm;
import com.project.interfacebuilder.http.forms.HTTPOrderForm;

public class HTTPOrderAction extends HTTPActionSupport {
	
	public HTTPOrderAction(){
		super("Order");
	}

	@Override
	public void perform() throws InterfaceException {
		
		Form sourceForm=getSourceForm();
		Form targetForm=getTargetForm();
		
		if(!(sourceForm instanceof HTTPDataRangeForm)) throw new InterfaceException("source form must be an instance of HTTPDataRangeForm");
		
		HTTPDataRangeForm src = (HTTPDataRangeForm)sourceForm;

		if(!(targetForm instanceof HTTPOrderForm)) throw new InterfaceException("target form must be an instance of HTTPOrderForm");
		
		HTTPOrderForm dst = (HTTPOrderForm)targetForm;
		
		dst.setDataSource(src.getDataSource());
		
		super.perform();
	}

}
