package com.project.queries;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import com.project.AgentRemote;
import com.project.ContextBootstrap;
import com.project.inspection.EntityInfo.EntityData;
import com.project.inspection.PropertyList;
import com.project.inspection.property.InformationPropertyInfo;
import com.project.interfacebuilder.InterfaceException;

public class QueryDataSource extends DataSource {

	private QueryDefinition queryDefinition;
	
	public QueryDataSource(QueryDefinition qd){
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
		AgentRemote agent;
		try {
			agent = ContextBootstrap.getAgentReference(null);
			return agent.runQuery(queryDefinition);
		} catch (NamingException e) {
			throw new InterfaceException(e);
		}
	}

	@Override
	public String getDisplayName() {
		return getQueryDefinition().getItemDescripion();
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
		return new PropertyList(this);
	}

	//TODO may be changed later depending on query properties
	@Override
	public boolean isModifiable() {
		return false;
	}

}
