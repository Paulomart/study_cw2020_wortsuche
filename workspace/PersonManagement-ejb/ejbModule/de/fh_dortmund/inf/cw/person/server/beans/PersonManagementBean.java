package de.fh_dortmund.inf.cw.person.server.beans;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import de.fh_dortmund.inf.cw.person.server.beans.interfaces.PersonManagementLocal;
import de.fh_dortmund.inf.cw.person.server.beans.interfaces.PersonManagementRemote;
import de.fh_dortmund.inf.cw.person.server.entities.Address;
import de.fh_dortmund.inf.cw.person.server.entities.Email;
import de.fh_dortmund.inf.cw.person.server.entities.NaturalPerson;
import de.fh_dortmund.inf.cw.person.server.entities.Person;
import de.fh_dortmund.inf.cw.person.server.enums.Gender;

@Singleton
@Startup
public class PersonManagementBean implements PersonManagementLocal, PersonManagementRemote {
	private Map<Long, Person> persons;
	private long lastId = 0;

	@PostConstruct
	public void init() {
		persons = new LinkedHashMap<Long, Person>();
		createDefaultPersons();
	}
	
	@Override
	public Person create(Person person) {
		if (person == null) {
			throw new IllegalArgumentException("Person cannot be null.");
		}
		if (person.getId() != 0) {
			throw new IllegalArgumentException("ID must be 0.");
		}
		
		person.setId(++lastId);
		persons.put(person.getId(), person);
		
		return person;	
	}
	
	@Override
	public List<Person> readAllPersons() {
		return new ArrayList<Person>(persons.values());
	}
	
	@Override
	public Person update(Person person) {
		if (person == null) {
			throw new IllegalArgumentException("Person cannot be null.");
		}
		if (person.getId() < 0) {
			throw new IllegalArgumentException("ID may not be negative.");
		}
		
		if (person.getId() == 0) {
			person.setId(++lastId);
		}
		
		persons.put(person.getId(), person);
		
		return person;	
	}

	@Override
	public void delete(Person person) {
		if (person == null) {
			throw new IllegalArgumentException("Person cannot be null.");
		}
		if (person.getId() < 0) {
			throw new IllegalArgumentException("ID may not be negative.");
		}
		
		
		persons.remove(person.getId());
	}

	private void createDefaultPersons() {
		create(buildPerson("Mustermann", "Erika", "erika@mustermann.test", "Musterstr. 1", "12345", "Musterstadt"));
		create(buildPerson("Testnutzer", "Jörg", "joerg@test.test", "Friedenstr. 72", "46485", "Wesel"));		 
	}
	
	private Person buildPerson(String name, String firstName, String email, String street, String zip, String city) {
		NaturalPerson person = new NaturalPerson();
		person.setName(name);
		person.setFirstName(firstName);
		person.setGender(Gender.UNKNOWN);
		
		
		Address address = new Address();
		address.setStreet(street);
		address.setZip(zip);
		address.setCity(city);
		
		Email emailObject = new Email();
		emailObject.setEmail(email);
		
		person.addToEmails(emailObject);
		person.addToAddress(address);
		
		return person;		
	}
	
}
