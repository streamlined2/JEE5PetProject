package com.project;

import java.io.PrintStream;

import javax.naming.NamingException;

import com.project.entities.EntityClass;
import com.project.inspection.EntityInfo;
import com.project.interfacebuilder.InterfaceException;

public class Client6 {

	/**
	 * @param args
	 * @throws NamingException 
	 * @throws InterfaceException 
	 */
	public static void main(String[] args) throws NamingException, InterfaceException {
		Client6.printEntities(System.out);
	}

	public static void printEntities(PrintStream out) throws InterfaceException{
		for(EntityInfo e:EntityClass.getEntitySet()){
			out.println(e.getDisplayName());
		}
	}

}
