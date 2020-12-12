package com.project;

import java.beans.IntrospectionException;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import com.project.entities.Customer;

public class Client5 {

	/**
	 * @param args
	 * @throws IntrospectionException 
	 * @throws NamingException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws IntrospectionException, NamingException, SQLException {

		Context con = Startup.getInitialContext();
		Object ref=con.lookup("Agent#com.project.AgentRemote");
		AgentRemote agent=(AgentRemote)PortableRemoteObject.narrow(ref,AgentRemote.class);
		
		System.out.println("name: "+agent.getColumnSize(Customer.class,"name"));
		System.out.println("address: "+agent.getColumnSize(Customer.class,"address"));
		System.out.println("phone: "+agent.getColumnSize(Customer.class,"phone"));
		System.out.println("kind: "+agent.getColumnSize(Customer.class,"kind"));
		System.out.println("rating: "+agent.getColumnSize(Customer.class,"rating"));
		System.out.println("creditavailable: "+agent.getColumnSize(Customer.class,"creditavailable"));
		System.out.println("lasttransactiontime: "+agent.getColumnSize(Customer.class,"lasttransactiontime"));
		System.out.println("creationdate: "+agent.getColumnSize(Customer.class,"creationdate"));

	}

}
