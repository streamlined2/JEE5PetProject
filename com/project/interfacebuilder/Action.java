package com.project.interfacebuilder;

import com.project.interfacebuilder.ControllerSupport.FormContextItem;

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
	public FormContextItem findTarget(Form sourceForm) throws InterfaceException;

}
