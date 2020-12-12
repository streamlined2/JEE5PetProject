package com.project.interfacebuilder.http;

import com.project.interfacebuilder.Action;
import com.project.interfacebuilder.ControllerSupport.FormContextItem;
import com.project.interfacebuilder.Form;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.actions.HTTPAddNewAction;
import com.project.interfacebuilder.http.actions.HTTPApplyFilterAction;
import com.project.interfacebuilder.http.actions.HTTPApplyOrderAction;
import com.project.interfacebuilder.http.actions.HTTPApplyPropertyListAction;
import com.project.interfacebuilder.http.actions.HTTPBrowseAction;
import com.project.interfacebuilder.http.actions.HTTPCancelAction;
import com.project.interfacebuilder.http.actions.HTTPEditAction;
import com.project.interfacebuilder.http.actions.HTTPFilterAction;
import com.project.interfacebuilder.http.actions.HTTPOrderAction;
import com.project.interfacebuilder.http.actions.HTTPProceedAction;
import com.project.interfacebuilder.http.actions.HTTPPropertyListAction;
import com.project.interfacebuilder.http.actions.HTTPQueryAction;
import com.project.interfacebuilder.http.actions.HTTPRangeAction;
import com.project.interfacebuilder.http.actions.HTTPRunQueryAction;
import com.project.interfacebuilder.http.actions.HTTPSaveChangesAction;
import com.project.interfacebuilder.http.actions.HTTPSelectMenuItemAction;
import com.project.interfacebuilder.http.forms.HTTPBrowseForm;
import com.project.interfacebuilder.http.forms.HTTPEditForm;
import com.project.interfacebuilder.http.forms.HTTPEntitySelectionForm;
import com.project.interfacebuilder.http.forms.HTTPFilterForm;
import com.project.interfacebuilder.http.forms.HTTPForm;
import com.project.interfacebuilder.http.forms.HTTPInformationForm;
import com.project.interfacebuilder.http.forms.HTTPOrderForm;
import com.project.interfacebuilder.http.forms.HTTPPropertyListForm;
import com.project.interfacebuilder.http.forms.HTTPQueryForm;
import com.project.interfacebuilder.http.forms.HTTPQuerySelectionForm;
import com.project.interfacebuilder.http.forms.HTTPTopLevelMenuSelectionForm;
import com.project.interfacebuilder.transition.TransitionRule;
import com.project.interfacebuilder.transition.TransitionRuleSet;

public final class HTTPInterfaceBuilder {
	
	private HTTPQueryForm queryForm;
	private HTTPBrowseForm browseForm;
	private HTTPBrowseForm queryResultBrowseForm;
	private HTTPEditForm editForm;
	private HTTPFilterForm filterForm;
	private HTTPOrderForm orderForm;
	private HTTPForm propertyListForm;
	private HTTPEntitySelectionForm entitySelectionForm;
	private HTTPQuerySelectionForm querySelectionForm;
	private HTTPTopLevelMenuSelectionForm topLevelMenuSelectionForm;
	private HTTPInformationForm informationForm;
	
	private HTTPEditAction editAction;
	private HTTPAddNewAction addNewAction;
	private HTTPSaveChangesAction saveChangesAction;
	private HTTPRangeAction rangeAction;
	private HTTPProceedAction proceedAction;
	private HTTPSelectMenuItemAction selectMenuItemAction;
	private HTTPFilterAction filterAction;
	private HTTPApplyFilterAction applyFilterAction;
	private HTTPOrderAction orderAction;
	private HTTPApplyOrderAction applyOrderAction;
	private HTTPPropertyListAction propertyListAction;
	private HTTPApplyPropertyListAction applyPropertyListAction;
	private HTTPQueryAction queryAction;
	private HTTPBrowseAction browseAction;
	private HTTPRunQueryAction runQueryAction;
	
