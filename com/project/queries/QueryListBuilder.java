package com.project.queries;

import com.project.entities.Country;
import com.project.entities.Customer;
import com.project.entities.Phone;
import com.project.interfacebuilder.InterfaceException;
import com.project.queries.QueryDefinition.FilterEntry;
import com.project.queries.QueryDefinition.GroupEntry;
import com.project.queries.QueryDefinition.GroupOperation;
import com.project.queries.QueryDefinition.GroupPolicy;
import com.project.queries.QueryDefinition.FilterEntry.Relation;
import com.project.queries.QueryDefinition.OrderByEntry;
import com.project.queries.QueryDefinition.OrderByEntry.SortOrder;
import com.project.queries.QueryDefinition.PropertyEntry;

public final class QueryListBuilder {
	
	private QueryListBuilder(){}
	
	public static QueryList createQueryList() throws InterfaceException{
		QueryList queries=new QueryList();

		queries.addQuery(new QueryDefinition(
				"Countries", 
				"List of countries for population greater than 40 million and square greater or equal to 20 million sorted by population", 
				new PropertyEntry[]{
					new PropertyEntry(Country.class, "name"),
					new PropertyEntry(Country.class, "capital"),
					new PropertyEntry(Country.class, "population", "population"),
					new PropertyEntry(Country.class, "square", "square"),
					new PropertyEntry(Country.class, "longitude"),
					new PropertyEntry(Country.class, "latitude"),
					new PropertyEntry(Country.class, "foundationDate", "created")
				},
				new FilterEntry[]{
						new FilterEntry("population",Relation.GREATER_THAN,40000000),
						new FilterEntry("square", Relation.GREATER_OR_EQUAL, 20000000)},
				new OrderByEntry[]{
						new OrderByEntry("created",SortOrder.DESCENDING)}
				)
		);
		queries.addQuery(new QueryDefinition(
				"Customers", 
				"All known customers sorted by address,name", 
				new PropertyEntry[]{
						new PropertyEntry(Customer.class, "name", "name"),
						new PropertyEntry(Customer.class, "address", "address"),
						new PropertyEntry(Customer.class, "rating", "rate"),
						new PropertyEntry(Customer.class, "kind"),
						new PropertyEntry(Customer.class, "creditAvailable"),
						new PropertyEntry(Customer.class, "creationDate")
					},
					new FilterEntry[]{
						new FilterEntry("rate",Relation.CHOICE,new int[]{1,2,3})
					},
					new OrderByEntry[]{
						new OrderByEntry("address"),
						new OrderByEntry("name")}
				)
		);
		queries.addQuery(new QueryDefinition(
				"Phones", 
				"All known phones by number", 
				new PropertyEntry[]{
						new PropertyEntry(Phone.class, "kind"),
						new PropertyEntry(Phone.class, "number", "number")
					},
					null,
					new OrderByEntry[]{new OrderByEntry("number")}
				)
		);

		queries.addQuery(new QueryDefinition(
				"Customers_Phones", 
				"All known phones of customers by number,name", 
				new PropertyEntry[]{
						new PropertyEntry(Phone.class, "number", "num"),
						new PropertyEntry(Customer.class, "name", "name"),
						new PropertyEntry(Phone.class, "kind")
					},
					null,
					new OrderByEntry[]{
						new OrderByEntry("num",SortOrder.DESCENDING),
						new OrderByEntry("name")}
				)
		);

		queries.addQuery(new QueryDefinition(
				"Country_Customers", 
				"All known customers for each country", 
				new PropertyEntry[]{
						new PropertyEntry(Country.class, "name", "country"),
						new PropertyEntry(Customer.class, "name", "customer"),
					},
					null,
					new OrderByEntry[]{
						new OrderByEntry("country"), 
						new OrderByEntry("customer")}
				)
		);

		queries.addQuery(new QueryDefinition(
				"Customers_Rating", 
				"Ratings of customers", 
				new PropertyEntry[]{
						new PropertyEntry(Customer.class, "address", "address"),
					},
				null,
				new OrderByEntry[]{
					new OrderByEntry("address")}, 
				GroupPolicy.GROUP,
				new GroupEntry[]{
					new GroupEntry(Customer.class,"rating",GroupOperation.TOTAL,"ratings")}
			)
		);

		queries.addQuery(new QueryDefinition(
				"Customers_Average_Rating", 
				"Average Ratings of customers", 
				new PropertyEntry[]{
						new PropertyEntry(Customer.class, "address", "address"),
					},
				null,
				new OrderByEntry[]{
					new OrderByEntry("address")}, 
				GroupPolicy.GROUP,
				new GroupEntry[]{
					new GroupEntry(Customer.class,"rating",GroupOperation.AVERAGE,"ratings")}
			)
		);

		queries.addQuery(new QueryDefinition(
				"Customers_Count_Rating", 
				"Count Ratings of customers", 
				new PropertyEntry[]{
						new PropertyEntry(Customer.class, "address", "address"),
					},
				null,
				new OrderByEntry[]{
					new OrderByEntry("address")}, 
				GroupPolicy.GROUP,
				new GroupEntry[]{
					new GroupEntry(Customer.class,"rating",GroupOperation.COUNT,"ratings")}
			)
		);

		return queries;
	}

}
