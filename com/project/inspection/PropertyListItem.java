package com.project.inspection;

import com.project.inspection.property.InformationPropertyInfo;
import com.project.queries.QueryDefinition;
import com.project.queries.QueryDefinition.Property;

public class PropertyListItem extends ListItem {

	private int order;
	private QueryDefinition.Property property;
	
	public PropertyListItem(QueryDefinition.Property property, int order) {
		super(null);
		this.property = property;
		this.order = order;
	}

	public PropertyListItem(InformationPropertyInfo propertyInfo, int order) {
		super(propertyInfo);
		this.order = order;
	}

	public int getOrder() {
		return order;
	}
	
	@Override
	public boolean equals(Object another){
		if(!(another instanceof PropertyListItem)) return false;
		PropertyListItem item = (PropertyListItem) another;
		return super.equals(item) && property.equals(item.property) && order==item.order;
	}
	
	@Override
	public int hashCode(){
		return (super.hashCode()*31+property.hashCode())*31+order;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PropertyListItem [order=");
		builder.append(order);
		builder.append(", property = ");
		builder.append(property.toString());
		builder.append(", toString()=");
		builder.append(super.toString());
		builder.append("]");
		return builder.toString();
	}

	@Override
	public Property getProperty() {
		return property;
	}
	
}
