package com.project.i18n;

import java.util.ListResourceBundle;

//typical class used for localization of action names in Ukrainian  
public class ActionNamesBundle_uk_UA extends ListResourceBundle {

	@Override
	protected Object[][] getContents() {
		return new Object[][]{
				{"Confirm","Підтвердити"},
				{"SaveChanges","Зберегти"},
				{"Proceed","Продовжити"},
				{"MenuSelect","Вибрати"},
				{"Range","Діапазон"},
				{"rangeStart","від"},
				{"rangeFinish","до"},
				{"filterRangeStart","початковий"},
				{"filterRangeFinish","кінцевий"},
				{"AddNew","Додати новий"},
				{"Filter","Фільтр"},
				{"ApplyFilter","Фільтрувати"},
				{"Order","Впорядкування"},
				{"ApplyOrder","Впорядкувати"},
				{"PropertyList","Перелік"},
				{"ApplyPropertyList","Застосувати"},
				{"QueryAction","Переглянути"},
				{"BrowseAction","Змінити"},
				{"Edit","Редагувати"},
				{"Cancel","Відмінити"},
				{"RunQuery","Виконати запит"}
		};
	}

}
