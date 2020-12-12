package com.project.interfacebuilder.http.forms;

import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.menu.Menu;
import com.project.interfacebuilder.menu.MenuItem;
import com.project.interfacebuilder.transition.Dispatcher.InterfaceContext;

public class HTTPTopLevelMenuSelectionForm extends HTTPMenuSelectionForm {
	
	private static class TopLevelMenu extends Menu {
		public TopLevelMenu(){
			addMenuItem(new MenuItem("Entity",InterfaceContext.DATA_EDITING_TRANSITION_SET));
			addMenuItem(new MenuItem("Query",InterfaceContext.QUERY_EXECUTION_TRANSITION_SET));
			addMenuItem(new MenuItem("Info",InterfaceContext.INFORMATION_CONTEXT));
		}
	}

	public HTTPTopLevelMenuSelectionForm() throws InterfaceException {
		super(new TopLevelMenu());
	}

}
