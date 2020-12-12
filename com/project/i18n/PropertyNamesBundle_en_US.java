package com.project.i18n;

import java.util.ListResourceBundle;

public class PropertyNamesBundle_en_US extends ListResourceBundle {

	@Override
	protected Object[][] getContents() {
		return new Object[][]{
				{"Entity.property","Property"},
				{"Entity.orderBy","Order By"},
				{"Entity.order","Order"},
				{"Entity.sortOrder","Kind"},
				{"Customer.name","Name"},
				{"Customer.address","Address"},
				{"Customer.phone","Phone"},
				{"Customer.kind","Kind"},
				{"Customer.creationDate","Birth-Day"},
				{"Customer.rating","Rating"},
				{"Customer.creditAvailable","Credit"},
				{"Customer.sellVolume","Sell Volume"},
				{"Customer.lastTransactionTime","Last Transaction"},
				{"Phone.kind","Kind"},
				{"Phone.number","Number"},
				{"Country.name","Name"},
				{"Country.square","Square"},
				{"Country.longitude","Longitude"},
				{"Country.latitude","Latitude"},
				{"Country.capital","Capital"},
				{"Country.population","Population"},
				{"Country.foundationDate","Founded"}
		};
	}

}
