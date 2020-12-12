package com.project.inspection;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.project.Helpers;
import com.project.entities.EntityType;
import com.project.inspection.property.EntityCollectionPropertyInfo;
import com.project.inspection.property.ForeignKeyPropertyInfo;
import com.project.inspection.property.InformationPropertyInfo;
import com.project.inspection.property.PrimaryKeyPropertyInfo;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.SelectionViewItem;
import com.project.interfacebuilder.Selector;

public class EntityInfo implements Serializable, SelectionViewItem, Comparable<EntityInfo> { 
	
	private static final long serialVersionUID = -2103351979119508358L;

	private Class<? extends EntityType> entityClass = null;
	private PrimaryKeyPropertyInfo primaryKey = null;
	private SortedMap<String,ForeignKeyPropertyInfo> foreignKeys = new TreeMap<String,ForeignKeyPropertyInfo>();
	private SortedMap<String,InformationPropertyInfo> infoProperties=new TreeMap<String,InformationPropertyInfo>();
	private SortedMap<String,EntityCollectionPropertyInfo> entityCollection = new TreeMap<String,EntityCollectionPropertyInfo>();
	
	public EntityInfo(Class<? extends EntityType> cl){
		entityClass=cl;
	}
	
	public String getEntityName(){
		return entityClass.getSimpleName();
	}
	
	public String getDisplayName(Locale locale){
		return getDisplayProperty(locale, "name");
	}

	public String getDescription(Locale locale){
		return getDisplayProperty(locale, "desc");
	}
	
	private String getDisplayProperty(Locale locale, String propertyName) {
		return Helpers.getLocalizedDisplayName("EntityNamesBundle", locale, getEntityName(), propertyName);
	}
	
	public int getInfoPropertyCount(){
		return infoProperties.size();
	}
	
	public InformationPropertyInfo getInformationProperty(String name){
		return infoProperties.get(name);
	}
	
