package com.project;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import com.project.entities.Phone;
import com.project.entities.Phone.Kind;

public class Client7 {

	/**
	 * @param args
	 * @throws NamingException 
	 */
	public static void main(String[] args) throws NamingException {
		Context con=ContextBootstrap.getInitialContext(args);
		Object ref=con.lookup("Agent#com.project.AgentRemote");
		AgentRemote agent=(AgentRemote)PortableRemoteObject.narrow(ref,AgentRemote.class);
		
		Phone phone=new Phone();
		phone.setKind(Kind.CELLULAR);
		phone.setNumber("+380974443322");
		phone=agent.createEntity(phone);
		
		phone.setKind(Kind.FIXED);
		agent.updateEntity(phone);
		
		agent.removeEntity(phone);

	}

}
