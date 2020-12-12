package com.project.inspection;

//immutable filter item class
public class FilterItem extends ListItem {
	
	private Object minValue;
	private Object maxValue;
	
	public FilterItem(InformationPropertyInfo pInfo,Object minValue,Object maxValue){
		super(pInfo);
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	public Object getMinValue() {
		return minValue;
	}

	public Object getMaxValue() {
		return maxValue;
	}
	
}
