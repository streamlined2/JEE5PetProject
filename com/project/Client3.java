package com.project;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.naming.NamingException;

import com.project.entities.Customer;
import com.project.entities.Customer.Kind;

public class Client3 {

	/**
	 * @param args
	 * @throws NamingException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static void main(String[] args) throws NamingException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
/*		Context con=ContextBootstrap.getInitialContext(args);
		Object ref=con.lookup("Agent#com.project.AgentRemote");
		AgentRemote agent=(AgentRemote)PortableRemoteObject.narrow(ref,AgentRemote.class);
*/
		Customer.Kind en=Customer.Kind.PERSON;
		for(Customer.Kind a:Kind.values()){
			System.out.println(a);
		}
		
		Class<Kind> type=Customer.Kind.class;
		Method m=type.getMethod("values", new Class[]{});
		
		Object[] values=(Object[]) m.invoke(en, new Object[]{});
		for(Object o:values){
			System.out.println(o);
		}
		
		Enum<Kind> t=Customer.Kind.ORGANIZATION;
		System.out.println(t.getClass().getName());
		
	}

}
