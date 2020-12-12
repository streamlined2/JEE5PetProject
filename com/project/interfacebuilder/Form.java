package com.project.interfacebuilder;

import java.awt.Font;
import java.util.List;

public interface Form {
	
	public void addAction(Action a,int order);
	public void addAction(Action a);
	public List<Action> getActions();
	
	public String getName();
	
	public void activate() throws InterfaceException;
	
	public Font getRenderFont();
	public void setRenderFont(Font font);
	
	public Integer getNextControlId();
	
	public interface State {
		
		/**
		 * set form's state back from internal object
		 */
		public void reset();
		
	}
	public State getState();
	
}
