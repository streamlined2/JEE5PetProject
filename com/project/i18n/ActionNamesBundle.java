package com.project.i18n;

import java.util.ListResourceBundle;

public class ActionNamesBundle extends ListResourceBundle {

	@Override
	protected Object[][] getContents() {
		return new Object[][]{
				{"Confirm","Confirm"},
				{"SaveChanges","Save"},
				{"Edit","Edit"},
				{"Range","Set Range"},
				{"rangeStart","from"},
				{"rangeFinish","to"},
				{"filterRangeStart","start"},
				{"filterRangeFinish","finish"},
				{"AddNew","Add New"},
				{"Filter","Filter"},
				{"ApplyFilter","Apply Filter"},
				{"Order","Order"},
				{"ApplyOrder","Apply Order"},
				{"PropertyList","Properties"},
				{"ApplyPropertyList","Apply"},
				{"QueryAction","Query"},
				{"BrowseAction","Browse"},
				{"Proceed","Proceed"},
				{"Cancel","Cancel"},
				{"RunQuery","Run Query"}
		};
	}

}
