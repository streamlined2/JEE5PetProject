package com.project.interfacebuilder.http.forms;

import com.project.inspection.EntityInspector;
import com.project.inspection.Filter;
import com.project.inspection.FilterItem;
import com.project.inspection.Filter.FilterRangeBoundary;
import com.project.inspection.property.PropertyInfo;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.actions.HTTPApplyFilterAction;
import com.project.interfacebuilder.http.actions.HTTPCancelAction;

public class HTTPFilterForm extends HTTPSelectorSetForm {
	
	private FilterRangeBoundary kind;
	
	public HTTPFilterForm() throws InterfaceException {
		super();

		addAction(new HTTPApplyFilterAction());
		addAction(new HTTPCancelAction());

	}

	@Override
	protected void placeContent() throws InterfaceException {

		addSelectorSet();

	}

	@Override
	protected void addUseSelectorFlag(PropertyInfo pInfo) {
		
		out.print("<td>");
		
		out.print("<input type=\"checkbox\" ");
		out.print(getStyle());
		out.print("name=\"");
		out.print(getUseFlagName(pInfo));
		out.print("\" ");
		if(isChecked(pInfo)){
			out.print("checked");
		}
		out.print(" />");

		out.print("</td>");

	}
	
	private boolean isChecked(PropertyInfo pInfo){

		Filter filter=getDataSource().getFilter();
		if(filter==null) return false;

		FilterItem item=filter.findByProperty(pInfo);
		if(item==null) return false;

		return true;
	}
	
	@Override
	public void activate() throws InterfaceException {
		entityData=EntityInspector.initializeEntityData(getDataSource(),getBoundaryKind());

		super.activate();
	}

	public FilterRangeBoundary getBoundaryKind() {
		return kind;
	}

	public void setBoundaryKind(FilterRangeBoundary kind) {
		this.kind = kind;
	}

}
