package com.project.interfacebuilder.http.forms;

import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.HTTPInterfaceBuilder.InterfaceContext;
import com.project.interfacebuilder.menu.Menu;
import com.project.interfacebuilder.menu.MenuItem;

public class HTTPTopLevelMenuSelectionForm extends HTTPMenuSelectionForm {
	
	private static class TopLevelMenu extends Menu {
		public TopLevelMenu(){
			addMenuItem(new MenuItem("Entity",InterfaceContext.dataEditingUseCase));
			addMenuItem(new MenuItem("Query",InterfaceContext.queryPerformanceUseCase));
		}
	}

	public HTTPTopLevelMenuSelectionForm() throws InterfaceException {
		super(new TopLevelMenu());
		
	}

}
