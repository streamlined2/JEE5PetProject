package com.project;

import java.util.List;

import javax.ejb.Remote;

import com.project.entities.EntityType;
import com.project.inspection.EntityInfo.EntityData;
import com.project.interfacebuilder.InterfaceException;
import com.project.queries.EntityDataSource;
import com.project.queries.QueryDefinition;

@Remote
public interface AgentRemote {

	public <T extends EntityType> T createEntity(T entity);
	public <T extends EntityType> T createUpdateEntity(T entity, boolean createNew);
	public <T extends EntityType> T updateEntity(T entity);
	public void removeEntity(EntityType entity);
	
	public List<EntityData> runQuery(QueryDefinition queryDefinition) throws InterfaceException;

	public List<EntityData> fetchEntities(EntityDataSource dataSource) throws InterfaceException;
	public EntityData fetchEntity(EntityDataSource dataSource, Object primaryKey) throws InterfaceException;
	
	public int getColumnSize(Class<?> type,String fieldName);

}
