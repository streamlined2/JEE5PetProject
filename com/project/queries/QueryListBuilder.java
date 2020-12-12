package com.project.queries;

public final class QueryListBuilder {
	
	private QueryListBuilder(){}
	
	private static void fillInQueryList(QueryList queries){
		queries.addQuery(new QueryDefinition(
				"Countries", 
				"List of all known countries", 
				"select a.* from country as a"));
		queries.addQuery(new QueryDefinition(
				"Customers", 
				"List of all known customers", 
				"select a.* from customer as a"));
		queries.addQuery(new QueryDefinition(
				"Phones", 
				"List of all known phones", 
				"select a.* from phone as a"));
	}
	
	public static QueryList createQueryList(){
		QueryList queries=new QueryList();
		fillInQueryList(queries);
		return queries;
	}

}
