package com.project.interfacebuilder.http.forms;

import java.awt.Dimension;

import com.project.interfacebuilder.InterfaceException;
import com.project.interfacebuilder.http.actions.HTTPCancelAction;

public class HTTPInformationForm extends HTTPForm {

	public HTTPInformationForm() throws InterfaceException {
		super();
		addAction(new HTTPCancelAction());
		setDimension(new Dimension(1200,1200));
	}

	@Override
	protected void placeContent() throws InterfaceException {
		
		out.print("<center><h2>This program has been developed by <a href=\"#photo\"><i>Sergey G. Pilipenko</i></a> primarily for self-education, training and amusement");
		out.print("<br><i>Please do not regard it as fully-fledged commercial application</i></h2></center>");
		
		out.print("<h3><p>You may check author's LinkedIn profile <i><a href=\"http://ua.linkedin.com/pub/sergey-pilipenko/6/696/b70\"> here </a></i></p>");
		out.print("<p>or communicate to him by<ul>");
		out.print("<li><i><a href=\"skype:streamlined2\">getting on phone <u>+38(097)245-222-8</u></a></i></li>");
		out.print("<li><i><a href=\"mailto:sergij.pilipenko@gmail.com?Subject=We'd like to talk to you on...\">emailing</a></i></li>");
		out.print("<li><i><a href=\"skype:streamlined2\">sending skype</a></i></li>");
		out.print("<li><i><a href=\"icq:343252487\">or icq message</a></i></li>");
		out.print("</h3><p>");
		
		out.print("<p><center><a name=\"photo\"><img src=\"photo.jpg\" width=\"600\" height=\"600\"/></a><center><p>");
		
	}

}
