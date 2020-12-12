package com.project.interfacebuilder.http.forms;

import com.project.Helpers;
import com.project.inspection.InformationPropertyInfo;
import com.project.inspection.PropertyInfo;
import com.project.inspection.PropertyListItem;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.actions.HTTPApplyPropertyListAction;
import com.project.interfacebuilder.http.actions.HTTPCancelAction;

public class HTTPPropertyListForm extends HTTPQueryAwareForm {
	
	public HTTPPropertyListForm() throws InterfaceException {
		super();

		addAction(new HTTPApplyPropertyListAction());
		addAction(new HTTPCancelAction());

	}

	@Override
	protected void placeContent() throws InterfaceException {
		out.print("<table>");
		
		addHeaders();
		
		int order=1;

		for(InformationPropertyInfo pInfo:getDataSource().getSelectedInformationProperties()){
			Integer id=getNextControlId();

			out.print("<tr>");
			
			out.print("<td>");
			
			addPropertyLabel(pInfo, id);
		
			out.print("</td>");
			
			out.print("<td>");
			
			addUsePropertyFlag(pInfo, id);
		
			out.print("</td>");
			
			out.print("<td>");
			
			addOrderField(pInfo, order++);
		
			out.print("</td>");
			
			out.print("</tr>");
		}

		out.print("</table>");

	}

	private void addHeaders() {

		out.print("<tr>");
		
		out.print("<th>");
		
		out.print("<span ");
		out.print(getStyle());
		out.print(">");
		out.print(Helpers.getLocalizedDisplayName("PropertyNamesBundle", "Entity", "property"));
		out.print("</span>");
		
		out.print("</th>");
		
		out.print("<th>");
		
		out.print("<span ");
		out.print(getStyle());
		out.print(">");
		out.print(Helpers.getLocalizedDisplayName("PropertyNamesBundle", "Entity", "orderBy"));
		out.print("</span>");
		
		out.print("</th>");
		
		out.print("<th>");
		
		out.print("<span ");
		out.print(getStyle());
		out.print(">");
		out.print(Helpers.getLocalizedDisplayName("PropertyNamesBundle", "Entity", "order"));
		out.print("</span>");
		
		out.print("</th>");
		
		out.print("</tr>");
	
	}
	
	private void addOrderField(InformationPropertyInfo pInfo, int order) {
		
		out.print("<input type=\"text\" ");
		out.print(getStyle());
		out.print("name=\"");
		out.print(pInfo.getPropertyName());
		out.print("\" ");
		out.print("value=\"");
		int value=order;
		PropertyListItem item=getDataSource().getPropertyList().findByProperty(pInfo);
		if(item!=null){
			value=item.getOrder();
		}
		out.print(value);
		out.print("\" ");
		out.print("size=\"");
		out.print(3);
		out.print("\" ");
		out.print("maxlength=\"");
		out.print(3);
		out.print("\" ");
		out.print(" />");

	}

	private void addUsePropertyFlag(PropertyInfo pInfo, Integer id) {

		out.print("<input type=\"checkbox\" ");
		out.print(getStyle());
		out.print("name=\"");
		out.print(getUseFlagName(pInfo));
		out.print("\" ");
		out.print("id=\"");
		out.print(id);
		out.print("\" ");
		PropertyListItem item=getDataSource().getPropertyList().findByProperty(pInfo);
		if(item!=null){
			out.print("checked");
		}
		out.print("/>");
		
	}

	private void addPropertyLabel(InformationPropertyInfo pInfo, Integer id) {
		out.print("<label for=\"");
		out.print(id);
		out.print("\" ");
		out.print(getStyle());
		out.print(" >");
		out.print(pInfo.getDisplayName());
		out.print(" ");
		out.print("</label>");
	}

}
