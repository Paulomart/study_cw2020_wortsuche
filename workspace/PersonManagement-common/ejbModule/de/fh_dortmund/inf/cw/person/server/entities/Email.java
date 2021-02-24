package de.fh_dortmund.inf.cw.person.server.entities;

import java.io.Serializable;

public class Email  implements Serializable {
	private Person person;
	private String email;

	public Email() {
		
	}
	
	public Email(Person person) {
		super();
		this.person = person;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
