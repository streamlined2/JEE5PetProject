package com.project.inspection.property;

import com.project.inspection.EntityInfo;
import com.project.inspection.PropertyInfo;

public class EntityCollectionPropertyInfo extends PropertyInfo {
	
	private ForeignKeyPropertyInfo mappedByForeignKey;

	public EntityCollectionPropertyInfo(
			EntityInfo eInfo, String propertyName,
			Class<?> type, 
			String readMethod, String writeMethod, ForeignKeyPropertyInfo mappedByForeignKey) {
		super(eInfo, propertyName, type, readMethod, writeMethod);
		this.mappedByForeignKey = mappedByForeignKey;
	}

	public boolean isEntityCollection(){
		return true;
	}

	public ForeignKeyPropertyInfo getMappedByForeignKey() {
		return mappedByForeignKey;
	}

}