package com.project.queries;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class QueryList implements Iterable<QueryDefinition>{
	
	private TreeMap<String,QueryDefinition> list=new TreeMap<String,QueryDefinition>();
	
	public QueryList(){
	}
	
	public void addQuery(QueryDefinition def){
		list.put(def.getName(), def);
	}
	
	public QueryDefinition getQueryByName(String name){
		return list.get(name);
	}
	
	public Set<QueryDefinition> getQuerySet(){
		return Collections.unmodifiableSet(new TreeSet<QueryDefinition>(list.values()));
	}

	@Override
	public Iterator<QueryDefinition> iterator() {
		
		final Iterator<Entry<String, QueryDefinition>> i=list.entrySet().iterator();
		
		return new Iterator<QueryDefinition>(){

			@Override
			public boolean hasNext() {
				return i.hasNext();
			}

			@Override
			public QueryDefinition next() {
				return i.next().getValue();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("remove method is not supported operation");
			}
			
		};
	}

}
