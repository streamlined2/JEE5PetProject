package com.project.inspection;

public class OrderingItem extends ListItem {
	
	public enum SortOrderType { ASCENDING, DESCENDING };

	private int order;
	private SortOrderType sortOrderType;

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


}

