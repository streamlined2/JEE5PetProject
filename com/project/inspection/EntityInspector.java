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
import java.util.Locale;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.project.Helpers;
import com.project.Startup;
import com.project.datasource.DataSource;
import com.project.datatypes.Currency;
import com.project.entities.EntityClass;
import com.project.entities.EntityType;
import com.project.inspection.EntityInfo.EntityData;
import com.project.inspection.Filter.FilterRangeBoundary;
import com.project.inspection.property.EntityCollectionPropertyInfo;
import com.project.inspection.property.ForeignKeyPropertyInfo;
import com.project.inspection.property.InformationPropertyInfo;
import com.project.inspection.property.PrimaryKeyPropertyInfo;
import com.project.inspection.property.PropertyInfo;
import com.project.inspection.property.PropertyInfo.AlignType;
import com.project.inspection.property.PropertyInfo.FiniteType;
import com.project.inspection.property.PropertyInfo.MultipleType;
import com.project.inspection.property.PropertyInfo.OrderType;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.forms.HTTPForm;

public final class EntityInspector {
	
	private EntityInspector(){}

	//private EntityInfo instance pool to avoid excessive introspection and speed up execution just a tad
	private static class EntityInfoPool {
		
		//use interface reference to avoid dependency on implementation details of container class 
		private SortedMap<String,EntityInfo> cache = new TreeMap<String,EntityInfo>();
		
		// allow clients to operate cache only by means of addEntity/getEntity, thus hiding cache implementation and avoiding dependency on that   
		public void addEntity(String name,EntityInfo eInfo) {
			cache.put(name, eInfo);
		}
		
		public EntityInfo getEntity(String name){
			return cache.get(name);
		}
		
	}
	
	// singleton object design pattern
	private static EntityInfoPool entityPool = new EntityInfoPool();
	
	public static EntityInfo getEntityInfo(Class<?> eClass,Locale locale) throws InterfaceException {
		return getEntityInfo(eClass,locale,true);
	}
	
	public static EntityInfo getEntityInfo(Class<?> eClass,Locale locale,boolean constructEntityCollectionProperties) throws InterfaceException {
		
		if(!EntityType.class.isAssignableFrom(eClass)) throw new InterfaceException("parameter must be ancestor of interface EntityType");
			
		@SuppressWarnings("unchecked")
		Class<? extends EntityType> entityClass = (Class<? extends EntityType>) eClass;

		EntityInfo entityInfo = entityPool.getEntity(entityClass.getName());
		
		if(entityInfo == null){
			
			try{
				
				//involve JavaBeans Introspection API to pry into entity bean class details
				BeanInfo info=Introspector.getBeanInfo(entityClass,Object.class);//,Introspector.IGNORE_ALL_BEANINFO
				
				PropertyDescriptor[] propertyDescriptors=info.getPropertyDescriptors();
				
				entityInfo=new EntityInfo(entityClass);
				
				for(PropertyDescriptor propertyDescriptor:propertyDescriptors){//foreach loop walks through arrays
					
					Class<?> pClass=propertyDescriptor.getPropertyType();
					
					try {
						
						Method readMethod=propertyDescriptor.getReadMethod();
						Method writeMethod=propertyDescriptor.getWriteMethod();
						
						Field field=entityClass.getDeclaredField(propertyDescriptor.getName());
		
						//use Java Reflection API to guess if proper annotation is present for primary or foreign keys or entity collection
						if(isPrimaryKeyField(readMethod) || isPrimaryKeyField(field)){
						
							entityInfo.setPrimaryKeyProperty(new PrimaryKeyPropertyInfo(
									entityInfo, propertyDescriptor.getName(), pClass, 
									readMethod.getName(), writeMethod.getName()));
							
						}else if(isForeignKeyField(readMethod) || isForeignKeyField(field)){
		
							entityInfo.addForeignKeyProperty(new ForeignKeyPropertyInfo(
									entityInfo, propertyDescriptor.getName(), pClass,
									readMethod.getName(), writeMethod.getName(), 
									getMasterType(readMethod, field)));
						
						}else if(isEntityCollection(readMethod) || isEntityCollection(field)){
							
							if(constructEntityCollectionProperties){//skip entity collection property construction on initialization thus avoiding infinite recursion
								
								entityInfo.addEntityCollectionProperty(new EntityCollectionPropertyInfo(
										entityInfo, propertyDescriptor.getName(), pClass, 
										readMethod.getName(), writeMethod.getName(), 
										getMappedByForeignKey(entityInfo.getEntityClass(),readMethod,field,locale)));

							}
	
						}else{
							
							entityInfo.addInformationProperty(new InformationPropertyInfo(
									entityInfo,
									propertyDescriptor.getName(), 
									getPropertyDisplayName(entityInfo, locale, propertyDescriptor), 
									propertyDescriptor.getShortDescription(), 
									pClass, 
									Startup.getAgent().getFieldWidth(entityInfo.getEntityClass(),pClass,propertyDescriptor.getName(),locale), 
									getOrderType(pClass), 
									getFiniteType(pClass), 
									getMultipleType(pClass),
									getCardinality(pClass,getFiniteType(pClass)), 
									getAlignType(pClass), 
									readMethod.getName(), writeMethod.getName()));
							
						}
						
					} catch (SecurityException e) {
						throw new InterfaceException(e);
					} catch (NoSuchFieldException e) {
						throw new InterfaceException(e);
					}
					
				}
				
				entityPool.addEntity(entityClass.getName(), entityInfo);
				
				return entityInfo;
			
			}catch(IntrospectionException e){
				throw new InterfaceException(e);//wrap sub-system exception into application exception
			}

		}
		
		return entityInfo;//take advantage of pooled instance of one has been found
		
	}
	
