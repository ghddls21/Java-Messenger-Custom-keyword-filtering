package ChatProgram.Client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.sqlite.SQLite;

public class RequestDB {

	
	private Connection conn = null;
	private PreparedStatement pstm = null;
	ResultSet rs;

	public RequestDB() {

	}

	public void connection() {
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:chat_data/chat_data.db");

		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("Opened database successfully");
	}

	public Boolean checktable(String groupid) {
		try {
			connection();
			String sql = "SELECT groupid from group_info where groupid = '"+groupid+"';";
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			while (rs.next()) {
				return false;
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			close();
		}
	}

	public Vector<String> get_user_keyword(String groupid) {
		System.out.println("키워드가져오기");
		if(groupid.equals("all_group_key"))
		{
			groupid = "all_group";
		}
		Vector<String> vector = new Vector<>();
		try {
			connection();
			String sql = "select keyword from " + groupid + "_key;";
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			while (rs.next()) {
				String keyword = rs.getString("keyword");

				vector.add(keyword);
			}

			return vector;

		} catch (Exception e) {
			e.printStackTrace();

			return vector;
		} finally {
			close();
		}
	}
	public Vector<String> group_keyword_result(String groupid,String keyword) {
		Vector vector = new Vector<>();
		try {
			connection();
			String sql = "select chatdata from "+groupid+" where chatdata LIKE '%"+keyword+"%';";
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			while (rs.next()) {
				
				vector.add(rs.getString(1));
			}

			return vector;

		} catch (Exception e) {
			e.printStackTrace();

			return vector;
		} finally {
			close();
		}
	}
	public ConcurrentHashMap<String,String> group_keyword_result_code(String groupid,String keyword) {
		ConcurrentHashMap<String, String> number_code = new ConcurrentHashMap<>();
		try {
			connection();
			String sql = "select code_number, check_read from "+groupid+" where chatdata LIKE '%"+keyword+"%';";
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			while (rs.next()) {
				
				number_code.put(rs.getString(1),rs.getString(2));
			}

			return number_code;

		} catch (Exception e) {
			e.printStackTrace();

			return number_code;
		} finally {
			close();
		}
	}
	public ConcurrentHashMap<String,String> group_keyword_result_number(String groupid,String keyword) {
		ConcurrentHashMap<String, String> number_chat = new ConcurrentHashMap<>();
		try {
			connection();
			String sql = "select code_number, chatdata from "+groupid+" where chatdata LIKE '%"+keyword+"%';";
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			while (rs.next()) {
				
				number_chat.put(rs.getString(1),rs.getString(2));
			}

			return number_chat;

		} catch (Exception e) {
			e.printStackTrace();

			return number_chat;
		} finally {
			close();
		}
	}
	
	public void changeCheck(String groupid,String code,String chat_number) {
		System.out.println("그룹명---"+groupid);
		try {
			connection();
			String sql = "update "+groupid+" set check_read="+code+" where code_number="+chat_number+";";
			

			
			pstm = conn.prepareStatement(sql);
			pstm.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	

	public void set_user_keyword(String groupid, String keyword) {
		System.out.println("키워드삽입");
		try {
			connection();
			String sql = "";
			if(groupid.equals("all_group_key")) {
				sql = "insert into " + groupid + " values('" + keyword + "');";
			}
			else
			{
				sql = "insert into " + groupid + "_key values('" + keyword + "');";
			}

			
			pstm = conn.prepareStatement(sql);
			pstm.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

	}

	public Vector get_combolist() {
		connection();
		Vector<String> vector = new Vector<>();
		try {

			String sql = "select * from group_info;";
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			while (rs.next()) {
				String group_name = rs.getString("group_name");

					vector.add(group_name);
		
				
			}

			return vector;

		} catch (Exception e) {
			e.printStackTrace();

			return vector;
		} finally {
			close();
		}

	}

	public void creategroup(String groupid) {
		System.out.println("그룹테이블생성");
		connection();
		try {
			connection();
			String sql = "create table '" + groupid + "' ('chatdata' TEXT, 'code_number' INTEGER, 'check_read' INTEGER);";
			pstm = conn.prepareStatement(sql);
			int rs = pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

	}

	public void creategroupkey(String groupid) {
		
		System.out.println("그룹키워드테이블생성");
		try {
			connection();
			String sql = "create table '" + groupid + "_key' ('keyword' TEXT);";
			pstm = conn.prepareStatement(sql);
			int rs = pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	public int get_code_number(String groupid) {
		System.out.println("코드가져오기");
		int result = 0;
		connection();
		try {

			String sql = "SELECT code_number FROM " + groupid + " ORDER BY code_number DESC LIMIT 1;";
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			while (rs.next()) {
				result = rs.getInt(1);
			}

			return result;
		} catch (Exception e) {
			e.printStackTrace();

			return result;
		} finally {
			close();
		}

	}

	public Vector<String> get_chat_data(String groupid) {
		System.out.println("채팅내용가져오기");
		Vector<String> result = new Vector<>();
		connection();
		try {

			String sql = "SELECT chatdata FROM " + groupid + " ORDER BY code_number ASC;";
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();

			while (rs.next()) {
				result.add(rs.getString(1));
			}

			return result;
		} catch (Exception e) {
			e.printStackTrace();

			return result;
		} finally {
			close();
		}
	}

	public void set_new_chat(String groupid, String chatdata, String code) {
		System.out.println("새로운대와추가");
		String check_read = "0";
		System.out.println("구릅:" + groupid + "대화:" + chatdata + "코드:" + code);
		try {
			connection();
			String sql = "insert into " + groupid + " values(?,?,?);";
			pstm = conn.prepareStatement(sql);

			pstm.setString(1, chatdata);
			pstm.setString(2, code);
			pstm.setString(3, check_read);
			pstm.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

	}
	public ConcurrentHashMap<String,String> getgroup_id_to_name() {
		ConcurrentHashMap<String,String> id_name = new ConcurrentHashMap<>();
		try {
			connection();
			String sql = "select groupid, group_name from group_info;";
	
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			while (rs.next()) {
				
				id_name.put(rs.getString(1),rs.getString(2));
			}
			return id_name;
		} catch (SQLException e) {
			e.printStackTrace();
			return id_name;
		} finally {
			close();
		}
	}

	public void addgrouplist(String groupid,String group_name) {
		System.out.println("그룹리스트 추가");
		try {
			connection();
			String sql = "insert into group_info values(?,?);";
			pstm = conn.prepareStatement(sql);

			pstm.setString(1, groupid);
			pstm.setString(2, group_name);
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	public int get_room_new_keyword_count(String groupid,String keyword) {
		int count = 0;
		try {
			connection();
			String sql = "select check_read from " +groupid+ " where check_read = 0 and chatdata LIKE '%"+keyword+"%';";
	
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			while (rs.next()) {
				count = count + 1;
			}
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
			return count;
		} finally {
			close();
		}
	}
	public Vector<String> getmark(String groupid){
		Vector<String> chat_v = new Vector<>();
		try {
			connection();
			String sql = "select chatdata from " +groupid+ " where check_read = 2;";
	
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			while (rs.next()) {
				chat_v.add(rs.getString(1));
			}
			return chat_v;
		} catch (SQLException e) {
			e.printStackTrace();
			return chat_v;
		} finally {
			close();
		}
	}
	public ConcurrentHashMap<String,String> getmark_number_code(String groupid) {
		ConcurrentHashMap<String, String> number_code = new ConcurrentHashMap<>();
		try {
			connection();
			String sql = "select code_number, check_read from "+groupid+" where check_read = 2;";
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			while (rs.next()) {
				
				number_code.put(rs.getString(1),rs.getString(2));
			}

			return number_code;

		} catch (Exception e) {
			e.printStackTrace();

			return number_code;
		} finally {
			close();
		}
	}
	public ConcurrentHashMap<String,String> getmark_number_chat(String groupid) {
		ConcurrentHashMap<String, String> number_chat = new ConcurrentHashMap<>();
		try {
			connection();
			String sql = "select code_number, chatdata from "+groupid+" where check_read = 2;";
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			while (rs.next()) {
				
				number_chat.put(rs.getString(1),rs.getString(2));
			}

			return number_chat;

		} catch (Exception e) {
			e.printStackTrace();

			return number_chat;
		} finally {
			close();
		}
	}
	public void delte_keyword(String groupid,String keyword) {
		if(groupid.equals("all_group_key")) {
			groupid = "all_group";
		}
		try {
			connection();
			String sql = "DELETE FROM "+groupid+"_key where keyword = '"+keyword+"';";
			pstm = conn.prepareStatement(sql);
			pstm.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
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
