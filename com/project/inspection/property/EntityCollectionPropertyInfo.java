package com.project.inspection.property;

import com.project.inspection.EntityInfo;

public class EntityCollectionPropertyInfo extends PropertyInfo {
	
	private static final long serialVersionUID = -7504906445376837444L;
	
	private ForeignKeyPropertyInfo mappedByForeignKey;// reference to supporting foreign key property on other side of relation

	public EntityCollectionPropertyInfo(
			EntityInfo eInfo, String propertyName,
			Class<?> type, 
			String readMethod, String writeMethod, ForeignKeyPropertyInfo mappedByForeignKey) {
		super(eInfo, propertyName, type, readMethod, writeMethod);
		this.mappedByForeignKey = mappedByForeignKey;
	}

	public boolean isEntityCollection(){// replace instanceof operator usage with virtual method call
		return true;
	}

	public ForeignKeyPropertyInfo getMappedByForeignKey() {
		return mappedByForeignKey;
	}

}
