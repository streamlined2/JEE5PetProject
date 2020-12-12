package com.project;

import java.util.ResourceBundle;

import javax.naming.NamingException;

import com.project.inspection.property.PropertyInfo.AlignType;
import com.project.interfacebuilder.InterfaceException;

public final class Helpers {
	
	public static final String NON_BREAKING_SPACE = "&nbsp;";
	public static final char COLUMN_SEPARATOR=(char)0x2502;

	private Helpers(){}
	
	public static boolean nonEmtpyParameter(String parameter){
		return parameter!=null && !parameter.isEmpty();
	}
	
	//generic method
	public static <T> T getValue(T value,T defaultValue){
		return (value==null)?defaultValue:value;
	}

	public static String padString(
			AlignType alignType,int width,String value,String fillChar){
		
		int padLength=Math.max(width-value.length(),0);
		
		String source=value;
		if(width<value.length()){
			source=value.substring(0,width);
		}
		
		int leftPadLength=0, rightPadLength=0;
		switch(alignType){
			case LEFT:
				leftPadLength=0;
				rightPadLength=padLength;
				break;
			case RIGHT:
				leftPadLength=padLength;
				rightPadLength=0;
				break;
			case CENTER:
				leftPadLength=padLength/2;
				rightPadLength=padLength-leftPadLength;
				break;
			default:
		}
		return replicateChar(leftPadLength,fillChar)+source+replicateChar(rightPadLength, fillChar);
	}
	
	public static String replicateChar(int count,String ch){
		StringBuilder b=new StringBuilder();
		for(int k=0;k<count;k++){
			b.append(ch);
		}
		return b.toString();
	}
	
	private final static String localizationPackageName="com.project.i18n";
	
	public static String getLocalizationBundleFullName(String bundleName){
		return localizationPackageName+"."+bundleName; 
	}
	
	public static String getLocalizedDisplayName(String bundleName,String prefix,String suffix) {
		return getLocalizedDisplayName(bundleName,prefix,suffix,suffix);
	}

	public static String getLocalizedDisplayName(String bundleName,String prefix,String suffix,String defaultName) {
		ResourceBundle bundle=ResourceBundle.getBundle(Helpers.getLocalizationBundleFullName(bundleName));
		String localizedName=defaultName;
		String nameKey=prefix.isEmpty()?suffix:prefix+"."+suffix;
		if(bundle!=null && bundle.containsKey(nameKey)){
			localizedName=bundle.getString(nameKey);
		}
		return localizedName;
	}
	
	private final static String ENTITIES_PACKAGE_NAME="com.project.entities";
	
	public static String getEntityFullClassName(String entityName){
		return ENTITIES_PACKAGE_NAME+"."+entityName; 
	}
	
	//helper method to provide reference to agent
	public static AgentRemote getAgent() throws InterfaceException{
		try {
			return ContextBootstrap.getAgentReference(null);
		} catch (NamingException e) {
			throw new InterfaceException(e);
		}
	}

	public static String getValue(String[] args,int index,String defValue){
		if(args!=null && index>=0 && index<args.length){
			String v=args[index];
			if(v.isEmpty()) return defValue;
			else return v;
		}else{
			return defValue;
		}
	}

}
