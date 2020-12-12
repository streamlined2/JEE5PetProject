package com.project.inspection;

import java.io.Serializable;

import com.project.inspection.property.InformationPropertyInfo;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.forms.HTTPForm;
import com.project.queries.QueryDefinition;

public abstract class ListItem implements Serializable {
	
	private static final long serialVersionUID = 4787905024213893323L;
	
	protected InformationPropertyInfo propertyInfo = null;
	
	protected HTTPForm form;
	
	public ListItem(HTTPForm form, InformationPropertyInfo propertyInfo){
		this.form = form;
		this.propertyInfo = propertyInfo;
	}
	
	public abstract QueryDefinition.Property getProperty();
	
	public final InformationPropertyInfo getPropertyInfo() throws InterfaceException{
		if(propertyInfo!=null){
			return propertyInfo;
		}else if(getProperty()!=null){
			return getProperty().getReferencedProperty();
		}
		return null;
	}

	@Override
	public String toString() {
		return new StringBuilder().
			append("ListItem [property=").
			append(getProperty().toString()).
			append(", propertyInfo=").
			append(propertyInfo.toString()).
			append("]").
		toString();
	}
	
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof ListItem)) return false;
		ListItem item = (ListItem)obj;
		try {
			return getPropertyInfo().equals(item.getPropertyInfo());
		} catch (InterfaceException e) {
			return false;
		}
	}
	
	@Override
	public int hashCode(){
		try {
			return getPropertyInfo().hashCode();
		} catch (InterfaceException e) {
			return -1;
		}
	}
	
	public final String getDisplayName() throws InterfaceException {
		if(getProperty()!=null) return getProperty().getDisplayName(form.getSelectedLocale());
		else if(propertyInfo!=null) return propertyInfo.getDisplayName();
		return "";
	}

}
