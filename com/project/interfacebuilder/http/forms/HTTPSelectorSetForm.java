package com.project.interfacebuilder.http.forms;

import java.util.ArrayList;

import com.project.inspection.EntityInfo.EntityData;
import com.project.inspection.PropertyInfo;
import com.project.inspection.property.InformationPropertyInfo;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.HTTPController;
import com.project.interfacebuilder.http.selectors.HTTPSelector;
import com.project.interfacebuilder.http.selectors.SelectorFactory;
import com.project.datasource.EntityDataSource;

public abstract class HTTPSelectorSetForm extends HTTPDataAwareForm {

	private java.util.List<HTTPSelector> selectors = new ArrayList<HTTPSelector>();
	protected EntityData entityData;

	public HTTPSelectorSetForm() throws InterfaceException {
		super();
	}

	@Override
	public void activate() throws InterfaceException {
		controller.setAttribute(HTTPController.DATA_SOURCE_ATTRIBUTE, getDataSource());
		super.activate();
	}

	protected void addSelectorSet() throws InterfaceException {
		
		checkState();
		
		if(entityData!=null){
			
			out.print("<fieldset>");
			
			out.print("<legend>");
			out.print(getDataSource().getDisplayName());
			out.print("</legend>");
			
			out.print("<table>");
			
			int count=0;
			for(InformationPropertyInfo pInfo:getDataSource().getSelectedInformationProperties()){

				out.print("<tr>");
				
				Integer id=getNextControlId();
				Object value=entityData.getInfoData()[count++];
				HTTPSelector selector = setupSelector(value, pInfo, id);
				placeSelector(id, selector, value, pInfo);
				
				out.print("</tr>");
				
			}
				
			out.print("</table>");
	
			out.print("</fieldset>");
			
		}
			
	}
	
	protected void addUseSelectorFlag(PropertyInfo pInfo){}

	private void placeSelector(
			Integer id, HTTPSelector selector, Object value, InformationPropertyInfo pInfo) throws InterfaceException {
		
		out.print("<font face=\"");
		out.print(fontName);
		out.print("\" size=\"");
		out.print(fontSize);
		out.print("\">");
		
		addUseSelectorFlag(pInfo);
		
		out.print("<td>");

		addLabel(id, pInfo);

		addSelector(selector);
		
		out.print("</td>");

		selectors.add(selector);
		
		out.print("</font>");

	}

	private void addSelector(HTTPSelector selector) throws InterfaceException {
		selector.place(this);
	}

	private void addLabel(Integer id, InformationPropertyInfo pInfo) {

		out.print("<label for=\"");
		out.print(id);
		out.print("\" ");
		out.print(getStyle());
		out.print(" >");
		out.print(pInfo.getDisplayName());
		out.print(" ");
		out.print("</label>");
	
	}

	private HTTPSelector setupSelector(Object value, InformationPropertyInfo pInfo,
			Integer id) throws InterfaceException {
		HTTPSelector selector=SelectorFactory.getSelector(pInfo);
		selector.setPropertyInfo(pInfo);
		selector.setInitialValue(value);
		selector.setId(id);
		return selector;
	}

	@Override
	protected void checkState() throws InterfaceException {
		super.checkState();
		if(!(getDataSource() instanceof EntityDataSource)) throw new InterfaceException("dataSource for HTTPSelectorSetForm must be instance of EntityDataSource");
	}

}