	public Class<? extends EntityType> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<? extends EntityType> entityClass) {
		this.entityClass = entityClass;
	}

	public PrimaryKeyPropertyInfo getPrimaryKeyInfo() {
		return primaryKey;
	}
	
	public List<InformationPropertyInfo> getInfoFields(){
		List<InformationPropertyInfo> set=new LinkedList<InformationPropertyInfo>();
		for(InformationPropertyInfo item:infoProperties.values()){
			set.add((InformationPropertyInfo)item);
		}
		return set;
	}
	
	public SortedSet<ForeignKeyPropertyInfo> getForeignKeys(){
		SortedSet<ForeignKeyPropertyInfo> set = new TreeSet<ForeignKeyPropertyInfo>();
		for(ForeignKeyPropertyInfo foreignKey:foreignKeys.values()){
			set.add(foreignKey);
		}
		return set;
	}

	public void setSelector(InformationPropertyInfo propertyInfo, Selector selector) {
		propertyInfo.setSelector(selector);
		infoProperties.put(propertyInfo.getPropertyName(), propertyInfo);
	}
	
	public static class EntityData implements Serializable, SelectionViewItem {

		private static final long serialVersionUID = -2491192263978555306L;
		private Object primaryKey;
		private Object[] infoData;
		
		public EntityData(Object primaryKey, Object[] infoData, PropertyList propertyList){
			this.primaryKey = primaryKey;
			this.infoData = infoData;
		}

		public Object getPrimaryKey() {
			return primaryKey;
		}

		public Object[] getInfoData() {
			return infoData;
		}

		@Override
		public String getItemID() {
			return EntityInspector.convertToString(primaryKey);
		}

		@Override
		public String getItemName(Locale locale) {
			return getItemID();
		}

		@Override
		public String getItemDescripion(Locale locale) {
			return getItemID();
		}
		
	}

	@Override
	public String getItemDescripion(Locale locale) {
		return getDescription(locale);
	}

	@Override
	public String getItemName(Locale locale) {
		return getDisplayName(locale);
	}

	@Override
	public String getItemID() {
		return getEntityName();
	}

	@Override
	public int compareTo(EntityInfo o) {
		return getItemID().compareTo(o.getItemID());
	}
	
	@Override
	public boolean equals(Object another){
		if(!(another instanceof EntityInfo)) return false;
		return entityClass.getName().equals(((EntityInfo)another).entityClass.getName());
	}
	
	@Override
	public int hashCode(){
		return entityClass.getName().hashCode();
	}
	
	public void addInformationProperty(InformationPropertyInfo iProperty){
		infoProperties.put(iProperty.getPropertyName(), iProperty);
	}
	
	public void setPrimaryKeyProperty(PrimaryKeyPropertyInfo pKeyProperty){
		primaryKey=pKeyProperty;
	}
	
	public void addForeignKeyProperty(ForeignKeyPropertyInfo fKeyProperty){
		foreignKeys.put(fKeyProperty.getPropertyName(), fKeyProperty);
	}
	
	public ForeignKeyPropertyInfo getForeignKeyFor(String propertyName){
		return foreignKeys.get(propertyName);
	}
	
	public ForeignKeyPropertyInfo getForeignKeyFor(Class<? extends EntityType> masterType) {
		for(ForeignKeyPropertyInfo foreignKey:foreignKeys.values()){
			if(foreignKey.getMasterType().equals(masterType)) 
				return foreignKey;
		}
		return null;
	}
	
	public void addEntityCollectionProperty(EntityCollectionPropertyInfo eCProperty){
		entityCollection.put(eCProperty.getPropertyName(), eCProperty);
	}
	
	public Collection<EntityCollectionPropertyInfo> getEntityCollectionSet(){
		return Collections.unmodifiableCollection(entityCollection.values());
	}
	
	public class LinkCountKey implements Comparable<LinkCountKey> {

		private Set<ForeignKeyPropertyInfo> linkEntities = new HashSet<ForeignKeyPropertyInfo>();
		
		public LinkCountKey(){
		}
		
		public void addLink(ForeignKeyPropertyInfo foreignKey){
			linkEntities.add(foreignKey);
		}
		
		public Set<ForeignKeyPropertyInfo> getLinks(){
			return Collections.unmodifiableSet(linkEntities);
		}
		
		public int getCount() {
			return linkEntities.size();
		}

		@Override
		public boolean equals(Object obj){
			if(!(obj instanceof LinkCountKey)) return false;
			LinkCountKey key = (LinkCountKey) obj;
			return 
				getCount() == key.getCount() && 
				getEntityInfo().equals(key.getEntityInfo());
		}
		
		private EntityInfo getEntityInfo() {
			return EntityInfo.this;
		}

		@Override
		public int hashCode(){
			return getCount()*31+getEntityInfo().hashCode();
		}
		
		@Override
		public int compareTo(LinkCountKey key){
			if(getCount()<key.getCount()) return -1;
			else if(getCount()>key.getCount()) return 1;
			return 0;
		}
		
	}
	
	private static LinkCountKey getLinkCountKey(EntityInfo entityForKey, Collection<EntityInfo> collection, Locale locale) throws InterfaceException{
		
		LinkCountKey countKey = entityForKey.new LinkCountKey();
		
		for(EntityInfo entity:collection){
			if(!entity.equals(entityForKey)){
				for(ForeignKeyPropertyInfo foreignKey:entity.getForeignKeys()){
					if(foreignKey.getMasterEntity(locale).equals(entityForKey)) countKey.addLink(foreignKey);
				}
			}
		}
		
		return countKey;
	}
	
	public static SortedMap<LinkCountKey,EntityInfo> getLinkCountKeys(Collection<EntityInfo> collection,Locale locale) throws InterfaceException{
		SortedMap<LinkCountKey,EntityInfo> linkCountKeyMap = new TreeMap<LinkCountKey,EntityInfo>();
		for(EntityInfo entity:collection){
			linkCountKeyMap.put(getLinkCountKey(entity,collection,locale), entity);
		}
		return linkCountKeyMap;
	}

}
