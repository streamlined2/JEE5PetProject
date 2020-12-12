package com.project.interfacebuilder.http.selectors;

import java.awt.Font;

import com.project.inspection.property.InformationPropertyInfo;
import com.project.interfacebuilder.Form;
import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.Helpers;
import com.project.interfacebuilder.http.forms.HTTPForm;

public abstract class SelectorSupport implements HTTPSelector {
	
	private static final long serialVersionUID = 6424048727336656427L;
	
	private Object value;
	private Object initialValue;
	private InformationPropertyInfo propertyInfo;
	private Integer id;
	private Font renderFont;
	
	protected SelectorSupport(){
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		if(value!=null){
			this.value = value;
		}
	}

	public Object getInitialValue() {
		return initialValue;
	}

	public void setInitialValue(Object initialValue) {
		this.initialValue = initialValue;
		setValue(initialValue);
	}

	@Override
	public InformationPropertyInfo getPropertyInfo() {
		return propertyInfo;
	}

	@Override
	public void setPropertyInfo(InformationPropertyInfo propertyInfo) {
		this.propertyInfo = propertyInfo;
		propertyInfo.getEntityInfo().setSelector(propertyInfo,this);
	}
	
	public Integer getId(){
		return id;
	}
	
	public void setId(Integer id){
		this.id = id;
	}
	
	public Font getRenderFont() {
		if(renderFont==null){
			renderFont=new Font("Verdana",Font.ITALIC,18);
		}
		return renderFont;
	}

	public void setRenderFont(Font renderFont) {
		this.renderFont = renderFont;
	}

	public String mapStateToValue(String state) throws InterfaceException{
		return state;
	}
	
	public String getStyle(){
		return Helpers.getStyle(getRenderFont());
	}
	
	//helper method to check type & get reference to HTTPForm
	protected HTTPForm getHTTPForm(Form form) throws InterfaceException{
		if(!(form instanceof HTTPForm)) throw new InterfaceException("HTTPSelector must be placed on HTTPForm");
		return (HTTPForm)form;
	}
	
}
