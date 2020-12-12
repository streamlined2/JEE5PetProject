package com.project.interfacebuilder.http.forms;

import java.awt.Dimension;
import java.util.Locale;

import com.project.inspection.property.PropertyInfo;
import com.project.interfacebuilder.Action;
import com.project.interfacebuilder.Controller;
import com.project.interfacebuilder.Form;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.HTTPController;

public abstract class HTTPForm extends HTTPFormSupport implements Form {
	
	public final static String getUseFlagName(PropertyInfo pInfo) {
		return "$"+pInfo.getPropertyName();
	}

	protected String fontName="Verdana";
	protected int fontSize=1;
	protected Dimension dimension=new Dimension(1200,600);

	public HTTPForm() throws InterfaceException{
		super();
	}
	
	public Dimension getDimension() {
		return dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

	public String getFontName() {
		return fontName;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public void activate() throws InterfaceException {
		
		checkState();
		
		out.print("<form action=\""+getController().getLink()+"\" method=\"POST\">");

		out.print("<table>");
		out.print("<tr>");				

		out.print("<td>");

		out.print("<div style=\"overflow:auto; height: ");
		out.print(dimension.height);
		out.print("px; width: ");
		out.print(dimension.width);
		out.print("px;\">");

		placeContent();
		
		out.print("</div>");

		out.print("</td>");

		out.print("<td>");

		placeControls();
		
		out.print("</td>");
		
		out.print("</tr>");				
		out.print("</table>");

		out.print("</form>");
		
		controller.setAttribute(HTTPController.ACTIONS_ATTRIBUTE, getActions());
		controller.setAttribute(HTTPController.SOURCE_FORM_ATTRIBUTE, this);
		
		out.flush();
	}
	
	protected abstract void placeContent() throws InterfaceException;

	protected void placeControls() throws InterfaceException {

		out.print("<table>");
		
		for(Action action:getActions()){
			addButton(action);
		}

		out.print("</table>");

	}
	
	private void addButton(Action action) throws InterfaceException{
		
		out.print("<tr>");
		out.print("<td align=\"center\">");
		
		action.setController(controller);
		
		action.render(this);

		out.print("</td>");
		out.print("</tr>");

	}
	
	protected Controller controller;

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}
	
	@Override
	public Locale getSelectedLocale(){
		return getController().getSelectedLocale();
	}

}
