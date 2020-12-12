package com.project.inspection;

import java.io.Serializable;

import com.project.inspection.property.InformationPropertyInfo;

public abstract class ListItem implements Serializable {
	
	protected InformationPropertyInfo propertyInfo;

	public ListItem(InformationPropertyInfo pInfo){
		propertyInfo = pInfo;
	}

	public InformationPropertyInfo getPropertyInfo() {
		return propertyInfo;
	}
	
}
