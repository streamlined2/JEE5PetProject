package com.project.interfacebuilder.http.actions;

import java.awt.Font;
import java.io.PrintWriter;

import com.project.Startup;
import com.project.interfacebuilder.ActionSupport;
import com.project.interfacebuilder.Controller;
import com.project.interfacebuilder.ControllerSupport.FormChainElement;
import com.project.interfacebuilder.Form;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.Helpers;
import com.project.interfacebuilder.http.forms.HTTPForm;
import com.project.interfacebuilder.transition.Dispatcher;

// Bridge design pattern: abstract class hierarchy ActionSupport/HTTPActionSupport implements contract behavior, 
// that implied by interface hierarchy Action/HTTPAction
public abstract class HTTPActionSupport extends ActionSupport {
	
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
	
	protected Controller controller;
	
	public void setController(Controller controller){
		this.controller = controller;
	}
	
	protected void checkState(){ // sentinel method to watch for form's illegal state
		if(controller==null) throw new IllegalStateException("controller must be set for action "+getDisplayName(Startup.DEFAULT_LOCALE));
		if(getTargetForm()==null) throw new IllegalStateException("target form must be set for action "+getDisplayName(Startup.DEFAULT_LOCALE));
	}
	
	public void render(Form form) throws InterfaceException {
		if(form instanceof HTTPForm){
			PrintWriter out=((HTTPForm)form).getOut();
			
			out.print("<input ");
			out.print("type=\"submit\" name=\"");
			out.print(getInnerName());
			out.print("\" value=\"");
			out.print(getDisplayName(form.getSelectedLocale()));
			out.print("\" ");
			out.print(getStyle());
			out.print(" />");
			
		}else throw new InterfaceException("form must be instance of HTTPForm");
	}

	@Override // default behavior is to save current form & context and delegate call to Dispatcher.getTarget method 
	public FormChainElement findTarget(Form sourceForm) throws InterfaceException {
		if(sourceForm==null) throw new IllegalStateException("sourceForm must be not null");
		
		controller.push(sourceForm,Dispatcher.getDispatcher().getCurrentContext());
		
		return Dispatcher.getDispatcher().getTarget(sourceForm, this);
	}
	
}
