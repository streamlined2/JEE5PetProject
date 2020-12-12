package com.project.interfacebuilder.menu;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Menu implements Iterable<MenuItem>{

	private TreeMap<String,MenuItem> list=new TreeMap<String,MenuItem>();
	
	private int order;
	
	public Menu(){
		order = 0;
	}
	
	public void addMenuItem(MenuItem item){
		item.setOrder(++order);
		list.put(item.getItemID(), item);
	}
	
	public MenuItem getMenuItemByID(String id){
		return list.get(id);
	}
	
	public Set<MenuItem> getMenuItemSet(){
		return Collections.unmodifiableSet(new TreeSet<MenuItem>(list.values()));
	}
	
	@Override
	public Iterator<MenuItem> iterator() {
		
		final Iterator<MenuItem> i=getMenuItemSet().iterator();
		
		return new Iterator<MenuItem>(){

			@Override
			public boolean hasNext() {
				return i.hasNext();
			}

			@Override
			public MenuItem next() {
				return i.next();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("MenuItem iterator doesn't support remove operation");
			}
			
		};
	}

}
