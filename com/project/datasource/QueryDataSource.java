package com.project.datasource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.project.Startup;
import com.project.inspection.EntityInfo.EntityData;
import com.project.inspection.PropertyList;
import com.project.inspection.property.InformationPropertyInfo;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.forms.HTTPForm;
import com.project.queries.QueryDefinition;

public class QueryDataSource extends DataSource {

	private QueryDefinition queryDefinition;
	
	public QueryDataSource(HTTPForm form,QueryDefinition qd){
		super(form);
		setQueryDefinition(qd);
	}
	
	public QueryDefinition getQueryDefinition() {
		return queryDefinition;
	}

	public void setQueryDefinition(QueryDefinition queryDefinition) {
		this.queryDefinition = queryDefinition;
	}
	
	@Override
	public List<EntityData> get() throws InterfaceException {
		return Startup.getAgent().runQuery(queryDefinition);
	}

	@Override
	public String getDisplayName(Locale locale) {
		return getQueryDefinition().getItemDescripion(queryDefinition.getSelectedLocale());
	}

	@Override
	public List<InformationPropertyInfo> getInformationProperties() throws InterfaceException {
		List<InformationPropertyInfo> properties = new ArrayList<InformationPropertyInfo>();
		properties.addAll(queryDefinition.getInfoProperties());
		properties.addAll(queryDefinition.getGroupProperties());
		return properties; 
	}

	@Override
	public PropertyList getDefaultPropertyList() throws InterfaceException {
		return new PropertyList(form,this);
	}

	//TODO subject to be changed later to support live query results
	@Override
	public boolean isModifiable() {
		return false;
	}

}
