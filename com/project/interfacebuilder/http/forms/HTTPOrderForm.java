package com.project.interfacebuilder.http.forms;

import com.project.Helpers;
import com.project.inspection.ListItem;
import com.project.inspection.OrderingItem;
import com.project.inspection.OrderingItem.SortOrderType;
import com.project.inspection.property.InformationPropertyInfo;
import com.project.inspection.property.PropertyInfo;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.actions.HTTPApplyOrderAction;
import com.project.interfacebuilder.http.actions.HTTPCancelAction;

public class HTTPOrderForm extends HTTPDataAwareForm {
	
	public HTTPOrderForm() throws InterfaceException {
		super();

		addAction(new HTTPApplyOrderAction());
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
			
			out.print("<td>");
			
			addOrderType(pInfo);
		
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
		
		out.print("<th>");
		
		out.print("<span ");
		out.print(getStyle());
		out.print(">");
		out.print(Helpers.getLocalizedDisplayName("PropertyNamesBundle", "Entity", "sortOrder"));
		out.print("</span>");
		
		out.print("</th>");
	
		out.print("</tr>");
	
	}
	
	private void addOrderField(PropertyInfo pInfo, int order) {
		
		out.print("<input type=\"text\" ");
		out.print(getStyle());
		out.print("name=\"");
		out.print(pInfo.getPropertyName());
		out.print("\" ");
		out.print("value=\"");
		int value=order;
		OrderingItem item=getDataSource().getOrdering().findByProperty(pInfo);
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

	private void addOrderType(PropertyInfo pInfo) {
		
		out.print("<select ");
		out.print("name=\"");
		out.print(getOrderTypeName(pInfo));
		out.print("\" ");
		out.print(" size=");
		out.print(1);
		out.print(" ");
		out.print(getStyle());
		out.print(">");

		OrderingItem item=getDataSource().getOrdering().findByProperty(pInfo);
		
		out.print("<option ");
		out.print("value=\"");
		out.print(SortOrderType.ASCENDING.toString());
		out.print("\" ");
		if(item!=null && item.getSortOrderType()==SortOrderType.ASCENDING){
			out.print("selected");
		}
		out.print(">");
		out.print(Helpers.getLocalizedDisplayName("TypeValuesBundle","SortOrderType",SortOrderType.ASCENDING.toString(),SortOrderType.ASCENDING.toString()));//EntityInspector.convertToString(SortOrderType.ASCENDING)
		out.print("</option>");

		out.print("<option ");
		out.print("value=\"");
		out.print(SortOrderType.DESCENDING.toString());
		out.print("\" ");
		if(item!=null && item.getSortOrderType()==SortOrderType.DESCENDING){
			out.print("selected");
		}
		out.print(">");
		out.print(Helpers.getLocalizedDisplayName("TypeValuesBundle","SortOrderType",SortOrderType.DESCENDING.toString(),SortOrderType.DESCENDING.toString()));//EntityInspector.convertToString(SortOrderType.DESCENDING)
		out.print("</option>");

		out.print("</select>");

	}

	public final static String getOrderTypeName(PropertyInfo pInfo) {
		return "_"+pInfo.getPropertyName();
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
		ListItem item=getDataSource().getOrdering().findByProperty(pInfo);
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
