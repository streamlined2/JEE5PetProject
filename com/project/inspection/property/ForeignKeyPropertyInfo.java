package com.project.inspection.property;

import com.project.entities.EntityType;
import com.project.inspection.EntityInfo;
import com.project.inspection.EntityInspector;
import com.project.inspection.PropertyInfo;
import com.project.interfacebuilder.InterfaceException;

public class ForeignKeyPropertyInfo extends PropertyInfo {
	
	private Class<? extends EntityType> masterType;
	private EntityInfo masterEntity;

	public ForeignKeyPropertyInfo(
			EntityInfo eInfo, String propertyName,
			Class<?> type, String readMethod, String writeMethod, 
			Class<? extends EntityType> masterType) {
		super(eInfo, propertyName, type, readMethod, writeMethod);
		this.masterType = masterType;
	}
	
	public Class<? extends EntityType> getMasterType(){
		return masterType;
	}

	public EntityInfo getMasterEntity() throws InterfaceException {
		if(masterEntity==null){
			masterEntity = EntityInspector.getEntityInfo(masterType);
		}
		return masterEntity;
	}

	@Override
	public boolean isForeignKey() {
		return true;
	}

}
