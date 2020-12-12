package com.project;

import java.util.List;
import java.util.Locale;

import javax.ejb.Remote;

import com.project.datasource.EntityDataSource;
import com.project.entities.EntityType;
import com.project.inspection.EntityInfo.EntityData;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.forms.HTTPForm;
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
	public EntityData fetchEntity(EntityDataSource dataSource, Object primaryKey, HTTPForm form) throws InterfaceException;
	
	public String getUserInfo() throws InterfaceException;
	public String getCatalog() throws InterfaceException;
	
	public void initializeApplication();

	public int getFieldWidth(Class<?> entityType, Class<?> propertyType, String fieldName, Locale locale) throws InterfaceException;

}
