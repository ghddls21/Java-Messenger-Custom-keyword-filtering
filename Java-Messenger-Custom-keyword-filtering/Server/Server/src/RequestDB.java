
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.CommunicationException;
import javax.xml.bind.util.ValidationEventCollector;

public class RequestDB {

	Connection conn = null;
	PreparedStatement pstm = null;
	ResultSet rs = null;

	public void connect() {
		conn = DBConnection.getConnection();
	}

	public Boolean UserLoginCheck(String ID, String PWD) {

		String query = "select PWD from USERINFO where USERID = ?";
		Boolean result = null;
		try {
			connect();
			pstm = conn.prepareStatement(query);
			pstm.setString(1, ID);
			rs = pstm.executeQuery();
			if (rs.next()) {
				if (rs.getString("PWD").equals(PWD)) {
					System.out.println("접속성공");
					result = true;
				}
			} else
				result = false;

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			close();
		}

		return result;

	}

	public Boolean UserJoinCheck(String ID) {
		String query = "select USERID from USERINFO where USERID = ?";
		Boolean result = null;
		try {
			connect();
			pstm = conn.prepareStatement(query);
			pstm.setString(1, ID);
			rs = pstm.executeQuery();
			if (rs.next()) {
				result = false;
			} else
				result = true;

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			close();
		}

		return result;
	}