	private static ForeignKeyPropertyInfo getMappedByForeignKey(
						Class<? extends EntityType> masterEntityClass, 
						Method readMethod, Field field, Locale locale) throws InterfaceException {
		
		//either getter or field may be marked by annotation 
		ForeignKeyPropertyInfo foreignKey = getMappedByForeignKey(masterEntityClass,readMethod,locale);
		if(foreignKey == null){
			foreignKey = getMappedByForeignKey(masterEntityClass,field,locale);
		}
		return foreignKey;
	}

	@SuppressWarnings("unchecked")
	private static ForeignKeyPropertyInfo getMappedByForeignKey(
			Class<? extends EntityType> masterEntityClass, AccessibleObject object, Locale locale) throws InterfaceException {
		
		Annotation annotation = object.getAnnotation(OneToMany.class);//use Java Reflection API to check if relation 1:M annotation present
		
		if(annotation != null){
			
			String foreignKeyName = ((OneToMany)annotation).mappedBy();// get annotation "mappedBy" parameter value by calling appropriate method
			Class<? extends EntityClass> targetEntityClass = ((OneToMany)annotation).targetEntity();//try to find target entity the same way
			
			SortedSet<ForeignKeyPropertyInfo> foreignKeyPropertyCandidates = new TreeSet<ForeignKeyPropertyInfo>();
			
			if(isEmptyTargetEntityClass(targetEntityClass)){
				
				for(Class<? extends EntityType> slave:EntityClass.getEntityClassSet()){//foreach loop may walk through Iterable collection also
					
					if(!slave.equals(masterEntityClass.getClass())){
						
						EntityInfo slaveInfo = EntityInspector.getEntityInfo(slave,locale,false);
						ForeignKeyPropertyInfo foreignKey = slaveInfo.getForeignKeyFor(masterEntityClass);
						
						if(
							foreignKey!=null && 
							(
								foreignKeyName==null || (
									foreignKeyName!=null && 
									foreignKey.getPropertyName().equals(foreignKeyName)
								)
							)
						){
							foreignKeyPropertyCandidates.add(foreignKey);
						}
					}
				}
			}else{
				
				EntityInfo slaveEntity = EntityInspector.getEntityInfo(targetEntityClass,locale);
				
				if(foreignKeyName!=null) 
					foreignKeyPropertyCandidates.add(slaveEntity.getForeignKeyFor(foreignKeyName));
				else 
					foreignKeyPropertyCandidates.add(slaveEntity.getForeignKeyFor(masterEntityClass));
			
			}
			
			if(foreignKeyPropertyCandidates.size()>0) return foreignKeyPropertyCandidates.first();
			else throw new InterfaceException("foreign key not found for entity class "+masterEntityClass.getName());

		}

		throw new InterfaceException("entity collection must be marked by OneToMany annotation");
	
	}
	
