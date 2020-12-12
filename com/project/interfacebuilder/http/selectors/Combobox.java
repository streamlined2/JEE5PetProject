package com.project.interfacebuilder.http.selectors;

import com.project.inspection.EntityInspector;
import com.project.interfacebuilder.Form;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.Selector;
import com.project.interfacebuilder.http.forms.HTTPForm;

public class Combobox extends SelectorSupport {
	
	/**
	 * 
	 */
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

		HTTPForm f=getHTTPForm(form);

		StringBuilder b=new StringBuilder();
		b.append("<select ");
		b.append("name=\"");
		b.append(getPropertyInfo().getPropertyName());
		b.append("\" ");
		b.append("id=\"").append(getId()).append("\" ");
		b.append(" size=").append(visibleLines).append(" ");
		b.append(getStyle());
		b.append(">");
		for(Object value:EntityInspector.values(getPropertyInfo().getType())){
			b.append("<option ");
			
			b.append("value=\"");
			b.append(value.toString());
			b.append("\" ");

			if(value!=null && value.equals(getInitialValue())){
				b.append("selected ");
			}
			b.append(">");
			b.append(EntityInspector.convertToString(value));
			b.append("</option>");
		}
		b.append("</select>");
		
		f.getOut().print(b.toString());
		
	}

}
