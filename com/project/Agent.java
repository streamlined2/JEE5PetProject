package com.project;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.jdbc.meta.ClassMapping;
import org.apache.openjpa.jdbc.meta.FieldMapping;
import org.apache.openjpa.jdbc.meta.MappingRepository;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.meta.MetaDataRepository;
import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.OpenJPAEntityManagerSPI;
import org.apache.openjpa.persistence.OpenJPAPersistence;

import com.project.Startup;
import com.project.datasource.EntityDataSource;
import com.project.entities.EntityType;
import com.project.inspection.EntityInfo.EntityData;
import com.project.inspection.Filter;
import com.project.inspection.FilterItem;
import com.project.inspection.ListItem;
import com.project.inspection.Ordering;
import com.project.inspection.OrderingItem;
import com.project.inspection.OrderingItem.SortOrderType;
import com.project.inspection.property.PropertyInfo;
import com.project.inspection.PropertyList;
import com.project.interfacebuilder.InterfaceException;
import com.project.queries.QueryDefinition;

/**
 * Session Bean implementation class Agent
 */
@Stateless(mappedName = "Agent")
public class Agent implements AgentRemote {
	
	@PersistenceContext(unitName=Startup.UNIT_NAME)
	private EntityManager manager;

	@Override /* impose restrictions on generic type parameter: it must be a descendant of EntityType to be accessible by identifier */
	public <T extends EntityType> T createEntity(T entity){
		manager.persist(entity);
		return entity;
	}
	
	@Override
	public <T extends EntityType> T updateEntity(T entity){
		return manager.merge(entity);
	}
	
	@Override
	public <T extends EntityType> T createUpdateEntity(T entity, boolean createNew){
		if(createNew){
			entity=createEntity(entity);
		}else{
			entity=updateEntity(entity);
		}
		return entity;
	}

	@Override //retrieve entity ID by calling EntityType.getId method
	public void removeEntity(EntityType entity) throws EntityNotFoundException{
		EntityType managed=
				manager.getReference(entity.getClass(), entity.getId());
		manager.remove(managed);
	}

	// get QL statement from query definition, run it, and return result as list of entities  
	@Override
	public List<EntityData> runQuery(QueryDefinition queryDefinition) throws InterfaceException{
		Query query=manager.createQuery(queryDefinition.getStatement());
		
		@SuppressWarnings("unchecked")
		List<Object[]> list=(List<Object[]>)query.getResultList();
		
		return copyQueryResultToEntityData(list,queryDefinition.hasPrimaryKey());
	}

	@Override
	// fetch entity list data for given EntityDataSource instance 
	//TODO redo using Criteria Builder
	public List<EntityData> fetchEntities(EntityDataSource dataSource) throws InterfaceException{
		
		Query query=prepareQueryForEntityDataSource(dataSource);
		setFilterParameterValues(query,dataSource.getFilter());
		query.setFirstResult(dataSource.getRange().getStartFrom());
		query.setMaxResults(dataSource.getRange().getFinishAt()-dataSource.getRange().getStartFrom()+1);

		@SuppressWarnings("unchecked")
		List<Object[]> list=(List<Object[]>)query.getResultList();
		
		return copyQueryResultToEntityData(list,true);
	}
	
	private static List<EntityData> copyQueryResultToEntityData(List<Object[]> list, boolean hasPrimaryKey){
		
		List<EntityData> result=new ArrayList<EntityData>();
		for(Object[] o:list){
			result.add(getEntityData(o,null,hasPrimaryKey));
		}
		
		return result;

	}
	
	private static EntityData getEntityData(Object[] o,PropertyList propertyList, boolean hasPrimaryKey) {
		Object primaryKeyValue;
		Object[] infoFieldValues;
		if(hasPrimaryKey) {
			primaryKeyValue = o[0];
			infoFieldValues = Arrays.copyOfRange(o, 1, o.length);
		}else{
			primaryKeyValue = null;
			infoFieldValues = Arrays.copyOfRange(o, 0, o.length);
		}
		EntityData d=new EntityData(primaryKeyValue,infoFieldValues,propertyList);
		return d;
	}

	@Override //fetch single entity by primary key
	public EntityData fetchEntity(EntityDataSource dataSource, Object primaryKey) throws InterfaceException{

		Query query=prepareQueryForSingleEntity(dataSource,primaryKey);

		return getEntityData((Object[])query.getSingleResult(),new PropertyList(dataSource.getEntityInfo()),true);
	}


