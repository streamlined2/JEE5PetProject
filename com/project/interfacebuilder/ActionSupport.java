package com.project.interfacebuilder;

import com.project.Helpers;

public abstract class ActionSupport implements Action {
	
	private String name;
	
	public ActionSupport(String name){
		if(name==null || name.isEmpty()) throw new IllegalArgumentException("empty name is not allowed");
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public final String getInnerName(){
		return "$"+name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return getLocalizedName(getName());
	}
	
	protected String getLocalizedName(String key){
		return Helpers.getLocalizedDisplayName("ActionNamesBundle", "", key, key);
	}
	
	@Override
	public int hashCode(){
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Action){
			return name.equals(((Action)o).getName());
		}
		return false; 		
	}

	public void perform() throws InterfaceException {
	}
	
	private Form targetForm;
	
	public void setTargetForm(Form form){
		targetForm = form;
	}
	
	public Form getTargetForm(){
		return targetForm;
	}
	
	private Form sourceForm;

	public Form getSourceForm() {
		return sourceForm;
	}

	public void setSourceForm(Form sourceForm) {
		this.sourceForm = sourceForm;
	}

}
