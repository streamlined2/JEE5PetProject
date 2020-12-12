package com.project.datatypes;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class Currency extends Number implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3264674721725127161L;
	private Integer major;
	private Integer minor;
	
	public Currency(Double value){
		major=(int) Math.floor(value);
		minor=(int)((value-major)*100);
	}

	@Override
	public double doubleValue() {
		return (major+minor*0.01);
	}

	@Override
	public float floatValue() {
		return (float) (major+minor*0.01);
	}

	@Override
	public int intValue() {
		return major;
	}

	@Override
	public long longValue() {
		return major;
	}

	@Override
	public String toString(){
		return Double.toString(doubleValue());
	}

}
