package de.fh_dortmund.inf.cw.shop.server.beans.interfaces;

import java.util.List;

import javax.ejb.Local;

import de.fh_dortmund.inf.cw.shop.server.entities.CustomerRequest;

@Local
public interface CustomerRequestManagementLocal extends CustomerRequestManagement {
	public List<CustomerRequest> readAllCustomerRequests();
	public CustomerRequest update(CustomerRequest customerRequest);
	public void delete(CustomerRequest customerRequest);
}
