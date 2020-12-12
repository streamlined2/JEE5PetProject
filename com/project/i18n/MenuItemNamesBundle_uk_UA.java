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
				{"Phones.desc","Усі відомі телефони"},
				{"Customers_Phones.name","Клієнт та телефони"},
				{"Customers_Phones.desc","Перелік телефонів кожного клієнта"},
				{"Country_Customers.name","Клієнти кожної країни"},
				{"Country_Customers.desc","Перелік клієнтів за країнами"},
				{"Customers_Rating.name","Сумарний рейтинг"},
				{"Customers_Rating.desc","Рейтинг споживачів за адресою"},
				{"Customers_Average_Rating.name","Середній рейтинг"},
				{"Customers_Average_Rating.desc","Середній рейтинг споживачів за адресою"},
				{"Customers_Count_Rating.name","Кількість рейтингів"},
				{"Customers_Count_Rating.desc","Розрахунок кількості рейтингів споживачів"},
		};
	}

}
