package com.project.interfacebuilder.http.actions;

import java.awt.Font;
import java.io.PrintWriter;

import com.project.interfacebuilder.ActionSupport;
import com.project.interfacebuilder.ControllerSupport.FormContextItem;
import com.project.interfacebuilder.Form;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.HTTPController;
import com.project.interfacebuilder.http.HTTPInterfaceBuilder;
import com.project.interfacebuilder.http.Helpers;
import com.project.interfacebuilder.http.forms.HTTPForm;

public abstract class HTTPActionSupport extends ActionSupport implements HTTPAction {
	
	public HTTPActionSupport(String name) {
		super(name);
	}
	
	private Font renderFont;

	public Font getRenderFont() {
		if(renderFont==null){
			renderFont=new Font("Verdana",Font.PLAIN,15);
		}
		return renderFont;
	}

	public void setRenderFont(Font renderFont) {
		this.renderFont = renderFont;
	}

	public String getStyle(){
		return Helpers.getStyle(getRenderFont());
	}
	
	protected HTTPController controller;
	
	public void setController(HTTPController controller){
		this.controller = controller;
	}
	
	protected void checkState(){
		if(controller==null) throw new IllegalStateException("controller must be set for action "+getDisplayName());
		if(getTargetForm()==null) throw new IllegalStateException("target form must be set for action "+getDisplayName());
	}
	
	public void render(Form form) throws InterfaceException {
		if(form instanceof HTTPForm){
			PrintWriter out=((HTTPForm)form).getOut();
			
			out.print("<input ");
			out.print("type=\"submit\" name=\"");
			out.print(getInnerName());
			out.print("\" value=\"");
			out.print(getDisplayName());
			out.print("\" ");
			out.print(getStyle());
			out.print(" />");
			
		}else throw new InterfaceException("form must be instance of HTTPForm");
	}

	@Override
	public FormContextItem findTarget(Form sourceForm) throws InterfaceException {
		if(sourceForm==null) throw new IllegalStateException("sourceForm must be not null");
		
		HTTPInterfaceBuilder builder = HTTPInterfaceBuilder.getInterfaceBuilder();
		
		controller.push(sourceForm,builder.getCurrentContext());
		
		return builder.getTarget(sourceForm, this);
	}
	
}
