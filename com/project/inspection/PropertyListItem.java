package com.project.inspection;

import com.project.inspection.property.InformationPropertyInfo;

public class PropertyListItem extends ListItem {

	private int order;
	
	public PropertyListItem(InformationPropertyInfo propertyInfo, int order) {
		super(propertyInfo);
		this.order = order;
	}

	public int getOrder() {
		return order;
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

}
