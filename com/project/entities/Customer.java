package com.project.entities;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Customer extends EntityClass {
	
	private static final long serialVersionUID = 8581575444676033350L;

	public enum Kind {PERSON, ORGANIZATION};
	
	private Integer id;
	private String name;
	private String address;
	private Kind kind;
	private Date creationDate;
	private Set<Phone> phones = new HashSet<Phone>();
	private Country country;
	
	private int rating;
	private boolean creditAvailable;
	private Timestamp lastTransactionTime;
	
	public Customer() {
	}
	
	public Customer(String name, String address,
			Kind kind, Date creationDate) {
		super();
		this.name = name;
		this.address = address;
		this.kind = kind;
		this.creationDate = creationDate;
	}

	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Kind getKind() {
		return kind;
	}

	public void setKind(Kind kind) {
		this.kind = kind;
	}

	@Temporal(TemporalType.DATE)
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public boolean isCreditAvailable() {
		return creditAvailable;
	}

	public void setCreditAvailable(boolean creditAvailable) {
		this.creditAvailable = creditAvailable;
	}

/*	@Embedded
	public Currency getSellVolume() {
		return sellVolume;
	}

	public void setSellVolume(Currency sellVolume) {
		this.sellVolume = sellVolume;
	}

*/	public Timestamp getLastTransactionTime() {
		return lastTransactionTime;
	}

	public void setLastTransactionTime(Timestamp lastTransactionTime) {
		this.lastTransactionTime = lastTransactionTime;
	}

	@OneToMany(mappedBy="customer")
	public Set<Phone> getPhones() {
		return phones;
	}

	public void setPhones(Set<Phone> phones) {
		this.phones = phones;
	}

	@ManyToOne
	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

}
