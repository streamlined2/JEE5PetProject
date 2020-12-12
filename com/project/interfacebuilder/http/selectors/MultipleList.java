package com.project.interfacebuilder.http.selectors;



public class MultipleList extends List {

	public MultipleList() {}

	@Override
	public MultiplePolicy getMultiplePolicy() {
		return MultiplePolicy.MULTIPLE;
	}

}