	public enum InterfaceContext {
		TOP_LEVEL_MENU_SELECTION,
		DATA_EDITING_TRANSITION_SET,
		DATA_BROWSE_TRANSITION_SET,
		QUERY_EXECUTION_TRANSITION_SET,
		INFORMATION_CONTEXT
	};
	
	//singleton pattern
	private static HTTPInterfaceBuilder builder=null;
	
	private HTTPInterfaceBuilder() throws InterfaceException{
		setTransitionRuleSet(new TransitionRuleSet());
		
		setCurrentContext(InterfaceContext.TOP_LEVEL_MENU_SELECTION);
		
		defineInterfaceElements();
		defineTransitionRules();
	}
	
	//factory method pattern
	//lazy initialization
	public static HTTPInterfaceBuilder getInterfaceBuilder() throws InterfaceException{
		if(builder==null){
			builder=new HTTPInterfaceBuilder();
		}
		return builder;
	}

	private void defineInterfaceElements() throws InterfaceException{
		
		queryForm=new HTTPQueryForm();
		browseForm=new HTTPBrowseForm();
		queryResultBrowseForm=new HTTPBrowseForm();
		editForm=new HTTPEditForm();
		filterForm=new HTTPFilterForm();
		orderForm=new HTTPOrderForm();
		propertyListForm=new HTTPPropertyListForm();
		entitySelectionForm=new HTTPEntitySelectionForm();
		querySelectionForm=new HTTPQuerySelectionForm();
		topLevelMenuSelectionForm=new HTTPTopLevelMenuSelectionForm();
		informationForm=new HTTPInformationForm();
			
		editAction=new HTTPEditAction();
		addNewAction=new HTTPAddNewAction();
		saveChangesAction=new HTTPSaveChangesAction();
		new HTTPCancelAction();
		rangeAction=new HTTPRangeAction();
		proceedAction=new HTTPProceedAction();
		selectMenuItemAction=new HTTPSelectMenuItemAction();
		filterAction=new HTTPFilterAction();
		applyFilterAction=new HTTPApplyFilterAction();
		orderAction=new HTTPOrderAction();
		applyOrderAction=new HTTPApplyOrderAction();
		propertyListAction=new HTTPPropertyListAction();
		applyPropertyListAction=new HTTPApplyPropertyListAction();
		queryAction=new HTTPQueryAction();
		browseAction=new HTTPBrowseAction();
		runQueryAction=new HTTPRunQueryAction();
			
	}
	
	protected TransitionRuleSet ruleSet;
	
	private InterfaceContext currentContext;

	public InterfaceContext getCurrentContext() {
		return currentContext;
	}

	public void setCurrentContext(InterfaceContext currentContext) {
		this.currentContext = currentContext;
	}

	public void setTransitionRuleSet(TransitionRuleSet ruleSet) {
		this.ruleSet = ruleSet;
	}
	
