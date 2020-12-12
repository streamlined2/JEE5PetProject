package com.project.interfacebuilder.http.selectors;

import com.project.interfacebuilder.Form;

public class TextArea extends SelectorSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4128889847843071883L;

	public TextArea() {}

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
		return MultiplePolicy.MULTIPLE;
	}

	@Override
	public void place(Form form) {
		//TODO
	}

}
