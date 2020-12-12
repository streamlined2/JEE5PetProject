package com.project.i18n;

import java.util.ListResourceBundle;

public class EntityNamesBundle extends ListResourceBundle {

	@Override
	protected Object[][] getContents() {
		return new Object[][]{
				{"Entity.name","Entity"},
				{"Entity.desc","Description"},
				{"Query.name","Query"},
				{"Query.desc","Description"},
				{"Menu.name","Menu"},
				{"Menu.desc","Description"},
				{"Customer.name","Customer"},
				{"Phone.name","Phone"},
				{"Customer.desc","Company customer data"},
				{"Phone.desc","Private person and company phone information"},
				{"Country.name","Name"},
				{"Country.desc","Country information"},
		};
	}

}
