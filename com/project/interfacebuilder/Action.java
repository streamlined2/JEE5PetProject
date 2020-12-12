package com.project.interfacebuilder;

import java.awt.Font;
import java.util.Locale;

import com.project.interfacebuilder.ControllerSupport.FormChainElement;

// Command/Action design pattern interface
// interface hierarchy Action/HTTPAction and implementing class hierarchy ActionSupport/HTTPActionSupport combined together by Bridge design pattern
public interface Action {
	
	public String getName();
	public String getDisplayName(Locale locale);
	public String getInnerName();
	
	public void setSourceForm(Form form);
	public Form getSourceForm();
	public void setTargetForm(Form form);
	public Form getTargetForm();
	
	public void perform() throws InterfaceException;
	public void render(Form form) throws InterfaceException;
	
	public FormChainElement findTarget(Form sourceForm) throws InterfaceException;

	public String getStyle();
	
	public Font getRenderFont();
	public void setRenderFont(Font font);
	
	public void setController(Controller controller);

}
