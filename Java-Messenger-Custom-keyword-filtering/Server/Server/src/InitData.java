
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class InitData { //서버시작시 이미 생성된 그룹DB에서 꺼내오기 (현재 DB그룹방 정보클래스)

	RequestDB requestDB = new RequestDB();
	
	
	ConcurrentHashMap<String,RoomInfo> requestmap = new ConcurrentHashMap<String,RoomInfo>();
	ConcurrentHashMap<String,RoomInfo> room_user_info = new ConcurrentHashMap<String,RoomInfo>(); //각방에 속한 유저 목록
	Vector<ServerConnect.UserInfo> current_user = new Vector<ServerConnect.UserInfo>();//사용할 헤시맵 데이터
	
	
	public void setGroupID() {
		requestmap.putAll(requestDB.GroupInfo());     //<그룹이름,RoomInfo>
	}
	public void setGroupwithUser() {
		room_user_info.putAll(requestDB.GroupInUserInfo(requestmap));
	}
	
	InitData(){
		setGroupID();
		setGroupwithUser();
	}
	
	
	
}
