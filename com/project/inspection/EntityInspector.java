package com.project.inspection;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.naming.NamingException;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.project.ContextBootstrap;
import com.project.Helpers;
import com.project.datatypes.Currency;
import com.project.entities.EntityClass;
import com.project.entities.EntityType;
import com.project.inspection.EntityInfo.EntityData;
import com.project.inspection.Filter.FilterRangeBoundary;
import com.project.inspection.PropertyInfo.AlignType;
import com.project.inspection.PropertyInfo.FiniteType;
import com.project.inspection.PropertyInfo.MultipleType;
import com.project.inspection.PropertyInfo.OrderType;
import com.project.queries.DataSource;

public final class EntityInspector {
	
	private EntityInspector(){}

	//TODO must be replaced with Metamodel JPA 2 implementation
	public static EntityInfo getEntityInfo(Class<?> beanClass) throws IntrospectionException{
		
		BeanInfo info=Introspector.getBeanInfo(beanClass,Object.class);//,Introspector.IGNORE_ALL_BEANINFO
		
		PropertyDescriptor[] pd=info.getPropertyDescriptors();
		
		EntityInfo entityInfo=new EntityInfo(beanClass);
		
		for(PropertyDescriptor p:pd){
			
			Class<?> pClass=p.getPropertyType();
			
			try {
				
				Method readMethod=p.getReadMethod();
				Method writeMethod=p.getWriteMethod();
				
				Field field=beanClass.getDeclaredField(p.getName());

				PropertyInfo pInfo=null;
				if(isPrimaryKeyField(readMethod) || isPrimaryKeyField(field)){
				
					pInfo = new PrimaryKeyPropertyInfo(
							entityInfo, p.getName(), pClass, 
							readMethod.getName(), writeMethod.getName());

				}else if(isForeignKeyField(readMethod) || isForeignKeyField(field)){

					EntityInfo masterEntity = getMasterEntity(readMethod, field);
					
					pInfo = new ForeignKeyPropertyInfo(entityInfo, p.getName(), pClass,
							readMethod.getName(), writeMethod.getName(), masterEntity);
				
				}else{
					
					pInfo=new InformationPropertyInfo(
							entityInfo,
							p.getName(), 
							getPropertyDisplayName(entityInfo, p), 
							p.getShortDescription(), 
							pClass, 
							getFieldWidth(entityInfo.getEntityClass(),pClass,p.getName()), 
							getOrderType(pClass), 
							getFiniteType(pClass), 
							getMultipleType(pClass),
							getCardinality(pClass,getFiniteType(pClass)), 
							getAlignType(pClass), 
							readMethod.getName(), writeMethod.getName());
					
				}
				
				entityInfo.addProperty(pInfo);
			
			} catch (SecurityException e) {
			} catch (NoSuchFieldException e) {
			}
			
		}
		
		return entityInfo;
	}

	private static EntityInfo getMasterEntity(Method readMethod, Field field) throws IntrospectionException {
		
		EntityInfo masterEntity=null;
		if((masterEntity=getMasterEntity(readMethod.getAnnotations(),readMethod.getReturnType()))==null){
			masterEntity=getMasterEntity(field.getAnnotations(),field.getType());
		}
		
		return masterEntity;
	}

	private static EntityInfo getMasterEntity(Annotation[] annotations,Class<?> memberClass) throws IntrospectionException {
		EntityInfo masterEntity=null;
		for(Annotation a:annotations){
			if(a instanceof OneToOne){
				masterEntity = getTargetEntityClass(((OneToOne)a).targetEntity(),memberClass);
			}else if(a instanceof ManyToOne){
				masterEntity = getTargetEntityClass(((ManyToOne)a).targetEntity(),memberClass);
			}
		}
		return masterEntity;
	}
	
