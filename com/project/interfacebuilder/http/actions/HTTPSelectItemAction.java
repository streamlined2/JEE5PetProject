package com.project.interfacebuilder.http.actions;

import com.project.interfacebuilder.ControllerSupport.FormContextItem;
import com.project.interfacebuilder.Form;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.SelectionViewItem;
import com.project.interfacebuilder.http.forms.HTTPSelectionViewForm;

public class HTTPSelectItemAction <ItemType extends SelectionViewItem> extends HTTPActionSupport {
	
	public HTTPSelectItemAction(String actionName){
		super(actionName);
	}

	@Override
	public void perform() throws InterfaceException {
	}
	
	@Override
	public FormContextItem findTarget(Form srcForm)
			throws InterfaceException {

		HTTPSelectionViewForm<ItemType> sourceForm = getSourceSVForm();
		int itemIndex = sourceForm.getSelectedItemIndex();
		sourceForm.setSelectedIndex(itemIndex);
		
		return super.findTarget(sourceForm);
	}

	protected HTTPSelectionViewForm<ItemType> getSourceSVForm() throws InterfaceException {
		
		if(!(getSourceForm() instanceof HTTPSelectionViewForm<?>)) throw new InterfaceException("source form must instance of HTTPSelectionViewForm");
		@SuppressWarnings("unchecked")
		HTTPSelectionViewForm<ItemType> sourceForm = (HTTPSelectionViewForm<ItemType>)getSourceForm();
		
		return sourceForm;
		
	}
	
	protected ItemType getSelectedItem() throws InterfaceException {
		
		HTTPSelectionViewForm<ItemType> sourceForm = getSourceSVForm();
		
		ItemType selectedItem = sourceForm.getSelectedItem();
		if(selectedItem==null) throw new InterfaceException(sourceForm.getSelectedItemName()+" parameter must be set after HTTPSelectionViewForm submission");
		
		return selectedItem;
	}

}
