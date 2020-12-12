package com.project;

import java.io.PrintStream;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import com.project.entities.Customer;
import com.project.entities.EntityClass;
import com.project.entities.Phone;
import com.project.inspection.EntityInfo;
import com.project.interfacebuilder.InterfaceException;

public class Client6 {

	/**
	 * @param args
	 * @throws NamingException 
	 * @throws InterfaceException 
	 */
	public static void main(String[] args) throws NamingException, InterfaceException {
		Context con=ContextBootstrap.getInitialContext(args);
		Object ref=con.lookup("Agent#com.project.AgentRemote");
		AgentRemote agent=(AgentRemote)PortableRemoteObject.narrow(ref,AgentRemote.class);
		
		Customer cust1=new Customer();
		Customer cust2=new Customer();
		Phone phone1=new Phone();
		Phone phone2=new Phone();
		Client6.printEntities(System.out);

	}

	public static void printEntities(PrintStream out) throws InterfaceException{
		for(EntityInfo e:EntityClass.getEntitySet()){
			out.println(e.getDisplayName());
		}
	}

}
