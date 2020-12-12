package com.project.interfacebuilder.http.forms;

import java.util.List;

import com.project.Helpers;
import com.project.inspection.EntityInfo;
import com.project.inspection.EntityInfo.EntityData;
import com.project.inspection.EntityInspector;
import com.project.inspection.ListItem;
import com.project.inspection.PropertyListItem;
import com.project.inspection.property.InformationPropertyInfo;
import com.project.inspection.property.PropertyInfo.AlignType;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.HTTPController;
import com.project.interfacebuilder.http.actions.HTTPAddNewAction;
import com.project.interfacebuilder.http.actions.HTTPEditAction;

public class HTTPBrowseForm extends HTTPDataRangeForm {
	
	public HTTPBrowseForm() throws InterfaceException{
		super();

		addAction(new HTTPEditAction());
		addAction(new HTTPAddNewAction());

	}

	@Override
	protected void placeContent() throws InterfaceException {

		addTableDefinition(tableData);
	
	}

	protected int visibleLines = 20;

	public void setVisibleLines(int visibleLines) {
		this.visibleLines = visibleLines;
	}

	public int getVisibleLines() {
		return visibleLines;
	}

	private void addTableDefinition(List<EntityData> tableData) throws InterfaceException {
		
		Object primaryKey=controller.getAttribute(HTTPController.PRIMARY_KEY_ATTRIBUTE);

		Integer browseId=getNextControlId();

		StringBuilder buffer=new StringBuilder()
			.append("<label for=\"")
			.append(browseId)
			.append("\" ")
			.append(">")
		
			.append("<pre ").append(getStyle()).append(" >")
			.append(formListTitle())
			.append("</pre>")

			.append(" ")
			.append("</label>")
		
			.append("<select name=\"")
			.append(HTTPController.PRIMARY_KEY_ATTRIBUTE)
			.append("\" ")
			.append("id=\"").append(browseId).append("\"")
			.append(getStyle())
			.append("size=").append(getVisibleLines()).append(" ")
			.append(">");
			
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
		buffer
			.append("<option value=\"")
			.append(-1)
			.append("\" ")
			.append(">")
			.append(formBlankListValue(columnCount))
			.append("</option>");
	}

	private void addRow(Object primaryKey, StringBuilder buffer, int row,
			EntityInfo.EntityData data) throws InterfaceException {
		String primaryKeyValue=EntityInspector.convertToString(data.getPrimaryKey(),getSelectedLocale());
		buffer
			.append("<option value=\"")
			.append(primaryKeyValue)
			.append("\" ");
		if(isSelected(primaryKey, row, data)){
			buffer.append("selected ");
		}
		buffer
			.append(">")
			.append(formListValue(row,data))
			.append("</option>");
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
			b.append(Helpers.padString(AlignType.CENTER, getColumnWidth(item.getPropertyInfo(),item), item.getDisplayName(), Helpers.NON_BREAKING_SPACE)).append(Helpers.NON_BREAKING_SPACE);
		}
		return b.toString();
	}
	
	private static final int COUNTER_WIDTH=3;
	
	private String formListValue(int row, EntityInfo.EntityData d) throws InterfaceException {
		StringBuilder b=new StringBuilder();
		b.append(Helpers.padString(AlignType.RIGHT, COUNTER_WIDTH, Integer.toString(row), Helpers.NON_BREAKING_SPACE)).append(Helpers.COLUMN_SEPARATOR);
		int k=0;
		
		Object[] data=d.getInfoData();
		for(ListItem pItem:getDataSource().getPropertyList().getOrderedSet()){
			InformationPropertyInfo pInfo=pItem.getPropertyInfo();
			b.append(Helpers.padString(pInfo.getAlignType(), getColumnWidth(pInfo,pItem), EntityInspector.convertToString(data[k++],getSelectedLocale()), Helpers.NON_BREAKING_SPACE));
			if(k<data.length){
				b.append(Helpers.COLUMN_SEPARATOR);
			}
		}
		return b.toString();
	}

	private String formBlankListValue(int columnCount) throws InterfaceException {
		StringBuilder b=new StringBuilder();
		b.append(Helpers.padString(AlignType.RIGHT, COUNTER_WIDTH, "", Helpers.NON_BREAKING_SPACE)).append(Helpers.NON_BREAKING_SPACE);
		int k=0;
		for(ListItem item:getDataSource().getPropertyList().getOrderedSet()){
			InformationPropertyInfo pInfo=item.getPropertyInfo();
			b.append(Helpers.padString(pInfo.getAlignType(), getColumnWidth(pInfo,item), "", Helpers.NON_BREAKING_SPACE));
			if(k++<columnCount){
				b.append(Helpers.NON_BREAKING_SPACE);
			}
		}
		return b.toString();
	}

}
