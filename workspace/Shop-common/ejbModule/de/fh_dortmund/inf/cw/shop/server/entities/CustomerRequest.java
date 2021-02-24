package de.fh_dortmund.inf.cw.shop.server.entities;

import java.io.Serializable;

import de.fh_dortmund.inf.cw.person.server.entities.Person;

public class CustomerRequest implements Serializable {
	private long requestNumber;
	private String message;
	private Person customer;
	
	public CustomerRequest() {
		
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Person getCustomer() {
		return customer;
	}

	public void setCustomer(Person customer) {
		this.customer = customer;
	}

	public long getRequestNumber() {
		return requestNumber;
	}

	public void setRequestNumber(long requestNumber) {
		this.requestNumber = requestNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (requestNumber ^ (requestNumber >>> 32));
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
		CustomerRequest other = (CustomerRequest) obj;
		if (requestNumber != other.requestNumber)
			return false;
		return true;
	}
	
	
}