	private static Class<? extends EntityType> getMasterType(Method readMethod, Field field) throws InterfaceException {
		
		//try first getter, then field itself to see if they may yield master entity bean type
		Class<? extends EntityType> masterEntity = getMasterEntity(readMethod.getAnnotations(),readMethod.getReturnType());
		if(masterEntity == null){
			masterEntity = getMasterEntity(field.getAnnotations(),field.getType());
		}
		
		return masterEntity;
	}

	@SuppressWarnings("unchecked")
	private static Class<? extends EntityType> getMasterEntity(Annotation[] annotations,Class<?> memberClass) throws InterfaceException {
		
		Class<? extends EntityType> masterEntity=null;
		
		if(!EntityType.class.isAssignableFrom(memberClass)) throw new InterfaceException("getter return type or member type should be of type EntityType");
		Class<? extends EntityType> resultType = (Class<? extends EntityType>)memberClass;

		for(Annotation a:annotations){
			if(a instanceof OneToOne){
				masterEntity = getTargetEntityClass(((OneToOne)a).targetEntity(),resultType);
				break;
			}else if(a instanceof ManyToOne){
				masterEntity = getTargetEntityClass(((ManyToOne)a).targetEntity(),resultType);
				break;
			}
		}
		return masterEntity;
	
	}
	
	private static boolean isEmptyTargetEntityClass(Class<?> targetEntityClass){
		return 
			(targetEntityClass==null) || 
			(targetEntityClass.equals(void.class));
	}
	
	private static Class<? extends EntityType> getTargetEntityClass(
			Class<? extends EntityType> targetEntity,
			Class<? extends EntityType> memberClass) throws InterfaceException{
		
		if(isEmptyTargetEntityClass(targetEntity)){
			targetEntity = memberClass;
		}
		
		if(!isEmptyTargetEntityClass(targetEntity)){
			return targetEntity;
		}
		
		return null;
	}

	private static boolean isEntityCollection(AccessibleObject object) {
		return 
			object.isAnnotationPresent(OneToMany.class);
	}

	private static boolean isForeignKeyField(AccessibleObject object) {
		return 
			object.isAnnotationPresent(OneToOne.class) || 
			object.isAnnotationPresent(ManyToOne.class);
	}

	private static boolean isPrimaryKeyField(AccessibleObject object) {
		return 
			object.isAnnotationPresent(Id.class);
	}

	private static String getPropertyDisplayName(EntityInfo entityInfo, Locale locale, PropertyDescriptor p) {
		//call helper method to localize property name
		return Helpers.getLocalizedDisplayName("PropertyNamesBundle", locale, entityInfo.getEntityName(), p.getName(), p.getDisplayName());
	}
	
	/* following are methods to determine given type's features and map entity properties to interface elements (selectors) later
	 * */
	private static MultipleType getMultipleType(Class<?> pClass) {
		if(pClass.isArray()) return MultipleType.MULTIPLE;
		else return MultipleType.SINGLE;
	}

	private static int getCardinality(Class<?> pClass, FiniteType finiteType) {
		return 
			(finiteType==FiniteType.FINITE)?
					values(pClass).length:
					0;
	}

	private static AlignType getAlignType(Class<?> pClass) {
		return 
			(isNumeric(pClass) || isCurrency(pClass) || isDateTime(pClass))?
					PropertyInfo.AlignType.RIGHT:
					PropertyInfo.AlignType.LEFT;
	}

	private static FiniteType getFiniteType(Class<?> pClass) {
		return (
				isNumeric(pClass) || 
				isCurrency(pClass) || 
				isString(pClass) || 
				isDateTime(pClass))?
					PropertyInfo.FiniteType.INFINITE:
					PropertyInfo.FiniteType.FINITE;
	}

