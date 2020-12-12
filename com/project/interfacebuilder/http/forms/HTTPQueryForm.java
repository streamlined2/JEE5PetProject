package com.project.interfacebuilder.http.forms;

import java.awt.Color;
import java.util.List;

import com.project.inspection.EntityInfo.EntityData;
import com.project.inspection.EntityInspector;
import com.project.inspection.InformationPropertyInfo;
import com.project.inspection.PropertyInfo;
import com.project.inspection.PropertyListItem;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.Helpers;
import com.project.interfacebuilder.http.actions.HTTPProceedAction;
import com.project.queries.DataSource;

public class HTTPQueryForm extends HTTPDataRangeForm {
	
	protected int cellPadding = 1;
	protected int cellSpacing=1;
	protected int border=3;
	public enum RuleType { NONE, GROUPS, ROWS, COLS, ALL };
	protected RuleType ruleType=RuleType.ROWS;
	protected String summary="";
	protected String title="";
	protected Color backgroundColor=Color.WHITE;
	protected Color foregroundColor=Color.BLACK;
	
	public HTTPQueryForm() throws InterfaceException{
		super();
		addAction(new HTTPProceedAction());
	}
	
	@Override
	protected void placeContent() throws InterfaceException {
		addTableDefinition(tableData);
	}

	private void addTableDefinition(List<EntityData> tableData) throws InterfaceException {
		
		StringBuilder buffer=new StringBuilder();
		
		buffer.
			append("<table border=\"").
			append(border).
			append("\" cellpadding=\"").
			append(cellPadding).
			append("\" cellspacing=\"").
			append(cellSpacing).
			append("\" rules=\"").
			append(ruleType).
			append("\" title=\"").
			append(title).
			append("\" summary=\"").
			append(summary).
			append("\" ").
			append("style=\"").
			append("background-color: ").
			append(Helpers.getColorCode(backgroundColor)).
			append(";").
			append("color: ").
			append(Helpers.getColorCode(foregroundColor)).
			append("\"").
			append(">");
		
		for(PropertyListItem item:getDataSource().getPropertyList().getOrderedSet()){
			InformationPropertyInfo pInfo=item.getPropertyInfo();
			buffer.append("<th align=\"center\">").append(pInfo.getDisplayName()).append("</th>");
		}

		for(EntityData entityData:tableData){
			
			buffer.append("<tr>");
			
			Object[] infoData=entityData.getInfoData();
			
			int count=0;
			for(PropertyListItem item:getDataSource().getPropertyList().getOrderedSet()){
				InformationPropertyInfo pInfo=item.getPropertyInfo();
				Object value=infoData[count++];
				buffer.
					append("<td align=\"").
					append(pInfo.getAlignType().toString()).
					append("\">").
					append(EntityInspector.convertToString(value)).
					append("</td>");
			}
			
			buffer.append("</tr>");
			
		}
			
		buffer.
			append("</table>");
		
		out.print(buffer);
		
	}

	public int getCellPadding() {
		return cellPadding;
	}

	public void setCellPadding(int cellPadding) {
		this.cellPadding = cellPadding;
	}

	public int getCellSpacing() {
		return cellSpacing;
	}

	public void setCellSpacing(int cellSpacing) {
		this.cellSpacing = cellSpacing;
	}

	public int getBorder() {
		return border;
	}

	public void setBorder(int border) {
		this.border = border;
	}

	public RuleType getRuleType() {
		return ruleType;
	}

	public void setRuleType(RuleType ruleType) {
		this.ruleType = ruleType;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public Color getForegroundColor() {
		return foregroundColor;
	}

	public void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

}
