package com.project.entities;

import java.beans.IntrospectionException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import com.project.inspection.EntityInfo;
import com.project.inspection.EntityInspector;
import com.project.interfacebuilder.InterfaceException;

public abstract class EntityClass implements EntityType {
	
	private static Set<Class<? extends EntityType>> entities=new HashSet<Class<? extends EntityType>>();
	
	public EntityClass() {
		try{
			Class<? extends EntityType> cl=EntityInspector.getClassInstance();
			entities.add(cl);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Set<EntityInfo> getEntitySet() throws InterfaceException {
		TreeSet<EntityInfo> sortedSet=new TreeSet<EntityInfo>(new Comparator<EntityInfo>(){
			@Override
			public int compare(EntityInfo o1,EntityInfo o2) {
				return o1.getDisplayName().compareTo(o2.getDisplayName());
			}
		});
		for(Class<? extends EntityType> e:entities){
			EntityInfo eInfo;
			try {
				eInfo = EntityInspector.getEntityInfo(e);
				sortedSet.add(eInfo);
			} catch (IntrospectionException exc) {
				throw new InterfaceException(exc);
			}
		}
		return Collections.unmodifiableSet(sortedSet);
	}
	
	static{
		formEntitySet();
	}
	
	private static void formEntitySet(){
		new Customer();
		new Phone();
		new Country();
	}
	
}
