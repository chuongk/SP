package sp.chuongk.socialnetwork.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Person {
	@Id
	private String email;
	
	@OneToMany
	private List<Person> friends;
}
