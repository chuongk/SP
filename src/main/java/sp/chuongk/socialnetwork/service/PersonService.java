package sp.chuongk.socialnetwork.service;

import java.util.List;

import sp.chuongk.socialnetwork.entity.Person;
import sp.chuongk.socialnetwork.response.CommonFriendResponse;
import sp.chuongk.socialnetwork.response.ConnectionResponse;

public interface PersonService {
	ConnectionResponse addConnection(List<String> connectionList);
	
	Person getByEmail(String email);
	
	CommonFriendResponse getCommonFriends(List<String> connectionList);
	
	ConnectionResponse addSubscribe(String requestorEmail, String targetEmail);
}
