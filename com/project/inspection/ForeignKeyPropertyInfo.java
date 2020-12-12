package com.project.inspection;

public class ForeignKeyPropertyInfo extends PropertyInfo {
	
	private EntityInfo masterEntity;

	public ForeignKeyPropertyInfo(EntityInfo eInfo, String propertyName,
			Class<?> type, String readMethod, String writeMethod, EntityInfo masterEntity) {
		super(eInfo, propertyName, type, readMethod, writeMethod);
		this.masterEntity = masterEntity;
	}

	public EntityInfo getMasterEntity() {
		return masterEntity;
	}

	public void setMasterEntity(EntityInfo masterEntity) {
		this.masterEntity = masterEntity;
	}

}
