package com.project.interfacebuilder.http.selectors;

import com.project.inspection.EntityInspector;
import com.project.interfacebuilder.Form;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.forms.HTTPForm;

public class TextLine extends SelectorSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3062127441788982026L;
	
	private int maxLength=-1;

	public TextLine() {}

	public TextLine(int size) {
		Math.max(size, 1);
	}

	public TextLine(int size, int maxLength) {
		Math.max(size, 1);
		this.maxLength=Math.max(maxLength, size);
	}

	@Override
	public DimensionDependency getWidthDependency() {
		return DimensionDependency.NONE;
	}

	@Override
	public DimensionDependency getHeightDependency() {
		return DimensionDependency.NONE;
	}

	@Override
	public OrderPolicy getOrderPolicy() {
		return OrderPolicy.NOT_SUPPORTED;
	}

	@Override
	public CardinalityPolicy getCardinalityPolicy() {
		return CardinalityPolicy.INFINITE;
	}

	@Override
	public int getMaxCardinality() {
		return -1;
	}

	@Override
	public ContentVisibilityPolicy getContentVisibilityPolicy() {
		return ContentVisibilityPolicy.VISIBLE;
	}

	@Override
	public Object getInitialValue() {
		return super.getInitialValue();
	}

	@Override
	public Object getSelectedValue() {
		return super.getValue();
	}

	@Override
	public void setValue(Object o) {
		super.setValue(o);
	}

	@Override
	public MultiplePolicy getMultiplePolicy() {
		return MultiplePolicy.SINGLE;
	}

	public int getSize() {
		return getPropertyInfo().getWidth();
	}

	public void setSize(int size) {
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	@Override
	public void place(Form form) throws InterfaceException {
		
		HTTPForm f=getHTTPForm(form);

		StringBuilder b=new StringBuilder();
		b.append("<input type=\"text\" ");
		b.append(getStyle());
		b.append("name=\"").append(getPropertyInfo().getPropertyName()).append("\" ");
		b.append("id=\"").append(getId()).append("\" ");
		b.append("value=\"").append(EntityInspector.convertToString(getValue())).append("\" ");
		b.append("size=\"").append(getSize()).append("\" ");
		if(maxLength>0){
			b.append("maxlength=\"").append(getMaxLength()).append("\" ");
		}
		b.append(" />");
		
		f.getOut().print(b.toString());
		
	}

}
