package com.project.interfacebuilder.http.forms;

import java.util.List;

import com.project.inspection.ListItem;
import com.project.inspection.EntityInfo.EntityData;
import com.project.inspection.property.InformationPropertyInfo;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.HTTPController;
import com.project.interfacebuilder.http.actions.HTTPCancelAction;
import com.project.interfacebuilder.http.actions.HTTPFilterAction;
import com.project.interfacebuilder.http.actions.HTTPOrderAction;
import com.project.interfacebuilder.http.actions.HTTPPropertyListAction;
import com.project.interfacebuilder.http.actions.HTTPRangeAction;
import com.project.queries.DataSource;

public abstract class HTTPDataRangeForm extends HTTPDataAwareForm {

	public static final Integer RANGE_UNDEFINED = -1;
	protected Integer start = RANGE_UNDEFINED;
	protected Integer finish = RANGE_UNDEFINED;
	protected List<EntityData> tableData = null;
	
	public HTTPDataRangeForm() throws InterfaceException {
		super();

		addAction(new HTTPFilterAction(),1000);
		addAction(new HTTPRangeAction(),2000);
		addAction(new HTTPOrderAction(),3000);
		addAction(new HTTPPropertyListAction(),4000);
		addAction(new HTTPCancelAction(),5000);
	}

	@Override
	public void setDataSource(DataSource dataSource) {
		if(!dataSource.equals(getDataSource())){
			setStart(RANGE_UNDEFINED);
			setFinish(RANGE_UNDEFINED);
			super.setDataSource(dataSource);
		}
	}
	
	public Integer getStart() {
		return start;
	}
	
	public Integer getStartIndex(){
		return getStart()-1;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getFinish() {
		return finish;
	}

	public void setFinish(Integer finish) {
		this.finish = finish;
	}

	private boolean incorrectOrUndefinedRange() {
		return 
			(start==RANGE_UNDEFINED || finish==RANGE_UNDEFINED) ||
			(start>finish) || (start<FIRST_ROW);
	}
	
	private static int FIRST_ROW=1;
	private static int LAST_ROW=Integer.MAX_VALUE;
	
	@Override
	public void activate() throws InterfaceException {
	
		int start=RANGE_UNDEFINED;
		int finish=RANGE_UNDEFINED;
		if(incorrectOrUndefinedRange()){
			start=FIRST_ROW;
			finish=LAST_ROW;
			setStart(start);
			setFinish(finish);
		}else{
			start=getStart();
			finish=getRowCount();
		}
		
		getDataSource().getRange().setStartFrom(getStartIndex());
		getDataSource().getRange().setCount(getRowCount());
		
		tableData=getDataSource().get();

		setFinish(getStart()+tableData.size()-1);

		controller.setAttribute(HTTPController.DATA_SOURCE_ATTRIBUTE, getDataSource());
		
		super.activate();
	
	}

	public int getRowCount() {
		return getFinish()-getStart()+1;
	}
	
	protected int getColumnWidth(InformationPropertyInfo pInfo,ListItem item) throws InterfaceException{
		int titleWidth=item.getDisplayName().length();
		int contentWidth=pInfo.getWidth();
		return Math.max(titleWidth, contentWidth);
	}

}
