package com.project.inspection;

import java.io.Serializable;

public class Range implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5041493863665551617L;
	
	private int startFrom;
	private int finishAt;
	
	public Range(int startFrom, int finishAt) {
		super();
		this.startFrom = startFrom;
		this.finishAt = finishAt;
	}

	public int getStartFrom() {
		return startFrom;
	}

	public void setStartFrom(int startFrom) {
		this.startFrom = startFrom;
	}

	public int getFinishAt() {
		return finishAt;
	}

	public void setFinishAt(int finishAt) {
		this.finishAt = finishAt;
	}
	
	public int getCount(){
		return finishAt-startFrom+1;
	}
	
	public void setCount(int count){
		finishAt=startFrom+count-1;
	}
	
}
