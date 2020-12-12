package com.project.interfacebuilder.http;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.project.AgentRemote;
import com.project.ContextBootstrap;
import com.project.Helpers;
import com.project.entities.EntityType;
import com.project.inspection.EntityInspector;
import com.project.inspection.Filter.FilterRangeBoundary;
import com.project.inspection.InformationPropertyInfo;
import com.project.inspection.PrimaryKeyPropertyInfo;
import com.project.inspection.PropertyInfo;
import com.project.interfacebuilder.Action;
import com.project.interfacebuilder.ConfirmAction;
import com.project.interfacebuilder.ControllerSupport;
import com.project.interfacebuilder.Form;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.Selector;
import com.project.interfacebuilder.http.actions.HTTPAction;
import com.project.interfacebuilder.http.forms.HTTPForm;
import com.project.queries.DataSource;
import com.project.queries.EntityDataSource;

public class HTTPControllerSupport extends ControllerSupport implements HTTPController {
	
	private static final int MAX_INACTIVE_INTERVAL = 1000;
	
	private HttpSession session;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private ServletContext context;
	private ServletConfig config;
	
	public HTTPControllerSupport() throws InterfaceException{
		super();
	}
	
	private void setSession(HttpSession session){
		if(this.session==null || session.isNew()) {
			this.session = session;
			this.session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
		}
	}
	
	private HttpSession getSession(){
		return session;
	}
	
	public void setRequest(HttpServletRequest request){
		this.request = request;
		setSession(request.getSession(true));
	}
	
	public void setResponse(HttpServletResponse response){
		this.response = response;
	}
	
	public void setContext(ServletContext context){
		this.context = context;
	}
	
	public void setConfig(ServletConfig config){
		this.config = config;
	}
	
