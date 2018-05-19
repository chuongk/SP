package sp.chuongk.socialnetwork.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import sp.chuongk.socialnetwork.entity.Person;
import sp.chuongk.socialnetwork.repository.PersonRepository;
import sp.chuongk.socialnetwork.response.ConnectionResponse;
import sp.chuongk.socialnetwork.service.PersonService;


@Service
public class PersonServiceImpl implements PersonService {

	@Autowired
	PersonRepository personRepo;
	
	
	@Override
	public ConnectionResponse addConnection(List<String> connectionList) {
		boolean success = true;
		String message = "";
		if (CollectionUtils.isEmpty(connectionList) || connectionList.size() != 2) {
			success = false;
			message = "Fiend list size is not 2!";
			return new ConnectionResponse(success, message);
		}
		String email1 = connectionList.get(0);
		String email2 = connectionList.get(1);
		Person person1 = getAndInsertIfNotExist(email1);
		Person person2 = getAndInsertIfNotExist(email2);
		
		if (person1.getFriendList().contains(person2) || person2.getFriendList().contains(person1)) {
			success = false;
			message = "Friend connection already exist";
			return new ConnectionResponse(success, message);
		}
		if (person1.getBlockList().contains(person2)) {
			success = false;
			message = email1 + " blocked " + email2;
			return new ConnectionResponse(success, message);
		}
		
		if (person2.getBlockList().contains(person1)) {
			success = false;
			message = email2 + " blocked " + email1;
			return new ConnectionResponse(success, message);
		}
		
		person1.getFriendList().add(person2);
		person2.getFriendList().add(person1);
		personRepo.save(person1);
		personRepo.save(person2);
		return new ConnectionResponse(success, message);
	}
	
	private Person getAndInsertIfNotExist(String email) {
		Optional<Person> personOp = personRepo.findById(email);
		if (personOp.isPresent()) {
			return personOp.get();
		}
		Person person = new Person(email);
		personRepo.save(person);
		return person;
	}

	@Override
	public Person getByEmail(String email) {
		return getAndInsertIfNotExist(email);
	}

}
