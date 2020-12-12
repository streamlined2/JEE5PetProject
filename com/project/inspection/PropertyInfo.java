package com.project.inspection;

import java.io.Serializable;

import com.project.interfacebuilder.Selector;

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
		return propertyName.compareTo(o.propertyName);
	}
	
	public boolean isPrimaryKey(){
		return false;
	}
	
	public boolean isInformation(){
		return false;
	}

}
