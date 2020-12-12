package com.project.interfacebuilder.http.actions;

import java.io.PrintWriter;

import com.project.interfacebuilder.Action;
import com.project.interfacebuilder.Form;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.HTTPController;
import com.project.interfacebuilder.http.forms.HTTPDataRangeForm;

public class HTTPRangeAction extends HTTPActionSupport implements Action {

	public HTTPRangeAction() {
		super("Range");
	}

	@Override //this Action interface element has different view, so it redefines render method
	public void render(Form form) throws InterfaceException {

		if(!(form instanceof HTTPDataRangeForm)) throw new InterfaceException("form must be instance of HTTPDataRangeForm");
		
		HTTPDataRangeForm f=(HTTPDataRangeForm)form;
		
		PrintWriter out=f.getOut();

		out.print("<table>");
		out.print("<tr>");
		
		out.print("<td>");
		out.print("<table>");
		out.print("<tr>");
		out.print("<td>");
		renderRangeElement(f, out, HTTPController.RANGE_START_ATTRIBUTE, f.getStart());
		out.print("</td>");
		out.print("</tr>");
		out.print("<tr>");
		out.print("<td>");
		renderRangeElement(f, out, HTTPController.RANGE_FINISH_ATTRIBUTE, f.getFinish());
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

	private void renderRangeElement(
			HTTPDataRangeForm f, PrintWriter out, String elementName, Integer elementValue) {
		
		Integer fromId=f.getNextControlId();
		
		out.print("<label ");
		out.print("for=\"");
		out.print(fromId);
		out.print("\" ");
		out.print(getStyle());
		out.print(">");
		out.print(getLocalizedName(elementName));
		out.print("</label>");

		out.print("<input type=\"text\" ");
		out.print("name=\"");
		out.print(elementName);
		out.print("\" ");
		out.print("id=\"");
		out.print(fromId);
		out.print("\" ");
		out.print("value=\"");
		out.print(elementValue);
		out.print("\" ");
		out.print("size=\"");
		out.print(4);
		out.print("\" ");
		out.print(getStyle());
		out.print("/>");
	}

	@Override // different behavior implemented by redefinition of perform method 
	public void perform() throws InterfaceException {

		//autoboxing
		Integer from=1;
		String startValue=controller.getParameter(HTTPController.RANGE_START_ATTRIBUTE);
		if(!(startValue==null || startValue.isEmpty())) from=Integer.valueOf(startValue);

		Integer to=Integer.MAX_VALUE;
		String finishValue=controller.getParameter(HTTPController.RANGE_FINISH_ATTRIBUTE);
		if(!(finishValue==null || finishValue.isEmpty())) to=Integer.valueOf(finishValue);
		
		if(from>to) from=to;
		
		Form targetForm=getTargetForm();
		if(!(targetForm instanceof HTTPDataRangeForm)) throw new InterfaceException("target form must be an instance of HTTPDataRangeForm");

		HTTPDataRangeForm browseForm=(HTTPDataRangeForm)targetForm;
		browseForm.setStart(from);
		browseForm.setFinish(to);
		
		super.perform();
	}

}
