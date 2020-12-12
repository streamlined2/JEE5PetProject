package com.project.inspection.property;

import com.project.inspection.EntityInfo;
import com.project.inspection.PropertyInfo;

public class PrimaryKeyPropertyInfo extends PropertyInfo {

	public PrimaryKeyPropertyInfo(EntityInfo eInfo, String propertyName,
			Class<?> type, String readMethod, String writeMethod) {
		super(eInfo, propertyName, type, readMethod, writeMethod);
	}

	@Override
	public boolean isPrimaryKey(){
		return true;
	}
	
}
