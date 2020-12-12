package com.project.interfacebuilder.http.actions;

import com.project.interfacebuilder.ControllerSupport.FormChainElement;
import com.project.interfacebuilder.Form;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.SelectionViewItem;
import com.project.interfacebuilder.http.forms.HTTPSelectionViewForm;

public abstract class HTTPSelectItemAction <ItemType extends SelectionViewItem> extends HTTPActionSupport {
	
	public HTTPSelectItemAction(String actionName){
		super(actionName);
	}

	@Override // find target form for selected menu item
	public FormChainElement findTarget(Form srcForm)
			throws InterfaceException {

		HTTPSelectionViewForm<ItemType> sourceForm = getSourceSelectionViewForm();
		int itemIndex = sourceForm.getSelectedItemIndex();
		sourceForm.setSelectedIndex(itemIndex);
		
		return super.findTarget(sourceForm);
	}

	@SuppressWarnings("unchecked")
	protected HTTPSelectionViewForm<ItemType> getSourceSelectionViewForm() throws InterfaceException {
		
		if(!(getSourceForm() instanceof HTTPSelectionViewForm<?>)) throw new InterfaceException("source form must be instance of an HTTPSelectionViewForm");
		return (HTTPSelectionViewForm<ItemType>)getSourceForm();
		
	}
	
	protected ItemType getSelectedItem() throws InterfaceException {
		
		HTTPSelectionViewForm<ItemType> sourceForm = getSourceSelectionViewForm();
		
		ItemType selectedItem = sourceForm.getSelectedItem();
		if(selectedItem==null) throw new InterfaceException(sourceForm.getSelectedItemName()+" parameter must be set after HTTPSelectionViewForm submission");
		
		return selectedItem;
	}

}
