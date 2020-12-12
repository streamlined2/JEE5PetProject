package com.project.interfacebuilder.http;

import java.util.List;
import java.util.Map;

import com.project.inspection.Filter.FilterRangeBoundary;
import com.project.inspection.PropertyInfo;
import com.project.interfacebuilder.Controller;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.actions.HTTPAction;

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
	
	public Object getAttribute(String name);
	public void setAttribute(String name,Object value);
	
	public String getParameter(String name);
	public Map<String,String[]> getParameters();
	
	public String getLink();
	
	public void saveChanges() throws InterfaceException;

	public Object findPropertyValue(
			PropertyInfo pInfo, List<HTTPAction> actions, Map<String, String[]> map) throws InterfaceException;

	public FilterRangeBoundary getFilterRangeKind();
	public void updateFilterRangeKind();

}
