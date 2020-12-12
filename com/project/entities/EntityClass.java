package com.project.entities;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import com.project.inspection.EntityInfo;
import com.project.inspection.EntityInspector;
import com.project.interfacebuilder.InterfaceException;

public abstract class EntityClass implements EntityType {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4756534033339779087L;
	
	@SuppressWarnings("unchecked")
	private static Class<EntityClass>[] entities = (Class<EntityClass>[]) new Class<?>[]{
			Customer.class,
			Country.class,
			Phone.class
	};
	
	public static Set<Class<? extends EntityClass>> getEntityClassSet() {
		return Collections.unmodifiableSet(new HashSet<Class<? extends EntityClass>>(Arrays.asList(entities)));
	}
	
	public static Set<EntityInfo> getEntitySet() throws InterfaceException {
		TreeSet<EntityInfo> sortedSet=new TreeSet<EntityInfo>(new Comparator<EntityInfo>(){
			@Override
			public int compare(EntityInfo o1,EntityInfo o2) {
				return o1.getDisplayName().compareTo(o2.getDisplayName());
			}
		});
		for(Class<EntityClass> e:entities){
			EntityInfo eInfo = EntityInspector.getEntityInfo(e);
			sortedSet.add(eInfo);
		}
		return Collections.unmodifiableSet(sortedSet);
	}
	
}
