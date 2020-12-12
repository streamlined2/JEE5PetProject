package com.project.datasource;

import java.util.List;

import javax.naming.NamingException;

import com.project.Startup;
import com.project.inspection.EntityInfo;
import com.project.inspection.EntityInfo.EntityData;
import com.project.inspection.PropertyList;
import com.project.inspection.property.InformationPropertyInfo;
import com.project.inspection.property.PrimaryKeyPropertyInfo;
import com.project.interfacebuilder.InterfaceException;

//descendant classes (EntityDataSource, QueryDataSource) overrides virtual methods of ancestor (DataSource) to pinpoint default or blank behavior
public class EntityDataSource extends DataSource {
	
	private EntityInfo entityInfo;
	
	public EntityDataSource(EntityInfo eInfo){
		entityInfo = eInfo;
	}

	public EntityInfo getEntityInfo() {
		return entityInfo;
	}

	@Override
	public List<EntityData> get() throws InterfaceException {
		return Startup.getAgent().fetchEntities(this);
	}

	@Override
	public String getDisplayName() {
		return getEntityInfo().getDisplayName();
	}

	@Override
	public List<InformationPropertyInfo> getInformationProperties() {
		return entityInfo.getInfoFields();
	}
	
	public PrimaryKeyPropertyInfo getPrimaryKeyPropertyInfo(){
		return entityInfo.getPrimaryKeyInfo();
	}

	@Override
	public PropertyList getDefaultPropertyList() {
		return new PropertyList(entityInfo);
	}

	@Override
	public boolean isModifiable() {
		return true;
	}

}
