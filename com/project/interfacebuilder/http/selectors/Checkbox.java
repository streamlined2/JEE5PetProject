package com.project.interfacebuilder.http.selectors;

import com.project.inspection.EntityInspector;
import com.project.interfacebuilder.Form;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.Selector;
import com.project.interfacebuilder.http.forms.HTTPForm;

public class Checkbox extends SelectorSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7938515626661336508L;

	public Checkbox() {}

	@Override
	public CardinalityPolicy getCardinalityPolicy() {
		return CardinalityPolicy.FINITE;
	}

	@Override
	public int getMaxCardinality() {
		return 2;
	}

	@Override
	public DimensionDependency getWidthDependency() {
		return Selector.DimensionDependency.NONE;
	}

	@Override
	public DimensionDependency getHeightDependency() {
		return Selector.DimensionDependency.NONE;
	}

	@Override
	public OrderPolicy getOrderPolicy() {
		return OrderPolicy.NOT_SUPPORTED;
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

		HTTPForm f=getHTTPForm(form);

		StringBuilder b=new StringBuilder();
		b.append("<input type=\"checkbox\" ");
		b.append(getStyle());
		b.append("id=\"").append(getId()).append("\" ");
		b.append("name=\"").append(getPropertyInfo().getPropertyName()).append("\"");
		if(getValue()!=null && getValue().equals(EntityInspector.firstValue(getPropertyInfo().getType()))){
			b.append(" checked ");
		}
		b.append(" />");
		
		f.getOut().print(b.toString());
	}

	public String mapStateToValue(String state) throws InterfaceException{
		if(!(state instanceof String)) throw new InterfaceException("Checkbox state must be of String type");
		String st=(String)state;
		if(st.equalsIgnoreCase("ON")){
			return EntityInspector.firstValue(getPropertyInfo().getType()).toString();
		}else{
			return EntityInspector.valueAt(getPropertyInfo().getType(), 1).toString();
		}
	}
	
}
