package com.project.i18n;

import java.util.ListResourceBundle;

public class MenuItemNamesBundle_en_US extends ListResourceBundle {

	@Override
	protected Object[][] getContents() {
		return new Object[][]{
				{"Entity.name","Entity"},
				{"Entity.desc","Browse defined entities"},
				{"Query.name","Query"},
				{"Query.desc","Run query and see result"},
				{"Info.name","Info"},
				{"Info.desc","Get information about program"},
				{"Countries.name","Countries"},
				{"Countries.desc","All available countries"},
				{"Customers.name","Customers"},
				{"Customers.desc","All available customers"},
				{"Phones.name","Phones"},
				{"Phones.desc","All available phones"},
				{"Customers_Phones.name","Customers and phones"},
				{"Customers_Phones.desc","Phone numbers listed by customers"},
				{"Country_Customers.name","Customers by country"},
				{"Country_Customers.desc","Customers by country"},
				{"Customers_Rating.name","Total rating"},
				{"Customers_Rating.desc","Customers' address total rating"},
				{"Customers_Average_Rating.name","Average rating"},
				{"Customers_Average_Rating.desc","Customers' address average rating"},
				{"Customers_Count_Rating.name","Count rating"},
				{"Customers_Count_Rating.desc","Customers' address count rating"},
		};
	}

}
