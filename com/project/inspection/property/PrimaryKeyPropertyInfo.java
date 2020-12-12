package com.project.inspection.property;

import com.project.inspection.EntityInfo;

public class PrimaryKeyPropertyInfo extends PropertyInfo {

	private static final long serialVersionUID = -8989974409765166769L;

	public PrimaryKeyPropertyInfo(EntityInfo eInfo, String propertyName,
			Class<?> type, String readMethod, String writeMethod) {
		super(eInfo, propertyName, type, readMethod, writeMethod);
	}

	@Override
	public boolean isPrimaryKey(){
		return true;
	}
	
}
