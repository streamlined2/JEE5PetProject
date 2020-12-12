package com.project.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Phone extends EntityClass {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5541725130459526114L;

	public enum Kind {CELLULAR, FIXED};
	
	private Integer id;
	private Kind kind;
	private String number;
	
	public Phone(){
	}

	@Id @GeneratedValue
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Kind getKind() {
		return kind;
	}

	public void setKind(Kind kind) {
		this.kind = kind;
	}

	@Column(name="PHONENUMBER")
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
}
