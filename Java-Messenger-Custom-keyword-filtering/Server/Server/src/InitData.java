
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class InitData { //�������۽� �̹� ������ �׷�DB���� �������� (���� DB�׷�� ����Ŭ����)

	RequestDB requestDB = new RequestDB();
	
	
	ConcurrentHashMap<String,RoomInfo> requestmap = new ConcurrentHashMap<String,RoomInfo>();
	ConcurrentHashMap<String,RoomInfo> room_user_info = new ConcurrentHashMap<String,RoomInfo>(); //���濡 ���� ���� ���
	Vector<ServerConnect.UserInfo> current_user = new Vector<ServerConnect.UserInfo>();//����� ��ø� ������
	
	
	public void setGroupID() {
		requestmap.putAll(requestDB.GroupInfo());     //<�׷��̸�,RoomInfo>
	}
	public void setGroupwithUser() {
		room_user_info.putAll(requestDB.GroupInUserInfo(requestmap));
	}
	
	InitData(){
		setGroupID();
		setGroupwithUser();
	}
	
	
	
}
