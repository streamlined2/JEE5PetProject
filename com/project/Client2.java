package com.project;

import java.beans.IntrospectionException;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import com.project.entities.Customer;
import com.project.inspection.EntityInfo;
import com.project.inspection.EntityInfo.EntityData;
import com.project.inspection.property.InformationPropertyInfo;
import com.project.inspection.EntityInspector;
import com.project.inspection.PropertyInfo;
import com.project.inspection.Range;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.selectors.HTTPSelector;
import com.project.interfacebuilder.http.selectors.SelectorFactory;
import com.project.datasource.EntityDataSource;

public class Client2 {

	/**
	 * @param args
	 * @throws NamingException 
	 * @throws IntrospectionException 
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InterfaceException 
	 */
	public static void main(String[] args) throws NamingException, IntrospectionException, SecurityException, NoSuchFieldException, NoSuchMethodException, InterfaceException {
		Context con=ContextBootstrap.getInitialContext(args);
		Object ref=con.lookup("Agent#com.project.AgentRemote");
		AgentRemote agent=(AgentRemote)PortableRemoteObject.narrow(ref,AgentRemote.class);
		
		EntityInfo info=EntityInspector.getEntityInfo(Customer.class);
		
		EntityDataSource dataSource = new EntityDataSource(info);
		
		System.out.println("entity info:");
		
		printEntityInfo(info);
		
		System.out.println("entity name: "+info.getEntityName());
		
/*		Customer c=agent.findCustomerByName("Serge Brick");
		if(c!=null){
			System.out.println(c.getSellVolume());
			
			c.setSellVolume(new Currency(100.25));
			agent.updateCustomer(c);
			
			c=agent.findCustomerByName("Serge Brick");
			
			System.out.println(c.getSellVolume());
						
		}*/
		
		dataSource.setRange(new Range(1,Integer.MAX_VALUE));
		List<EntityData> list=agent.fetchEntities(dataSource);

		System.out.println("entity list:");
		
		printEntityList(Customer.class,list);		
	
		System.out.println("entity selectors:");
		
		printEntitySelectors(Customer.class);

	}

	private static void printEntityInfo(EntityInfo info){
		for(InformationPropertyInfo e:info.getInfoFields()){
			System.out.println(
					"property: "+e.getPropertyName()+
					" ("+e.getDisplayName()+"), "+e.getDescription()+
					"; type="+e.getType().getName()+
					"; order type="+e.getOrderType().toString()+
					"; finite type="+e.getFiniteType().toString()
					);
		}
	}
	
	public static void printEntityList(Class<?> entityClass,List<EntityData> list) throws InterfaceException {
		
		EntityInfo info=EntityInspector.getEntityInfo(entityClass);
		
		StringBuilder buffer=new StringBuilder();
		
		for(EntityData data:list){
			
			Object[] infoData=data.getInfoData();
			
			int count=0;
			for(PropertyInfo e:info.getInfoFields()){
				String name=e.getPropertyName();
				Class<?> cl=e.getType();
				Object value=infoData[count++];
				String s=EntityInspector.convertToString(value);
				buffer.append(name).append("(").append(cl.getSimpleName()).append(")").append("=").append(s).append(";");
			}
			
			System.out.println(buffer.toString());
			
			buffer.setLength(0);
			
		}

	}

	public static void printEntitySelectors(Class<?> entityClass) throws IntrospectionException, InterfaceException {
		
		EntityInfo info=EntityInspector.getEntityInfo(entityClass);
		
		StringBuilder buffer=new StringBuilder();
		
		for(InformationPropertyInfo e:info.getInfoFields()){
				
				String name=e.getPropertyName();
				Class<?> cl=e.getType();
				HTTPSelector selector=SelectorFactory.getSelector(e);

				buffer.
				append(name).
				append("(").
				append(cl.getSimpleName()).
				append(")").
				append("=");
				if(selector!=null){
					buffer.append("selector: ").append(selector.getClass().getName());
				}else{
					buffer.append("select not found");
				}

				System.out.println(buffer.toString());
				
				buffer.setLength(0);
			}
			
	}

}
