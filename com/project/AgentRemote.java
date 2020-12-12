package com.project;

import java.util.List;

import javax.ejb.Remote;

import com.project.entities.EntityType;
import com.project.inspection.EntityInfo.EntityData;
import com.project.interfacebuilder.InterfaceException;
import com.project.datasource.EntityDataSource;
import com.project.queries.QueryDefinition;

@Remote //business interface for stateless session bean
public interface AgentRemote {

	// generic methods to operate with entities 
	public <T extends EntityType> T createEntity(T entity);
	public <T extends EntityType> T createUpdateEntity(T entity, boolean createNew);
	public <T extends EntityType> T updateEntity(T entity);
	public void removeEntity(EntityType entity);
	
	//run query or fetch entities and get result set
	public List<EntityData> runQuery(QueryDefinition queryDefinition) throws InterfaceException;
	public List<EntityData> fetchEntities(EntityDataSource dataSource) throws InterfaceException;
	public EntityData fetchEntity(EntityDataSource dataSource, Object primaryKey) throws InterfaceException;
	
	//determine field size by means of EntityManager extensions
	public int getColumnSize(Class<?> type,String fieldName);

}
