package com.project.interfacebuilder;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.NamingException;

import com.project.AgentRemote;
import com.project.ContextBootstrap;

public abstract class FormSupport implements Form {
	
	public FormSupport() throws InterfaceException{
	}
	
	//helper method to provide reference to agent
	public AgentRemote getAgent() throws InterfaceException{
		try {
			return ContextBootstrap.getAgentReference(null);
		} catch (NamingException e) {
			throw new InterfaceException(e);
		}
	}

	private Map<Integer,Action> actions=new TreeMap<Integer,Action>();
	
	public void addAction(Action action,int order){
		actions.put(order,action);
	}
	
	private int defaultOrder=1;
	
	public void addAction(Action action){
		actions.put(defaultOrder++,action);
	}
	
	public List<Action> getActions(){
		//can't modify action list except by addAction
		//don't expose inner data structure?
		return Collections.unmodifiableList(new ArrayList<Action>(actions.values()));
	}
	
	public String getName(){
		return getClass().getSimpleName();
	}
	
	@Override
	public boolean equals(Object o){
		return false;
	}
	
	@Override
	public int hashCode(){
		return getName().hashCode();
	}

	private Font renderFont=null;

	public Font getRenderFont(){
		if(renderFont==null){
			renderFont=new Font("Lucida Console",Font.PLAIN,20);
		}
		return renderFont;
	}

	public void setRenderFont(Font font){
		renderFont=font;
	}
	
	private Integer controlId=1;
	
	public Integer getNextControlId(){
		if(controlId>Integer.MAX_VALUE){
			controlId=1;
		}
		return controlId++;
	}

	protected void checkState() throws InterfaceException {
	}
	
	public class FormState implements Form.State{

		@Override
		public void reset() {
		}
		
	}
	
	@Override
	public State getState() {
		return this.new FormState();
	}

}
