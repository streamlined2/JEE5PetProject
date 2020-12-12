package com.project.inspection;

import com.project.inspection.property.InformationPropertyInfo;
import com.project.queries.QueryDefinition;

public class PropertyListItem extends ListItem {

	private int order;
	
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
		return getPropertyInfo().equals(item.getPropertyInfo()) && order==item.order;
	}
	
	@Override
	public int hashCode(){
		return getPropertyInfo().hashCode()*31+order;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PropertyListItem [order=");
		builder.append(order);
		builder.append(", toString()=");
		builder.append(super.toString());
		builder.append("]");
		return builder.toString();
	}

}
