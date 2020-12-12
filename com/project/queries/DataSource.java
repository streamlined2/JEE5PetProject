package com.project.queries;

import java.util.List;
import java.util.Set;

import com.project.inspection.Filter;
import com.project.inspection.InformationPropertyInfo;
import com.project.inspection.Ordering;
import com.project.inspection.PropertyList;
import com.project.inspection.Range;
import com.project.inspection.EntityInfo.EntityData;
import com.project.interfacebuilder.InterfaceException;

public abstract class DataSource {
	
	public abstract List<EntityData> get() throws InterfaceException;
	
	public abstract List<InformationPropertyInfo> getInformationProperties();
	
	private Range range=null;

	public Range getRange() {
		if(range==null){
			range=new Range(0,0);
		}
		return range;
	}

	public void setRange(Range range) {
		this.range = range;
	}

	private Ordering ordering=null;

	public Ordering getOrdering() {
		if(ordering==null){
			ordering=new Ordering();
		}
		return ordering;
	}

	public void setOrdering(Ordering ordering) {
		this.ordering = ordering;
	}

	private PropertyList propertyList=null;

	public PropertyList getPropertyList() {
		if(propertyList==null){
			propertyList=getDefaultPropertyList();
		}
		return propertyList;
	}

	public void setPropertyList(PropertyList propertyList) {
		this.propertyList = propertyList;
	}
	
	public abstract PropertyList getDefaultPropertyList();
	
	private Filter filter=null;

	public void setFilter(Filter filter) {
		this.filter=filter;
	}
	
	public Filter getFilter(){
		if(filter==null){
			filter=new Filter();
		}
		return filter;
	}
	
	public abstract String getDisplayName();

	public final List<InformationPropertyInfo> getSelectedInformationProperties(){
		return getPropertyList().getSelectedInformationProperties(getInformationProperties());
	}

}
