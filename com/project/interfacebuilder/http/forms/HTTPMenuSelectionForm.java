package com.project.interfacebuilder.http.forms;

import java.util.Locale;
import java.util.Set;

import com.project.Helpers;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.HTTPController;
import com.project.interfacebuilder.http.actions.HTTPSelectMenuItemAction;
import com.project.interfacebuilder.menu.Menu;
import com.project.interfacebuilder.menu.MenuItem;

public abstract class HTTPMenuSelectionForm extends HTTPSelectionViewForm<MenuItem> {
	
	private static final int DEFAULT_MENU_ITEM_NAME_WIDTH = 10;
	private static final int DEFAULT_MENU_ITEM_DESC_WIDTH = 80;
	
	private Menu menu;
	
	private int itemNameWidth=DEFAULT_MENU_ITEM_NAME_WIDTH;
	private int itemDescWidth=DEFAULT_MENU_ITEM_DESC_WIDTH;

	public HTTPMenuSelectionForm(Menu menu) throws InterfaceException {
		super();
		
		this.menu = menu;

		addAction(new HTTPSelectMenuItemAction());
	}
	
	public HTTPMenuSelectionForm(Menu menu,int itemNameWidth,int itemDescWidth) throws InterfaceException {
		this(menu);
		this.itemNameWidth = itemNameWidth;
		this.itemDescWidth = itemDescWidth;
	}

	@Override
	protected Set<MenuItem> getItemSet() throws InterfaceException {
		return menu.getMenuItemSet();
	}

	@Override
	protected int getItemNameWidth() {
		return itemNameWidth;
	}

	@Override
	protected int getItemDescriptionWidth() {
		return itemDescWidth;
	}

	@Override
	public String getSelectedItemName() {
		return HTTPController.SELECTED_MENU_ITEM_ATTRIBUTE;
	}
	
	public int getSelectedItemIndex(){
		String id = getSelectedItemID();
		int index=0;
		for(MenuItem item:menu){
			index++;
			if(item.getItemID().equals(id)){
				return index; 
			}
		}
		return -1;
	}

	@Override
	public MenuItem getSelectedItem() {
		String id = getSelectedItemID();
		for(MenuItem item:menu){
			if(item.getItemID().equals(id)){
				return item;
			}
		}
		return null;
	}

	@Override
	protected String getTitleName(Locale locale) {
		return Helpers.getLocalizedDisplayName("EntityNamesBundle", locale, "Menu", "name");
	}

	@Override
	protected String getTitleDesc(Locale locale) {
		return Helpers.getLocalizedDisplayName("EntityNamesBundle", locale, "Menu", "desc");
	}
	
}
