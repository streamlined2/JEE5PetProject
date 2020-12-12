package com.project.inspection;

import java.io.Serializable;

import com.project.inspection.property.InformationPropertyInfo;
import com.project.interfacebuilder.InterfaceException;

public abstract class ListItem implements Serializable {
	
	protected InformationPropertyInfo propertyInfo;

	public ListItem(InformationPropertyInfo pInfo){
		propertyInfo = pInfo;
	}

	public InformationPropertyInfo getPropertyInfo() {
		return propertyInfo;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ListItem [propertyInfo=");
		builder.append(propertyInfo);
		builder.append("]");
		return builder.toString();
	}

	public String getDisplayName() throws InterfaceException {
		if(propertyInfo!=null) return propertyInfo.getDisplayName();
		else return "";
	}

}
