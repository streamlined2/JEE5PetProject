package com.project.interfacebuilder.http.actions;

import java.awt.Font;

import com.project.interfacebuilder.Action;
import com.project.interfacebuilder.http.HTTPController;

// interface hierarchy Action/HTTPAction represents contract that implementing objects obliged to comply
public interface HTTPAction extends Action {

	public void setController(HTTPController controller);
	
	public String getStyle();
	
	public Font getRenderFont();
	public void setRenderFont(Font font);
	

}
