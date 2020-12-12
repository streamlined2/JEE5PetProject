package com.project.inspection;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.project.Helpers;
import com.project.datasource.QueryDataSource;
import com.project.inspection.property.InformationPropertyInfo;
import com.project.inspection.property.PropertyInfo;
import com.project.interfacebuilder.Action;
import com.project.interfacebuilder.Controller;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.HTTPControllerSupport;
import com.project.interfacebuilder.http.forms.HTTPForm;
import com.project.queries.QueryDefinition.Property;

public class PropertyList implements Serializable, ListIterable<PropertyListItem> {

	private static final long serialVersionUID = 5060808322150957060L;
	
	private HTTPForm form;
	
	private SortedMap<InformationPropertyInfo,PropertyListItem> map=new TreeMap<InformationPropertyInfo,PropertyListItem>();
	
	public PropertyList(HTTPForm form,List<InformationPropertyInfo> infoProperties){
		this.form = form;
		int k=1;
		for(InformationPropertyInfo pInfo:infoProperties){
			map.put(pInfo, new PropertyListItem(form,pInfo, k++));
		}
	}
	
	public PropertyList(HTTPForm form,QueryDataSource queryDataSource) throws InterfaceException{
		this.form = form;
		int k=1;
		for(Property property:queryDataSource.getQueryDefinition().getInfoGroupProperties()){
			PropertyListItem item = new PropertyListItem(form,property, k++);
			map.put(item.getPropertyInfo(), item);
		}
	}
	
	public PropertyList(HTTPForm form,EntityInfo eInfo){
		this.form = form;
		int k=1;
		for(InformationPropertyInfo pInfo:eInfo.getInfoFields()){
			map.put(pInfo, new PropertyListItem(form,pInfo, k++));
		}
	}
	
	public int size(){
		return map.size();
	}
	
	public PropertyListItem findByProperty(PropertyInfo pInfo){
		return map.get(pInfo);
	}
	
	public final List<InformationPropertyInfo> getSelectedInformationProperties(List<InformationPropertyInfo> properties){
		List<InformationPropertyInfo> set = new LinkedList<InformationPropertyInfo>();
		for(InformationPropertyInfo infoProperty:properties){
			if(findByProperty(infoProperty)!=null){
				set.add(infoProperty);
			}
		}
		return set;
	}
	
	public SortedSet<PropertyListItem> getOrderedSet(){
		
		SortedSet<PropertyListItem> set=new TreeSet<PropertyListItem>(new Comparator<PropertyListItem>(){
			@Override
			public int compare(PropertyListItem a, PropertyListItem b) {
				return a.getOrder()-b.getOrder();
			}
		});
		
		set.addAll(map.values());
		
		return set;

	}
	
	@Override
	public Iterator<PropertyListItem> iterator() {

		final Iterator<Entry<InformationPropertyInfo,PropertyListItem>> i=map.entrySet().iterator();
		
		return new Iterator<PropertyListItem>() {

			@Override
			public boolean hasNext() {
				return i.hasNext();
			}

			@Override
			public PropertyListItem next() {
				Entry<InformationPropertyInfo,PropertyListItem> entry=i.next();
				return entry.getValue();
			}

			@Override
			public void remove() {
				i.remove();
			}
			
		};
		
	}

	@Override
	public PropertyListItem createItem(
			Controller controller,InformationPropertyInfo pInfo,Map<String, String[]> parameters, List<Action> actions) {
		
		String orderParameterName=pInfo.getPropertyName();
		
		String orderParameterStringValue=
			HTTPControllerSupport.findParameterValue(parameters,orderParameterName);
		
		Integer order=Integer.valueOf(
			Helpers.getValue(orderParameterStringValue,"1")
		);
		
		return new PropertyListItem(form,pInfo,order);
	}

	@Override
	public void addItem(PropertyListItem item) throws InterfaceException {
		map.put(item.getPropertyInfo(), item);
	}

	public void add(PropertyList list){
		map.putAll(list.map);
	}

}
