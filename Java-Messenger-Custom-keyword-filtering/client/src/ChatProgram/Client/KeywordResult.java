package ChatProgram.Client;

public class KeywordResult {

	private String chat = "";
	private String check = "";
	private String kategory = "";
	private String groupid = "";
	private String kategory_data = "";
	private String chat_number ="";

	public String getChat_number() {
		return chat_number;
	}

	public void setChat_number(String chat_number) {
		this.chat_number = chat_number;
	}

	public String getKategory_data() {
		return kategory_data;
	}

	public void setKategory_data(String kategory_data) {
		this.kategory_data = kategory_data;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getKategory() {
		return kategory;
	}

	public void setKategory(String kategory) {
		this.kategory = kategory;
	}

	public KeywordResult(String kategory,String Kategory_data) {
		this.kategory = kategory;
		this.kategory_data = Kategory_data;
	}

	public KeywordResult(String kategory, String groupid, String chat, String check,String chat_number) {
		// TODO Auto-generated constructor stub

		this.chat = chat;
		this.check = check;
		this.groupid = groupid;
		this.chat_number = chat_number;
		this.kategory = kategory;
	}

	public String getChat() {
		return chat;
	}

	public void setChat(String chat) {
		this.chat = chat;
	}

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

}
