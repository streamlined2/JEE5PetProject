package com.project.queries;

import java.beans.IntrospectionException;
import java.util.List;
import java.util.SortedSet;

import javax.naming.NamingException;

import com.project.AgentRemote;
import com.project.ContextBootstrap;
import com.project.inspection.EntityInfo;
import com.project.inspection.EntityInfo.EntityData;
import com.project.inspection.property.InformationPropertyInfo;
import com.project.inspection.property.PrimaryKeyPropertyInfo;
import com.project.inspection.PropertyList;
import com.project.interfacebuilder.InterfaceException;

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
		AgentRemote agent;
		try {
			agent = ContextBootstrap.getAgentReference(null);
			return agent.fetchEntities(this);
		} catch (NamingException e) {
			throw new InterfaceException(e);
		}
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

}
