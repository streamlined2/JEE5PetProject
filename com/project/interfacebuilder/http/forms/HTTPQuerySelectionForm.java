package com.project.interfacebuilder.http.forms;

import java.util.Set;

import com.project.Helpers;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.HTTPController;
import com.project.interfacebuilder.http.actions.HTTPRunQueryAction;
import com.project.queries.QueryDefinition;
import com.project.queries.QueryList;
import com.project.queries.QueryListBuilder;

public class HTTPQuerySelectionForm extends HTTPSelectionViewForm<QueryDefinition> {
	
	private static final int QUERY_NAME_WIDTH = 10;
	private static final int QUERY_DESC_WIDTH = 60;
	
	private QueryList queries=QueryListBuilder.createQueryList();
	
	public HTTPQuerySelectionForm() throws InterfaceException {
		super();

		addAction(new HTTPRunQueryAction());

	}

	protected Set<QueryDefinition> getItemSet() throws InterfaceException {
		return queries.getQuerySet();
	}

	@Override
	protected int getItemNameWidth() {
		return QUERY_NAME_WIDTH;
	}

	@Override
	protected int getItemDescriptionWidth() {
		return QUERY_DESC_WIDTH;
	}

	@Override
	public String getSelectedItemName() {
		return HTTPController.SELECTED_QUERY_ATTRIBUTE;
	}

	@Override
	protected String getTitleName() {
		return Helpers.getLocalizedDisplayName("EntityNamesBundle", "Query", "name");
	}

	@Override
	protected String getTitleDesc() {
		return Helpers.getLocalizedDisplayName("EntityNamesBundle", "Query", "desc");
	}

	@Override
	public int getSelectedItemIndex() {
		String id = getSelectedItemID();
		int index=0;
		for(QueryDefinition def:queries){
			index++;
			if(def.getItemID().equals(id)){
				return index;
			}
		}
		return -1;
	}

	@Override
	public QueryDefinition getSelectedItem() {
		String id = getSelectedItemID();
		for(QueryDefinition def:queries){
			if(def.getItemID().equals(id)){
				return def;
			}
		}
		return null;
	}

}
