package com.project.interfacebuilder.transition;

import java.util.TreeMap;

import com.project.interfacebuilder.Action;
import com.project.interfacebuilder.Form;
import com.project.interfacebuilder.http.HTTPInterfaceBuilder.InterfaceContext;

public class UseCase {
	
	private TreeMap<TransitionRule.TransitionRuleKey,TransitionRule> map=new TreeMap<TransitionRule.TransitionRuleKey,TransitionRule>();
	
	public UseCase(){}
	
	public void addRule(TransitionRule rule){
		TransitionRule.TransitionRuleKey key=rule.getKey();
		map.put(key, rule);
	}
	
	public TransitionRule getTransitionRule(InterfaceContext context, Form source, Action action){
		
		TransitionRule rule=new TransitionRule(context,source,action);
		if(map.containsKey(rule.getKey())){
			return map.get(rule.getKey());
		}
		return null;
	}

}
