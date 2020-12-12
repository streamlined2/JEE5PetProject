package com.project.interfacebuilder.http.forms;

import java.util.Locale;

import com.project.Helpers;
import com.project.inspection.property.PropertyInfo.AlignType;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.SelectionViewItem;

public abstract class HTTPSelectionViewForm <ItemType extends SelectionViewItem> extends HTTPOptionForm<ItemType> {
	
	public HTTPSelectionViewForm() throws InterfaceException {
		super();
	}

	@Override
	protected String formListTitle() throws InterfaceException {
		return new StringBuilder().
			append(
				Helpers.padString(
					AlignType.CENTER, 
					getItemNameWidth(), 
					getTitleName(getSelectedLocale()), 
					Helpers.NON_BREAKING_SPACE
				)
			).
			append(Helpers.NON_BREAKING_SPACE).
			append(
				Helpers.padString(
					AlignType.CENTER, 
					getItemDescriptionWidth(), 
					getTitleDesc(getSelectedLocale()), 
					Helpers.NON_BREAKING_SPACE
				)
			).
			toString();
	}
	
	@Override
	protected String formListValue(ItemType e) {
		return new StringBuilder().
			append(
				Helpers.padString(
					AlignType.CENTER, 
					getItemNameWidth(), 
					e.getItemName(getSelectedLocale()), 
					Helpers.NON_BREAKING_SPACE
				)
			).
			append(Helpers.COLUMN_SEPARATOR).
			append(
				Helpers.padString(
					AlignType.LEFT, 
					getItemDescriptionWidth(), 
					e.getItemDescripion(getSelectedLocale()), 
					Helpers.NON_BREAKING_SPACE
				)
			).
			toString();
	}

	protected abstract int getItemNameWidth();

	protected abstract int getItemDescriptionWidth();

	protected abstract String getTitleDesc(Locale locale);

	protected abstract String getTitleName(Locale locale);

}