	private static OrderType getOrderType(Class<?> pClass) {
		return (
				isNumeric(pClass) ||
				isCharacter(pClass) ||
				isCurrency(pClass) ||
				isString(pClass) ||
				isDateTime(pClass) ||
				isComparable(pClass))?
					OrderType.ORDERED:
					OrderType.UNORDERED;
	}

	//convert string value to given type object
	public static Object convertFromString(String pValue, Class<?> propertyType) {
		
		if(pValue==null) return null;
		if(propertyType==null) throw new IllegalArgumentException("null property type parameter");
		
		try{
		
			if(isInteger(propertyType)){
				return NumberFormat.getIntegerInstance().parse(pValue).intValue();
			
			}else if(isLong(propertyType)){
				return NumberFormat.getIntegerInstance().parse(pValue);
			
			}else if(isFloatPoint(propertyType)){
				return NumberFormat.getNumberInstance().parse(pValue);
			
			}else if(isCurrency(propertyType)){
				return NumberFormat.getCurrencyInstance().parse(pValue);
			
			}else if(isNumber(propertyType)){
				return NumberFormat.getInstance().parse(pValue);
			
			}else if(isTime(propertyType)){
				return new Time(DateFormat.getDateTimeInstance().parse(pValue).getTime());
			
			}else if(isTimestamp(propertyType)){
				Date date=DateFormat.getDateTimeInstance().parse(pValue);
				return new Timestamp(date.getTime());
			
			}else if(isDate(propertyType)){
				return DateFormat.getDateInstance().parse(pValue);
			
			}else if(propertyType.isEnum()){
				try {
					Method valueOfMethod = propertyType.getDeclaredMethod("valueOf", new Class<?>[]{String.class});
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
				return null;
			
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
	
	public static String convertToDecoratedString(Object value){
		StringBuilder decorated = new StringBuilder();
		
		Class<?> c=value.getClass();
		if(isString(c) || isCharacter(c)){
			decorated = decorated.append("'").append(value).append("'");
		}else{
			decorated = decorated.append(value);
		}
		
		return decorated.toString();
	}
	
	public static String convertToString(Object value){
		return convertToString(value,Startup.DEFAULT_LOCALE);
	}
	//convert object value to string according to it's type
	public static String convertToString(Object value, Locale locale){
		
		if(value==null){
			return "";
		}else{
			String s=value.toString();
			
			Class<?> c=value.getClass();
			
			if(isInteger(c)){
				s = NumberFormat.getIntegerInstance(locale).format(value);
			
			}else if(isCurrency(c)){
				s = NumberFormat.getCurrencyInstance(locale).format(value);
			
			}else if(isFloatPoint(c)){
				s = NumberFormat.getInstance(locale).format(value);
			
			}else if(isNumber(c)){
				s = NumberFormat.getInstance(locale).format(value);
			
			}else if(isTime(c)){
				s = DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.DEFAULT,locale).format(value);
			
			}else if(isDate(c)){
				s = DateFormat.getDateInstance(DateFormat.DEFAULT,locale).format(value);
			
			}else if(c.isEnum()){
				String valueName = getValueDisplayName(s, locale, c);
				String firstChar = valueName.substring(0,1).toUpperCase();
				String rest = valueName.substring(1).toLowerCase();
				s = firstChar+rest;
			
			}else if(isBoolean(c)){
				s = getValueDisplayName(s, locale, c);
			
			}
			return s;
			
		}
	}

	private static String getValueDisplayName(String propertyValue, Locale locale, Class<?> propertyClass) {
		return Helpers.getLocalizedDisplayName("TypeValuesBundle", locale, getClassName(propertyClass), propertyValue);
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

	/* following are methods to check if given type belongs to some ordinary & primitive or common & widely accepted classes
	 * */
	
	public static boolean isComparable(Class<?> cl) {
		return Comparable.class.isAssignableFrom(cl);
	}

	public static boolean isDateTime(Class<?> pClass) {
		return 
			isDate(pClass) || 
			isTime(pClass);
	}

	public static boolean isTime(Class<?> c) {
		return
			java.sql.Time.class.isAssignableFrom(c);
	}

	public static boolean isTimestamp(Class<?> c) {
		return 
			java.sql.Timestamp.class.isAssignableFrom(c);
	}
	
	public static boolean isDate(Class<?> c) {
		return
			java.util.Date.class.isAssignableFrom(c)||
			java.sql.Date.class.isAssignableFrom(c)||
			Calendar.class.isAssignableFrom(c);
	}

	public static boolean isNumber(Class<?> cl) {
		return 
			Number.class.isAssignableFrom(cl);
	}

	public static boolean isCurrency(Class<?> cl) {
		return 
			Currency.class.isAssignableFrom(cl);
	}

	public static boolean isInteger(Class<?> cl) {
		return (
			cl.isPrimitive() && (cl==int.class || cl==short.class || cl==byte.class) ||
			!cl.isPrimitive() && 
			(
				Integer.class.isAssignableFrom(cl) ||
				Short.class.isAssignableFrom(cl) ||
				Byte.class.isAssignableFrom(cl)
			)
		);
	}

	public static boolean isLong(Class<?> cl) {
		return (
			cl.isPrimitive() && (cl==long.class) ||
			!cl.isPrimitive() && 
			(
				Long.class.isAssignableFrom(cl)
			)
		);
	}

	public static boolean isString(Class<?> cl) {
		return 
			String.class.isAssignableFrom(cl);
	}

	public static boolean isBoolean(Class<?> pClass){
		return 
			(pClass.isPrimitive() && pClass==boolean.class) || 
			(Boolean.class.isAssignableFrom(pClass));
	}
	
	public static boolean isCharacter(Class<?> pClass){
		return
			pClass.isPrimitive() && (
				pClass==char.class
				) ||
			!pClass.isPrimitive() && (
				Character.class.isAssignableFrom(pClass));

	}
	
	public static boolean isFloatPoint(Class<?> pClass) {
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

	public static boolean isNumeric(Class<?> pClass){
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
	
	public static int getValuesMaxWidth(Class<?> pClass,Locale locale){
		int maxLength=0;
		for(Object v:values(pClass)){
			maxLength=Math.max(
						maxLength, 
						EntityInspector.convertToString(v,locale).length()
					);
		}
		return maxLength;
	}
	
	public static Object[] values(Class<?> pClass){
		if(pClass.isEnum()){
			try {
				// look inside enumeration class for 'values' method and invoke it by means of Java Reflection API
				// thus we collect all defined values of enumeration 
				Method vMethod=pClass.getMethod("values", new Class[]{});
				return (Object[])vMethod.invoke(null, new Object[]{});
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
			return null;
		}else if(isBoolean(pClass)){
			return new Boolean[]{Boolean.TRUE,Boolean.FALSE};
		}
		return null;
	}

	public static EntityType createEntity(EntityInfo eInfo) throws InstantiationException, IllegalAccessException {
		return (EntityType)eInfo.getEntityClass().newInstance();
	}

	public static EntityData initializeEntityData(HTTPForm form,DataSource dataSource) throws InterfaceException {
		return initializeEntityData(form,dataSource,FilterRangeBoundary.START);
	}
	
	public static EntityData initializeEntityData(HTTPForm form,DataSource dataSource,FilterRangeBoundary boundaryKind) throws InterfaceException {
		
		List<InformationPropertyInfo> infoList=dataSource.getSelectedInformationProperties();
		
		List<Object> dataList = new LinkedList<Object>();
		
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
		
		return new EntityData(null,dataList.toArray(),new PropertyList(form,dataSource.getInformationProperties()));
	
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
	
	// set entity bean property value by reflection API
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

	// get entity bean property value by reflection API
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
		return readMethod.invoke(entity, new Object[]{});
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
	
	public static int getDefaultFieldWidth(
			Class<?> propertyType, int columnSize, Locale locale){

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
			return EntityInspector.convertToString(initialValueForType(propertyType),locale).length();
		
		}else if(isBoolean(propertyType) || propertyType.isEnum()){
			return getValuesMaxWidth(propertyType,locale);
		}
		return 0;
		
	}

	private static int getWidthValue(int columnSize,int defaultValue){
		return columnSize==-1?defaultValue:columnSize;
	}

}
