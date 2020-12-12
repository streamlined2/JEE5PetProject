package com.project.i18n;

import java.util.ListResourceBundle;

public class MenuItemNamesBundle_uk_UA extends ListResourceBundle {

	@Override
	protected Object[][] getContents() {
		return new Object[][]{
				{"Entity.name","Сутність"},
				{"Entity.desc","Перегляд визначених сутностей"},
				{"Query.name","Запит"},
				{"Query.desc","Виконати запит та переглянути результат"},
				{"Countries.name","Країни"},
				{"Countries.desc","Всі відомі країни"},
				{"Customers.name","Клієнти"},
				{"Customers.desc","Перелік усіх клієнтів"},
				{"Phones.name","Телефони"},
				{"Phones.desc","Усі відомі телефони"}
		};
	}

}
