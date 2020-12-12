package com.project.queries;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.project.Helpers;
import com.project.entities.EntityType;
import com.project.inspection.EntityInfo;
import com.project.inspection.EntityInspector;
import com.project.inspection.PropertyInfo;
import com.project.inspection.property.ForeignKeyPropertyInfo;
import com.project.inspection.property.InformationPropertyInfo;
import com.project.inspection.property.PrimaryKeyPropertyInfo;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.SelectionViewItem;
import com.project.queries.QueryDefinition.FilterEntry;
import com.project.queries.QueryDefinition.OrderByEntry;
import com.project.queries.QueryDefinition.FilterEntry.Relation;
import com.project.queries.QueryDefinition.OrderByEntry.SortOrder;
import com.project.queries.QueryDefinition.Property;

public class QueryDefinition implements Serializable, SelectionViewItem, Comparable<QueryDefinition> {

	public enum GroupPolicy { DONT_GROUP, GROUP };
	public enum GroupOperation { COUNT, SUM, AVERAGE };
	
	private String name;
	private String description;
	private Map<String,InformationProperty> properties = new HashMap<String,InformationProperty>();
	private List<OrderByEntry> orderAliases = new ArrayList<OrderByEntry>();
	private Set<FilterEntry> filterEntries = new HashSet<FilterEntry>();
	private GroupPolicy groupPolicy = GroupPolicy.DONT_GROUP;
	private Set<GroupEntry> groupEntries = new HashSet<GroupEntry>(); 
	
	public AggregatedProperty getAggregatedProperty(GroupEntry entry) throws InterfaceException{
		InformationProperty dataProperty = new InformationProperty(entry.getDataPropertyEntityType(), entry.getDataPropertyAlias(), entry.getAlias());
		InformationPropertyInfo pInfo = dataProperty.getInformationProperty();
		if(pInfo!=null){
			switch(entry.getOperation()){
			case AVERAGE:
				return new AverageSumProperty(dataProperty,entry.getAlias());
			case COUNT:
				return new CountProperty(dataProperty,entry.getAlias());
			case SUM:
				return new TotalSumProperty(dataProperty,entry.getAlias());
			}
		}
		return null;
	}
	
	public abstract class Property implements Serializable {
		
		private String alias;
		
		protected Property(String alias){
			this.alias = alias;
		}

		public String getAlias() {
			return alias;
		}

		public abstract StringBuilder getPropertyExpression(Character alias);
		
	}
	
	public class InformationProperty extends Property {
		
		private String propertyName;
		private Class<? extends EntityType> entityClass;
		
		protected InformationProperty(Class<? extends EntityType> entityClass, String propertyName, String alias){
			super(alias);
			this.entityClass = entityClass;
			this.propertyName = propertyName;
		}

		public InformationPropertyInfo getInformationProperty() throws InterfaceException {
			EntityInfo entityInfo = EntityInspector.getEntityInfo(getEntityClass());
			PropertyInfo propertyInfo = null;
			if((propertyInfo = entityInfo.getInformationProperty(propertyName))!=null && propertyInfo.isInformation()){
				return (InformationPropertyInfo)propertyInfo;
			}else throw new InterfaceException("property "+propertyName+" not found for given entity "+entityInfo.getEntityName());
		}

		@Override
		public StringBuilder getPropertyExpression(Character alias) {
			return new StringBuilder()
				.append(alias)
				.append(".")
				.append(propertyName);
		}

		public Class<? extends EntityType> getEntityClass() {
			return entityClass;
		}
	}
	
	public abstract class AggregatedProperty extends Property {
		
		private InformationProperty dataProperty;
		
		protected AggregatedProperty(InformationProperty dataProperty, String alias) {
			super(alias);
			this.dataProperty = dataProperty;
		}
		
		protected InformationProperty getDataProperty(){
			return dataProperty;
		}

	}
	
