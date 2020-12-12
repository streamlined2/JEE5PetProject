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

import com.project.entities.EntityType;
import com.project.inspection.EntityInfo.EntityData;
import com.project.inspection.Filter;
import com.project.inspection.FilterItem;
import com.project.inspection.Ordering;
import com.project.inspection.OrderingItem;
import com.project.inspection.OrderingItem.SortOrderType;
import com.project.inspection.PropertyInfo;
import com.project.inspection.PropertyList;
import com.project.inspection.PropertyListItem;
import com.project.interfacebuilder.InterfaceException;
import com.project.queries.EntityDataSource;
import com.project.queries.QueryDefinition;

/**
 * Session Bean implementation class Agent
 */
@Stateless(mappedName = "Agent")
public class Agent implements AgentRemote {
	
	@PersistenceContext(unitName=ContextBootstrap.UNIT_NAME)
	private EntityManager manager;

	@Override
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

	@Override
	public void removeEntity(EntityType entity) throws EntityNotFoundException{
		EntityType managed=
				manager.getReference(entity.getClass(), entity.getId());
		manager.remove(managed);
	}

	public List<EntityData> runQuery(QueryDefinition queryDefinition) throws InterfaceException{
		Query query=manager.createQuery(queryDefinition.getStatement());
		
		@SuppressWarnings("unchecked")
		List<Object[]> list=(List<Object[]>)query.getResultList();
		
		return copyQueryResultToEntityData(list);
	}

	//TODO implement it with Criteria Builder (JPA2)
	@Override
	public List<EntityData> fetchEntities(EntityDataSource dataSource) throws InterfaceException{
		
		Query query=getQuery(dataSource);
		setFilterParameterValues(query,dataSource.getFilter());
		query.setFirstResult(dataSource.getRange().getStartFrom());
		query.setMaxResults(dataSource.getRange().getFinishAt()-dataSource.getRange().getStartFrom()+1);

		@SuppressWarnings("unchecked")
		List<Object[]> list=(List<Object[]>)query.getResultList();
		
		return copyQueryResultToEntityData(list);
	}
	
	private static List<EntityData> copyQueryResultToEntityData(List<Object[]> list){
		
		List<EntityData> result=new ArrayList<EntityData>();
		for(Object[] o:list){
			result.add(getEntityData(o,null));
		}
		
		return result;

	}

	private static EntityData getEntityData(Object[] o,PropertyList propertyList) {
		Object primaryKeyValue=o[0];
		Object[] infoFieldValues=Arrays.copyOfRange(o, 1, o.length);
		EntityData d=new EntityData(primaryKeyValue,infoFieldValues,propertyList);
		return d;
	}
	
	public EntityData fetchEntity(EntityDataSource dataSource, Object primaryKey) throws InterfaceException{

		Query query=getQuery(dataSource,primaryKey);

		return getEntityData((Object[]) query.getSingleResult(),new PropertyList(dataSource.getEntityInfo()));
	}


	private String getQueryString(EntityDataSource dataSource, Object primaryKey) throws InterfaceException{

		final String prefix="a";
		
		StringBuilder q=new StringBuilder();
		q.
			append("select ").
			append(getPKInfoFieldList(prefix,dataSource)).
			append(" from ").
			append(dataSource.getEntityInfo().getEntityName()).
			append(" ").
			append(prefix).
			append(" where ").
			append(prefix).
			append(".").
			append(dataSource.getEntityInfo().getPrimaryKeyInfo().getPropertyName()).
			append("=?1");
		
		return q.toString();
	}
	
	private Query getQuery(EntityDataSource dataSource, Object primaryKey) throws InterfaceException {
		
		Query query=manager.createQuery(getQueryString(dataSource,primaryKey));
		
		query.setParameter(1, primaryKey);

		return query;
	}

	private Query getQuery(
			EntityDataSource dataSource) throws InterfaceException{
		
		final String prefix="a";
		
		StringBuilder q=new StringBuilder();
		q.
			append("select ").
			append(getPKInfoFieldList(prefix,dataSource)).
			append(" from ").
			append(dataSource.getEntityInfo().getEntityName()).
			append(" ").
			append(prefix).
			append(" ").
			append(getFilterCondition(prefix,dataSource.getFilter())).
			append(" ").
			append(getOrderClause(prefix,dataSource.getOrdering()));
		
		Query query=manager.createQuery(q.toString());
		
		return query;
	}
	