	private void defineTransitionRules(){
	
	//top level menu selection
		ruleSet.addRule(new TransitionRule(InterfaceContext.DATA_EDITING_TRANSITION_SET,topLevelMenuSelectionForm,selectMenuItemAction,entitySelectionForm));
		ruleSet.addRule(new TransitionRule(InterfaceContext.QUERY_EXECUTION_TRANSITION_SET,topLevelMenuSelectionForm,selectMenuItemAction,querySelectionForm));
		ruleSet.addRule(new TransitionRule(InterfaceContext.INFORMATION_CONTEXT,topLevelMenuSelectionForm,selectMenuItemAction,informationForm));

	//run query form
		ruleSet.addRule(new TransitionRule(InterfaceContext.QUERY_EXECUTION_TRANSITION_SET,querySelectionForm,runQueryAction,queryResultBrowseForm));

	//entity selection
		ruleSet.addRule(new TransitionRule(InterfaceContext.DATA_EDITING_TRANSITION_SET,entitySelectionForm,queryAction,queryForm));
		ruleSet.addRule(new TransitionRule(InterfaceContext.DATA_EDITING_TRANSITION_SET,queryForm,proceedAction,entitySelectionForm));

		ruleSet.addRule(new TransitionRule(InterfaceContext.DATA_EDITING_TRANSITION_SET,entitySelectionForm,browseAction,browseForm));
		ruleSet.addRule(new TransitionRule(InterfaceContext.DATA_EDITING_TRANSITION_SET,browseForm,proceedAction,entitySelectionForm));
	
	//browseForm
		//editing/adding new record
		ruleSet.addRule(new TransitionRule(InterfaceContext.DATA_EDITING_TRANSITION_SET,browseForm,editAction,editForm));
		ruleSet.addRule(new TransitionRule(InterfaceContext.DATA_EDITING_TRANSITION_SET,browseForm,addNewAction,editForm));

		//range setting
		ruleSet.addRule(new TransitionRule(InterfaceContext.DATA_EDITING_TRANSITION_SET,browseForm,rangeAction,browseForm));

		//saving/discarding changes
		ruleSet.addRule(new TransitionRule(InterfaceContext.DATA_EDITING_TRANSITION_SET,editForm,saveChangesAction,browseForm));

		//filtering
		ruleSet.addRule(new TransitionRule(InterfaceContext.DATA_EDITING_TRANSITION_SET,browseForm,filterAction,filterForm));
		ruleSet.addRule(new TransitionRule(InterfaceContext.DATA_EDITING_TRANSITION_SET,filterForm,applyFilterAction,browseForm));

		//ordering
		ruleSet.addRule(new TransitionRule(InterfaceContext.DATA_EDITING_TRANSITION_SET,browseForm,orderAction,orderForm));
		ruleSet.addRule(new TransitionRule(InterfaceContext.DATA_EDITING_TRANSITION_SET,orderForm,applyOrderAction,browseForm));

		//property list selection
		ruleSet.addRule(new TransitionRule(InterfaceContext.DATA_EDITING_TRANSITION_SET,browseForm,propertyListAction,propertyListForm));
		ruleSet.addRule(new TransitionRule(InterfaceContext.DATA_EDITING_TRANSITION_SET,propertyListForm,applyPropertyListAction,browseForm));

	//queryForm
		//range setting
		ruleSet.addRule(new TransitionRule(InterfaceContext.DATA_BROWSE_TRANSITION_SET,queryForm,proceedAction,queryForm));
		ruleSet.addRule(new TransitionRule(InterfaceContext.DATA_BROWSE_TRANSITION_SET,queryForm,rangeAction,queryForm));

		//filtering
		ruleSet.addRule(new TransitionRule(InterfaceContext.DATA_BROWSE_TRANSITION_SET,queryForm,filterAction,filterForm));
		ruleSet.addRule(new TransitionRule(InterfaceContext.DATA_BROWSE_TRANSITION_SET,filterForm,applyFilterAction,queryForm));

		//ordering
		ruleSet.addRule(new TransitionRule(InterfaceContext.DATA_BROWSE_TRANSITION_SET,queryForm,orderAction,orderForm));
		ruleSet.addRule(new TransitionRule(InterfaceContext.DATA_BROWSE_TRANSITION_SET,orderForm,applyOrderAction,queryForm));

		//property list selection
		ruleSet.addRule(new TransitionRule(InterfaceContext.DATA_BROWSE_TRANSITION_SET,queryForm,propertyListAction,propertyListForm));
		ruleSet.addRule(new TransitionRule(InterfaceContext.DATA_BROWSE_TRANSITION_SET,propertyListForm,applyPropertyListAction,queryForm));

	}
	
	public FormContextItem getTarget(Form sourceForm,Action action) throws InterfaceException{
		
		TransitionRule rule=ruleSet.getTransitionRule(currentContext,sourceForm,action);
		
		if(rule==null) throw new InterfaceException("transition rule is undefined for context "+currentContext+", source form "+sourceForm+" and action "+action);
		
		return new FormContextItem(
				rule.getTarget(),
				rule.getTargetContext());
	}
	
	public Form getDefaultForm() {
		return topLevelMenuSelectionForm;
	}

}