	private static EntityInfo getTargetEntityClass(Class<?> targetEntity,Class<?> memberClass) throws IntrospectionException{
		if(targetEntity==null){
			targetEntity = memberClass;
			if(targetEntity!=null){
				return getEntityInfo(targetEntity);
			}
		}
		return null;
	}

	private static boolean isForeignKeyField(AccessibleObject object) {
		return 
			object.isAnnotationPresent(OneToOne.class) || object.isAnnotationPresent(ManyToOne.class);
	}

	private static boolean isPrimaryKeyField(AccessibleObject object) {
		return object.isAnnotationPresent(Id.class);
	}

	private static String getPropertyDisplayName(EntityInfo entityInfo, PropertyDescriptor p) {
		return Helpers.getLocalizedDisplayName("PropertyNamesBundle", entityInfo.getEntityName(), p.getName(), p.getDisplayName());
/*		ResourceBundle bundle=ResourceBundle.getBundle(Helpers.getLocalizationBundleFullName("PropertyNamesBundle"));
		String pDName=p.getDisplayName();
		String resourceKey=entityInfo.getEntityName()+"."+p.getName();
		if(bundle!=null && bundle.containsKey(resourceKey)){
			pDName=bundle.getString(resourceKey);
		}
		return pDName;
*/	}
	
	private static MultipleType getMultipleType(Class<?> pClass) {
		if(pClass.isArray()) return MultipleType.MULTIPLE;
		else return MultipleType.SINGLE;
	}

	private static int getCardinality(Class<?> pClass, FiniteType finiteType) {
		if(finiteType==FiniteType.FINITE){
			return values(pClass).length;
		}
		return 0;
	}

	private static AlignType getAlignType(Class<?> pClass) {
		AlignType type=PropertyInfo.AlignType.LEFT;
		if(
			isNumeric(pClass) || isCurrency(pClass) || isDateTime(pClass)
		){
			type=PropertyInfo.AlignType.RIGHT;
		}else if(
			isCharacter(pClass)
		){
			type=PropertyInfo.AlignType.LEFT;
		}
		return type;
	}

	private static FiniteType getFiniteType(Class<?> pClass) {
		FiniteType type=PropertyInfo.FiniteType.FINITE;
		if(
				isNumeric(pClass)||
				isCurrency(pClass) ||
				isString(pClass) ||
				isDateTime(pClass)
		){
			type=PropertyInfo.FiniteType.INFINITE;
		}
		return type;
	}

	private static OrderType getOrderType(Class<?> pClass) {
		OrderType type=OrderType.UNORDERED;
		if(
			isNumeric(pClass)||
			isCharacter(pClass)||
			isCurrency(pClass) ||
			isString(pClass) ||
			isDateTime(pClass)||
			isComparable(pClass)
		){
			type=OrderType.ORDERED;
		}
		return type;
	}

