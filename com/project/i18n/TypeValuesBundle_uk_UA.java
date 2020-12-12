package com.project.i18n;

import java.util.ListResourceBundle;

public class TypeValuesBundle_uk_UA extends ListResourceBundle {

	@Override
	protected Object[][] getContents() {
		return new Object[][]{
				{"Customer$Kind.PERSON","Персона"},
				{"Customer$Kind.ORGANIZATION","Організація"},
				{"Boolean.true","Так"},
				{"Boolean.false","Ні"},
				{"SortOrderType.ASCENDING","зростаючий"},
				{"SortOrderType.DESCENDING","спадний"},
				{"Phone$Kind.CELLULAR","мобільний"},
				{"Phone$Kind.FIXED","стаціонарний"}
		};
	}

}
