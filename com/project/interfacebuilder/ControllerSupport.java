package com.project.interfacebuilder;

import java.util.LinkedList;
import java.util.List;

import com.project.interfacebuilder.http.HTTPInterfaceBuilder;
import com.project.interfacebuilder.http.HTTPInterfaceBuilder.InterfaceContext;


public abstract class ControllerSupport implements Controller {
	
	public static class FormContextItem {
		private Form form;
		private InterfaceContext context;
		private Form.State formState;
		
		public FormContextItem(Form form, InterfaceContext context) {
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
	
	private List<FormContextItem> chain = new LinkedList<FormContextItem>();
	
	public void push(Form form,InterfaceContext context){
		chain.add(0,new FormContextItem(form,context));
	}
	
	public FormContextItem pop(){
		if(chain.size()==0) return null;
		if(chain.size()==1) return chain.get(0);
		return chain.remove(0);
	}
	
	//template method
	@Override
	public final void service() throws InterfaceException{
		
		try{
			
			Form targetForm = null;
			
			Form sourceForm = getSource();
			
			Action action = getAction();
			
			if(sourceForm==null || action==null){
	
				targetForm = getDefaultForm();
			
			}else{
	
				setUpAction(action,sourceForm);
				
				FormContextItem item = action.findTarget(sourceForm); 
				
				if(item!=null){
					
					targetForm = item.getForm();
					item.getState().reset();
					HTTPInterfaceBuilder.getInterfaceBuilder().setCurrentContext(item.getContext());
				
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
