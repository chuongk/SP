package sp.chuongk.socialnetwork.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import sp.chuongk.socialnetwork.entity.Person;
import sp.chuongk.socialnetwork.repository.PersonRepository;
import sp.chuongk.socialnetwork.response.CommonFriendResponse;
import sp.chuongk.socialnetwork.response.ConnectionResponse;
import sp.chuongk.socialnetwork.response.UpdateRespone;
import sp.chuongk.socialnetwork.service.PersonService;
import sp.chuongk.socialnetwork.util.StringUtil;

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
		String email1 = connectionList.get(0).trim();
		String email2 = connectionList.get(1).trim();

		if (!StringUtil.isEmailFormat(email1)) {
			success = false;
			message = email1 + " is in wrong format!";
			return new ConnectionResponse(success, message);
		}
		
		if (!StringUtil.isEmailFormat(email2)) {
			success = false;
			message = email2 + " is in wrong format!";
			return new ConnectionResponse(success, message);
		}

		if (email1.equals(email2)) {
			success = false;
			message = "Both emails are the same";
			return new ConnectionResponse(success, message);
		}
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

	@Override
	public CommonFriendResponse getCommonFriends(List<String> connectionList) {
		CommonFriendResponse response = new CommonFriendResponse();
		if (CollectionUtils.isEmpty(connectionList) || connectionList.size() != 2) {
			response.setSuccess(false);
			response.setMessage("Fiend list size is not 2!");
			return response;
		}
		String email1 = connectionList.get(0).trim();
		String email2 = connectionList.get(1).trim();
		
		if (!StringUtil.isEmailFormat(email1)) {
			response.setSuccess(false);
			response.setMessage(email1 + " is in wrong format!");
			return response;
		}
		
		if (!StringUtil.isEmailFormat(email2)) {
			response.setSuccess(false);
			response.setMessage(email2 + " is in wrong format!");
			return response;
		}
		
		List<String> friendList1 = getAndInsertIfNotExist(email1).getFriendList().stream().map(Person::getEmail)
				.collect(Collectors.toList());
		List<String> friendList2 = getAndInsertIfNotExist(email2).getFriendList().stream().map(Person::getEmail)
				.collect(Collectors.toList());

		List<String> commonList = friendList1.stream().filter(o -> friendList2.contains(o))
				.collect(Collectors.toList());
		response.setSuccess(true);
		response.setFriends(commonList);
		response.setCount(commonList.size());
		return response;
	}

	@Override
	public ConnectionResponse addSubscribe(String requestorEmail, String targetEmail) {
		boolean success = true;
		String message = "";
		requestorEmail = requestorEmail.trim();
		targetEmail = targetEmail.trim();
		if (requestorEmail.equals(targetEmail)) {
			success = false;
			message = "Both emails are the same";
			return new ConnectionResponse(success, message);
		}
		Person requestor = getAndInsertIfNotExist(requestorEmail);
		Person target = getAndInsertIfNotExist(targetEmail);
		boolean subscriberAlreadyExist = target.getSubscribers().stream().map(Person::getEmail)
				.collect(Collectors.toList()).contains(requestorEmail);
		if (subscriberAlreadyExist) {
			success = false;
			message = "Subcribers already exist!";
			return new ConnectionResponse(success, message);
		}
		target.getSubscribers().add(requestor);
		personRepo.save(target);
		return new ConnectionResponse(success, message);
	}

	@Override
	public ConnectionResponse addBlock(String requestorEmail, String targetEmail) {
		boolean success = true;
		String message = "";
		requestorEmail = requestorEmail.trim();
		targetEmail = targetEmail.trim();
		if (StringUtils.isEmpty(requestorEmail) || StringUtils.isEmpty(targetEmail)) {
			success = false;
			message = "Email cannot be empty!";
			return new ConnectionResponse(success, message);
		}
		if (requestorEmail.equals(targetEmail)) {
			success = false;
			message = "Both emails are the same";
			return new ConnectionResponse(success, message);
		}
		
		if (!StringUtil.isEmailFormat(requestorEmail)) {
			success = false;
			message = requestorEmail + " is in wrong format!";			
			return new ConnectionResponse(success, message);
		}
		
		if (!StringUtil.isEmailFormat(targetEmail)) {
			success = false;
			message = targetEmail + " is in wrong format!";	
			return new ConnectionResponse(success, message);
		}
		
		Person requestor = getAndInsertIfNotExist(requestorEmail);
		Person target = getAndInsertIfNotExist(targetEmail);
		boolean blockerAlreadyExist = requestor.getBlockList().stream().map(Person::getEmail)
				.collect(Collectors.toList()).contains(targetEmail);
		if (blockerAlreadyExist) {
			success = false;
			message = "Target already blocked!";
			return new ConnectionResponse(success, message);
		}
		requestor.getBlockList().add(target);
		personRepo.save(requestor);
		target.getIsBlockedList().add(requestor);
		personRepo.save(target);
		return new ConnectionResponse(success, message);
	}

	@Override
	public UpdateRespone doUpdate(String email, String text) {
		email = email.trim();
		if (!StringUtil.isEmailFormat(email)) {
			UpdateRespone response = new UpdateRespone();
			response.setSuccess(false);
			response.setMessage("Email is in wrong format!");
		}

		Person sender = getAndInsertIfNotExist(email);
		Set<String> recepients = new HashSet<>();
		Set<String> mentionList = StringUtil.findMentionEmails(text);
		recepients.addAll(mentionList);
		recepients.addAll(sender.getSubscribers().stream().map(Person::getEmail).collect(Collectors.toList()));
		recepients.addAll(sender.getFriendList().stream().map(Person::getEmail).collect(Collectors.toList()));
		recepients.removeAll(sender.getIsBlockedList().stream().map(Person::getEmail).collect(Collectors.toList()));

		UpdateRespone response = new UpdateRespone();
		response.setSuccess(true);
		response.setRecepients(recepients.stream().collect(Collectors.toList()));
		return response;
	}

}
