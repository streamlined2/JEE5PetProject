package com.project.inspection;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.project.Helpers;
import com.project.inspection.OrderingItem.SortOrderType;
import com.project.inspection.property.InformationPropertyInfo;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.HTTPController;
import com.project.interfacebuilder.http.HTTPControllerSupport;
import com.project.interfacebuilder.http.actions.HTTPAction;
import com.project.interfacebuilder.http.forms.HTTPOrderForm;

public class Ordering implements Serializable, ListIterable<OrderingItem>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8069092787682328055L;
	
	private SortedMap<PropertyInfo,OrderingItem> map=new TreeMap<PropertyInfo,OrderingItem>();
	
	public int size(){
		return map.size();
	}
	
	public OrderingItem findByProperty(PropertyInfo pInfo){
		return map.get(pInfo);
	}
	
	public SortedSet<OrderingItem> getOrderedSet(){
		
		SortedSet<OrderingItem> set=new TreeSet<OrderingItem>(new Comparator<OrderingItem>(){
			@Override
			public int compare(OrderingItem a, OrderingItem b) {
				return a.new OrderKey().compareTo(b.new OrderKey());
			}
		});
		
		set.addAll(map.values());
		
		return set;

	}
	
	@Override
	public Iterator<OrderingItem> iterator() {

		final Iterator<Entry<PropertyInfo,OrderingItem>> i=map.entrySet().iterator();
		
		//delegate to intrinsic iterator
		return new Iterator<OrderingItem>() {

			@Override
			public boolean hasNext() {
				return i.hasNext();
			}

			@Override
			public OrderingItem next() {
				Entry<PropertyInfo,OrderingItem> entry=i.next();
				return entry.getValue();
			}

			@Override
			public void remove() {
				i.remove();
			}
			
		};
		
	}

	@Override
	public OrderingItem createItem(
			HTTPController controller,InformationPropertyInfo pInfo,Map<String, String[]> parameters, List<HTTPAction> actions) {
		String orderPriorityParameterName=pInfo.getPropertyName();
		String orderPriorityParameterStringValue=
			HTTPControllerSupport.findParameterValue(parameters,orderPriorityParameterName);
		Integer orderPriority=Integer.valueOf(
			Helpers.getValue(orderPriorityParameterStringValue,"1")
		);
		String orderKindParameterStringValue=
			HTTPControllerSupport.findParameterValue(parameters,HTTPOrderForm.getOrderTypeName(pInfo));
		SortOrderType orderKind=OrderingItem.SortOrderType.valueOf(orderKindParameterStringValue);
		return new OrderingItem(pInfo,orderPriority,orderKind);
	}

	@Override
	public void addItem(OrderingItem item) throws InterfaceException {
		map.put(item.getPropertyInfo(), item);
	}

}
