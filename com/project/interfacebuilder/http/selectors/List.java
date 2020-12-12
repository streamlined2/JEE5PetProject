package com.project.interfacebuilder.http.selectors;

import com.project.interfacebuilder.Form;
import com.project.interfacebuilder.InterfaceException;

public class List extends Combobox {

	private static final long serialVersionUID = -5778258070069269885L;

	public List() {}

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
		return OrderPolicy.SUPPORTS;
	}

	@Override
	public CardinalityPolicy getCardinalityPolicy() {
		return CardinalityPolicy.FINITE;
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

	@Override
	public void place(Form form) throws InterfaceException {
		setVisibleLines(Math.max(getVisibleLines(), 2));
		super.place(form);
	}

}
