package com.project.interfacebuilder.http.forms;

import java.util.Set;

import com.project.Helpers;
import com.project.inspection.PropertyInfo.AlignType;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.SelectionViewItem;
import com.project.interfacebuilder.http.HTTPController;
import com.project.interfacebuilder.http.actions.HTTPCancelAction;
import com.project.interfacebuilder.menu.MenuItem;

public abstract class HTTPSelectionViewForm <ItemType extends SelectionViewItem> extends HTTPForm {
	
	public HTTPSelectionViewForm() throws InterfaceException {
		super();
		addAction(new HTTPCancelAction(),1000);
	}

	protected int visibleLines = 20;
	
	public void setVisibleLines(int visibleLines) {
		this.visibleLines = visibleLines;
	}

	public int getVisibleLines() {
		return visibleLines;
	}

	protected void placeContent() throws InterfaceException {
		addItemListDefinition(getItemSet());
	}

	protected void addItemListDefinition(
			Set<ItemType> itemSet) throws InterfaceException {
		
		StringBuilder buffer=new StringBuilder();

		buffer.append("<pre ").append(getStyle()).append(" >");
		buffer.append(formListTitle());
		buffer.append("</pre>");

		buffer.append("<select name=\"");
		buffer.append(getSelectedItemName());
		buffer.append("\" ");
		buffer.append(getStyle());
		buffer.append("size=").append(getVisibleLines()).append(" ");
		buffer.append(">");
		
		int row=1;
		for(ItemType e:itemSet){
			addRow(
				buffer,
				e,
				row++
			);
		}
		
		buffer.append("</select>");
		
		out.print(buffer);
		
	}

	private void addRow(StringBuilder buffer, ItemType e, int row) throws InterfaceException {
		buffer.append("<option value=\"");
		buffer.append(e.getItemID());
		buffer.append("\" ");
		if(isSelected(e,row)){
			buffer.append("selected");
		}
		buffer.append(">");
		buffer.append(formListValue(e));
		buffer.append("</option>");
}

	protected String formListTitle() throws InterfaceException {
		StringBuilder b=new StringBuilder();
		b.
			append(
				Helpers.padString(
					AlignType.CENTER, 
					getItemNameWidth(), 
					getTitleName(), 
					Helpers.NON_BREAKING_SPACE)
				).
			append(Helpers.NON_BREAKING_SPACE);
		b.
			append(
				Helpers.padString(
					AlignType.CENTER, 
					getItemDescriptionWidth(), 
					getTitleDesc(), 
					Helpers.NON_BREAKING_SPACE
				)
		);
		return b.toString();
	}
	
	private String formListValue(ItemType e) {
		StringBuilder b=new StringBuilder();
		b.
			append(
				Helpers.padString(
					AlignType.CENTER, 
					getItemNameWidth(), 
					e.getItemName(), 
					Helpers.NON_BREAKING_SPACE
				)
			).
			append(Helpers.COLUMN_SEPARATOR);
		b.append(
			Helpers.padString(
				AlignType.LEFT, 
				getItemDescriptionWidth(), 
				e.getItemDescripion(), 
				Helpers.NON_BREAKING_SPACE)
			);
		return b.toString();
	}

	private boolean isSelected(ItemType e, int row) {
		int selectedIndex = getSelectedIndex();
		if(selectedIndex==-1){
			return (row==1); 
		}else{
			return (row==selectedIndex);
		}
	}
	
	protected String getSelectedItemID(){
		String parameterName=getSelectedItemName();
		String selectedItemID=controller.getParameter(parameterName);
		return selectedItemID;
	}
	
	public abstract String getSelectedItemName();
	public abstract int getSelectedItemIndex();
	public abstract ItemType getSelectedItem();
	protected abstract Set<ItemType> getItemSet() throws InterfaceException;

	protected abstract int getItemNameWidth(); 
	protected abstract int getItemDescriptionWidth(); 
	
	protected abstract String getTitleDesc();
	protected abstract String getTitleName();
	
	protected int selectedIndex = -1;
	
	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

	public class SelectionFormState extends FormState {
		
		private int selectedItemIndex = -1;
		
		public SelectionFormState() {
			super();
			selectedItemIndex = selectedIndex;
		}

		@Override
		public void reset() {
			selectedIndex = selectedItemIndex;
		}
		
	}

	@Override
	public State getState() {
		return this.new SelectionFormState();
	}

}
