package com.project;

import java.util.Locale;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import com.project.interfacebuilder.InterfaceException;

public final class Startup {
	
	//parameters for getting initial context
	private static final String DEFAULT_INITIAL_CONTEXT_FACTORY = "weblogic.jndi.WLInitialContextFactory";
	private static String initialContextFactory = null;
	private static final String DEFAULT_PROVIDER_URL = "t3://localhost:7001";
	private static String providerURL = null;
	private static final String DEFAULT_USER_NAME = "";
	private static String userName = null;
	private static final String DEFAULT_USER_PASSWORD = "";
	private static String userPassword = null;
	
	//persistent context unit name for annotation
	public static final String UNIT_NAME="unit";
	public static final String DATA_SOURCE="datasource";
	
	public final static Locale DEFAULT_LOCALE = Locale.ENGLISH;
	
	private Startup(){}
	
	public static void setStartupParameters(
			String _initialContextFactory, String _providerURL, String _userName, String _userPassword){
		initialContextFactory = _initialContextFactory;
		providerURL = _providerURL;
		userName = _userName;
		userPassword = _userPassword;
	}

	// reference to stashed initial context instance for repeated use
	private static Context initialContext = null;

	public static Context getInitialContext() throws NamingException{
		
		if(initialContext!=null) return initialContext;

		String factory=Helpers.getValue(initialContextFactory,DEFAULT_INITIAL_CONTEXT_FACTORY);
		String url=Helpers.getValue(providerURL,DEFAULT_PROVIDER_URL);
		String principal=Helpers.getValue(userName,DEFAULT_USER_NAME);
		String password=Helpers.getValue(userPassword,DEFAULT_USER_PASSWORD);

		Properties p=new Properties();
		p.setProperty(Context.INITIAL_CONTEXT_FACTORY, factory);
		p.setProperty(Context.PROVIDER_URL, url);
		p.setProperty(Context.SECURITY_PRINCIPAL, principal);
		p.setProperty(Context.SECURITY_CREDENTIALS, password);
	
		return (initialContext = new InitialContext(p));
	
	}
	
	//utility method for stateless bean reference
	public static AgentRemote getAgent() throws InterfaceException{
		try {
			Context con=Startup.getInitialContext();
			if(con!=null){
				Object ref=con.lookup("Agent#com.project.AgentRemote");
				return (AgentRemote)PortableRemoteObject.narrow(ref,AgentRemote.class);
			}else throw new InterfaceException("failed to obtain initial context instance");
		} catch (NamingException e) {
			throw new InterfaceException(e);//wrap unchecked exception into application exception
		}
	}
	
}
