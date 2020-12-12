package com.project.interfacebuilder;

import java.util.LinkedList;
import java.util.List;

import com.project.interfacebuilder.transition.Dispatcher;
import com.project.interfacebuilder.transition.Dispatcher.InterfaceContext;


public abstract class ControllerSupport implements Controller {
	
	public static class FormChainElement {
		private Form form;
		private InterfaceContext context;
		private Form.State formState;
		
		public FormChainElement(Form form, InterfaceContext context) {
			super();
			this.form = form;
			this.context = context;
			this.formState = form.getState();
		}

		public Form getForm() {
			return form;
		}

		public InterfaceContext getContext() {
			return context;
		}
		
		public Form.State getState(){
			return formState;
		}
		
	}
	
	private List<FormChainElement> formChain = new LinkedList<FormChainElement>();
	
	// save form state in chain
	public void push(Form form,InterfaceContext context){
		formChain.add(0,new FormChainElement(form,context));
	}
	
	// retrieve last form state from chain
	public FormChainElement pop(){
		if(formChain.size()==0) return null;
		if(formChain.size()==1) return formChain.get(0);
		return formChain.remove(0);
	}
	
	@Override // template method specifies fixed behavior pattern, ancestors allowed to change it only by overriding restricted set of abstract methods, such as performAction, activateTarget and so on   
	public final void service() throws InterfaceException{
		
		try{
			
			Form targetForm = null;
			
			Form sourceForm = getSource();
			
			Action action = getAction();
			
			if(sourceForm==null || action==null){
	
				targetForm = getDefaultForm();
			
			}else{
	
				setUpAction(action,sourceForm);
				
				FormChainElement item = action.findTarget(sourceForm); 
				
				if(item!=null){
					
					targetForm = item.getForm();
					item.getState().reset();
					Dispatcher.getDispatcher().setCurrentContext(item.getContext());
				
					setUpActionTarget(action,targetForm);
					performAction(action);
				}
				
			}
			
			if(targetForm==null){
				targetForm = sourceForm; 
			}
		
			setUpTarget(targetForm);
			
			activateTarget(targetForm);
		
		}catch(Exception e){
			errorPage(e);
		}
		
	}
	
	protected abstract void errorPage(Exception e) throws InterfaceException;

	protected abstract Form getDefaultForm() throws InterfaceException;
	
	protected abstract Action getAction();
	
	protected abstract Form getSource();

	protected abstract void setUpAction(Action action, Form sourceForm) throws InterfaceException;
	
	protected abstract void setUpActionTarget(Action action, Form targetForm) throws InterfaceException;

	protected abstract void performAction(Action action) throws InterfaceException;

	protected abstract void setUpTarget(Form form) throws InterfaceException;

	protected abstract void activateTarget(Form form) throws InterfaceException;
	
}
