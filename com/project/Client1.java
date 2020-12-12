package com.project;

import java.util.Calendar;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import com.project.entities.Customer;

public class Client1 {

	/**
	 * @param args
	 * @throws NamingException 
	 */
	public static void main(String[] args) throws NamingException {
		Context con=Startup.getInitialContext();
		Object ref=con.lookup("Agent#com.project.AgentRemote");
		AgentRemote agent=(AgentRemote)PortableRemoteObject.narrow(ref,AgentRemote.class);
		
		Calendar c=Calendar.getInstance();
		c.set(Calendar.YEAR, 1972);
		c.set(Calendar.MONTH, 9-1);
		c.set(Calendar.DAY_OF_MONTH,27);
		c.set(Calendar.HOUR_OF_DAY,12);
		c.set(Calendar.MINUTE, 40);
		c.set(Calendar.SECOND, 30);
		Customer cust=new Customer("John Doe", "New York", Customer.Kind.PERSON, c.getTime());
		Customer customer=agent.createEntity(cust);
		
		customer.setName("John West");
		agent.updateEntity(customer);
		
		agent.removeEntity(customer);
		
	}

}
