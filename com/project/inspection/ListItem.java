package com.project.inspection;

import java.io.Serializable;

public abstract class ListItem implements Serializable {
	
	protected InformationPropertyInfo propertyInfo;

	public ListItem(InformationPropertyInfo pInfo){
		propertyInfo = pInfo;
	}

	public InformationPropertyInfo getPropertyInfo() {
		return propertyInfo;
	}
	
}