	private String getQueryStatementForSingleEntity(EntityDataSource dataSource, Object primaryKey) throws InterfaceException{

		final String tablePrefix="a";
		
		return new StringBuilder().
			append("select ").
			append(getPKInfoFieldList(tablePrefix,dataSource)).
			append(" from ").
			append(dataSource.getEntityInfo().getEntityName()).
			append(" ").
			append(tablePrefix).
			append(" where ").
			append(tablePrefix).
			append(".").
			append(dataSource.getEntityInfo().getPrimaryKeyInfo().getPropertyName()).
			append("=?1").
		toString();
	}
	
	private Query prepareQueryForSingleEntity(EntityDataSource dataSource, Object primaryKey) throws InterfaceException {
		
		Query query=manager.createQuery(getQueryStatementForSingleEntity(dataSource,primaryKey));
		
		query.setParameter(1, primaryKey);

		return query;
	}

	private Query prepareQueryForEntityDataSource(
			EntityDataSource dataSource) throws InterfaceException{
		
		final String tablePrefix="a";
		
		StringBuilder q=new StringBuilder().
			append("select ").
			append(getPKInfoFieldList(tablePrefix,dataSource)).
			append(" from ").
			append(dataSource.getEntityInfo().getEntityName()).
			append(" ").
			append(tablePrefix).
			append(" ").
			append(getFilterCondition(tablePrefix,dataSource.getFilter())).
			append(" ").
			append(getQueryOrderClause(tablePrefix,dataSource.getOrdering()));
		
		Query query=manager.createQuery(q.toString());
		
		return query;
	}
	
	private String getQueryOrderClause(
			String prefix, Ordering ordering) throws InterfaceException {
		
		final String listSeparator = ",";
		
		if(doFormOrderClause(ordering)){
			
			StringBuilder clause=new StringBuilder("order by ");
			
			int itemIndex=1;
			for(OrderingItem item:ordering.getOrderedSet()){// foreach loop: get rid of container implementation dependency 
				clause.
					append(prefix).append(".").append(item.getPropertyInfo().getPropertyName());
				if(item.getSortOrderType()==SortOrderType.DESCENDING){
					clause.append(" ").append("desc");
				}
				if(itemIndex<ordering.size()){
					clause.append(listSeparator);
				}
				itemIndex++;
			}
			return clause.toString();
		}
		return "";
	}

	private boolean doFormOrderClause(Ordering ordering){
		if(ordering==null || ordering.size()==0) return false;
		return true;
	}

	private enum FilterKind { 
		UNDEFINED, EQUALITY_BY_MIN_VALUE, EQUALITY_BY_MAX_VALUE, RANGE };
	
	@SuppressWarnings("unchecked")
	private FilterKind getFilterKind(FilterItem item){
		//sentinel conditional clauses
		if(item==null || item.getMinValue()==null && item.getMaxValue()==null) return FilterKind.UNDEFINED;
		if(item.getMinValue()==null) return FilterKind.EQUALITY_BY_MAX_VALUE;
		if(item.getMaxValue()==null) return FilterKind.EQUALITY_BY_MIN_VALUE;
		if(item.getMinValue().equals(item.getMaxValue())) return FilterKind.EQUALITY_BY_MIN_VALUE;
		if(item.getMinValue() instanceof Comparable && item.getMaxValue() instanceof Comparable){
			Comparable<?> min=(Comparable<?>)item.getMinValue();
			Comparable<Comparable<?>> max=(Comparable<Comparable<?>>)item.getMaxValue();
			if(max.compareTo(min)<0) return FilterKind.EQUALITY_BY_MIN_VALUE;  
		}
		return FilterKind.RANGE; 
	}
	
	private boolean doFormFilterCondition(Filter filter){
		if(filter==null || filter.size()==0) return false;
		int validCount=0;
		for(FilterItem item:filter){//walk through Iterable-supporting container
			if(getFilterKind(item)!=FilterKind.UNDEFINED) validCount++;
		}
		if(validCount==0) return false;
		return true;
	}

