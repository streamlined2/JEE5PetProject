package com.project.queries;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

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
import com.project.queries.QueryDefinition.FilterEntry.Relation;
import com.project.queries.QueryDefinition.OrderByEntry.SortOrder;

public class QueryDefinition implements Serializable, SelectionViewItem, Comparable<QueryDefinition> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6281065723667190950L;

	public enum GroupPolicy { DONT_GROUP, GROUP };
	public enum GroupOperation { COUNT, TOTAL, AVERAGE };
	
	private String name;
	private String description;
	private Map<String,InformationProperty> properties = new HashMap<String,InformationProperty>();
	private List<OrderByEntry> orderAliases = new ArrayList<OrderByEntry>();
	private Set<FilterEntry> filterEntries = new HashSet<FilterEntry>();
	private GroupPolicy groupPolicy = GroupPolicy.DONT_GROUP;
	private Set<GroupEntry> groupEntries = new HashSet<GroupEntry>();
	private boolean hasPrimaryKey = true;
	
	public boolean hasPrimaryKey() {
		return hasPrimaryKey;
	}

	public AggregatedProperty getAggregatedProperty(GroupEntry entry,int order) throws InterfaceException{
		InformationProperty dataProperty = new InformationProperty(entry.getDataPropertyEntityType(), entry.getDataPropertyAlias(), entry.getAlias(), order);
		if(dataProperty.getInformationProperty()!=null){
			switch(entry.getOperation()){
			case AVERAGE:
				return new AverageSumProperty(dataProperty,entry.getAlias(),order);
			case COUNT:
				return new CountProperty(dataProperty,entry.getAlias(),order);
			case TOTAL:
				return new TotalSumProperty(dataProperty,entry.getAlias(),order);
			}
		}
		return null;
	}
	
	public abstract class Property implements Serializable, Comparable<Property> {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -3229399257410937276L;
		
		private String alias;
		protected int order;
		
		protected Property(String alias, int order){
			this.alias = alias;
			this.order = order;
		}

		public String getAlias() {
			return alias;
		}
		
		public int getOrder(){
			return order;
		}

		public abstract StringBuilder getPropertyExpression(Character alias);
		
		public abstract String getDisplayName() throws InterfaceException;
		
		public abstract InformationPropertyInfo getReferencedProperty() throws InterfaceException;
		
		@Override
		public int compareTo(Property other){
			return getOrder()-other.getOrder();
		}
		
	}
	
	public class InformationProperty extends Property {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -36199791544939390L;
		
		private String propertyName;
		private Class<? extends EntityType> entityClass;
		
		protected InformationProperty(Class<? extends EntityType> entityClass, String propertyName, String alias, int order){
			super(alias,order);
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

		@Override
		public String getDisplayName() throws InterfaceException {
			return getInformationProperty().getDisplayName();
		}

		@Override
		public InformationPropertyInfo getReferencedProperty() throws InterfaceException {
			return getInformationProperty();
		}
	}
	
	public abstract class AggregatedProperty extends Property {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 8290209885349421913L;
		
		private InformationProperty dataProperty;
		
		protected AggregatedProperty(InformationProperty dataProperty, String alias, int order) {
			super(alias,order);
			this.dataProperty = dataProperty;
		}
		
		protected InformationProperty getDataProperty(){
			return dataProperty;
		}
		
		public abstract GroupOperation getGroupOperation();

		@Override
		public String getDisplayName() throws InterfaceException {
			StringBuilder builder = new StringBuilder().
				append(Helpers.getLocalizedDisplayName("QueryPropertyPrefixBundle", "", getQueryPropertyPrefixKey(), getQueryPropertyPrefixKey())).
				append("(").
				append(getDataProperty().getInformationProperty().getDisplayName().toLowerCase()).
				append(")");
			return builder.toString(); 
		}

		private String getQueryPropertyPrefixKey() {
			return EntityInspector.convertToString(getGroupOperation());
		}
		
	}
	
	public class AverageSumProperty extends AggregatedProperty {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -5043913274803273260L;

		public AverageSumProperty(InformationProperty dataProperty, String alias, int order) {
			super(dataProperty,alias,order);
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

		@Override
		public InformationPropertyInfo getReferencedProperty() throws InterfaceException {
			return getDataProperty().getInformationProperty();
		}

		@Override
		public GroupOperation getGroupOperation() {
			return GroupOperation.AVERAGE;
		}
		
	}
	
	public class TotalSumProperty extends AggregatedProperty {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -2025243733642085329L;

		public TotalSumProperty(InformationProperty dataProperty, String alias, int order) {
			super(dataProperty,alias,order);
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
		
		@Override
		public InformationPropertyInfo getReferencedProperty() throws InterfaceException {
			return getDataProperty().getInformationProperty();
		}

		@Override
		public GroupOperation getGroupOperation() {
			return GroupOperation.TOTAL;
		}
		
	}
	
	public class CountProperty extends AggregatedProperty {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2689856981073582801L;

		public CountProperty(InformationProperty dataProperty, String alias, int order) {
			super(dataProperty,alias,order);
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

		@Override
		public InformationPropertyInfo getReferencedProperty() throws InterfaceException {
			return getDataProperty().getInformationProperty();
		}

		@Override
		public GroupOperation getGroupOperation() {
			return GroupOperation.COUNT;
		}
		
	}

	public static final class PropertyEntry implements Serializable {
	
		/**
		 * 
		 */
		private static final long serialVersionUID = 3819182058886911338L;
		
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
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 8034627485094186252L;

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
	
		/**
		 * 
		 */
		private static final long serialVersionUID = 691079013285763091L;

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
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -6155683174088768628L;
		
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
		
		if(properties!=null){
			int order = 1;
			for(PropertyEntry property:properties) 
				this.properties.put(
					property.getAlias(), 
					new InformationProperty(property.getEntityClass(), property.getPropertyName(), property.getAlias(), order++));
		} 
		
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

			for(InformationProperty property:getInformationPropertyList()){
			
				addFieldDesc(statement, property, separator, getEntityAlias(entities, property.getInformationProperty()));
			
			}

			removeLastSeparator(statement, separator);
			
			hasPrimaryKey = false;
		
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
		for(InformationProperty property:getInformationPropertyList()){
			propertyList.add(property.getInformationProperty());
		}
		return propertyList;
	}

	public List<Property> getInfoGroupProperties() throws InterfaceException {
		List<Property> propertyList = new LinkedList<Property>();
		propertyList.addAll(getInformationPropertyList());
		propertyList.addAll(getGroupPropertyList());
		return propertyList;
	}

	public SortedSet<InformationProperty> getInformationPropertyList() {
		return Collections.unmodifiableSortedSet(new TreeSet<InformationProperty>(properties.values()));
	}

	public List<InformationPropertyInfo> getGroupProperties() throws InterfaceException {
		List<InformationPropertyInfo> propertyList = new LinkedList<InformationPropertyInfo>();
		int order = 1;
		for(GroupEntry entry:groupEntries){
			AggregatedProperty property = getAggregatedProperty(entry,order);
			if(property!=null && property.getDataProperty()!=null){
				InformationProperty infoProperty = property.getDataProperty();
				propertyList.add(infoProperty.getInformationProperty());
			}
		}
		return propertyList;
	}

	public List<Property> getGroupPropertyList() throws InterfaceException {
		List<Property> propertyList = new LinkedList<Property>();
		int order = 1;
		for(GroupEntry entry:groupEntries){
			AggregatedProperty property = getAggregatedProperty(entry,order);
			if(property!=null){
				propertyList.add(property);
			}
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
		int order = 1;
		for(InformationProperty property:getInformationPropertyList()){
			InformationPropertyInfo pInfo = property.getInformationProperty();
			if(order == 1 && groupPolicy == GroupPolicy.DONT_GROUP){
				addPrimaryKeyFieldDesc(statement, pInfo.getEntityInfo(), separator, getEntityAlias(entities, pInfo));
			}
			addFieldDesc(statement, property, separator, getEntityAlias(entities, pInfo));
			order++;
		}
		for(GroupEntry entry:groupEntries){
			AggregatedProperty property = getAggregatedProperty(entry,order);
			if(property!=null && property.getDataProperty()!=null){
				statement.
				append(
					property.getPropertyExpression(
						getEntityAlias(entities, property.getDataProperty().getInformationProperty()))).
				append(separator);
			}
			order++;
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
