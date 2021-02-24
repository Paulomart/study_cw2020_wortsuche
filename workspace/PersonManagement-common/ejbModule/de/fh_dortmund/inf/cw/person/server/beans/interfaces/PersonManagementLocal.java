package de.fh_dortmund.inf.cw.person.server.beans.interfaces;

import java.util.List;

import javax.ejb.Local;

import de.fh_dortmund.inf.cw.person.server.entities.Person;

@Local
public interface PersonManagementLocal extends PersonManagement {
	public void delete(Person person);
	public Person update(Person person);
	public List<Person> readAllPersons();
}
