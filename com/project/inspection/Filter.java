package com.project.inspection;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import com.project.inspection.property.InformationPropertyInfo;
import com.project.inspection.property.PropertyInfo;
import com.project.interfacebuilder.Action;
import com.project.interfacebuilder.Controller;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.forms.HTTPForm;

//Iterable implementation for Java 5 foreach loop
public class Filter implements Serializable, ListIterable<FilterItem> {
	
	private static final long serialVersionUID = -729480478609313439L;

	public enum FilterRangeBoundary { START, FINISH };
	
	private SortedMap<PropertyInfo,FilterItem> map=new TreeMap<PropertyInfo,FilterItem>();
	
	private HTTPForm form;
	
	public Filter(HTTPForm form){
		this.form = form;
	}
	
	public int size(){
		return map.size();
	}
	
	public FilterItem findByProperty(PropertyInfo pInfo){
		return map.get(pInfo);
	}
	
	// Iterator design pattern
	public Iterator<FilterItem> iterator(){
		
		//final reference to host class property
		final Iterator<Entry<PropertyInfo,FilterItem>> i=map.entrySet().iterator();
		
		//anonymous inner class
		return new Iterator<FilterItem>() {

			@Override
			public boolean hasNext() {
				return i.hasNext();
			}

			@Override
			public FilterItem next() {
				return i.next().getValue();
			}

			@Override
			public void remove() {
				//throw new UnsupportedOperationException("remove method is not supported for Filter");
				i.remove();
			}
			
		};
		
	}

	@Override
	public FilterItem createItem(
			Controller controller,
			InformationPropertyInfo pInfo,
			Map<String, String[]> parameters, 
			List<Action> actions) throws InterfaceException {

		FilterItem item=map.get(pInfo);
		Object minValue=null;
		Object maxValue=null;
		if(item!=null){
			minValue=item.getMinValue();
			maxValue=item.getMaxValue();
		}

        Object value=controller.findPropertyValue(pInfo, actions, parameters);
        if(value==null){
            value=EntityInspector.initialValueForType(pInfo.getType());
        }

		FilterRangeBoundary boundary=controller.getFilterRangeKind();
		if(boundary==FilterRangeBoundary.START){
			minValue=value;
			maxValue=value;
		}else if(boundary==FilterRangeBoundary.FINISH){
			maxValue=value;
		}
		return new FilterItem(form, pInfo, minValue, maxValue);
	}

	@Override
	public void addItem(FilterItem item) throws InterfaceException {
		map.put(item.getPropertyInfo(), item);
	}

}
