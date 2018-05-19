package sp.chuongk.socialnetwork.response;

import java.util.List;

public class UpdateRespone {
	boolean isSuccess;
	List<String> recepients;
	String message;
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public List<String> getRecepients() {
		return recepients;
	}
	public void setRecepients(List<String> recepients) {
		this.recepients = recepients;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
