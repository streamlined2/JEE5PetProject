package com.project.interfacebuilder;

import java.awt.Font;
import java.io.Serializable;

import com.project.inspection.property.InformationPropertyInfo;

public interface Selector extends Serializable {
	
	public enum DimensionDependency { NONE, LINEAR };
	public DimensionDependency getWidthDependency();
	public DimensionDependency getHeightDependency();

	public enum OrderPolicy { NOT_SUPPORTED, SUPPORTS };
	public OrderPolicy getOrderPolicy();
	
	public enum OrientationType { HORIZONTAL, VERTICAL }

	public enum CardinalityPolicy { INFINITE, FINITE };
	public CardinalityPolicy getCardinalityPolicy();
	public int getMaxCardinality();
	
	public enum ContentVisibilityPolicy { INVISIBLE, VISIBLE };
	public ContentVisibilityPolicy getContentVisibilityPolicy();
	
	public enum MultiplePolicy { SINGLE, MULTIPLE};
	public MultiplePolicy getMultiplePolicy();
	
	public void setPropertyInfo(InformationPropertyInfo info);
	public InformationPropertyInfo getPropertyInfo();
	
	public Object getInitialValue();
	public Object getSelectedValue();
	public void setInitialValue(Object o);
	public void setValue(Object o);
	
	public String mapStateToValue(String state) throws InterfaceException;
	
	public void place(Form form) throws InterfaceException;
	
	public Font getRenderFont();
	public void setRenderFont(Font font);
	
}
