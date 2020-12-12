package com.project.interfacebuilder.http.forms;

import java.util.Locale;
import java.util.Set;

import com.project.Helpers;
import com.project.entities.EntityClass;
import com.project.inspection.EntityInfo;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.HTTPController;
import com.project.interfacebuilder.http.actions.HTTPBrowseAction;
import com.project.interfacebuilder.http.actions.HTTPQueryAction;

// substitute formal parameter ItemType of HTTPSelectionViewForm by actual parameter EntityInfo (instance of SelectionViewItem) to make use of generic class HTTPSelectionViewForm  
public class HTTPEntitySelectionForm extends HTTPSelectionViewForm<EntityInfo> {
	
	private static final int ENTITY_NAME_WIDTH = 15;
	private static final int ENTITY_DESC_WIDTH = 80;
	
	public HTTPEntitySelectionForm() throws InterfaceException {
		super();

		addAction(new HTTPBrowseAction());
		addAction(new HTTPQueryAction());

	}

	@Override
	protected Set<EntityInfo> getItemSet() throws InterfaceException {
		return EntityClass.getEntitySet(getSelectedLocale());
	}

	@Override
	protected int getItemNameWidth() {
		return ENTITY_NAME_WIDTH;
	}

	@Override
	protected int getItemDescriptionWidth() {
		return ENTITY_DESC_WIDTH;
	}

	@Override
	public String getSelectedItemName() {
		return HTTPController.SELECTED_ENTITY_ATTRIBUTE;
	}

	@Override
	protected String getTitleName(Locale locale) {
		return Helpers.getLocalizedDisplayName("EntityNamesBundle", locale, "Entity", "name");
	}

	@Override
	protected String getTitleDesc(Locale locale) {
		return Helpers.getLocalizedDisplayName("EntityNamesBundle", locale, "Entity", "desc");
	}

	@Override
	public int getSelectedItemIndex() {
		try {
			String id = getSelectedItemID();
			int index=0;
			for(EntityInfo eInfo:getItemSet()){
				index++;
				if(eInfo.getItemID().equals(id)){
					return index; 
				}
			}
		} catch (InterfaceException e) {
		}
		return -1;
	}

	@Override
	public EntityInfo getSelectedItem() {
		try {
			String id = getSelectedItemID();
			for(EntityInfo eInfo:getItemSet()){
				if(eInfo.getItemID().equals(id)){
					return eInfo; 
				}
			}
		} catch (InterfaceException e) {
		}
		return null;
	}

}
