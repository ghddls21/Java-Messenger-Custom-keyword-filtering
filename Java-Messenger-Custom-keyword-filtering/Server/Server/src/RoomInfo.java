import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;


public class RoomInfo{

	  
		public String groupid ="";
		public ConcurrentHashMap<String, ServerConnect.UserInfo> map_online_user = new ConcurrentHashMap<>();
		private String Admin_User;
		public Vector<String> vector_offline_user = new Vector(); //오프라인유저
		public String group_name="";
		
		
		public String getGroup_name() {
			return group_name;
		}
		public void setGroup_name(String group_name) {
			this.group_name = group_name;
		}
		RoomInfo(){
			
		}
		RoomInfo(String Admin_User){
			this.Admin_User = Admin_User;
		}
		RoomInfo(String userid, ServerConnect.UserInfo userInfo,String groupid,String group_name){
			this.groupid = groupid;
			this.map_online_user.put(userid, userInfo);
			this.group_name = group_name;
		}
		public void setgroupid(String groupid) {
			this.groupid = groupid;
		}
		public String getgroupid(){
			return groupid;
		}
		public ConcurrentHashMap<String, ServerConnect.UserInfo> get_online_user(){
			return map_online_user;
		}
		public Vector get_offline_user() {
			return vector_offline_user;
		}
		public void online_User(ServerConnect.UserInfo userInfo){   // 온라인 유저 정보추가
			this.map_online_user.put(userInfo.getUserId(), userInfo);
		}
		public void add_user(String ID) {    //오프라인 유저 정보.(그룹내 유저 정보)
			vector_offline_user.add(ID);
		}
		public void Remove_User(ServerConnect.UserInfo userInfo){  //온라인 유저 오프라인전환
			this.map_online_user.remove(userInfo.getUserId());
		}
		public void BroadCast_Room(String str){ //현재 방 접속자들에게 전달			
				Set set = map_online_user.entrySet();
				Iterator iterator = set.iterator();
				
				while(iterator.hasNext()) {
					Map.Entry entry = (Map.Entry)iterator.next();
					String id = (String)entry.getKey();
					ServerConnect.UserInfo userInfo = (ServerConnect.UserInfo)entry.getValue();
					userInfo.send_Message(str);
			}
		}	
}
