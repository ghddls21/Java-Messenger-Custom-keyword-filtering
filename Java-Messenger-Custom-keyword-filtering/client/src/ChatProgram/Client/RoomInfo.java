package ChatProgram.Client;


import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class RoomInfo {

	
	private String groupid ="";

	private Vector<String> user_search_chat = new Vector<>();
	private String group_name="";
	

	public RoomInfo() {
		
	}
	public RoomInfo(String groupid,String groupName) {
		this.groupid = groupid;
		this.group_name = groupName;
	}
	
	public String getGroupid() {
		return groupid;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String roomname) {
		this.group_name = roomname;
	}

	public ConcurrentHashMap<String, Boolean> user_status = new ConcurrentHashMap<>();
	public Vector<String> chat_data = new Vector<>();
	
	
	public void user_online(String str) {
	
		user_status.put(str, true);
	}
	public void user_offline(String str) {
		user_status.put(str, false);
	}
	public ConcurrentHashMap<String, Boolean> get_user_status(){
		return user_status;
	}
	
	public void user_search_result(ChatStruct chatstruct) {
	
		user_search_chat.add("[" + chatstruct.time + "]" + chatstruct.userid + " : " + chatstruct.chatdata);
	}
	
}
