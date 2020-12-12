package com.project.interfacebuilder;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.project.inspection.Filter.FilterRangeBoundary;
import com.project.inspection.property.PropertyInfo;
import com.project.interfacebuilder.ControllerSupport.FormChainElement;
import com.project.interfacebuilder.transition.Dispatcher.InterfaceContext;


public interface Controller {
	
	public Locale getSelectedLocale();
	public void service() throws InterfaceException;
	public void push(Form target,InterfaceContext context);
	public FormChainElement pop();
	public String getLink();
	
	public Object getAttribute(String name);
	public void setAttribute(String name,Object value);
	
	public String getParameter(String name);
	public Map<String,String[]> getParameters();
	
	public void saveChanges() throws InterfaceException;

	public FilterRangeBoundary getFilterRangeKind();
	public void updateFilterRangeKind();
	
	public Object findPropertyValue(
			PropertyInfo pInfo, List<Action> actions, Map<String, String[]> map) throws InterfaceException;

}
