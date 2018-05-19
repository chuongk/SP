package sp.chuongk.socialnetwork.response;

import java.util.ArrayList;
import java.util.List;

public class CommonFriendResponse {
	private boolean isSuccess;
	private List<String> friends;
	private int count;
	private String message;
	
	public CommonFriendResponse() {
		friends = new ArrayList<>();
		count = 0;
	}
	
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public List<String> getFriends() {
		return friends;
	}
	public void setFriends(List<String> friends) {
		this.friends = friends;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
