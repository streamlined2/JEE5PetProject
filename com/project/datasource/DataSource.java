package com.project.datasource;

import java.util.List;
import java.util.Locale;

import com.project.inspection.EntityInfo.EntityData;
import com.project.inspection.Filter;
import com.project.inspection.Ordering;
import com.project.inspection.PropertyList;
import com.project.inspection.Range;
import com.project.inspection.property.InformationPropertyInfo;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.forms.HTTPForm;

//basic behavior for data source determined by this abstract class
public abstract class DataSource {
	
	public abstract boolean isModifiable();
	
	public abstract List<EntityData> get() throws InterfaceException;
	
	public abstract List<InformationPropertyInfo> getInformationProperties() throws InterfaceException;
	
	protected HTTPForm form;
	
	private Range range=null;
	
	public DataSource(HTTPForm form){
		this.form = form;
	}

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
			ordering=new Ordering(form);
		}
		return ordering;
	}

	public void setOrdering(Ordering ordering) {
		this.ordering = ordering;
	}

	private PropertyList propertyList=null;

	public PropertyList getPropertyList() throws InterfaceException {
		if(propertyList==null){
			propertyList=getDefaultPropertyList();
		}
		return propertyList;
	}

	public void setPropertyList(PropertyList propertyList) {
		this.propertyList = propertyList;
	}
	
	public abstract PropertyList getDefaultPropertyList() throws InterfaceException;
	
	private Filter filter=null;

	public void setFilter(Filter filter) {
		this.filter=filter;
	}
	
	public Filter getFilter(){
		if(filter==null){
			filter=new Filter(form);
		}
		return filter;
	}
	
	public abstract String getDisplayName(Locale locale);

	public final List<InformationPropertyInfo> getSelectedInformationProperties() throws InterfaceException{
		return getPropertyList().getSelectedInformationProperties(getInformationProperties());
	}

}
