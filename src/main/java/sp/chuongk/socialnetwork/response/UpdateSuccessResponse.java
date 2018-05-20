package sp.chuongk.socialnetwork.response;

import java.util.List;

public class UpdateSuccessResponse {
	boolean success;
	List<String> recepients;
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean isSuccess) {
		this.success = isSuccess;
	}
	public List<String> getRecepients() {
		return recepients;
	}
	public void setRecepients(List<String> recepients) {
		this.recepients = recepients;
	}		
}
