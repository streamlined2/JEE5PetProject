package com.project.interfacebuilder.http.selectors;

import java.util.Map;
import java.util.TreeMap;

import com.project.inspection.PropertyInfo.FiniteType;
import com.project.inspection.PropertyInfo.MultipleType;
import com.project.inspection.PropertyInfo.OrderType;
import com.project.inspection.property.InformationPropertyInfo;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.Selector.CardinalityPolicy;
import com.project.interfacebuilder.Selector.MultiplePolicy;
import com.project.interfacebuilder.Selector.OrderPolicy;
import com.project.interfacebuilder.Selector.OrientationType;

public final class SelectorFactory {
	
	static{
		map=new TreeMap<SelectorDataKey,HTTPSelector>();
		prepareSelectorData();
	}
	
	//interface-specific list of property value selectors
	private static void prepareSelectorData() {
		
		putSelectorData(new Checkbox());
		putSelectorData(new Combobox());
		putSelectorData(new List());
		putSelectorData(new MultipleList());
		putSelectorData(new RadioButtonGroup(OrientationType.HORIZONTAL));
		putSelectorData(new RadioButtonGroup(OrientationType.VERTICAL));
		putSelectorData(new TextArea());
		putSelectorData(new TextLine());
		
		/*printSelectorData();*/

	}
	
	private static TreeMap<SelectorDataKey,HTTPSelector> map;
	
/*	private static void printSelectorData() {
		for(SelectorDataKey key:map.keySet()){
			HTTPSelector sel=map.get(key);
			System.out.println(
					key.getMultipleType()+","+
					key.getFiniteType()+","+
					key.getOrderType()+","+key.getMaxCardinality()+","+
					sel.getClass().getName());
		}
	}*/

	private static class SelectorDataKey implements Comparable<SelectorDataKey> {

		private OrderType orderType;
		private FiniteType finiteType;
		private int maxCardinality;
		private MultipleType multipleType;
		private String selectorClassName;
		
		SelectorDataKey(
				OrderType oPolicy, 
				FiniteType cPolicy,int maxCardinality,
				MultipleType multipleType,
				String selectorClassName){
			this.orderType = oPolicy;
			this.finiteType = cPolicy;
			this.maxCardinality = maxCardinality;
			this.multipleType = multipleType;
			this.selectorClassName = selectorClassName;
		}
		
		private String getKey(){
			StringBuilder s=new StringBuilder();
			s
				.append(multipleType==MultipleType.SINGLE?"0":"1")
				.append(finiteType==FiniteType.FINITE?"0":"1")
				.append(Integer.toString(maxCardinality))
				.append(orderType==OrderType.UNORDERED?"0":"1")
				.append(selectorClassName);
			return s.toString();
		}

		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof SelectorDataKey)) return false;

			SelectorDataKey key=(SelectorDataKey)obj;
			return key.getKey().equals(getKey()); 
		}

		@Override
		public int compareTo(SelectorDataKey o) {
			return getKey().compareTo(o.getKey());
		}
		
		@Override
		public int hashCode(){
			return getKey().hashCode();
		}

	}
	
	private static void putSelectorData(HTTPSelector selector){
		MultiplePolicy multiplePolicy=selector.getMultiplePolicy();
		if(multiplePolicy==MultiplePolicy.MULTIPLE){
			unfoldMultiplePolicy(selector,MultipleType.MULTIPLE);
		}else{
			unfoldMultiplePolicy(selector,MultipleType.SINGLE);
		}
	}
	
	private static void unfoldMultiplePolicy(HTTPSelector selector,MultipleType multiple){
		OrderPolicy oPolicy=selector.getOrderPolicy();
		unfoldOrderPolicy(selector,multiple,OrderType.UNORDERED);
		if(oPolicy==OrderPolicy.SUPPORTS){
			unfoldOrderPolicy(selector,multiple,OrderType.ORDERED);
		}
	}
	
	private static void unfoldOrderPolicy(HTTPSelector selector, MultipleType multipleType, OrderType ordered) {
		CardinalityPolicy cPolicy=selector.getCardinalityPolicy();
		if(cPolicy==CardinalityPolicy.FINITE){
			int maxCardinality=selector.getMaxCardinality();
			if(maxCardinality>0){
				unfoldCardinalityPolicy(selector,multipleType,ordered,FiniteType.FINITE,maxCardinality);
			}else{
				unfoldCardinalityPolicy(selector,multipleType,ordered,FiniteType.FINITE,Integer.MAX_VALUE);
			}
		}else{
			unfoldCardinalityPolicy(selector,multipleType,ordered,FiniteType.INFINITE,Integer.MAX_VALUE);
			unfoldCardinalityPolicy(selector,multipleType,ordered,FiniteType.FINITE,Integer.MAX_VALUE);
		}
	}

	private static void unfoldCardinalityPolicy(HTTPSelector selector, 
			MultipleType multipleType,OrderType ordered, FiniteType finite, int maxCardinality) {
		SelectorDataKey key=new SelectorDataKey(
				ordered, finite, maxCardinality, 
				multipleType, 
				selector.getClass().getName());
		map.put(key,selector);
	}

	public static HTTPSelector getSelector(InformationPropertyInfo type) throws InterfaceException{
		
		OrderType order=type.getOrderType();
		FiniteType finite=type.getFiniteType();
		MultipleType multiple=type.getMultipleType();
		int cardinality=type.getCardinality();
		
		SelectorDataKey minKey=
			new SelectorDataKey(order, finite, cardinality, multiple, "");
		
		Map.Entry<SelectorDataKey,HTTPSelector> entry=map.ceilingEntry(minKey);
		if(entry==null) throw new InterfaceException("selector entry mapping failed for key "+minKey);

		Class<? extends HTTPSelector> cl=entry.getValue().getClass();
		HTTPSelector selector=null;
		try {
			selector = cl.newInstance();
			return selector;
		} catch (InstantiationException e) {
			throw new InterfaceException(e);
		} catch (IllegalAccessException e) {
			throw new InterfaceException(e);
		}
		
	}
	
}
