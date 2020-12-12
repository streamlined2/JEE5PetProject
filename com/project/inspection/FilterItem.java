package com.project.inspection;

import com.project.inspection.property.InformationPropertyInfo;
import com.project.queries.QueryDefinition;
import com.project.queries.QueryDefinition.Property;

//immutable filter item class
public class FilterItem extends ListItem {
	
	private Object minValue;
	private Object maxValue;
	private QueryDefinition.InformationProperty property;
	
	public FilterItem(QueryDefinition.InformationProperty property,Object minValue,Object maxValue){
		super(null);
		this.property = property;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	public FilterItem(InformationPropertyInfo propertyInfo,Object minValue,Object maxValue){
		super(propertyInfo);
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
		return super.equals(item) && property.equals(item.property) && minValue.equals(item.minValue) && maxValue.equals(item.maxValue);
	}
	
	@Override
	public int hashCode(){
		return ((super.hashCode()*31+property.hashCode())*31+minValue.hashCode())*31+maxValue.hashCode();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FilterItem [minValue=");
		builder.append(minValue.toString());
		builder.append(", maxValue = ");
		builder.append(maxValue.toString());
		builder.append(", property = ");
		builder.append(property.toString());
		builder.append(", toString()=");
		builder.append(super.toString());
		builder.append("]");
		return builder.toString();
	}

}
