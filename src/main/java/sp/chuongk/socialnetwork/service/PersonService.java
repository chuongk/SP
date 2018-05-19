package sp.chuongk.socialnetwork.service;

import java.util.List;

import sp.chuongk.socialnetwork.entity.Person;
import sp.chuongk.socialnetwork.response.CommonFriendResponse;
import sp.chuongk.socialnetwork.response.ConnectionResponse;
import sp.chuongk.socialnetwork.response.UpdateRespone;

public interface PersonService {
	ConnectionResponse addConnection(List<String> connectionList);
	
	Person getByEmail(String email);
	
	CommonFriendResponse getCommonFriends(List<String> connectionList);
	
	ConnectionResponse addSubscribe(String requestorEmail, String targetEmail);
	
	ConnectionResponse addBlock(String requestorEmail, String targetEmail);
	
	UpdateRespone doUpdate(String email, String text);
}
