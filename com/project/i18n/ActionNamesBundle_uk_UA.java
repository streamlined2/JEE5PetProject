package com.project.i18n;

import java.util.ListResourceBundle;

//typical class used for localization of action names in Ukrainian  
public class ActionNamesBundle_uk_UA extends ListResourceBundle {

	@Override
	protected Object[][] getContents() {
		return new Object[][]{
				{"Confirm","ϳ���������"},
				{"SaveChanges","��������"},
				{"Proceed","����������"},
				{"MenuSelect","�������"},
				{"Range","ĳ������"},
				{"rangeStart","��"},
				{"rangeFinish","��"},
				{"filterRangeStart","����������"},
				{"filterRangeFinish","�������"},
				{"AddNew","������ �����"},
				{"Filter","Գ����"},
				{"ApplyFilter","Գ���������"},
				{"Order","�������������"},
				{"ApplyOrder","������������"},
				{"PropertyList","������"},
				{"ApplyPropertyList","�����������"},
				{"QueryAction","�����������"},
				{"BrowseAction","������"},
				{"Edit","����������"},
				{"Cancel","³������"},
				{"RunQuery","�������� �����"}
		};
	}

}
