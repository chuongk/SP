package sp.chuongk.socialnetwork.response;

public class SuccessResponse {
	private boolean success;

	public SuccessResponse(boolean success_) {
		this.success = success_;
	}
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	
}