	private void checkState(){
		if(context==null) throw new IllegalStateException("context reference must be set");
		if(config==null) throw new IllegalStateException("config reference must be set");
		if(session==null) throw new IllegalStateException("session reference must be set");
		if(request==null) throw new IllegalStateException("request reference must be set");
		if(response==null) throw new IllegalStateException("response reference must be set");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Action getAction() {
		
		checkState();
		
		Action action=null;
		
		java.util.List<HTTPAction> actions=
			(java.util.List<HTTPAction>)getAttribute(HTTPController.ACTIONS_ATTRIBUTE);
		
		if(actions!=null){
			action = getActivatedAction(actions, request.getParameterMap());
		}
		
		return action;
	}

	@Override
	protected void performAction(Action action) throws InterfaceException {
		checkState();
		
		action.perform();
	}

	@Override
	protected void activateTarget(Form form) throws InterfaceException {
		
		if(form instanceof HTTPForm){
			HTTPForm targetForm=(HTTPForm)form;
			targetForm.activate();
		}
		
	}

	@Override
	protected Form getSource() {
		checkState();
		HTTPForm sourceForm=
			(HTTPForm)getAttribute(HTTPController.SOURCE_FORM_ATTRIBUTE);
		return sourceForm;
	}

	@Override
	protected void setUpAction(Action action, Form sourceForm) {
		
		if(action instanceof HTTPAction){
			HTTPAction a=(HTTPAction)action;
			a.setController(this);
			a.setSourceForm(sourceForm);
		}
		
	}
	
	@Override
	protected void setUpActionTarget(Action action, Form targetForm) {
		
		if(action instanceof HTTPAction){
			HTTPAction a=(HTTPAction)action;
			a.setTargetForm(targetForm);
		}
		
	}

	@Override
	protected void setUpTarget(Form form) throws InterfaceException {
		
		checkState();

		if(form instanceof HTTPForm){
			HTTPForm f=(HTTPForm)form;
			try {
				f.setOut(response.getWriter());
				f.setSession(session);
				f.setController(this);
			} catch (IOException e) {
				throw new InterfaceException(e);
			}
			
		}
	}

	public void setAttribute(String aName,Object value){
		context.setAttribute(aName, value);
	}

	public Object getAttribute(String aName){
		return context.getAttribute(aName);	
	}

	public void saveChanges() throws InterfaceException{
	
		DataSource dataSource=(DataSource)getAttribute(HTTPController.DATA_SOURCE_ATTRIBUTE);
		if(dataSource==null) throw new InterfaceException(HTTPController.DATA_SOURCE_ATTRIBUTE+" must be set before applying saveChanges");
		if(!(dataSource instanceof EntityDataSource)) throw new InterfaceException("saveChanges may be applied only to EntityDataSource");

		@SuppressWarnings("unchecked")
		java.util.List<HTTPAction> actions=(java.util.List<HTTPAction>)getAttribute(HTTPController.ACTIONS_ATTRIBUTE);
		
		@SuppressWarnings("unchecked")
		Map<String,String[]> requestParametersMap=(Map<String,String[]>)request.getParameterMap();

		Boolean createNew=(Boolean)getAttribute(HTTPController.CREATE_NEW_ATTRIBUTE);
		
		Object primaryKey=getAttribute(HTTPController.PRIMARY_KEY_ATTRIBUTE);
		
		saveChanges(
				(EntityDataSource)dataSource,actions,requestParametersMap,createNew.booleanValue(),primaryKey);
		
	}

	private void saveChanges(
			EntityDataSource dataSource,
			java.util.List<HTTPAction> actions, 
			Map<String,String[]> requestParametersMap, 
			boolean createNew, Object primaryKey) throws InterfaceException{
		
		HTTPAction action=getActivatedAction(actions, requestParametersMap);
		if(action instanceof ConfirmAction){
			
			updateEntity(actions, requestParametersMap, dataSource, createNew, primaryKey);
			
		}
		
	}

	private void updateEntity(
			List<HTTPAction> actions, 
			Map<String, String[]> map,
			EntityDataSource dataSource, 
			boolean createNew, Object primaryKey) throws InterfaceException {
	
		if(dataSource!=null){
			
			EntityType entity=null;
			try {

				entity = EntityInspector.createEntity(dataSource.getEntityInfo());

				if(entity!=null){
					
					for(InformationPropertyInfo pInfo:dataSource.getSelectedInformationProperties()){
						Object propertyValue=findPropertyValue(
								pInfo, actions, map);
						if(propertyValue!=null){
							EntityInspector.setPropertyValue(
									pInfo, entity, propertyValue);
						}
					}
					
					if(!createNew){
		
						PrimaryKeyPropertyInfo primaryKeyInfo=dataSource.getPrimaryKeyPropertyInfo();
						
						if(primaryKeyInfo!=null){
						
							EntityInspector.setPropertyValue(primaryKeyInfo, entity, primaryKey);
						
						}
					
					}
					
					try{
						AgentRemote agent=ContextBootstrap.getAgentReference(null);
						entity=agent.createUpdateEntity(entity, createNew);
						
						primaryKey=entity.getId();

						setAttribute(HTTPController.PRIMARY_KEY_ATTRIBUTE, primaryKey);

					}catch(NamingException e){
						throw new InterfaceException(e);
					}
					
				}
				
			} catch (Exception e) {
				throw new InterfaceException(e);
			}finally{
				entity=null;
			}
		}
	
	}

	public Object findPropertyValue(
			PropertyInfo pInfo, List<HTTPAction> actions, Map<String, String[]> map) throws InterfaceException {
	
		String propertyName=pInfo.getPropertyName();
		Class<?> propertyType=pInfo.getType();
		
		for(Iterator<String> keys=map.keySet().iterator();keys.hasNext();){
	
			String pName=keys.next();
			
			if(!isActivatedAction(pName,actions)){
				
				if(propertyName.equals(pName)){
					
					String pValue=map.get(pName)[0];		
					if(pValue!=null && !pValue.isEmpty()){
						
						if(pInfo.isInformation()){
							
							Selector selector=((InformationPropertyInfo)pInfo).getSelector();

							if(selector!=null){
		
								return 
									EntityInspector.convertFromString(
										selector.mapStateToValue(pValue),
										propertyType);
								
							
							}

						}
						
					}
					
				}
	
			}
	
		}
		
		return null;
	}

	private boolean isActivatedAction(String pName,List<HTTPAction> actions){
		return getActivatedAction(pName,actions)!=null;
	}

	private HTTPAction getActivatedAction(String parameterName,java.util.List<HTTPAction> actions){
		if(parameterName!=null && !parameterName.isEmpty()){
			if(actions!=null){
				for(HTTPAction a:actions){
					if(parameterName.trim().equals(a.getInnerName())) return a;
				}
			}
		}
		return null;
	}

	public HTTPAction getActivatedAction(
			java.util.List<HTTPAction> actions, java.util.Map<String,String[]> requestParameters) {
	
		for(Iterator<String> keys=requestParameters.keySet().iterator();keys.hasNext();){
			String pName=keys.next();
	
			HTTPAction a=getActivatedAction(pName,actions);
			if(a!=null) return a;
		
		}
	
		return null;
	
	}

	@Override
	public String getLink() {
		return context.getContextPath()+"/"+config.getServletName();
	}

	@Override
	public String getParameter(String name) {
		if(request==null) throw new IllegalStateException("request must be set for getParameter");
		return request.getParameter(name);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String[]> getParameters() {
		if(request==null) throw new IllegalStateException("request must be set for getParameter");
		return (Map<String, String[]>)request.getParameterMap();
	}
	
	@Override
	protected Form getDefaultForm() throws InterfaceException {
		return HTTPInterfaceBuilder.getInterfaceBuilder().getDefaultForm();
	}

	public static String findParameterValue(Map<String, String[]> parameters, String parameterKey){
		String[] values=parameters.get(parameterKey);
		if(values==null) return null;
		else{
			return values[0];
		} 
	}

	public static boolean parameterIsNotEmpty(Map<String, String[]> parameters, String parameterKey){
		return Helpers.nonEmtpyParameter(findParameterValue(parameters,parameterKey));
	}

	@Override
	protected void errorPage(Exception e) throws InterfaceException {
		RequestDispatcher dispatcher=context.getRequestDispatcher("/WEB-INF/errorPage.jsp");
		request.setAttribute(HTTPController.WRAPPED_EXCEPTION, getWrappedException(e));
		try {
			dispatcher.forward(request, response);
		} catch (ServletException exc) {
			throw new InterfaceException(exc);
		} catch (IOException exc) {
			throw new InterfaceException(exc);
		}
		
	}

	private Exception getWrappedException(Exception e) {
		if(e instanceof InterfaceException){
			return ((InterfaceException) e).getWrappedException();
		}else if(e instanceof EJBException){
			return ((EJBException) e).getCausedByException();
		}
		return e;
	}
	
	private FilterRangeBoundary filterRangeKind = FilterRangeBoundary.START;
	
	public void updateFilterRangeKind(){
		
		String parameter=getParameter(HTTPController.FILTER_RANGE_GROUP);
		FilterRangeBoundary kind=FilterRangeBoundary.START;
		if(parameter!=null && parameter.equalsIgnoreCase(HTTPController.FILTER_RANGE_FINISH)){
			kind=FilterRangeBoundary.FINISH;
		}
		
		filterRangeKind = kind;

	}
	
	public FilterRangeBoundary getFilterRangeKind(){
		return filterRangeKind;
	}

}
