package com.project.i18n;

import java.util.ListResourceBundle;

public class QueryPropertyPrefixBundle extends ListResourceBundle {

	@Override
	protected Object[][] getContents() {
		return new Object[][]{
				{"Average","Average"},
				{"Total","Total"},
				{"Count","Count"}
		};
	}

}
