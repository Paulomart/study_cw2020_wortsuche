package de.fh_dortmund.inf.cw.shop.server.beans;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import de.fh_dortmund.inf.cw.shop.server.beans.interfaces.CustomerRequestManagementLocal;
import de.fh_dortmund.inf.cw.shop.server.beans.interfaces.CustomerRequestManagementRemote;
import de.fh_dortmund.inf.cw.shop.server.entities.CustomerRequest;

@Singleton
@Startup
public class CustomerRequestManagementBean implements CustomerRequestManagementLocal, CustomerRequestManagementRemote {

	private Map<Long, CustomerRequest> customerRequests;
	private long lastRequestNumber = 0;
	
	@PostConstruct
	private void init() {
		customerRequests = new LinkedHashMap<Long, CustomerRequest>();
	}
	
	@Override
	public List<CustomerRequest> readAllCustomerRequests() {
		return new ArrayList<CustomerRequest>(customerRequests.values());
	}
	
	@Override
	public CustomerRequest create(CustomerRequest customerRequest) {
		if (customerRequest == null) {
			throw new IllegalArgumentException("CustomerRequest cannot be null.");
		}
		if (customerRequest.getRequestNumber() != 0) {
			throw new IllegalArgumentException("Item-Number must be 0.");
		}
		
		customerRequest.setRequestNumber(++lastRequestNumber);
		customerRequests.put(customerRequest.getRequestNumber(), customerRequest);
		
		return customerRequest;
	}

	@Override
	public CustomerRequest update(CustomerRequest customerRequest) {
		if (customerRequest == null) {
			throw new IllegalArgumentException("CustomerRequest cannot be null.");
		}
		if (customerRequest.getRequestNumber() < 0) {
			throw new IllegalArgumentException("Item-Number may not be negative.");
		}
		
		if (customerRequest.getRequestNumber() == 0) {
			customerRequest.setRequestNumber(++lastRequestNumber);
		}
		
		customerRequests.put(customerRequest.getRequestNumber(), customerRequest);
		
		return customerRequest;
	}

	@Override
	public void delete(CustomerRequest customerRequest) {
		if (customerRequest == null) {
			throw new IllegalArgumentException("CustomerRequest cannot be null.");
		}
		if (customerRequest.getRequestNumber() < 0) {
			throw new IllegalArgumentException("Item-Number may not be negative.");
		}
		
		
		customerRequests.remove(customerRequest.getRequestNumber());
	}
}
