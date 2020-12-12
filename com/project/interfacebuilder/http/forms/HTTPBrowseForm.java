package com.project.interfacebuilder.http.forms;

import java.util.List;

import com.project.Helpers;
import com.project.inspection.EntityInfo;
import com.project.inspection.EntityInfo.EntityData;
import com.project.inspection.EntityInspector;
import com.project.inspection.InformationPropertyInfo;
import com.project.inspection.PropertyInfo.AlignType;
import com.project.inspection.PropertyListItem;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.HTTPController;
import com.project.interfacebuilder.http.actions.HTTPAddNewAction;
import com.project.interfacebuilder.http.actions.HTTPEditAction;

public class HTTPBrowseForm extends HTTPDataRangeForm {
	
	private int visibleLines=20;
	
	public HTTPBrowseForm() throws InterfaceException{
		super();

		addAction(new HTTPEditAction());
		addAction(new HTTPAddNewAction());

	}

	@Override
	protected void placeContent() throws InterfaceException {

		addTableDefinition(tableData);
	
	}

	private void addTableDefinition(List<EntityData> tableData) throws InterfaceException {
		
		Object primaryKey=controller.getAttribute(HTTPController.PRIMARY_KEY_ATTRIBUTE);

		StringBuilder buffer=new StringBuilder();

		Integer browseId=getNextControlId();
		buffer.append("<label for=\"");
		buffer.append(browseId);
		buffer.append("\" ");
		buffer.append(">");
		
		buffer.append("<pre ").append(getStyle()).append(" >");
		buffer.append(formListTitle());
		buffer.append("</pre>");

		buffer.append(" ");
		buffer.append("</label>");
		
		buffer.append("<select name=\"");
		buffer.append(HTTPController.PRIMARY_KEY_ATTRIBUTE);
		buffer.append("\" ");
		buffer.append("id=\"").append(browseId).append("\"");
		buffer.append(getStyle());
		buffer.append("size=").append(getVisibleLines()).append(" ");
		buffer.append(">");
		
		int columnCount=0;
		
		int row=getStart();
		for(EntityInfo.EntityData data:tableData){
			addRow(primaryKey, buffer, row, data);
			row++;
			columnCount=data.getInfoData().length;
		}
		
		if(tableData.isEmpty()){
			addBlankRow(buffer, columnCount);
		}
		
		buffer.append("</select>");
		
		out.print(buffer);
		
	}

	private void addBlankRow(StringBuilder buffer, int columnCount) throws InterfaceException {
		buffer.append("<option value=\"");
		buffer.append(-1);
		buffer.append("\" ");
		buffer.append(">");
		buffer.append(formBlankListValue(columnCount));
		buffer.append("</option>");
	}

	private void addRow(Object primaryKey, StringBuilder buffer, int row,
			EntityInfo.EntityData data) throws InterfaceException {
		buffer.append("<option value=\"");
		String primaryKeyValue=EntityInspector.convertToString(data.getPrimaryKey());
		buffer.append(primaryKeyValue);
		buffer.append("\" ");
		if(isSelected(primaryKey, row, data)){
			buffer.append("selected ");
		}
		buffer.append(">");
		buffer.append(formListValue(row,data));
		buffer.append("</option>");
	}

	private boolean isSelected(Object primaryKey, int row,
			EntityInfo.EntityData data) {
		return 
			(primaryKey==null && row==getStart()) ||
			(primaryKey!=null && primaryKey.equals(data.getPrimaryKey()));
	}

	private String formListTitle() throws InterfaceException {
		StringBuilder b=new StringBuilder();
		b.append(Helpers.padString(AlignType.CENTER, COUNTER_WIDTH, "", Helpers.NON_BREAKING_SPACE)).append(Helpers.NON_BREAKING_SPACE);
		for(PropertyListItem item:getDataSource().getPropertyList().getOrderedSet()){
			InformationPropertyInfo pInfo=item.getPropertyInfo();
			b.append(Helpers.padString(AlignType.CENTER, getColumnWidth(pInfo), pInfo.getDisplayName(), Helpers.NON_BREAKING_SPACE)).append(Helpers.NON_BREAKING_SPACE);
		}
		return b.toString();
	}
	
	private static final int COUNTER_WIDTH=3;
	
	private String formListValue(int row, EntityInfo.EntityData d) {
		StringBuilder b=new StringBuilder();
		b.append(Helpers.padString(AlignType.RIGHT, COUNTER_WIDTH, Integer.toString(row), Helpers.NON_BREAKING_SPACE)).append(Helpers.COLUMN_SEPARATOR);
		int k=0;
		
		Object[] data=d.getInfoData();
		for(PropertyListItem pItem:getDataSource().getPropertyList().getOrderedSet()){
			InformationPropertyInfo pInfo=pItem.getPropertyInfo();
			b.append(Helpers.padString(pInfo.getAlignType(), getColumnWidth(pInfo), EntityInspector.convertToString(data[k++]), Helpers.NON_BREAKING_SPACE));
			if(k<data.length){
				b.append(Helpers.COLUMN_SEPARATOR);
			}
		}
		return b.toString();
	}

	private Object formBlankListValue(int columnCount) {
		StringBuilder b=new StringBuilder();
		b.append(Helpers.padString(AlignType.RIGHT, COUNTER_WIDTH, "", Helpers.NON_BREAKING_SPACE)).append(Helpers.NON_BREAKING_SPACE);
		int k=0;
		for(PropertyListItem item:getDataSource().getPropertyList().getOrderedSet()){
			InformationPropertyInfo pInfo=item.getPropertyInfo();
			b.append(Helpers.padString(pInfo.getAlignType(), getColumnWidth(pInfo), "", Helpers.NON_BREAKING_SPACE));
			if(k++<columnCount){
				b.append(Helpers.NON_BREAKING_SPACE);
			}
		}
		return b.toString();
	}

	public int getVisibleLines() {
		return visibleLines;
	}

	public void setVisibleLines(int visibleLines) {
		this.visibleLines = visibleLines;
	}

}
