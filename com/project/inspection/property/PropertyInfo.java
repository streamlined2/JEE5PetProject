package com.project.inspection.property;

import java.io.Serializable;

import com.project.inspection.EntityInfo;

public abstract class PropertyInfo implements Serializable, Comparable<PropertyInfo> {
	
	private static final long serialVersionUID = 1301705573489615320L;

	public enum OrderType { ORDERED, UNORDERED };
	public enum FiniteType { FINITE, INFINITE };
	public enum AlignType { LEFT, CENTER, RIGHT };
	public enum MultipleType { SINGLE, MULTIPLE };
	
	private String propertyName;
	private Class<?> type;
	private String readMethod;
	private String writeMethod;
	
	private EntityInfo entityInfo;
	
	public PropertyInfo(EntityInfo eInfo,
			String propertyName, Class<?> type, 
			String readMethod, String writeMethod) {
		super();
		entityInfo = eInfo;
		this.propertyName = propertyName;
		this.type = type;
		this.readMethod = readMethod;
		this.writeMethod = writeMethod;
	}
	
	public EntityInfo getEntityInfo(){
		return entityInfo;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public Class<?> getType() {
		return type;
	}

	public String getReadMethod() {
		return readMethod;
	}

	public String getWriteMethod() {
		return writeMethod;
	}

	@Override
	public int compareTo(PropertyInfo o) {
		if(this.equals(o)) return 0;
		if(this.entityInfo.hashCode()>o.entityInfo.hashCode()) return 1;
		else if(this.entityInfo.hashCode()<o.entityInfo.hashCode()) return -1;
		else return this.propertyName.compareToIgnoreCase(o.propertyName); 
	}
	
	/* following methods will be overridden in descendants to distinguish different properties (instead of using instanceof operator) 
	 * */
	public boolean isPrimaryKey(){
		return false;
	}
	
	public boolean isForeignKey(){
		return false;
	}
	
	public boolean isInformation(){
		return false;
	}
	
	public boolean isEntityCollection(){
		return false;
	}

	@Override
	public int hashCode() {
		return entityInfo.hashCode()*31+propertyName.toUpperCase().hashCode();
	}

	@Override
	public boolean equals(Object another) {
		if(!(another instanceof PropertyInfo)) return false;
		PropertyInfo pInfo = (PropertyInfo) another;
		if(!entityInfo.equals(pInfo.entityInfo)) return false;
		if(!propertyName.equalsIgnoreCase(pInfo.propertyName)) return false;
		return true;
	}

}