	public class AverageSumProperty extends AggregatedProperty {
		
		public AverageSumProperty(InformationProperty dataProperty, String alias) {
			super(dataProperty,alias);
		}
		
		@Override
		public StringBuilder getPropertyExpression(Character alias) {
			return 
				new StringBuilder().
					append("avg(").
					append(alias).
					append(".").
					append(getDataProperty().propertyName).
					append(")");
		}
		
	}
	
	public class TotalSumProperty extends AggregatedProperty {
		
		public TotalSumProperty(InformationProperty dataProperty, String alias) {
			super(dataProperty,alias);
		}

		@Override
		public StringBuilder getPropertyExpression(Character alias) {
			return 
				new StringBuilder().
					append("sum(").
					append(alias).
					append(".").
					append(getDataProperty().propertyName).
					append(")");
		}
		
	}
	
	public class CountProperty extends AggregatedProperty {

		public CountProperty(InformationProperty dataProperty, String alias) {
			super(dataProperty,alias);
		}

		@Override
		public StringBuilder getPropertyExpression(Character alias) {
			return 
			new StringBuilder().
				append("count(").
				append(alias).
				append(".").
				append(getDataProperty().propertyName).
				append(")");
		}

	}

	public static final class PropertyEntry implements Serializable {
	
		private String propertyName;
		private Class<? extends EntityType> entityClass;
		private String alias;
		
		public PropertyEntry(Class<? extends EntityType> entityClass,
				String propertyName) {
			this(entityClass,propertyName,entityClass.getSimpleName()+"."+propertyName);
		}
		
		public PropertyEntry(Class<? extends EntityType> entityClass,
				String propertyName, String alias) {
			super();
			this.entityClass = entityClass;
			this.propertyName = propertyName;
			this.alias = alias;
		}

		public String getPropertyName() {
			return propertyName;
		}

		public Class<? extends EntityType> getEntityClass() {
			return entityClass;
		}

		public String getAlias() {
			return alias;
		}
		
	}
	
	public static final class OrderByEntry implements Serializable {
		
		public enum SortOrder {ASCENDING, DESCENDING};
		
		private String alias;
		private SortOrder order;
		
		public OrderByEntry(String alias,SortOrder order){
			this.alias = alias;
			this.order = order; 
		}
		
		public OrderByEntry(String alias){
			this.alias = alias;
			this.order = SortOrder.ASCENDING; 
		}
		
		public String getAlias(){ return alias;}
		
		public SortOrder getSortOrder() { return order;}
		
	}
	
	public static final class FilterEntry implements Serializable {
	
		public enum Relation { EQUAL_TO, LESS_THAN, GREATER_THAN, LESS_OR_EQUAL, GREATER_OR_EQUAL, CHOICE };
		
		private String alias;
		private Relation relation;
		private Object operand;
		
		public FilterEntry(String alias, Relation relation, Object operand) {
			super();
			this.alias = alias;
			this.relation = relation;
			this.operand = operand;
		}

		public String getAlias() {
			return alias;
		}

		public Relation getRelation() {
			return relation;
		}

		public Object getOperand() {
			return operand;
		}
		
	}
	
	public static final class GroupEntry implements Serializable {
		
		private Class<? extends EntityType> dataPropertyEntityType;
		private String dataPropertyAlias;
		private GroupOperation operation;
		private String alias;
		
		public GroupEntry(Class<? extends EntityType> dataPropertyEntityType,String dataPropertyAlias,GroupOperation operation,String alias){
			this.dataPropertyEntityType = dataPropertyEntityType;
			this.dataPropertyAlias = dataPropertyAlias;
			this.operation = operation;
			this.alias = alias;
		}

		public Class<? extends EntityType> getDataPropertyEntityType() {
			return dataPropertyEntityType;
		}

		public String getDataPropertyAlias() {
			return dataPropertyAlias;
		}

		public GroupOperation getOperation() {
			return operation;
		}

