package com.project;

import java.beans.IntrospectionException;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import com.project.entities.Customer;
import com.project.inspection.EntityInfo;
import com.project.inspection.EntityInfo.EntityData;
import com.project.inspection.EntityInspector;
import com.project.inspection.PropertyInfo;
import com.project.queries.EntityDataSource;

public class Client4 {

	/**
	 * @param args
	 * @throws IntrospectionException 
	 * @throws NamingException 
	 */
	public static void main(String[] args) throws IntrospectionException, NamingException {

		Class<?> entityClass=Customer.class;
		Object primaryKey=new Integer(11);

		EntityInfo info=EntityInspector.getEntityInfo(entityClass);
		
		Context con = ContextBootstrap.getInitialContext(null);
		Object ref=con.lookup("Agent#com.project.AgentRemote");
		AgentRemote agent=(AgentRemote)PortableRemoteObject.narrow(ref,AgentRemote.class);
		
		EntityDataSource dataSource = new EntityDataSource(info);
		
		EntityData entityData=agent.fetchEntity(dataSource,primaryKey);

		addSelectorSet(entityClass,entityData);
		
	}

	protected static void addSelectorSet(Class<?> entityClass, EntityData entityData) {
		
		EntityInfo info;
		try {
			info = EntityInspector.getEntityInfo(entityClass);
	
			StringBuilder buffer=new StringBuilder();
			
			if(entityData!=null){
				
				int count=0;
				for(PropertyInfo e:info.getInfoFields()){
					Object[] infoFieldValue=entityData.getInfoData();
					Object value=infoFieldValue[count++];
					String name=e.getPropertyName();
					String s=EntityInspector.convertToString(value);
					buffer.
						append(name).
						append("=").
						append(s).
						append("; ");
				}
				System.out.println(buffer);
				buffer.setLength(0);
					
			}
				
		} catch (IntrospectionException e1) {
			e1.printStackTrace();
		}
		
	}

}
