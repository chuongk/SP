package sp.chuongk.socialnetwork;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import sp.chuongk.socialnetwork.entity.Person;
import sp.chuongk.socialnetwork.request.DirectRequest;
import sp.chuongk.socialnetwork.request.EmailRequest;
import sp.chuongk.socialnetwork.request.FriendListRequest;
import sp.chuongk.socialnetwork.request.UpdateRequest;
import sp.chuongk.socialnetwork.response.CommonFriendResponse;
import sp.chuongk.socialnetwork.response.ConnectionResponse;
import sp.chuongk.socialnetwork.response.FriendResponse;
import sp.chuongk.socialnetwork.response.SuccessResponse;
import sp.chuongk.socialnetwork.response.UpdateRespone;
import sp.chuongk.socialnetwork.response.UpdateSuccessResponse;
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
	public ResponseEntity<?> addConnection(@RequestBody FriendListRequest friendListPojo){
		ConnectionResponse response = personService.addConnection(friendListPojo.getFriends());
		if (response.isSuccess()) {
			return ResponseEntity.ok(new SuccessResponse(true));
		}
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/email")
	public ResponseEntity<?> getEmail(@RequestBody EmailRequest email){
		FriendResponse response = new FriendResponse();
		if (StringUtils.isEmpty(email.getEmail())) {
			response.setSuccess(false);
			response.setFriends(new ArrayList<>());
			response.setCount(0);
		}
		else{
			Person person = personService.getByEmail(email.getEmail());
			response.setSuccess(true);
			response.setFriends(person.getFriendList().stream().map(Person::getEmail).collect(Collectors.toList()));
			response.setCount(person.getFriendList().size());
		}		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/connection/common")
	public ResponseEntity<?> getCommonConnection(@RequestBody FriendListRequest friendListPojo){
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
	
	@PostMapping("/email/subscribe")
	public ResponseEntity<?> addSubscriber(@RequestBody DirectRequest directRequest){
		ConnectionResponse response = personService.addSubscribe(directRequest.getRequestor(), directRequest.getTarget());
		if (response.isSuccess()) {
			return ResponseEntity.ok(new SuccessResponse(true));
		}
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/email/block")
	public ResponseEntity<?> addBlocker(@RequestBody DirectRequest directRequest){
		ConnectionResponse response = personService.addBlock(directRequest.getRequestor(), directRequest.getTarget());
		if (response.isSuccess()) {
			return ResponseEntity.ok(new SuccessResponse(true));
		}
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/email/update")
	public ResponseEntity<?> doUpdate(@RequestBody UpdateRequest updateRequest){
		UpdateRespone response = personService.doUpdate(updateRequest.getSender(), updateRequest.getText());
		if (response.isSuccess()) {
			UpdateSuccessResponse sucessResponse = new UpdateSuccessResponse();
			sucessResponse.setSuccess(true);
			sucessResponse.setRecepients(response.getRecepients());
			return ResponseEntity.ok(sucessResponse);
		}
		
		return ResponseEntity.ok(response);
	}
}
