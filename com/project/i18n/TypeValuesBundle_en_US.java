package com.project.i18n;

import java.util.ListResourceBundle;

public class TypeValuesBundle_en_US extends ListResourceBundle {

	@Override
	protected Object[][] getContents() {
		return new Object[][]{
				{"Customer$Kind.PERSON","Human"},
				{"Customer$Kind.ORGANIZATION","Firm"},
				{"Boolean.true","yes"},
				{"Boolean.false","no"},
				{"SortOrderType.ASCENDING","ascending"},
				{"SortOrderType.DESCENDING","descending"},
				{"Phone$Kind.CELLULAR","cellular"},
				{"Phone$Kind.FIXED","fixed"}
		};
	}

}
