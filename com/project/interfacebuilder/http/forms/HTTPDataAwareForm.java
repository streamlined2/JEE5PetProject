package com.project.interfacebuilder.http.forms;

import com.project.interfacebuilder.InterfaceException;
import com.project.datasource.DataSource;

public abstract class HTTPDataAwareForm extends HTTPForm {

	public HTTPDataAwareForm() throws InterfaceException {
		super();
	}
	
	private DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	protected void checkState() throws InterfaceException{
		if(dataSource==null) throw new IllegalStateException("DataSource reference must be set");
	}
	
}
