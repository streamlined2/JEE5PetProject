package com.project.queries;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.naming.NamingException;

import com.project.AgentRemote;
import com.project.ContextBootstrap;
import com.project.inspection.EntityInfo.EntityData;
import com.project.inspection.property.InformationPropertyInfo;
import com.project.inspection.PropertyList;
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
	public SortedSet<InformationPropertyInfo> getInformationProperties() throws InterfaceException {
		return new TreeSet(queryDefinition.getInfoProperties()); 
	}

	@Override
	public PropertyList getDefaultPropertyList() throws InterfaceException {
		return new PropertyList(getInformationProperties());
	}

}
