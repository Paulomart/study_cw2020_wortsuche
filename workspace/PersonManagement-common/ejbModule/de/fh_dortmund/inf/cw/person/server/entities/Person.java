package de.fh_dortmund.inf.cw.person.server.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Person implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String name;
	
	private List<Email> emails;
	private List<Address> addresses;
	
	public Person() {
		this.emails = new ArrayList<Email>();
		this.addresses = new ArrayList<Address>();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Email[] getEmails() {
		return (Email[]) emails.toArray();
	}

	public Address[] getAddresses() {
		return (Address[]) addresses.toArray();
	}
	
	public void addToEmails(Email email) {
		email.setPerson(this);
		emails.add(email);
	}
	
	public void removeFromEmails(Email email) {
		emails.remove(email);
	}
	
	public void addToAddress(Address address) {
		address.setPerson(this);
		addresses.add(address);
	}
	
	public void removeFromAddress(Address address) {
		addresses.remove(address);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}	
}
