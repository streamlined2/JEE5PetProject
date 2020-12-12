package com.project.interfacebuilder;

import com.project.interfacebuilder.ControllerSupport.FormChainElement;

// Command/Action design pattern interface
// interface hierarchy Action/HTTPAction and implementing class hierarchy ActionSupport/HTTPActionSupport combined together by Bridge design pattern
public interface Action {
	
	public String getName();
	public String getDisplayName();
	public String getInnerName();
	public void setSourceForm(Form form);
	public Form getSourceForm();
	public void setTargetForm(Form form);
	public Form getTargetForm();
	public void perform() throws InterfaceException;
	public void render(Form form) throws InterfaceException;
	public FormChainElement findTarget(Form sourceForm) throws InterfaceException;

}
