package com.project;

import java.beans.IntrospectionException;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import com.project.entities.Customer;
import com.project.entities.EntityType;
import com.project.inspection.EntityInfo;
import com.project.inspection.Filter;
import com.project.inspection.Ordering;
import com.project.inspection.PropertyList;
import com.project.inspection.EntityInfo.EntityData;
import com.project.queries.EntityDataSource;
import com.project.queries.QueryDefinition;

@Remote
public interface AgentRemote {

	public <T extends EntityType> T createEntity(T entity);
	public <T extends EntityType> T createUpdateEntity(T entity, boolean createNew);
	public <T extends EntityType> T updateEntity(T entity);
	public void removeEntity(EntityType entity);
	
	public List<EntityData> runQuery(QueryDefinition queryDefinition);

	public List<EntityData> fetchEntities(EntityDataSource dataSource) throws IntrospectionException;
	public EntityData fetchEntity(EntityDataSource dataSource, Object primaryKey);
	
	public int getColumnSize(Class<?> type,String fieldName);

}
