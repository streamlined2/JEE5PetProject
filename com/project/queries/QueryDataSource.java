package com.project.queries;

import java.beans.IntrospectionException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.naming.NamingException;

import com.project.AgentRemote;
import com.project.ContextBootstrap;
import com.project.inspection.EntityInfo;
import com.project.inspection.EntityInfo.EntityData;
import com.project.inspection.InformationPropertyInfo;
import com.project.inspection.PropertyList;
import com.project.interfacebuilder.InterfaceException;

public class QueryDataSource extends DataSource {

	private QueryDefinition queryDefinition;
	
	private Set<EntityInfo> entities = new HashSet<EntityInfo>();
	
	public QueryDataSource(QueryDefinition qd){
		queryDefinition = qd;
	}
	
	public QueryDefinition getQueryDefinition() {
		return queryDefinition;
	}

	public void setQueryDefinition(QueryDefinition queryDefinition) throws InterfaceException {
		this.queryDefinition = queryDefinition;
		setEntities();
	}
	
	private void setEntities() throws InterfaceException{
		entities = new HashSet<EntityInfo>();
		
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
	public List<InformationPropertyInfo> getInformationProperties() {
		List<InformationPropertyInfo> result = new LinkedList<InformationPropertyInfo>();
		for(EntityInfo entity:entities){
			result.addAll(entity.getInfoFields());
		}
		return result;
	}

	@Override
	public PropertyList getDefaultPropertyList() {
		PropertyList list = new PropertyList();
		for(EntityInfo entityInfo:entities){
			list.add(new PropertyList(entityInfo));
		}
		return list;
	}

}
