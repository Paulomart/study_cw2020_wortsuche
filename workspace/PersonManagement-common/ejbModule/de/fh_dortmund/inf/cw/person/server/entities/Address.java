package de.fh_dortmund.inf.cw.person.server.entities;

import java.io.Serializable;

public class Address implements Serializable {
	private Person person;
	private String street;
	private String zip;
	private String city;
	
	public Address() {
		
	}
	
	public Address(Person person) {
		this.person = person;
	}
	
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	
}
