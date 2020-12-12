package com.project.interfacebuilder.http.actions;

import java.awt.Font;

import com.project.interfacebuilder.Action;
import com.project.interfacebuilder.http.HTTPController;

public interface HTTPAction extends Action {

	public void setController(HTTPController controller);
	
	public String getStyle();
	
	public Font getRenderFont();
	public void setRenderFont(Font font);
	

}
