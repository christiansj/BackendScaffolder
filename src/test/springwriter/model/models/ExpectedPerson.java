package test.springwriter.model.models;

import javax.persistance.Entity;
import javax.persistance.Id;

@Entity
public class Person {

	private int id;
	private String firstName;
	private String lastName;
}
