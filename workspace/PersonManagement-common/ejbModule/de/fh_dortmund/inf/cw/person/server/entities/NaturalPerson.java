package de.fh_dortmund.inf.cw.person.server.entities;

import de.fh_dortmund.inf.cw.person.server.enums.Gender;

public class NaturalPerson extends Person {
	private static final long serialVersionUID = 1L;

	private String firstName;
	private Gender gender;
	
	public NaturalPerson() {
		super();
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}	
}
