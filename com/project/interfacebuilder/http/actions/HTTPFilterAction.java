package com.project.interfacebuilder.http.actions;

import java.io.PrintWriter;

import com.project.interfacebuilder.Form;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.HTTPController;
import com.project.interfacebuilder.http.forms.HTTPDataRangeForm;
import com.project.interfacebuilder.http.forms.HTTPFilterForm;

public class HTTPFilterAction extends HTTPActionSupport {

	public HTTPFilterAction() {
		super("Filter");
	}

	@Override
	public void render(Form form) throws InterfaceException {

		if(!(form instanceof HTTPDataRangeForm)) throw new InterfaceException("form must be an instance of HTTPDataRangeForm");
			
		HTTPDataRangeForm f=(HTTPDataRangeForm)form;
		
		PrintWriter out=f.getOut();

		out.print("<table>");
		out.print("<tr>");
		
		out.print("<td>");
		out.print("<table>");
		out.print("<tr>");
		out.print("<td>");
		renderFilterRangeElement(f, out, HTTPController.FILTER_RANGE_START, HTTPController.FILTER_RANGE_GROUP, true);
		out.print("</td>");
		out.print("</tr>");
		out.print("<tr>");
		out.print("<td>");
		renderFilterRangeElement(f, out, HTTPController.FILTER_RANGE_FINISH, HTTPController.FILTER_RANGE_GROUP, false);
		out.print("</td>");
		out.print("</tr>");
		out.print("</table>");
		out.print("</td>");

		out.print("<td>");
		super.render(f);
		out.print("</td>");
		out.print("</tr>");

		out.print("</table>");

	}

	private void renderFilterRangeElement(
			HTTPDataRangeForm f, PrintWriter out, 
			String elementName, String groupName, boolean checked) {
		
		out.print("<input type=\"radio\" ");
		out.print("name=\"");
		out.print(groupName);
		out.print("\" ");
		out.print("value=\"");
		out.print(elementName);
		out.print("\" ");
		if(checked){
			out.print("checked ");
		}
		out.print(getStyle());
		out.print("/>");
		out.print("<span ");
		out.print(getStyle());
		out.print(">");
		out.print(getLocalizedName(elementName));
		out.print("</span>");

	}

	@Override
	public void perform() throws InterfaceException {

		controller.updateFilterRangeKind();
		
		Form sourceForm=getSourceForm();
		Form targetForm=getTargetForm();
		
		if(!(sourceForm instanceof HTTPDataRangeForm)) throw new InterfaceException("source form must be an instance of HTTPDataRangeForm");
		
		HTTPDataRangeForm src = (HTTPDataRangeForm)sourceForm;

		if(!(targetForm instanceof HTTPFilterForm)) throw new InterfaceException("target form must be an instance of HTTPFilterForm");
		
		HTTPFilterForm dst = (HTTPFilterForm)targetForm;
		
		dst.setBoundaryKind(controller.getFilterRangeKind());
		dst.setDataSource(src.getDataSource());
		
		super.perform();
	}

}
