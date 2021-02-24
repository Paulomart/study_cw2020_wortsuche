package de.fh_dortmund.inf.cw.shop.server.beans;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import de.fh_dortmund.inf.cw.person.server.entities.Email;
import de.fh_dortmund.inf.cw.person.server.entities.NaturalPerson;
import de.fh_dortmund.inf.cw.shop.server.beans.interfaces.CustomerRequestManagementLocal;
import de.fh_dortmund.inf.cw.shop.server.entities.CustomerRequest;

@MessageDriven(mappedName = "java:global/jms/CustomerRequestQueue", activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class CustomerRequestBean implements MessageListener {

	@EJB
	private CustomerRequestManagementLocal customerRequestManagement;
	
	@Override
	public void onMessage(Message message) {
		try {
			TextMessage textMessage = (TextMessage) message;
			CustomerRequest customerRequest = new CustomerRequest();
						
			NaturalPerson customer = new NaturalPerson();
			customer.setName(textMessage.getStringProperty("NAME"));
			customer.setFirstName(textMessage.getStringProperty("FIRST_NAME"));
			
			Email customerEmail = new Email();
			customerEmail.setEmail(textMessage.getStringProperty("MAIL"));
			customer.addToEmails(customerEmail);
			
			customerRequest.setCustomer(customer);
			customerRequest.setMessage(textMessage.getText());
			
			customerRequest = customerRequestManagement.create(customerRequest);
			
			System.out.println(String.format("Customer-Request #%d received.", customerRequest.getRequestNumber()));
			
		} catch (Exception ex) {
			System.err.println("Error while process customer request: " + ex.getMessage());
		}
	}
	
}