	public static Object convertFromString(String pValue, Class<?> propertyType) {
		
		if(pValue==null) return null;
		if(propertyType==null) throw new IllegalArgumentException("null property type parameter");
		
		try{
		
			if(isInteger(propertyType)){
				NumberFormat f=NumberFormat.getIntegerInstance();
				return f.parse(pValue).intValue();
			}else if(isLong(propertyType)){
				NumberFormat f=NumberFormat.getIntegerInstance();
				return f.parse(pValue);
			}else if(isFloatPoint(propertyType)){
				NumberFormat f=NumberFormat.getNumberInstance();
				return f.parse(pValue);
			}else if(isCurrency(propertyType)){
				NumberFormat f=NumberFormat.getCurrencyInstance();
				return f.parse(pValue);
			}else if(isNumber(propertyType)){
				NumberFormat f=NumberFormat.getInstance();
				return f.parse(pValue);
			}else if(isTime(propertyType)){
				DateFormat f=DateFormat.getDateTimeInstance();
				Date date=f.parse(pValue);
				return new Time(date.getTime());
			}else if(isTimestamp(propertyType)){
				DateFormat f=DateFormat.getDateTimeInstance();
				Date date=f.parse(pValue);
				return new Timestamp(date.getTime());
			}else if(isDate(propertyType)){
				DateFormat f=DateFormat.getDateInstance();
				return f.parse(pValue);
			}else if(propertyType.isEnum()){
				Method valueOfMethod;
				try {
					valueOfMethod = propertyType.getDeclaredMethod("valueOf", new Class<?>[]{String.class});
					return valueOfMethod.invoke(null, new Object[]{pValue});
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}else if(isBoolean(propertyType)){
				return Boolean.valueOf(pValue);
			}else if(isString(propertyType)){
				return pValue;
			}

		}catch(ParseException e){
			return null;
		}
		
		return null;
		
	}
	
	public static String convertToString(Object value){
		if(value==null){
			return "";
		}else{
			String s=value.toString();
			
			Class<?> c=value.getClass();
			
			if(isInteger(c)){
				NumberFormat f=NumberFormat.getIntegerInstance();
				s=f.format(value);
			}else if(isCurrency(c)){
				NumberFormat f=NumberFormat.getCurrencyInstance();
				s=f.format(value);
			}else if(isFloatPoint(c)){
				NumberFormat f=NumberFormat.getInstance();
				s=f.format(value);
			}else if(isNumber(c)){
				NumberFormat f=NumberFormat.getInstance();
				s=f.format(value);
			}else if(isTime(c)){
				DateFormat f=DateFormat.getDateTimeInstance();
				s=f.format(value);
			}else if(isDate(c)){
				DateFormat f=DateFormat.getDateInstance();
				s=f.format(value);
			}else if(c.isEnum()){
				String valueName = getValueDisplayName(s, c);
				String firstChar=valueName.substring(0,1).toUpperCase();
				String rest=valueName.substring(1).toLowerCase();
				s=firstChar+rest;
			}else if(isBoolean(c)){
				s = getValueDisplayName(s, c);
			}
			return s;
			
		}
	}

/*
	public static String getValueDisplayName(String bundleName, String prefix, String propertyValue) {
		ResourceBundle bundle=ResourceBundle.getBundle(bundleName);
		String key=prefix+"."+propertyValue;
		String valueName=propertyValue;
		if(bundle!=null && bundle.containsKey(key)){
			valueName=bundle.getString(key);
		}
		return valueName;
	}
*/	

	private static String getValueDisplayName(String propertyValue, Class<?> propertyClass) {
		return Helpers.getLocalizedDisplayName("TypeValuesBundle", getClassName(propertyClass), propertyValue);
	}
	
	public static String getClassName(Class<?> propertyClass) {
		String name=propertyClass.getName();
		int pos=name.lastIndexOf(".");
		String result;
		if(pos>=0 && pos+1<name.length()){
			result=name.substring(pos+1);
		}else{
			result=name;
		}
		return result;
	}

	private static boolean isComparable(Class<?> cl) {
		return Comparable.class.isAssignableFrom(cl);
	}

	private static boolean isDateTime(Class<?> pClass) {
		return isDate(pClass) || isTime(pClass);
	}

	private static boolean isTime(Class<?> c) {
		return
			java.sql.Time.class.isAssignableFrom(c);
	}

	private static boolean isTimestamp(Class<?> c) {
		return 
			java.sql.Timestamp.class.isAssignableFrom(c);
	}
	
	private static boolean isDate(Class<?> c) {
		return
			java.util.Date.class.isAssignableFrom(c)||
			java.sql.Date.class.isAssignableFrom(c)||
			Calendar.class.isAssignableFrom(c);
	}

	private static boolean isNumber(Class<?> cl) {
		return Number.class.isAssignableFrom(cl);
	}

	private static boolean isCurrency(Class<?> cl) {
		return Currency.class.isAssignableFrom(cl);
	}

	private static boolean isInteger(Class<?> cl) {
		return (
			cl.isPrimitive() && (cl==int.class || cl==short.class || cl==byte.class) ||
			!cl.isPrimitive() && 
			(
				Integer.class.isAssignableFrom(cl)||
				Short.class.isAssignableFrom(cl)||
				Byte.class.isAssignableFrom(cl)
			)
		);
	}

	private static boolean isLong(Class<?> cl) {
		return (
			cl.isPrimitive() && (cl==long.class) ||
			!cl.isPrimitive() && 
			(
				Long.class.isAssignableFrom(cl)
			)
		);
	}

	private static boolean isString(Class<?> cl) {
		return String.class.isAssignableFrom(cl);
	}

	private static boolean isBoolean(Class<?> pClass){
		return 
			(pClass.isPrimitive() && pClass==boolean.class) || 
			(Boolean.class.isAssignableFrom(pClass));
	}
	
	private static boolean isCharacter(Class<?> pClass){
		return
			pClass.isPrimitive() && (
					pClass==char.class
					)||
			!pClass.isPrimitive() && (
					Character.class.isAssignableFrom(pClass));

	}
	
	private static boolean isFloatPoint(Class<?> pClass) {
		return (
			pClass.isPrimitive() && (
				pClass==float.class ||
				pClass==double.class
			) ||
			!pClass.isPrimitive() && (
				Double.class.isAssignableFrom(pClass) ||
				Float.class.isAssignableFrom(pClass)
			)
		);
	}

	private static boolean isNumeric(Class<?> pClass){
		return
			isInteger(pClass) ||
			isLong(pClass) ||
			isFloatPoint(pClass) ||
			isNumber(pClass);
	}
	
	public static Object firstValue(Class<?> pClass){
		return valueAt(pClass,0);
	}
	
	public static Object valueAt(Class<?> pClass,int index){
		return values(pClass)[index];
	}
	
	public static int getValuesMaxWidth(Class<?> pClass){
		int maxLength=0;
		Object[] values=values(pClass);
		for(Object v:values){
			maxLength=Math.max(maxLength, EntityInspector.convertToString(v).length());
		}
		return maxLength;
	}
	
	public static Object[] values(Class<?> pClass){
		if(pClass.isEnum()){
			try {
				Method vMethod=pClass.getMethod("values", new Class[]{});
				Object[] v=(Object[])vMethod.invoke(null, new Object[]{});
				return v;
			} catch (SecurityException e) {
				e.printStackTrace();
				return null;
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				return null;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				return null;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				return null;
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				return null;
			}
		}else if(isBoolean(pClass)){
			return new Boolean[]{Boolean.TRUE,Boolean.FALSE};
		}
		return null;
	}

	public static EntityType createEntity(EntityInfo eInfo) throws InstantiationException, IllegalAccessException {
		Class<?> entityType=eInfo.getEntityClass();
		return (EntityType)entityType.newInstance();
	}

	public static EntityData initializeEntityData(DataSource dataSource) {
		return initializeEntityData(dataSource,FilterRangeBoundary.START);
	}
	
	public static EntityData initializeEntityData(DataSource dataSource,FilterRangeBoundary boundaryKind) {
		
		List<InformationPropertyInfo> infoList=dataSource.getSelectedInformationProperties();
		
		LinkedList<Object> dataList = new LinkedList<Object>();
		
		for(InformationPropertyInfo pInfo:infoList){
			
			Object value = initialValueForType(pInfo.getType());
			FilterItem filterValue=dataSource.getFilter().findByProperty(pInfo);
			if(filterValue!=null){
				if(boundaryKind==FilterRangeBoundary.START){
					value=filterValue.getMinValue();
				}else if(boundaryKind==FilterRangeBoundary.FINISH){
					value=filterValue.getMaxValue();
				}
			}
			dataList.add(value);

		}
		
		Object[] data=dataList.toArray();
		
		return new EntityData(null,data,new PropertyList(dataSource.getInformationProperties()));
	
	}
	
	public static Object initialValueForType(Class<?> type){
		
		if(isCharacter(type)){
			return Character.SPACE_SEPARATOR;
		}else if(isString(type)){
			return "";
		}else if(isInteger(type)){
			return 0;
		}else if(isFloatPoint(type)){
			return 0.0;
		}else if(isNumber(type)){
			return 0;
		}else if(isLong(type)){
			return 0L;
		}else if(isDate(type)){
			return new Date();
		}else if(isTime(type)){
			return new java.sql.Time(new Date().getTime());
		}else if(isTimestamp(type)){
			return new java.sql.Timestamp(new Date().getTime());
		}else if(isBoolean(type)){
			return Boolean.FALSE;
		}else if(type.isEnum()){
			return firstValue(type);
		}
		
		return null;

	}
	
	public static void setPropertyValue(
			PropertyInfo pInfo,
			Object entity, 
			Object value) 
	throws 
		SecurityException, 
		NoSuchMethodException, 
		IllegalArgumentException, 
		IllegalAccessException, 
		InvocationTargetException {

		Class<?> entityType = pInfo.getEntityInfo().getEntityClass();
		Method writeMethod = entityType.getMethod(pInfo.getWriteMethod(), new Class[]{pInfo.getType()});
		writeMethod.invoke(entity, new Object[]{value});
	
	}

	public static Object getPropertyValue(
			PropertyInfo pInfo,
			Object entity) 
	throws 
		SecurityException, 
		NoSuchMethodException, 
		IllegalArgumentException, 
		IllegalAccessException, 
		InvocationTargetException {

		Class<?> entityType = pInfo.getEntityInfo().getEntityClass();
		Method readMethod = entityType.getMethod(pInfo.getReadMethod(), new Class[]{});
		Object value=readMethod.invoke(entity, new Object[]{});
		return value;
	}

	private static int getFieldWidth(Class<?> entityType,Class<?> propertyType, String fieldName) {
		
		int columnSize=-1;
		try {
			columnSize=ContextBootstrap.getAgentReference(null).getColumnSize(entityType,fieldName);

			if(isCharacter(propertyType)){
				return 1;
			}else if(isString(propertyType)){
				return getWidthValue(columnSize,20);
			}else if(isInteger(propertyType)){
				return getWidthValue(columnSize,7);
			}else if(isLong(propertyType)){
				return getWidthValue(columnSize,10);
			}else if(isFloatPoint(propertyType)){
				return getWidthValue(columnSize,15);
			}else if(isNumber(propertyType)){
				return getWidthValue(columnSize,15);
			}else if(isDate(propertyType) || isTime(propertyType) || isTimestamp(propertyType)){
				return EntityInspector.convertToString(initialValueForType(propertyType)).length();
			}else if(isBoolean(propertyType) || propertyType.isEnum()){
				return getValuesMaxWidth(propertyType);
			}

		} catch (NamingException e) {
		}

		return 0;
	}
	
	private static int getWidthValue(int columnSize,int defaultValue){
		return columnSize==-1?defaultValue:columnSize;
	}

	public static Class<? extends EntityType> getClassInstance() throws ClassNotFoundException{
		Throwable t=new Throwable();
		StackTraceElement[] trace=t.getStackTrace();
		boolean found=false;
		int index=-1;
		for(int i=0;i<trace.length;i++){
			if(
					trace[i].getMethodName().equals("<init>") &&
					trace[i].getClassName().equals(EntityClass.class.getName())
			) {
				found=true;
				index=i+1;
				break;
			}
		}
		if(found && index<trace.length){
			String className=trace[index].getClassName();
			Class<?> callerClass=Class.forName(className);
			if(EntityType.class.isAssignableFrom(callerClass)){
				Class<? extends EntityType> eType=callerClass.asSubclass(EntityType.class);
				return eType;
			}
			throw new ClassNotFoundException("found class is not subclass of EntityType");
		}
		throw new ClassNotFoundException("entity class not found");
	}

}
