package com.project.interfacebuilder.http.selectors;

public class MultipleList extends List {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6085845781435965882L;

	public MultipleList() {}

	@Override
	public MultiplePolicy getMultiplePolicy() {
		return MultiplePolicy.MULTIPLE;
	}

}
