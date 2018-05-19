package sp.chuongk.socialnetwork;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import sp.chuongk.socialnetwork.entity.Person;
import sp.chuongk.socialnetwork.request.EmailRequest;
import sp.chuongk.socialnetwork.request.FriendListPojo;
import sp.chuongk.socialnetwork.response.CommonFriendResponse;
import sp.chuongk.socialnetwork.response.ConnectionResponse;
import sp.chuongk.socialnetwork.response.FriendResponse;
import sp.chuongk.socialnetwork.response.SuccessResponse;
import sp.chuongk.socialnetwork.service.PersonService;

@RestController
public class SocialNetworkController {
	
	@Autowired
	PersonService personService;
	
	@GetMapping("/health")
	public String getHealth() {
		return "ok";
	}
	
	@PostMapping("/connection")
	public ResponseEntity<?> addConnection(@RequestBody FriendListPojo friendListPojo){
		ConnectionResponse response = personService.addConnection(friendListPojo.getFriends());
		if (response.isSuccess()) {
			return ResponseEntity.ok(new SuccessResponse(true));
		}
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/email")
	public ResponseEntity<?> getEmail(@RequestBody EmailRequest email){
		Person person = personService.getByEmail(email.getEmail());
		FriendResponse response = new FriendResponse();
		response.setSuccess(true);
		response.setFriends(person.getFriendList().stream().map(Person::getEmail).collect(Collectors.toList()));
		response.setCount(person.getFriendList().size());
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/connection/common")
	public ResponseEntity<?> getCommonConnection(@RequestBody FriendListPojo friendListPojo){
		CommonFriendResponse response= personService.getCommonFriends(friendListPojo.getFriends());
		if (response.isSuccess()) {
			FriendResponse successResponse = new FriendResponse();
			successResponse.setCount(response.getCount());
			successResponse.setSuccess(true);
			successResponse.setFriends(response.getFriends());
			return ResponseEntity.ok(successResponse);
		}
		return ResponseEntity.ok(new ConnectionResponse(response.isSuccess(), response.getMessage()));
	}
}
