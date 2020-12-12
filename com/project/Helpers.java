package com.project;


import java.util.ResourceBundle;

import com.project.inspection.PropertyInfo.AlignType;


public final class Helpers {
	
	public static final String NON_BREAKING_SPACE = "&nbsp;";
	public static final char COLUMN_SEPARATOR=(char)0x2502;

	private Helpers(){}
	
	public static boolean nonEmtpyParameter(String parameter){
		return parameter!=null && !parameter.isEmpty();
	}
	
	//generic method
	public static <T> T getValue(T value,T defaultValue){
		if(value==null){
			return defaultValue;
		}else{
			return value;
		}
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
	
	private static String localizationPackageName="com.project.i18n";
	
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
	
	private static String entitiesPackageName="com.project.entities";
	
	public static String getEntityFullClassName(String entityName){
		return entitiesPackageName+"."+entityName; 
	}
	
}