	public void UserAdd(String ID, String PWD) {

		try {
			connect();
			String query = "Insert Into USERINFO values(?,?)";
			pstm = conn.prepareStatement(query);
			pstm.setString(1, ID);
			pstm.setString(2, PWD);

			rs = pstm.executeQuery();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Vector getUserFriendList(String ID) {
		Vector friend_vector = new Vector<>();
		try {
			connect();
			String query = "Select USERID2 From FRIENDINFO Where USERID1 = ?";
			pstm = conn.prepareStatement(query);
			pstm.setString(1, ID);
			rs = pstm.executeQuery();

			while (rs.next()) {

				friend_vector.add(rs.getString("USERID2"));
			}
			return friend_vector;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return friend_vector;

	}

	public Vector broadcast_friend(String ID) {
		Vector friend_vector = new Vector<>();
		try {
			connect();
			String query = "Select USERID1 From FRIENDINFO Where USERID2 = ?";
			pstm = conn.prepareStatement(query);
			pstm.setString(1, ID);
			rs = pstm.executeQuery();

			while (rs.next()) {

				friend_vector.add(rs.getString("USERID1"));
			}
			return friend_vector;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return friend_vector;

	}

	public String addFriend(String frinedId, String id) {
		try {
			connect();
			String query = "Select USERID2 From FRIENDINFO Where GROUPID1 = ? and GROUPID2 = ?";
			pstm = conn.prepareStatement(query);
			pstm.setString(1, id);
			pstm.setString(2, frinedId);
			rs = pstm.executeQuery();
			if (!rs.isBeforeFirst()) {
				query = "Insert Into USERINFO values(?,?)";
				pstm.setString(1, id);
				pstm.setString(2, frinedId);
				return "fadd_ok";
			} else
				return "fadd_already";

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return "fadd_fail";
	}

	public void UserUnuse(String ID) {

		try {
			connect();
			String query = "Update USERINFO Set postion = 0 Where = ?";
			rs = pstm.executeQuery(query);

		} catch (SQLException e) {
			e.printStackTrace();

		}
	}

	public void UserChangePWd(String ID, String PWD) {
		connect();
		try {
			String query = "Update USERINFO Set PWD = " + "'" + PWD + "'" + "Where = " + ID + ";";
			pstm = conn.prepareStatement(query);
			rs = pstm.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void group_add_user(String groupid, String userid) {
		connect();
		try {
			String query2 = "Insert Into GROUPINUSER values(?,?)";
			pstm = conn.prepareStatement(query2);
			pstm.setString(1, groupid);
			pstm.setString(2, userid);
			rs = pstm.executeQuery();

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			close();
		}
	}

	public String AddGroup(String AdminID,String group_name) {

		String GroupID = "";
		connect();
		try {
			String query = "SELECT * FROM (SELECT GROUPID FROM GROUPINFO ORDER BY GROUPID DESC) WHERE ROWNUM = 1";
			pstm = conn.prepareStatement(query);
			rs = pstm.executeQuery();
			while(rs.next()) {
				 GroupID = rs.getString("GROUPID");
			}
			if(GroupID.equals("")) {
				GroupID = "ROOM1";
			}
			else
			{
				StringTokenizer stringTokenizer;
				stringTokenizer = new StringTokenizer(GroupID, "M");
				stringTokenizer.nextToken();
				String number_S = stringTokenizer.nextToken();
				int number_I = Integer.parseInt(number_S);
				number_I = number_I + 1;
				GroupID = "ROOM"+number_I;
			}
			
			String query2 = "Insert Into GROUPINFO values(?,?,?)";
			pstm = conn.prepareStatement(query2);
			pstm.setString(1, GroupID);
			pstm.setString(2, AdminID);
			pstm.setString(3, group_name);
			rs = pstm.executeQuery();
			close();
			return GroupID;

		} catch (SQLException e) {
			GroupID = "";
			e.printStackTrace();
			close();
			return GroupID;
		}
	}

	public ConcurrentHashMap<String, RoomInfo> GroupInfo() {

		ConcurrentHashMap<String, RoomInfo> map = new ConcurrentHashMap<String, RoomInfo>();
		RoomInfo roomInfo;
		try {
			connect();
			String query = "SELECT * FROM GROUPINFO";
			pstm = conn.prepareStatement(query);
			rs = pstm.executeQuery();
			while (rs.next()) {
				roomInfo = new RoomInfo(rs.getString("USERID"));
				roomInfo.setGroup_name(rs.getString("GROUP_NAME"));
				roomInfo.setgroupid(rs.getString("GROUPID"));
				map.put(rs.getString("GROUPID"), roomInfo);
			}
			return map;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return map;

	}

	public ConcurrentHashMap<String, RoomInfo> GroupInUserInfo(ConcurrentHashMap<String, RoomInfo> map) {

		ConcurrentHashMap changemap = new ConcurrentHashMap<String, RoomInfo>();
		changemap.putAll(map);
		Set set = changemap.keySet();
		Iterator iterator = set.iterator();

		ConcurrentHashMap resultmap = new ConcurrentHashMap<String, RoomInfo>();

		String ID;

		String query;

		try {
			connect();
			while (iterator.hasNext()) {
				ID = (String) iterator.next();
				RoomInfo roominfoMidify = (RoomInfo) changemap.get(ID);
				System.out.println(ID);
				query = "SELECT USERID FROM GROUPINUSER WHERE GROUPID = ?";
				pstm = conn.prepareStatement(query);
				pstm.setString(1, ID);
				rs = pstm.executeQuery();
				while (rs.next()) {
					System.out.println(rs.getString("USERID"));
					roominfoMidify.add_user((String) rs.getString("USERID"));
				}
				resultmap.put(ID, roominfoMidify);

			}
			return resultmap;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return resultmap;
	}

	public Vector<String> PuserInfo(String ID) {

		String query = "SELECT GROUPID FROM GROUPINUSER WHERE USERID = ?";
		Vector vector_GroupID = new Vector();
		try {
			connect();
			pstm = conn.prepareStatement(query);
			pstm.setString(1, ID);
			rs = pstm.executeQuery();
			while (rs.next()) {
				vector_GroupID.add(rs.getString("GROUPID"));
			}
			return vector_GroupID;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return vector_GroupID;
	}

	public void createChatTable(String groupid) {
		try {
			connect();
			String query = "create table " + groupid
					+ "(TIME date, USERID varchar(20), CHATDATA varchar(1000), CODE_NUMBER number(38))";
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	public int getcode(String groupid) {
		int code = 0;
		String sql_get_code = "select code_number from (select * From " + groupid
				+ " ORDER BY ROWNUM DESC) where ROWNUM = 1";
		try {
			connect();
			pstm = conn.prepareStatement(sql_get_code);
			rs = pstm.executeQuery();

			while (rs.next()) {
				code = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close();
		return code;
	}

	public String savechatdata(String groupID, String time, String userid, String chatdata) {

		int code = getcode(groupID) + 1;
		String result = "";
		System.out.println("저장하는 코드:" + code);
		System.out.println(groupID + "/" + time + "/" + userid + "/" + chatdata);
		String query = "Insert Into " + groupID + " values(to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?)";
		try {
			connect();
			pstm = conn.prepareStatement(query);
			pstm.setString(1, time);
			pstm.setString(2, userid);
			pstm.setString(3, chatdata);
			pstm.setInt(4, code);
			rs = pstm.executeQuery();
			result = code + "/" + groupID + "/" + time + "/" + userid + "/" + chatdata;
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return result;
		} finally {
			close();
		}

	}

	public ChatStruct getchatdata(String groupid, int code) {
		connect();
		ChatStruct chatStruct = new ChatStruct();

		try {
			String query = "SELECT CHATATA FROM " + groupid + " where code = " + code;
			pstm = conn.prepareStatement(query);
			rs = pstm.executeQuery();
			while (rs.next()) {
				chatStruct = new ChatStruct(groupid, rs.getInt("CODE_NUMBER number"), rs.getString("TIME"),
						rs.getString("USERID"), rs.getString("CHATDATA"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		close();
		return chatStruct;
	}

	public Vector search(String data, Vector room_list) {
		Vector result = new Vector<>();
		connect();

		for (int i = 0; i < room_list.size(); i++) {
			String roomname = (String) room_list.elementAt(i);
			System.out.println(roomname);
			String query = "SELECT TO_CHAR(TIME, 'yyyy-mm-dd hh24:mi:ss'), USERID, CHATDATA FROM " + roomname
					+ " WHERE CHATDATA LIKE '%" + data + "%'";
			try {
				pstm = conn.prepareStatement(query);

				rs = pstm.executeQuery();

				while (rs.next()) {
					ChatStruct chatStruct = new ChatStruct(roomname, rs.getString(1), rs.getString(2), rs.getString(3));
					result.add(chatStruct);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		close();
		return result;

	}

	public Vector send_chat_data_list_for_new_and_change_online(String groupid, int code) {
		Vector result = new Vector<>();
		connect();
		System.out.println("코드:" + code);
		try {
			String query = "";
			if (code == 0) {
				query = "SELECT CODE_NUMBER, TO_CHAR(TIME, 'yyyy-mm-dd hh24:mi:ss'), USERID, CHATDATA FROM " + groupid;
				pstm = conn.prepareStatement(query);

			} else {
				query = "SELECT CODE_NUMBER, TO_CHAR(TIME, 'yyyy-mm-dd hh24:mi:ss'), USERID, CHATDATA FROM " + groupid
						+ " where CODE_NUMBER > " + code;
				pstm = conn.prepareStatement(query);

			}
			rs = pstm.executeQuery();
			while (rs.next()) {

				result.add(rs.getInt(1) + "/" + groupid + "/" + rs.getString(2) + "/" + rs.getString(3) + "/"
						+ rs.getString(4));

			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return result;
		}

	}

	public String send_chat_data_for_online_user(String groupid, int code) {
		String result = "";

		try {
			connect();
			String query = "SELECT CODE_NUMBER, TO_CHAR(TIME, 'yyyy-mm-dd hh24:mi:ss'), USERID, CHATDATA FROM ? where CODE_NUMBER = ?";

			pstm = conn.prepareStatement(query);
			pstm.setString(1, groupid);
			pstm.setInt(2, code);
			rs = pstm.executeQuery();
			while (rs.next()) {
				result = rs.getInt(1) + "/" + groupid + "/" + rs.getString(2) + "/" + rs.getString(3) + "/"
						+ rs.getString(4);
			}
			System.out.println("결과물:" + result);
		} catch (SQLException e) {
			// TODO: hand1le exception
		}
		close();
		return result;
	}

	
		



	public void close() {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {

			}
		}
		if (pstm != null) {
			try {
				pstm.close();
			} catch (SQLException e) {

			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {

			}
		}
	}
}
