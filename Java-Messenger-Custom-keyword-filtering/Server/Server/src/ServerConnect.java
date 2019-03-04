import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JOptionPane;

public class ServerConnect {

	private ServerSocket serverSocket;
	private Socket socket;
	private String msg;
	

	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	ArrayQueue arrayQueue = new ArrayQueue(10000);

	
	private ConcurrentHashMap<String, UserInfo> map_user = new ConcurrentHashMap(); // 접속하고있는 유저.
	RequestDB requestDB = new RequestDB();
	InitData initdata = new InitData();
	private ConcurrentHashMap<String, RoomInfo> map_room = (ConcurrentHashMap<String, RoomInfo>) initdata.room_user_info; // 서버 실시간 그룹정보
					
	// <방이름,방정보>


	public ServerConnect() {

	}

	private void server_start() {
		try {
			serverSocket = new ServerSocket(9000);
		} catch (IOException e) {
			// OptionPane.showMessageDialog(null, "이미 사용중인 포트", "알림",
			// JOptionPane.ERROR_MESSAGE);
		}
		if (serverSocket != null) { // socket ok!
			connection();
		}
	}

	private void connection() {
		Thread thread = new Thread(new Socket_thread());
		thread.start();

	}

	public class Socket_thread implements Runnable {
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				try { // wait client
						// textArea.append("사용자 접속 대기중\n");
					System.out.println("사용자 접속 대기중\n");
					socket = serverSocket.accept();
					UserInfo userInfo = new UserInfo(socket);
					userInfo.start();
				} catch (IOException e) {
					// textArea.append("서버가 중지 되었습니다\n");
					System.out.println("서버가 중지 되었습니다\\n");
					break;
				}
			}
		}
	}

	public static void main(String[] args) {
		ServerConnect serverConnect = new ServerConnect();
		serverConnect.server_start();

	}

	public class UserInfo extends Thread {
		private InputStream inputStream;
		private OutputStream outputStream;
		private DataOutputStream dataOutputStream;
		private DataInputStream dataInputStream;
		private Socket socket_user;
		private String UserId = "";
		Vector user_frined_list = new Vector<>();
		// 사용자의 참여중인 그룹정보
		private String CurrentRoom = null;
		private boolean RoomCheck = true; // 기본적으로 만들 수 있는 상태

		private Vector vector_room_user = new Vector();// 방의 유저정보담을 변수
		
		
		FileOutputStream out = null;
		
		

		private StringTokenizer stringTokenizer;
		private String frist_data;
		private Vector<String> vector_User_Room = new Vector();
		// DB에서 서버에 생성되어있는 그룹정보가져오기

		public UserInfo(Socket socket) {
			this.socket_user = socket;
			userNetwork();
		}

		public String getUserId() {
			return UserId;
		}

		public void userNetwork() {
			try {
				inputStream = socket_user.getInputStream();
				dataInputStream = new DataInputStream(inputStream);
				outputStream = socket_user.getOutputStream();
				dataOutputStream = new DataOutputStream(outputStream);
				frist_data = dataInputStream.readUTF();

				
				
				
				
				stringTokenizer = new StringTokenizer(frist_data, "/");
				String protocol = stringTokenizer.nextToken();
				String id = stringTokenizer.nextToken();
				String pwd = stringTokenizer.nextToken();

				if (protocol.equals("login")) {

					System.out.println("접속자 ID와 PWD:" + id + "and" + pwd);
					if (requestDB.UserLoginCheck(id, pwd) == true) {
						// textArea.append(Nickname + " : 사용자 접속\n");
						System.out.println(id + ": 사용자 접속\n");

						UserId = id;
						// BroadCast("NewUser/" + UserId);
						user_frined_list = requestDB.broadcast_friend(UserId);
						BroadCast_friend("online_friend/" + UserId); // 사용자를 친구추가한 유저에게 온라인상테 전달.
						map_user.put(UserId, this); // 소켓 현재 접속자로 저장

						for (int i = 0; i < user_frined_list.size(); i++) {
							this.send_Message("friend_list/" + user_frined_list.elementAt(i));// 사용자 개인 친구목록 전달
						}
						for (int i = 0; i < user_frined_list.size(); i++) {
							if (map_user.containsKey(user_frined_list.elementAt(i))) {
								UserInfo userInfo = map_user.get(user_frined_list.elementAt(i));
								this.send_Message("online_friend/" + userInfo.UserId); // 친구한테 온라인상태 보내기
							}
						}
						SetOldRoom(); // 사용자가 참여중인 그룹 전달.

					} else {
						this.socket_user.close();
					}
				} else if (protocol.equals("join")) {
					if (requestDB.UserJoinCheck(id)) { // 가입가능
						requestDB.UserAdd(id, pwd);
						send_Message("join_sucess/ ");
						System.out.println("회원가입성공 :" + id);
						this.socket_user.close();
					} else {
						send_Message("join_fail/ ");
						System.out.println("회원가입실패:" + id);
						this.socket_user.close();
					}
				} else if(protocol.equals("file_send")) {
					
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Stream설정 에러", "알림", JOptionPane.ERROR_MESSAGE);
			}
		}

		private void BroadCast_friend(String str) {
			for (int i = 0; i < user_frined_list.size(); i++) {
				if (map_user.containsKey(user_frined_list.elementAt(i))) {
					UserInfo userInfo = map_user.get(user_frined_list.elementAt(i));
					userInfo.send_Message(str);
				}
			}

		}

		/*
		 * private void BroadCast(String str){ System.out.println("브로드캐스팅중"); for(int i
		 * = 0; i< vector_user.size(); i++){ // server alert at existing user and send
		 * the new user's id UserInfo userInfo = (UserInfo) vector_user.elementAt(i);
		 * System.out.println(userInfo.getNicname() +"현재사용자" );
		 * userInfo.send_Message(str); } System.out.println("브로드캐스팅끝"); }
		 */
		private void SetOldRoom() {
			vector_User_Room = requestDB.PuserInfo(UserId); // 사용자가 참여중인 그룹정보 전달.
			for (int i = 0; i < vector_User_Room.size(); i++) {
				System.out.println("사용자에게 전달할 그룹ID" + vector_User_Room.elementAt(i));
				String groupid = (String) vector_User_Room.elementAt(i);
				RoomInfo roomInfo = (RoomInfo) map_room.get(groupid);
				this.send_Message("OldRoom/" + groupid +"/"+roomInfo.getGroup_name() + setroom_userlist(groupid, roomInfo));
				Vector offline_user = roomInfo.vector_offline_user;
				
				this.send_Message("group_each_online/" + groupid + send_group_user_online(groupid, roomInfo)); // 그룹유저
																													// 온라인사용자
																													// 정보전달.
				roomInfo.BroadCast_Room("group_online/" + groupid + "/" + UserId);
				roomInfo.online_User(this); // 사용자 온라인 소켓저장 //소켓을 각 룸정보에 저장.(나중에 그룹사용자들에게 모아 보내기위해 따로 저장)
				map_room.put(groupid, roomInfo);
			}
			this.send_Message("end_update/ ");
			
			

		}


		private void user_offline() { // 사용자 오프라인
			vector_User_Room = requestDB.PuserInfo(UserId); // 사용자가 참여중인 그룹정보 전달.
			for (int i = 0; i < vector_User_Room.size(); i++) {
				RoomInfo roomInfo = (RoomInfo) map_room.get(vector_User_Room.elementAt(i));
				roomInfo.BroadCast_Room("userout/" + roomInfo.getgroupid() + "/" + UserId);
				roomInfo.Remove_User(this);
			}
			for (int i = 0; i < user_frined_list.size(); i++) {
				if (map_user.containsKey(user_frined_list.elementAt(i))) {
					UserInfo userInfo = map_user.get(user_frined_list.elementAt(i));
					this.send_Message("offline_friend/" + userInfo.UserId); // 친구한테 오프라인상태 보내기
				}
			}

		}

		private String setroom_userlist(String roomname, RoomInfo roomInfo) { // 각 그굽 사용자 명단 전달.
			Vector<String> vector = roomInfo.vector_offline_user;
			String send_data = "";
			for (int j = 0; j < vector.size(); j++) {
				if(!vector.elementAt(j).equals(UserId)){
					send_data = send_data + "/" + vector.elementAt(j);
				}
				
			}
			send_data = send_data +"/"+UserId;
			System.out.println(send_data);
			return send_data;
		}

		private String send_group_user_online(String groupid, RoomInfo roomInfo) {

			Set set = roomInfo.map_online_user.entrySet();
			Iterator iterator = set.iterator();
			String send_data = "/"+UserId;
			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator.next();
				String id = (String) entry.getKey();
				send_data = send_data + "/" + id;
			}
			return send_data;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while (true) {
				try {
					String msg = dataInputStream.readUTF();
					// textArea.append(Nickname + " : " + msg + "\n");
					System.out.println(UserId + " : " + msg + "\n");
					InMessage(msg);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					// textArea.append(Nickname + " : 사용자 접속 끊어짐\n");
					System.out.println(UserId + " : 사용자 접속 끊어짐\n");
					BroadCast_friend("userout/ ");

					// ExitRoom(this);
					try {
						dataInputStream.close();
						dataOutputStream.close();
						socket_user.close();
						map_user.remove(this.UserId);
						RoomInfo roomInfo = new RoomInfo();
						for (int i = 0; i < vector_User_Room.size(); i++) {
							roomInfo = map_room.get(vector_User_Room.elementAt(i));
							roomInfo.map_online_user.remove(UserId);
							map_room.put(vector_User_Room.elementAt(i), roomInfo);
							roomInfo.BroadCast_Room("group_user_offline/"+roomInfo.groupid+"/"+UserId);
						}

						BroadCast_friend("friend_user_offline/" + UserId); // 유저아웃 브로드케스트
						BroadCast_friend("user_list_update/ ");
						
					} catch (IOException e1) {
					}
					break;
				}
			}
		}

		private void InMessage(String str) { // handle the message from client
			StringTokenizer stringTokenizer;
			stringTokenizer = new StringTokenizer(str, "/");
			Vector<String> data = new Vector<>();

			while (stringTokenizer.hasMoreTokens()) {

				data.add(stringTokenizer.nextToken());
			}
			String protocol = data.elementAt(0);
			System.out.println("protocol : " + protocol);

			if (protocol.equals("CreateRoom")) {
				String group_name = data.elementAt(1);
			
					String groupid = requestDB.AddGroup(UserId,group_name);
					requestDB.createChatTable(groupid);

					requestDB.group_add_user(groupid, UserId);
					RoomInfo roomInfo = new RoomInfo(UserId, this, groupid,group_name);
					map_room.put(groupid, roomInfo);
					send_Message("NewRoom/" + groupid + "/" +group_name);
					System.out.println("생성 그룹ID:"+groupid +"," + "그룹명:"+group_name);

				
			} else if (protocol.equals("Chatting")) {
				String groupid = data.elementAt(1);
				String msg = data.elementAt(2);
				RoomInfo roomInfo = (RoomInfo) map_room.get(groupid);
				Date date = new Date();
				String nowTime = dateFormat.format(date);

				ChatStruct chatStruct = new ChatStruct(groupid, nowTime, UserId, msg);
				arrayQueue.insert(chatStruct);
				ChatStruct outqueue = (ChatStruct) arrayQueue.remove();

				String result = requestDB.savechatdata(outqueue.getGruoupID(), outqueue.getTime(), outqueue.getUserid(),
						outqueue.getChatdata());
				roomInfo.BroadCast_Room("Chatting/" + result);

			} else if (protocol.equals("JoinRoom")) {
				String groupid = data.elementAt(1);
				RoomInfo roomInfo = (RoomInfo) map_room.get(groupid);

				roomInfo.BroadCast_Room("group_online/" + groupid + "/" + UserId); // 방입장 온라인표시
				CurrentRoom = groupid;

				send_Message("JoinRoom/" + groupid);
				this.send_Message("room_list_update/ ");
			} else if (protocol.equals("add_frined")) {
				String id = data.elementAt(1);
				send_Message("result_add_friend/" + requestDB.addFriend(id, UserId));
			} else if (protocol.equals("invite_group")) {
				String groupid = data.elementAt(1);
				String userid = data.elementAt(2);
				requestDB.group_add_user(groupid, userid);
				vector_User_Room.add(groupid);
				if (map_user.containsKey(userid)) {

					RoomInfo roomInfo = map_room.get(groupid);
					UserInfo user = map_user.get(userid);
					roomInfo.add_user(userid);
					roomInfo.online_User(user);
					user.send_Message("NewRoom/" + groupid +"/"+ roomInfo.group_name);
					user.send_Message("group_online/" + groupid + "/" + UserId);
					send_Message("group_online/" + groupid + "/" + user.getUserId());

				}else {
					send_Message("set_offline/" + groupid + "/" + userid);
				}
			} else if (protocol.equals("get_room_chat_data")) {
				String groupid = data.elementAt(1);
				int code = Integer.parseInt(data.elementAt(2));
				Vector<ChatStruct> result = new Vector<>();
				result = requestDB.send_chat_data_list_for_new_and_change_online(groupid, code);
				for (int i = 0; i < result.size(); i++) {
					send_Message("new_chat/" + result.elementAt(i)); // result = code/groupid/time/user/chatdata
				}
				this.send_Message("end_update/ ");
			}
		}
		public void send_Message(String message) {
			try {
				dataOutputStream.writeUTF(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("메세지 전송 실패:" + message);
			}
		}
	}

}
