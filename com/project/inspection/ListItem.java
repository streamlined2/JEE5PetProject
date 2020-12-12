package com.project.inspection;

import java.io.Serializable;

import com.project.inspection.property.InformationPropertyInfo;
import com.project.interfacebuilder.InterfaceException;
import com.project.queries.QueryDefinition;

public abstract class ListItem implements Serializable {
	
	private static final long serialVersionUID = 4787905024213893323L;
	
	protected InformationPropertyInfo propertyInfo = null;
	
	public ListItem(InformationPropertyInfo propertyInfo){
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
		if(getProperty()!=null) return getProperty().getDisplayName();
		else if(propertyInfo!=null) return propertyInfo.getDisplayName();
		return "";
	}

}
