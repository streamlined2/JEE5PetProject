package com.project.inspection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.ResourceBundle;
import java.util.TreeMap;

import com.project.Helpers;
import com.project.interfacebuilder.SelectionViewItem;
import com.project.interfacebuilder.Selector;


public class EntityInfo implements Serializable, SelectionViewItem, Comparable<EntityInfo> {
	
	private static final long serialVersionUID = -2103351979119508358L;

	private Class<?> entityClass=null;
	private PrimaryKeyPropertyInfo primaryKeyInfo=null;
	
	private TreeMap<String,PropertyInfo> info=new TreeMap<String,PropertyInfo>();
	
	public EntityInfo(Class<?> cl){
		entityClass=cl;
	}
	
	public String getEntityName(){
		return entityClass.getSimpleName();
	}
	
	public String getDisplayName(){
		return getDisplayProperty("name");
	}

	public String getDescription(){
		return getDisplayProperty("desc");
	}
	
	private String getDisplayProperty(String propertyName) {
		return Helpers.getLocalizedDisplayName("EntityNamesBundle", getEntityName(), propertyName);
/*		String key=getEntityName()+"."+propertyName;
		ResourceBundle bundle=ListResourceBundle.getBundle(Helpers.getLocalizationBundleFullName("EntityNamesBundle"));
		String name=key;
		if(bundle!=null && bundle.containsKey(key)){
			name=bundle.getString(key);
		}
		return name;
*/	}
	
	public int getCount(){
		return info.size();
	}
	
	public Class<?> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

	public void addProperty(PropertyInfo pInfo){
		info.put(pInfo.getPropertyName(), pInfo);
		if(pInfo.isPrimaryKey()){
			primaryKeyInfo=(PrimaryKeyPropertyInfo)pInfo;
		}
	}
	
	public PropertyInfo getPropertyInfo(String name){
		return info.get(name);
	}
	
	public Collection<PropertyInfo> getProperties(){
		return info.values(); 
	}

	public PrimaryKeyPropertyInfo getPrimaryKeyInfo() {
		return primaryKeyInfo;
	}
	
	public List<InformationPropertyInfo> getInfoFields(){
		List<InformationPropertyInfo> list=new ArrayList<InformationPropertyInfo>();
		for(PropertyInfo item:info.values()){
			if(item.isInformation()){
				list.add((InformationPropertyInfo)item);
			}
		}
		return list;
	}

	public void setSelector(InformationPropertyInfo propertyInfo, Selector selector) {
		propertyInfo.setSelector(selector);
		PropertyInfo pInfo=getPropertyInfo(propertyInfo.getPropertyName());
		if(pInfo.isInformation()){
			((InformationPropertyInfo)pInfo).setSelector(selector);
		}
		info.put(pInfo.getPropertyName(), pInfo);
	}
	
	public static class EntityData implements Serializable {

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
		
	}

	@Override
	public String getItemDescripion() {
		return getDescription();
	}

	@Override
	public String getItemName() {
		return getDisplayName();
	}

	@Override
	public String getItemID() {
		return getEntityName();
	}

	@Override
	public int compareTo(EntityInfo o) {
		return getItemName().compareTo(o.getItemName());
	}
	
	
}
