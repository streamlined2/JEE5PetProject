package com.project.interfacebuilder.transition;

import com.project.interfacebuilder.Action;
import com.project.interfacebuilder.Form;
import com.project.interfacebuilder.http.HTTPInterfaceBuilder.InterfaceContext;

public class TransitionRule {
	
	private InterfaceContext sourceContext;
	private Form source;
	private Action action;
	private Form target;
	private InterfaceContext targetContext;

	public TransitionRule(InterfaceContext sourceContext, Form source, Action action, Form target) {
		this(sourceContext, source, action, sourceContext, target);
	}

	public TransitionRule(InterfaceContext sourceContext, Form source, Action action, InterfaceContext targetContext, Form target) {
		super();
		
		if(sourceContext==null) throw new IllegalArgumentException("source context must not be empty");
		if(source==null) throw new IllegalArgumentException("source must not be empty");
		if(action==null) throw new IllegalArgumentException("action must not be empty");
		
		this.sourceContext = sourceContext;
		this.targetContext = targetContext;
		this.source = source;
		this.action = action;
		this.target = target;
		
	}
	
	public TransitionRule(InterfaceContext name, Form source, Action action) {
		this(name,source,action,null);
	}

	public InterfaceContext getSourceContext() {
		return sourceContext;
	}

	public Form getSource() {
		return source;
	}

	public Action getAction() {
		return action;
	}

	public Form getTarget() {
		return target;
	}
	
	public InterfaceContext getTargetContext(){
		return targetContext;
	}
	
	public class TransitionRuleKey implements Comparable<TransitionRuleKey> {
		
		private String getKey(){
			return new StringBuilder().
					append(sourceContext.name()).
					append(source.getName()).
					append(action.getName()).
				toString();
		}

		@Override
		public int compareTo(TransitionRuleKey key) {
			return getKey().compareTo(key.getKey());
		}
		
		@Override
		public int hashCode() {
			
			assert sourceContext!=null && source!=null && action!=null && target!=null;

			final int prime = 31;
			int result = 1;
			result = prime * result + sourceContext.hashCode();
			result = prime * result + source.hashCode();
			result = prime * result + action.hashCode();
			return result;
		}
		
		@Override
		public boolean equals(Object o){
			if(o instanceof TransitionRuleKey){
				TransitionRuleKey key=(TransitionRuleKey)o;
				return getKey().equals(key.getKey());
			}
			return false;
		}

	}
	
	public TransitionRuleKey getKey(){
		return this.new TransitionRuleKey();
	}

}