	private String getOrderClause(String prefix, Ordering ordering) {
		final String LIST_SEPARATOR = ",";
		StringBuilder clause=new StringBuilder();
		if(formOrderClause(ordering)){
			clause.append("order by ");
			int itemIndex=1;
			for(OrderingItem item:ordering.getOrderedSet()){
				PropertyInfo pInfo=item.getPropertyInfo();
				
				clause.
					append(prefix).append(".").append(pInfo.getPropertyName());
				if(item.getSortOrderType()==SortOrderType.DESCENDING){
					clause.append(" ").append("desc");
				}
				if(itemIndex<ordering.size()){
					clause.append(LIST_SEPARATOR);
				}
				itemIndex++;
			}
			
		}
		return clause.toString();
	}

	private boolean formOrderClause(Ordering ordering){
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
	
	private boolean formFilterCondition(Filter filter){
		if(filter==null || filter.size()==0) return false;
		int validCount=0;
		for(FilterItem item:filter){
			if(getFilterKind(item)!=FilterKind.UNDEFINED) validCount++;
		}
		if(validCount==0) return false;
		return true;
	}

	private String getFilterCondition(String prefix, Filter filter) {
		StringBuilder condition=new StringBuilder();
		if(formFilterCondition(filter)){

			condition.append("where ");
			int itemIndex=1;
			int parameterIndex=1;
			
			for(FilterItem item:filter){
				
				PropertyInfo pInfo=item.getPropertyInfo();
				FilterKind kind=getFilterKind(item);
				
				switch(kind){
					case EQUALITY_BY_MIN_VALUE:
					case EQUALITY_BY_MAX_VALUE:
						condition.
							append(prefix).
							append(".").
							append(pInfo.getPropertyName()).
							append("=").
							append("?").
							append(parameterIndex++);
						break;
					case RANGE:
						condition.
							append(prefix).
							append(".").
							append(pInfo.getPropertyName()).
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
		}
		return condition.toString();
	}

	private void setFilterParameterValues(Query query,Filter filter) {

		int parameterIndex=1;
		
		if(formFilterCondition(filter)){

			for(FilterItem item:filter){
				FilterKind kind=getFilterKind(item);
				switch(kind){
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
		
		StringBuilder buffer=new StringBuilder();
		
		final String listSeparator=",";
		buffer.
			append(prefix).
			append(".").
			append(dataSource.getEntityInfo().getPrimaryKeyInfo().getPropertyName()).
			append(listSeparator);
		for(PropertyListItem i:dataSource.getPropertyList().getOrderedSet()){
			buffer.
				append(prefix).
				append(".").
				append(i.getPropertyInfo().getPropertyName()).
				append(listSeparator);
		}
		buffer.delete(buffer.length()-listSeparator.length(), buffer.length());
		
		return buffer;
	}
	
	//persistence provider-specific methods (OpenJPA)
	private DatabaseMetaData getDatabaseMetaData() throws SQLException{
		Connection con = getConnection();
		DatabaseMetaData metaData=con.getMetaData();
		return metaData;
	}
	
	private String getCatalogName() throws SQLException{
		Connection con = getConnection();
		String catalog=con.getCatalog();
		return catalog;
	}

/*	@Resource(name=ContextBootstrap.DATA_SOURCE)
	private DataSource dataSource;
	
*/	private Connection getConnection() throws SQLException {
		OpenJPAEntityManager em=OpenJPAPersistence.cast(manager);
		Connection con=(Connection)em.getConnection();
		return con;
		//return dataSource.getConnection();
	}
	
	private int getColumnSize(String tableName, String columnName) throws SQLException{
		DatabaseMetaData metaData=getDatabaseMetaData();
		String catalog=getCatalogName();
		ResultSet r=metaData.getColumns(catalog, null, tableName, columnName);
		while(r.next()){
			String fieldName=r.getString("COLUMN_NAME");
			if(fieldName.equalsIgnoreCase(columnName)){
				int fieldSize=r.getInt("COLUMN_SIZE");
				return fieldSize;
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
		Column column=fieldMapping.getColumns()[0];
		return column;
	}
	
	private String getColumnTable(Class<?> type,String fieldName){
		Column column = getColumn(type, fieldName);
		return column.getTable().getFullName().toUpperCase();
	}

	private String getColumnName(Class<?> type,String fieldName){
		Column column = getColumn(type, fieldName);
		return column.getName().toUpperCase();
	}

	public int getColumnSize(Class<?> type,String fieldName){
		try {
			String tableName = getColumnTable(type,fieldName);
			String columnName = getColumnName(type,fieldName);
			return getColumnSize(tableName, columnName);
		} catch (SQLException e) {
		} catch(NullPointerException e2){
		}
		return -1;
	}
	
}
