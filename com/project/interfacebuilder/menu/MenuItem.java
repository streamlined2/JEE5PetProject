package com.project.interfacebuilder.menu;

import java.io.Serializable;

import com.project.Helpers;
import com.project.interfacebuilder.SelectionViewItem;
import com.project.interfacebuilder.http.HTTPInterfaceBuilder.InterfaceContext;

public class MenuItem implements Serializable, SelectionViewItem, Comparable<MenuItem> {
	
	private String name;
	private InterfaceContext context=null;
	
	public MenuItem(String name){
		this.name = name;
	}

	public MenuItem(String name,InterfaceContext context){
		this(name);
		this.context = context;
	}

	@Override
	public String getItemID() {
		return name;
	}

	@Override
	public String getItemName() {
		return Helpers.getLocalizedDisplayName("MenuItemNamesBundle", name, "name");
	}

	public InterfaceContext getContext() {
		return context;
	}

	@Override
	public String getItemDescripion() {
		return Helpers.getLocalizedDisplayName("MenuItemNamesBundle", name, "desc");
	}

	@Override
	public int compareTo(MenuItem item) {
		return getItemName().compareTo(item.getItemName());
	}

}
