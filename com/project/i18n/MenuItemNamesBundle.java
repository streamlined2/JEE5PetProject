package com.project.i18n;

import java.util.ListResourceBundle;

public class MenuItemNamesBundle extends ListResourceBundle {

	@Override
	protected Object[][] getContents() {
		return new Object[][]{
				{"Entity.name","Entity"},
				{"Entity.desc","Browse defined entities"},
				{"Query.name","Query"},
				{"Query.desc","Run query and see result"},
				{"Countries.name","Countries"},
				{"Countries.desc","All available countries"},
				{"Customers.name","Customers"},
				{"Customers.desc","All available customers"},
				{"Phones.name","Phones"},
				{"Phones.desc","All available phones"}
		};
	}

}
