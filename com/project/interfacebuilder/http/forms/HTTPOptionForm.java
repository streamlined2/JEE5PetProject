package com.project.interfacebuilder.http.forms;

import java.util.Set;

import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.SelectionViewItem;
import com.project.interfacebuilder.http.actions.HTTPCancelAction;

public abstract class HTTPOptionForm <ItemType extends SelectionViewItem> extends HTTPForm {

	protected int visibleLines = 20;

	public HTTPOptionForm() throws InterfaceException {
		super();
		addAction(new HTTPCancelAction(),1000);
	}

	@Override
	protected void placeContent() throws InterfaceException {
		StringBuilder buffer=new StringBuilder();
		
		buffer
			.append("<pre ").append(getStyle()).append(" >")
			.append(formListTitle())
			.append("</pre>")
			.append("<select name=\"")
			.append(getSelectedItemName())
			.append("\" ")
			.append(getStyle())
			.append("size=").append(getVisibleLines()).append(" ")
			.append(">");
		
		int row=1;
		for(ItemType e:getItemSet()){
			buffer
				.append("<option value=\"")
				.append(e.getItemID())
				.append("\" ");
			if(isSelected(e,row++)){
				buffer.append("selected");
			}
			buffer
				.append(">")
				.append(formListValue(e))
				.append("</option>");
		}
		
		buffer.append("</select>");
		
		out.print(buffer);
	}
	
	protected abstract String formListTitle() throws InterfaceException;

	protected abstract String formListValue(ItemType e);

	public void setVisibleLines(int visibleLines) {
		this.visibleLines = visibleLines;
	}

	public int getVisibleLines() {
		return visibleLines;
	}

	public abstract String getSelectedItemName();
	public abstract int getSelectedItemIndex() throws InterfaceException;
	public abstract ItemType getSelectedItem() throws InterfaceException;
	protected abstract Set<ItemType> getItemSet() throws InterfaceException;

	protected int selectedIndex = -1;
	
	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

	protected boolean isSelected(ItemType e, int row) {
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
