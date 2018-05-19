package sp.chuongk.socialnetwork.service;

import java.util.List;

import sp.chuongk.socialnetwork.response.ConnectionResponse;

public interface PersonService {
	ConnectionResponse addConnection(List<String> connectionList);
}
