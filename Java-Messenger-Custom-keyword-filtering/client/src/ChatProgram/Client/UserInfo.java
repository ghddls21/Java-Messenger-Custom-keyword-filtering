package ChatProgram.Client;

public class UserInfo {

	private String id ="";
	private Boolean status = false;
	
	
	public UserInfo(String id,Boolean status) {
		this.id = id;
		this.status = status;
		// TODO Auto-generated constructor stub
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}

	
	
}
