package com.project.inspection;

import com.project.inspection.property.InformationPropertyInfo;
import com.project.queries.QueryDefinition;
import com.project.queries.QueryDefinition.Property;

public class OrderingItem extends ListItem {
	
	public enum SortOrderType { ASCENDING, DESCENDING };

	private int order;
	private SortOrderType sortOrderType;
	private QueryDefinition.InformationProperty property;

	public OrderingItem(QueryDefinition.InformationProperty property, int order,
			SortOrderType sortOrderType) {
		super(null);
		this.property = property;
		this.order = order;
		this.sortOrderType = sortOrderType;
	}

	public OrderingItem(InformationPropertyInfo propertyInfo, int order,
			SortOrderType sortOrderType) {
		super(propertyInfo);
		this.order = order;
		this.sortOrderType = sortOrderType;
	}

	public int getOrder() {
		return order;
	}

	public SortOrderType getSortOrderType() {
		return sortOrderType;
	}
	
	public class OrderKey implements Comparable<OrderKey> {
		
		private int getKey(){
			return order;
		}

		@Override
		public int compareTo(OrderKey a) {
			return getKey()-a.getKey();
		}
		
	}

	@Override
	public boolean equals(Object another){
		if(!(another instanceof OrderingItem)) return false;
		OrderingItem item = (OrderingItem) another;
		return super.equals(item) && property.equals(item.property) && order==item.order && sortOrderType==item.sortOrderType;
	}
	
	@Override
	public int hashCode(){
		return ((super.hashCode()*31+property.hashCode())*31+order)*31+sortOrderType.ordinal();
	}

	@Override
	public Property getProperty() {
		return property;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FilterItem [order=");
		builder.append(order);
		builder.append(", sortOrderType = ");
		builder.append(sortOrderType.toString());
		builder.append(", property = ");
		builder.append(property.toString());
		builder.append(", toString()=");
		builder.append(super.toString());
		builder.append("]");
		return builder.toString();
	}

}

