package com.project.inspection;

import com.project.inspection.property.InformationPropertyInfo;
import com.project.interfacebuilder.http.forms.HTTPForm;
import com.project.queries.QueryDefinition;
import com.project.queries.QueryDefinition.Property;

//immutable filter item class
public class FilterItem extends ListItem {
	
	private static final long serialVersionUID = 2445893979584491678L;
	
	private Object minValue;
	private Object maxValue;
	private QueryDefinition.InformationProperty property;
	
	public FilterItem(HTTPForm form,QueryDefinition.InformationProperty property,Object minValue,Object maxValue){
		super(form,null);
		this.property = property;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	public FilterItem(HTTPForm form,InformationPropertyInfo propertyInfo,Object minValue,Object maxValue){
		super(form,propertyInfo);
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	public Object getMinValue() {
		return minValue;
	}

	public Object getMaxValue() {
		return maxValue;
	}

	@Override
	public Property getProperty() {
		return property;
	}
	
	@Override
	public boolean equals(Object another){
		if(!(another instanceof FilterItem)) return false;
		FilterItem item = (FilterItem) another;
		return 
			super.equals(item) && 
			property.equals(item.property) && 
			minValue.equals(item.minValue) && 
			maxValue.equals(item.maxValue);
	}
	
	@Override
	public int hashCode(){
		return ((super.hashCode()*31+property.hashCode())*31+minValue.hashCode())*31+maxValue.hashCode();
	}

	@Override
	public String toString() {
		return new StringBuilder().
			append("FilterItem [minValue=").
			append(minValue.toString()).
			append(", maxValue = ").
			append(maxValue.toString()).
			append(", property = ").
			append(property.toString()).
			append(", toString()=").
			append(super.toString()).
			append("]").
		toString();
	}

}
