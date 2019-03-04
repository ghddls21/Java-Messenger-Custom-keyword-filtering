package ChatProgram.Client;

public class Combo_Keyword_Info {

	String Groupid="";
	String Group_name="";
	
	public Combo_Keyword_Info(String groupid,String group_name) {
		this.Groupid = groupid;
		this.Group_name = group_name;
	}
	
	public String getGroupid() {
		return Groupid;
	}
	public void setGroupid(String groupid) {
		Groupid = groupid;
	}
	public String getGroup_name() {
		return Group_name;
	}
	public void setGroup_name(String group_name) {
		Group_name = group_name;
	}
	
}
