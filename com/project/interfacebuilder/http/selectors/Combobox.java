package com.project.interfacebuilder.http.selectors;

import com.project.inspection.EntityInspector;
import com.project.interfacebuilder.Form;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.Selector;

public class Combobox extends SelectorSupport {
	
	private static final long serialVersionUID = -8406843331854035788L;
	
	private int visibleLines;

	public Combobox() {
		setVisibleLines(1);
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
	public CardinalityPolicy getCardinalityPolicy() {
		return CardinalityPolicy.FINITE;
	}

	@Override
	public int getMaxCardinality() {
		return -1;
	}

	@Override
	public OrderPolicy getOrderPolicy() {
		return OrderPolicy.SUPPORTS;
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

	public int getVisibleLines() {
		return visibleLines;
	}

	public void setVisibleLines(int visibleLines) {
		this.visibleLines = visibleLines;
	}

	@Override
	public void place(Form form) throws InterfaceException {

		StringBuilder b=new StringBuilder();
		b
			.append("<select ")
			.append("name=\"")
			.append(getPropertyInfo().getPropertyName())
			.append("\" ")
			.append("id=\"").append(getId()).append("\" ")
			.append(" size=").append(visibleLines).append(" ")
			.append(getStyle())
			.append(">");

		for(Object value:EntityInspector.values(getPropertyInfo().getType())){
			b
				.append("<option ")
				.append("value=\"")
				.append(value.toString())
				.append("\" ");

			if(value!=null && value.equals(getInitialValue())){
				b.append("selected ");
			}
			b
				.append(">")
				.append(EntityInspector.convertToString(value))
				.append("</option>");
		}
		b.append("</select>");
		
		getHTTPForm(form).getOut().print(b.toString());
		
	}

}
