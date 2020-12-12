package com.project;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

public final class ContextBootstrap {
	
	private static final String INITIAL_CONTEXT_FACTORY = "weblogic.jndi.WLInitialContextFactory";
	private static final String PROVIDER_URL = "t3://localhost:7001";
	private static final String USER_NAME = "weblogic";
	private static final String USER_PASSWORD = "f6h8Jk9k";
	
	public static final String UNIT_NAME="unit";
	public static final String DATA_SOURCE="xe";
	
	private ContextBootstrap(){}

	private static Context initialContext = null;

	public static Context getInitialContext(String[] args) throws NamingException{
		
		if(initialContext!=null) return initialContext;

		String factory=Helpers.getValue(args,0,INITIAL_CONTEXT_FACTORY);
		String url=Helpers.getValue(args,1,PROVIDER_URL);
		String principal=Helpers.getValue(args,2,USER_NAME);
		String password=Helpers.getValue(args,3,USER_PASSWORD);

		Properties p=new Properties();
		p.setProperty(Context.INITIAL_CONTEXT_FACTORY, factory);
		p.setProperty(Context.PROVIDER_URL, url);
		p.setProperty(Context.SECURITY_PRINCIPAL, principal);
		p.setProperty(Context.SECURITY_CREDENTIALS, password);
	
		return (initialContext = new InitialContext(p));
	
	}
	
	//helper method
	public static AgentRemote getAgentReference(Context con) throws NamingException{
		if(con==null){
			con=ContextBootstrap.getInitialContext(null);
		}
		if(con!=null){
			Object ref=con.lookup("Agent#com.project.AgentRemote");
			return (AgentRemote)PortableRemoteObject.narrow(ref,AgentRemote.class);
		}else throw new NamingException("can\'t get hold of context instance");
	}

}
