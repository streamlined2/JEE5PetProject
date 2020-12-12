package com.project.interfacebuilder.menu;

import java.io.Serializable;

import com.project.Helpers;
import com.project.interfacebuilder.SelectionViewItem;
import com.project.interfacebuilder.transition.Dispatcher.InterfaceContext;

public class MenuItem implements Serializable, SelectionViewItem, Comparable<MenuItem> {
	
	private static final long serialVersionUID = 4297802628035969207L;
	
	private String name;
	private InterfaceContext context=null;
	private int order = 0;
	
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
	
	public int getOrder(){
		return order;
	}
	
	public void setOrder(int order){
		this.order = order;
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
		return getOrder()-item.getOrder();
	}

}
