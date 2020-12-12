package com.project.inspection;

import com.project.inspection.property.InformationPropertyInfo;
import com.project.interfacebuilder.http.forms.HTTPForm;
import com.project.queries.QueryDefinition;
import com.project.queries.QueryDefinition.Property;

public class PropertyListItem extends ListItem {

	private static final long serialVersionUID = -5036246889621861506L;
	
	private int order;
	private QueryDefinition.Property property;
	
	public PropertyListItem(HTTPForm form,QueryDefinition.Property property, int order) {
		super(form,null);
		this.property = property;
		this.order = order;
	}

	public PropertyListItem(HTTPForm form,InformationPropertyInfo propertyInfo, int order) {
		super(form,propertyInfo);
		this.order = order;
	}

	public int getOrder() {
		return order;
	}
	
	@Override
	public boolean equals(Object another){
		if(!(another instanceof PropertyListItem)) return false;
		PropertyListItem item = (PropertyListItem) another;
		return 
			super.equals(item) && 
			property.equals(item.property) && 
			order==item.order;
	}
	
	@Override
	public int hashCode(){
		return (super.hashCode()*31+property.hashCode())*31+order;
	}

	@Override
	public String toString() {
		return new StringBuilder().
			append("PropertyListItem [order=").
			append(order).
			append(", property = ").
			append(property.toString()).
			append(", toString()=").
			append(super.toString()).
			append("]").
		toString();
	}

	@Override
	public Property getProperty() {
		return property;
	}
	
}
