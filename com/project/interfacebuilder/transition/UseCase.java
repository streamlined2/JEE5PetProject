package com.project.interfacebuilder.transition;

import java.util.TreeMap;

import com.project.interfacebuilder.Action;
import com.project.interfacebuilder.Form;
import com.project.interfacebuilder.http.HTTPInterfaceBuilder.InterfaceContext;

public class UseCase {
	
	private TreeMap<TransitionRule.TransitionRuleKey,TransitionRule> map=new TreeMap<TransitionRule.TransitionRuleKey,TransitionRule>();
	
	public UseCase(){}
	
	public void addRule(TransitionRule rule){
		map.put(rule.getKey(), rule);
	}
	
	public TransitionRule getTransitionRule(InterfaceContext context, Form source, Action action){
		return map.get(new TransitionRule(context,source,action).getKey());
	}

}
