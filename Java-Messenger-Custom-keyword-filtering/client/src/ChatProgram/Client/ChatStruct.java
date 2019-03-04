package ChatProgram.Client;

public class ChatStruct {
	String GruoupID;
	String time;
	String userid;
	String chatdata;
	int code;
	
    ChatStruct(String GroupID,String time, String userid, String chatdata, int code){
    	this.GruoupID = GroupID;
    	this.time = time;
    	this.userid = userid;
    	this.chatdata = chatdata;
    	this.code = code;
    }
    public String getGruoupID() {
		return GruoupID;
	}
	public void setGruoupID(String gruoupID) {
		GruoupID = gruoupID;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getChatdata() {
		return chatdata;
	}
	public void setChatdata(String chatdata) {
		this.chatdata = chatdata;
	}
}
