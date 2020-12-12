package com.project.interfacebuilder.http.selectors;

import com.project.inspection.EntityInspector;
import com.project.interfacebuilder.Form;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.Selector;
import com.project.interfacebuilder.http.forms.HTTPForm;

public class RadioButtonGroup extends SelectorSupport {
	
	private static final long serialVersionUID = -5584223794463079998L;

	public RadioButtonGroup() {}

	private OrientationType orientation=OrientationType.VERTICAL;
	
	public RadioButtonGroup(OrientationType orientation){
		this.orientation = orientation;
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
	public DimensionDependency getWidthDependency() {
		switch(orientation){
		case HORIZONTAL:
			return Selector.DimensionDependency.LINEAR;
		case VERTICAL:
			return Selector.DimensionDependency.NONE;
		default:
			return Selector.DimensionDependency.NONE;
		}
	}

	@Override
	public DimensionDependency getHeightDependency() {
		switch(orientation){
		case HORIZONTAL:
			return Selector.DimensionDependency.NONE;
		case VERTICAL:
			return Selector.DimensionDependency.LINEAR;
		default:
			return Selector.DimensionDependency.NONE;
		}
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

		String propName=getPropertyInfo().getPropertyName();

		StringBuilder b=new StringBuilder();
		for(Object value:EntityInspector.values(getPropertyInfo().getType())){
			b
				.append("<input type=\"radio\" ")
				.append(getStyle())
				.append("id=\"").append(getId()).append("\" ")
				.append("name=\"").append(propName).append("\" ")
				.append("value=\"").append(value).append("\" ");
			
			if(value!=null && value.equals(getValue())){
				b.append(" checked ");
			}
			
			b.append(" />");

			b.append(EntityInspector.convertToString(value));
			
		}
		
		getHTTPForm(form).getOut().print(b.toString());
		
	}

}
