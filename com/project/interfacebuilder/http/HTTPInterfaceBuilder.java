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
import com.project.interfacebuilder.http.forms.HTTPBrowseForm;
import com.project.interfacebuilder.http.forms.HTTPEditForm;
import com.project.interfacebuilder.http.forms.HTTPEntitySelectionForm;
import com.project.interfacebuilder.http.forms.HTTPFilterForm;
import com.project.interfacebuilder.http.forms.HTTPForm;
import com.project.interfacebuilder.http.forms.HTTPOrderForm;
import com.project.interfacebuilder.http.forms.HTTPPropertyListForm;
import com.project.interfacebuilder.http.forms.HTTPQueryForm;
import com.project.interfacebuilder.http.forms.HTTPQuerySelectionForm;
import com.project.interfacebuilder.http.forms.HTTPTopLevelMenuSelectionForm;
import com.project.interfacebuilder.transition.TransitionRule;
import com.project.interfacebuilder.transition.UseCase;

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
	
	private HTTPEditAction editAction;
	private HTTPAddNewAction addNewAction;
	private HTTPSaveChangesAction saveChangesAction;
	private HTTPCancelAction cancelAction;
	private HTTPRangeAction rangeAction;
	private HTTPProceedAction proceedAction;
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
		topLevelMenuSelection,
		dataEditingUseCase,
		dataBrowsingUseCase,
		queryPerformanceUseCase
	};
	
	//singleton pattern
	private static HTTPInterfaceBuilder builder=null;
	
	private HTTPInterfaceBuilder() throws InterfaceException{
		setUseCase(new UseCase());
		
		setCurrentContext(InterfaceContext.topLevelMenuSelection);
		
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
			
		editAction=new HTTPEditAction();
		addNewAction=new HTTPAddNewAction();
		saveChangesAction=new HTTPSaveChangesAction();
		cancelAction=new HTTPCancelAction();
		rangeAction=new HTTPRangeAction();
		proceedAction=new HTTPProceedAction();
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
	
	protected UseCase useCase;
	
	private InterfaceContext currentContext;

	public InterfaceContext getCurrentContext() {
		return currentContext;
	}

	public void setCurrentContext(InterfaceContext currentContext) {
		this.currentContext = currentContext;
	}

	public void setUseCase(UseCase useCase) {
		this.useCase = useCase;
	}
	
	private void defineTransitionRules(){
	
	//top level menu selection
		useCase.addRule(new TransitionRule(InterfaceContext.dataEditingUseCase,topLevelMenuSelectionForm,proceedAction,entitySelectionForm));
		useCase.addRule(new TransitionRule(InterfaceContext.queryPerformanceUseCase,topLevelMenuSelectionForm,proceedAction,querySelectionForm));

	//run query form
		useCase.addRule(new TransitionRule(InterfaceContext.queryPerformanceUseCase,querySelectionForm,runQueryAction,queryResultBrowseForm));

	//entity selection
		useCase.addRule(new TransitionRule(InterfaceContext.dataEditingUseCase,entitySelectionForm,queryAction,queryForm));
		useCase.addRule(new TransitionRule(InterfaceContext.dataEditingUseCase,queryForm,proceedAction,entitySelectionForm));

		useCase.addRule(new TransitionRule(InterfaceContext.dataEditingUseCase,entitySelectionForm,browseAction,browseForm));
		useCase.addRule(new TransitionRule(InterfaceContext.dataEditingUseCase,browseForm,proceedAction,entitySelectionForm));
	
	//browseForm
		//editing/adding new record
		useCase.addRule(new TransitionRule(InterfaceContext.dataEditingUseCase,browseForm,editAction,editForm));
		useCase.addRule(new TransitionRule(InterfaceContext.dataEditingUseCase,browseForm,addNewAction,editForm));

		//range setting
		useCase.addRule(new TransitionRule(InterfaceContext.dataEditingUseCase,browseForm,rangeAction,browseForm));

		//saving/discarding changes
		useCase.addRule(new TransitionRule(InterfaceContext.dataEditingUseCase,editForm,saveChangesAction,browseForm));

		//filtering
		useCase.addRule(new TransitionRule(InterfaceContext.dataEditingUseCase,browseForm,filterAction,filterForm));
		useCase.addRule(new TransitionRule(InterfaceContext.dataEditingUseCase,filterForm,applyFilterAction,browseForm));

		//ordering
		useCase.addRule(new TransitionRule(InterfaceContext.dataEditingUseCase,browseForm,orderAction,orderForm));
		useCase.addRule(new TransitionRule(InterfaceContext.dataEditingUseCase,orderForm,applyOrderAction,browseForm));

		//property list selection
		useCase.addRule(new TransitionRule(InterfaceContext.dataEditingUseCase,browseForm,propertyListAction,propertyListForm));
		useCase.addRule(new TransitionRule(InterfaceContext.dataEditingUseCase,propertyListForm,applyPropertyListAction,browseForm));

	//queryForm
		//range setting
		useCase.addRule(new TransitionRule(InterfaceContext.dataBrowsingUseCase,queryForm,proceedAction,queryForm));
		useCase.addRule(new TransitionRule(InterfaceContext.dataBrowsingUseCase,queryForm,rangeAction,queryForm));

		//filtering
		useCase.addRule(new TransitionRule(InterfaceContext.dataBrowsingUseCase,queryForm,filterAction,filterForm));
		useCase.addRule(new TransitionRule(InterfaceContext.dataBrowsingUseCase,filterForm,applyFilterAction,queryForm));

		//ordering
		useCase.addRule(new TransitionRule(InterfaceContext.dataBrowsingUseCase,queryForm,orderAction,orderForm));
		useCase.addRule(new TransitionRule(InterfaceContext.dataBrowsingUseCase,orderForm,applyOrderAction,queryForm));

		//property list selection
		useCase.addRule(new TransitionRule(InterfaceContext.dataBrowsingUseCase,queryForm,propertyListAction,propertyListForm));
		useCase.addRule(new TransitionRule(InterfaceContext.dataBrowsingUseCase,propertyListForm,applyPropertyListAction,queryForm));

	}
	
	public FormContextItem getTarget(Form sourceForm,Action action){
		TransitionRule rule=useCase.getTransitionRule(currentContext,sourceForm,action);
		return new FormContextItem(rule.getTarget(),rule.getTargetContext());
	}
	
	public Form getDefaultForm() {
		return topLevelMenuSelectionForm;//entitySelectionForm;//browseForm;//queryForm
	}

}
