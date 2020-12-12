package com.project.queries;

import java.io.Serializable;

import com.project.Helpers;
import com.project.inspection.EntityInspector;
import com.project.interfacebuilder.SelectionViewItem;

public class QueryDefinition implements Serializable, SelectionViewItem, Comparable<QueryDefinition> {
	
	private String name;
	private String description;
	private String statement;
	
	public QueryDefinition(String name,String description,String statement){
		this.name = name;
		this.description = description;
		this.statement = statement;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getStatement() {
		return statement;
	}

	@Override
	public String getItemID() {
		return getName();
	}

	@Override
	public String getItemName() {
		return Helpers.getLocalizedDisplayName("MenuItemNamesBundle", name, "name");
	}
	
	@Override
	public String getItemDescripion() {
		return Helpers.getLocalizedDisplayName("MenuItemNamesBundle", name, "desc");
	}

	@Override
	public int compareTo(QueryDefinition o) {
		return getItemName().compareTo(o.getItemName());
	}

}