	private String getFilterCondition(String prefix, Filter filter) throws InterfaceException {

		if(doFormFilterCondition(filter)){

			StringBuilder condition=new StringBuilder("where ");

			int itemIndex=1;
			int parameterIndex=1;
			
			for(FilterItem item:filter){
				
				switch(getFilterKind(item)){
					
					case EQUALITY_BY_MIN_VALUE:
					case EQUALITY_BY_MAX_VALUE:
						condition.
							append(prefix).
							append(".").
							append(item.getPropertyInfo().getPropertyName()).
							append("=").
							append("?").
							append(parameterIndex++);
						break;
					
					case RANGE:
						condition.
							append(prefix).
							append(".").
							append(item.getPropertyInfo().getPropertyName()).
							append(" between ").
							append("?").
							append(parameterIndex++).
							append(" and ").
							append("?").
							append(parameterIndex++);
						break;
					
					default:
				}
				
				if(itemIndex<filter.size()){
					condition.append(" ").append("and").append(" ");
				}
				
				itemIndex++;
			}
			return condition.toString();
		}
		
		return "";
	}

	private void setFilterParameterValues(Query query,Filter filter) {

		int parameterIndex=1;
		
		if(doFormFilterCondition(filter)){

			for(FilterItem item:filter){
				switch(getFilterKind(item)){
				
				case EQUALITY_BY_MIN_VALUE:
					query.setParameter(parameterIndex++, item.getMinValue());
					break;
				
				case EQUALITY_BY_MAX_VALUE:
					query.setParameter(parameterIndex++, item.getMaxValue());
					break;
				
				case RANGE:
					query.setParameter(parameterIndex++, item.getMinValue());
					query.setParameter(parameterIndex++, item.getMaxValue());
					break;
				
				default:
				}
			}

		}

	}
	
	private StringBuilder getPKInfoFieldList(
			String prefix,EntityDataSource dataSource) throws InterfaceException{
		
		final String listSeparator=",";

		StringBuilder buffer=new StringBuilder().
			append(prefix).
			append(".").
			append(dataSource.getEntityInfo().getPrimaryKeyInfo().getPropertyName()).
			append(listSeparator);

		for(ListItem i:dataSource.getPropertyList().getOrderedSet()){
			buffer.
				append(prefix).
				append(".").
				append(i.getPropertyInfo().getPropertyName()).
				append(listSeparator);
		}

		buffer.delete(buffer.length()-listSeparator.length(), buffer.length());
		
		return buffer;
	}
	
	private DatabaseMetaData getDatabaseMetaData() throws SQLException{
		return getConnection().getMetaData();
	}
	
	private String getCatalogName() throws SQLException{
		return getConnection().getCatalog();
	}

/*	@Resource(name=Startup.DATA_SOURCE)
	private DataSource dataSource;
	
*/	
	//persistence provider-specific methods (currently for Apache OpenJPA)
	private Connection getConnection() throws SQLException {
		OpenJPAEntityManager em=OpenJPAPersistence.cast(manager);
		return (Connection)em.getConnection();
		//return dataSource.getConnection();
	}
	
	private int getColumnSize(String tableName, String columnName) throws SQLException{
		DatabaseMetaData metaData=getDatabaseMetaData();
		ResultSet r=metaData.getColumns(getCatalogName(), null, tableName, columnName);
		while(r.next()){
			if(r.getString("COLUMN_NAME").equalsIgnoreCase(columnName)){
				return r.getInt("COLUMN_SIZE");
			}
		};
		return -1;
	}
	
	private Column getColumn(Class<?> type, String fieldName) {
		OpenJPAEntityManager em=OpenJPAPersistence.cast(manager);
		OpenJPAEntityManagerSPI spi=(OpenJPAEntityManagerSPI)em;
		OpenJPAConfiguration conf=spi.getConfiguration();
		MetaDataRepository repository=conf.getMetaDataRepositoryInstance();
		MappingRepository mappingRepository=(MappingRepository)repository;
		ClassMapping classMapping=mappingRepository.getMapping(type, null, true);
		FieldMapping fieldMapping=classMapping.getFieldMapping(fieldName);
		return fieldMapping.getColumns()[0];
	}
	
	private String getColumnTable(Class<?> type,String fieldName){
		return getColumn(type, fieldName).getTable().getFullName().toUpperCase();
	}

	private String getColumnName(Class<?> type,String fieldName){
		return getColumn(type, fieldName).getName().toUpperCase();
	}

	@Override
	public int getColumnSize(Class<?> type,String fieldName){
		try {
			return getColumnSize(getColumnTable(type,fieldName), getColumnName(type,fieldName));
		} catch (SQLException e) {
		} catch(NullPointerException e2){
		}
		return -1;
	}
	
}
