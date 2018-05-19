package sp.chuongk.socialnetwork.response;

import java.util.List;

public class UpdateSuccessResponse {
	boolean isSuccess;
	List<String> recepients;
	
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
}
