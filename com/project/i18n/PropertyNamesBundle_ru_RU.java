package com.project.i18n;

import java.util.ListResourceBundle;

public class PropertyNamesBundle_ru_RU extends ListResourceBundle {

	@Override
	protected Object[][] getContents() {
		return new Object[][]{
				{"Entity.property","Поле"},
				{"Entity.orderBy","Включать"},
				{"Entity.order","Номер"},
				{"Entity.sortOrder","Порядок"},
				{"Customer.name","Название"},
				{"Customer.address","Адрес"},
				{"Customer.phone","Телефон"},
				{"Customer.kind","Тип"},
				{"Customer.creationDate","Создан"},
				{"Customer.rating","Рейтинг"},
				{"Customer.creditAvailable","Кредит"},
				{"Customer.sellVolume","Объем продаж"},
				{"Customer.lastTransactionTime","Последняя транзакция"},
				{"Phone.kind","Разновидность"},
				{"Phone.number","Номер"},
				{"Country.name","Название"},
				{"Country.square","Площадь"},
				{"Country.longitude","Долгота"},
				{"Country.latitude","Широта"},
				{"Country.capital","Столица"},
				{"Country.population","Население"},
				{"Country.foundationDate","Основано"}
		};
	}

}