		public String getAlias() {
			return alias;
		}
		
	
	}
	
	public QueryDefinition(
			String name,
			String description,
			PropertyEntry[] properties
			) throws InterfaceException{
	
		this(name, description, properties, null);
			
	}
		
	public QueryDefinition(
			String name,
			String description,
			PropertyEntry[] properties,
			FilterEntry[] filterEntries
			) throws InterfaceException{
	
		this(name, description, properties, filterEntries, null);
			
	}
		
	public QueryDefinition(
			String name,
			String description,
			PropertyEntry[] properties,
			FilterEntry[] filterEntries,
			OrderByEntry[] orderAliases
			) throws InterfaceException{
		
		this(name, description, properties, filterEntries, orderAliases, GroupPolicy.DONT_GROUP);

	}
	
	public QueryDefinition(
			String name,
			String description,
			PropertyEntry[] properties,
			FilterEntry[] filterEntries,
			OrderByEntry[] orderAliases,
			GroupPolicy groupPolicy
			) throws InterfaceException{

		this(name, description, properties, filterEntries, orderAliases, groupPolicy, null);

	}

	public QueryDefinition(
			String name,
			String description,
			PropertyEntry[] properties,
			FilterEntry[] filterEntries,
			OrderByEntry[] orderAliases,
			GroupPolicy groupPolicy,
			GroupEntry[] groupEntries
			) throws InterfaceException{

		this.name = name;
		
		this.description = description;
		
		if(properties!=null) 
			for(PropertyEntry property:properties) 
				this.properties.put(
					property.getAlias(), 
					new InformationProperty(property.getEntityClass(), property.getPropertyName(), property.getAlias()));
		
		if(filterEntries!=null) 
			this.filterEntries.addAll(Arrays.asList(filterEntries));
		
		if(orderAliases!=null) 
			this.orderAliases.addAll(Arrays.asList(orderAliases));
		
		this.groupPolicy = groupPolicy;
		
		if(groupEntries!=null) 
			this.groupEntries.addAll(Arrays.asList(groupEntries));
		
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getStatement() throws InterfaceException {
		
		final String separator = ",";

		List<InformationPropertyInfo> propertyInfos = getInfoProperties();
		
		StringBuilder statement = new StringBuilder().append("select ");
		
		SortedMap<EntityInfo, Character> entities = getQueryEntityList(propertyInfos);
		
		addQueryPropertyList(statement, entities, separator);
		
		addQueryFromClause(statement, entities, separator);
		
		addFilterClause(statement, entities, separator);
		
		addGroupByClause(statement, entities, separator);
		
		addQueryOrderByClause(statement, entities, separator);
		
		return statement.toString();
	}

	private void addGroupByClause(StringBuilder statement,
			SortedMap<EntityInfo, Character> entities, String separator) throws InterfaceException {
		
		if(!properties.isEmpty() && groupPolicy == GroupPolicy.GROUP){

			statement.append(" group by ");

			for(InformationProperty property:properties.values()){
			
				addFieldDesc(statement, property, separator, getEntityAlias(entities, property.getInformationProperty()));
			
			}

			removeLastSeparator(statement, separator);
		
		}

	}

	private void addFilterClause(StringBuilder statement,
			SortedMap<EntityInfo, Character> entities, String listSeparator) throws InterfaceException {
		
		final String CONDITION_SEPARATOR = " and ";
		
		if(!filterEntries.isEmpty()){
			
			statement.append(" where ");
			
			for(FilterEntry entry:filterEntries){
				InformationProperty property = properties.get(entry.getAlias());
				if(property!=null){
					InformationPropertyInfo pInfo = property.getInformationProperty();
					addFilterEntryDesc(
							statement, 
							pInfo.getPropertyName(), 
							entry.getRelation(), 
							entry.getOperand(), 
							CONDITION_SEPARATOR,
							listSeparator,
							getEntityAlias(entities, pInfo)
					);
				}
			}
			removeLastSeparator(statement, CONDITION_SEPARATOR);
			
		}
		
	}

	public List<InformationPropertyInfo> getInfoProperties() throws InterfaceException {
		List<InformationPropertyInfo> propertyList = new LinkedList<InformationPropertyInfo>();
		for(InformationProperty property:properties.values()){
			propertyList.add(property.getInformationProperty());
		}
		return propertyList;
	}

	private void addQueryOrderByClause(
			StringBuilder statement,
			SortedMap<EntityInfo, Character> entities, 
			final String separator) throws InterfaceException {
		
		if(!orderAliases.isEmpty()){
			statement.append(" order by ");
			for(OrderByEntry entry:orderAliases){
				InformationProperty property = properties.get(entry.getAlias());
				if(property!=null) {
					InformationPropertyInfo informationProperty = property.getInformationProperty();
					addOrderEntryDesc(
							statement,
							informationProperty.getPropertyName(),
							entry.getSortOrder(),
							separator,
							getEntityAlias(entities, informationProperty));
				}
			}
			removeLastSeparator(statement, separator);
		}
	
	}
	
	private void addQueryFromClause(
			StringBuilder statement,
			SortedMap<EntityInfo, Character> entities, 
			final String separator) throws InterfaceException {

		statement.append(" from ");
		
		SortedMap<EntityInfo.LinkCountKey,EntityInfo> countKeys = EntityInfo.getLinkCountKeys(entities.keySet());
		
		for(Map.Entry<EntityInfo.LinkCountKey, EntityInfo> entry:countKeys.entrySet()){
			
			EntityInfo.LinkCountKey key = entry.getKey();
			EntityInfo entity = entry.getValue();
			
			if(key.getCount()==0){
				
				statement
					.append(entity.getEntityName())
					.append(" as ")
					.append(getEntityAlias(entities, entity))
					.append(separator);
			
			}else{
				
				PrimaryKeyPropertyInfo primaryKey = entity.getPrimaryKeyInfo();

				for(ForeignKeyPropertyInfo foreignKey:key.getLinks()){
					
					statement
						.append(getKeyExpression(
							primaryKey,
							foreignKey, 
							getEntityAlias(entities, foreignKey), 
							getEntityAlias(entities, primaryKey)))
						.append(separator);
					
				}
				
			}
			
			removeLastSeparator(statement, separator);
			
		}
		
	}

	public StringBuilder getKeyExpression(
			PropertyInfo primaryKeyProperty,
			PropertyInfo foreignKeyProperty, 
			Character slaveAlias, 
			Character masterAlias) throws InterfaceException{
		
		StringBuilder expression = new StringBuilder();
		expression.append(" ").append("inner join").append(" ");
		expression.
			append(slaveAlias).append(".").append(foreignKeyProperty.getPropertyName()).
			append(" as ").append(masterAlias);

		return expression;
	}
	
	private void addQueryPropertyList(
			StringBuilder statement,
			SortedMap<EntityInfo, Character> entities,
			final String separator) throws InterfaceException {
		int k = 0;
		for(InformationProperty property:properties.values()){
			InformationPropertyInfo pInfo = property.getInformationProperty();
			if(++k == 1 && groupPolicy == GroupPolicy.DONT_GROUP){
				addPrimaryKeyFieldDesc(statement, pInfo.getEntityInfo(), separator, getEntityAlias(entities, pInfo));
			}
			addFieldDesc(statement, property, separator, getEntityAlias(entities, pInfo));
		}
		for(GroupEntry entry:groupEntries){
			AggregatedProperty property = getAggregatedProperty(entry);
			if(property!=null && property.getDataProperty()!=null){
				statement.
				append(
					property.getPropertyExpression(
						getEntityAlias(entities, property.getDataProperty().getInformationProperty()))).
				append(separator);
			}
		}
		removeLastSeparator(statement, separator);
	}
	
	private final static Character PRIMARY_ENTITY_ALIAS = 'A';

	private SortedMap<EntityInfo, Character> getQueryEntityList(List<InformationPropertyInfo> propertyInfos) {
		SortedMap<EntityInfo,Character> entities = new TreeMap<EntityInfo,Character>();
		Character alias = PRIMARY_ENTITY_ALIAS;
		for(InformationPropertyInfo property:propertyInfos){
			if(!entities.containsKey(property.getEntityInfo())){
				entities.put(property.getEntityInfo(),alias++);
			}
		}
		return entities;
	}

	private Character getEntityAlias(
			Map<EntityInfo, Character> entities,
			PropertyInfo property) throws InterfaceException {
		if(entities.containsKey(property.getEntityInfo())) return entities.get(property.getEntityInfo());
		else throw new InterfaceException("alias not found for entity "+property.getEntityInfo().getEntityName());
	}
	
	private Character getEntityAlias(
			Map<EntityInfo, Character> entities,
			EntityInfo entity) throws InterfaceException {
		if(entities.containsKey(entity)) return entities.get(entity);
		else throw new InterfaceException("alias not found for entity "+entity.getEntityName());
	}
	
	private void addPrimaryKeyFieldDesc(
			StringBuilder statement,
			EntityInfo entity,
			String separator, Character alias){
		statement
			.append(alias)
			.append(".")
			.append(entity.getPrimaryKeyInfo().getPropertyName())
			.append(separator);
	}

	private void addFieldDesc(
			StringBuilder statement,
			Property property,
			String separator, Character alias){
		statement
			.append(property.getPropertyExpression(alias))
			.append(separator);
	}

	private void addOrderEntryDesc(
			StringBuilder statement,
			String propertyName,
			SortOrder order,
			String separator,
			Character alias){
		statement
			.append(alias)
			.append('.')
			.append(propertyName);
		if(order==SortOrder.DESCENDING) statement.append(" desc");
		statement.append(separator);
	}

	private void addFilterEntryDesc(
			StringBuilder statement,
			String propertyName,
			Relation relation,
			Object operand,
			String separator,
			String listSeparator, Character alias){
		
		statement
			.append(alias)
			.append('.')
			.append(propertyName);
		
		if(relation!=Relation.CHOICE){
			
			switch (relation) {
			case EQUAL_TO:
				statement.append("=");
				break;
			case GREATER_OR_EQUAL:
				statement.append(">=");
				break;
			case GREATER_THAN:
				statement.append(">");
				break;
			case LESS_OR_EQUAL:
				statement.append("<=");
				break;
			case LESS_THAN:
				statement.append("<");
				break;
			}
			
			statement.append(EntityInspector.convertToDecoratedString(operand));
			
		}else{
			
			statement.append(" in (");
			
			if(operand.getClass().isArray()){
				
				for(int index=0;index<Array.getLength(operand);index++){
					statement.append(EntityInspector.convertToDecoratedString(Array.get(operand, index))).append(listSeparator);
				}
				removeLastSeparator(statement, listSeparator);
				
			}else{
				statement.append(EntityInspector.convertToDecoratedString(operand));
			}
			
			statement.append(")");
			
		}
		
		statement.append(separator);
	}

	private void removeLastSeparator(StringBuilder statement, String separator) {
		statement.delete(statement.length()-separator.length(), statement.length());
	}

	@Override
	public String getItemID() {
		return getName();
	}

	@Override
	public String getItemName() {
		return Helpers.getLocalizedDisplayName("MenuItemNamesBundle", name, "name");
	}
	
	@Override
	public String getItemDescripion() {
		return Helpers.getLocalizedDisplayName("MenuItemNamesBundle", name, "desc");
	}

	@Override
	public int compareTo(QueryDefinition o) {
		return getItemName().compareTo(o.getItemName());
	}

}
