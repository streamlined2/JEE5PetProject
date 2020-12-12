package com.project.interfacebuilder.http;

import com.project.interfacebuilder.Controller;

public interface HTTPController extends Controller {
	
	public static final String SELECTED_ENTITY_ATTRIBUTE = "selectedEntity";
	public static final String SELECTED_QUERY_ATTRIBUTE = "selectedQuery";
	public static final String SELECTED_MENU_ITEM_ATTRIBUTE = "selectedMenuItem";
	public static final String PRIMARY_KEY_ATTRIBUTE = "primaryKey";
	public static final String CREATE_NEW_ATTRIBUTE = "createNew";
	public static final String DATA_SOURCE_ATTRIBUTE = "dataSource";
	public static final String RANGE_START_ATTRIBUTE = "rangeStart";
	public static final String RANGE_FINISH_ATTRIBUTE = "rangeFinish";
	public static final String ACTIONS_ATTRIBUTE = "actions";
	public static final String SOURCE_FORM_ATTRIBUTE = "sourceForm";
	public static final String FILTER_RANGE_START = "filterRangeStart";
	public static final String FILTER_RANGE_FINISH = "filterRangeFinish";
	public static final String FILTER_RANGE_GROUP = "filterRangeGroup";
	public static final String WRAPPED_EXCEPTION = "wrappedException";
	public static final String INTERFACE_EXCEPTION_MESSAGE = "exceptionMessage";
	public static final String AVAILABLE_LOCALE_SET_ATTRIBUTE = "availableLocaleSet";
	
}
