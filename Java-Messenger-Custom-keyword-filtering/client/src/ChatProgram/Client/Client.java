package ChatProgram.Client;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.ImagingOpException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.security.auth.Refreshable;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;


import javax.swing.JTree;
import javax.swing.JComboBox;

public class Client extends JFrame implements ActionListener {

	// Login Frame
	private JFrame Login_GUI = new JFrame();
	private JPanel Login_panel;
	private JTextField textField_id;
	private JPasswordField textField_pwd;
	private JButton button_login = new JButton("접 속");
	private JButton button_join = new JButton("회원가입");

	// Join Frame
	private JFrame Join_GUI = new JFrame();
	private JPanel Join_panel;
	private JTextField Join_textField_id;
	private JPasswordField Join_textField_pwd;
	private JTextField Join_textField_pwd_check;
	private JButton Join_dobule_check;
	private JButton submit = new JButton("가입");
	// Main Frame
	
	
	private JPanel contentpane; // 메인 페널

	Dimension dim = new Dimension(922,505);
	private JTextField textField_message;
	private JButton button_send_note = new JButton("쪽지보내기");
	private JButton button_create_room = new JButton("그룹생성");
	private JButton button_send_message = new JButton("전송");
	private JButton button_send_invite = new JButton("초대");
	private JButton fadd_button = new JButton("친구추가");
	private JButton change_to_board = new JButton(
			new ImageIcon("icon/searchbtn.png"));
	private CardLayout card;
	JButton button_search = new JButton("검색");
	JButton goto_keyword_set = new JButton("키워드설정");
	JTree tree;
	private JList<RoomInfo> group_list = new JList<RoomInfo>();
	private JList<UserInfo> friend_list_user = new JList<UserInfo>();
	private JList<UserInfo> room_list_user = new JList<UserInfo>();
	private JTextArea textArea_chat = new JTextArea();
	private CardLayout fg_card;
	private JTextField search_txf;
	private JButton btnchangelist;
	JComboBox comboBox;
	JList keyword_quick_list = new JList();
    int test;
	private JPanel main_p;
	private JPanel board_p;
	private JPanel fg_list_panel;
	private String cb_groupid;
	JButton file_send_btn;
	FileChoose fileChoose = new FileChoose();
	// data
	RequestDB requestDB = new RequestDB();
	DefaultMutableTreeNode tree_root_search = new DefaultMutableTreeNode("그룹목록");
	private Vector<String> room_list = new Vector<>(); // 그룹리스트
	DefaultListModel<RoomInfo> groupId_model = new DefaultListModel<>();
	DefaultListModel<UserInfo> friend_model = new DefaultListModel<>(); // 친구리스트모델
	DefaultListModel<UserInfo> group_user_model = new DefaultListModel<>(); // 그룹리스트모델
	DefaultComboBoxModel<Combo_Keyword_Info> combo_list_model = new DefaultComboBoxModel<>();
	private ConcurrentHashMap<String, RoomInfo> group_user_status = new ConcurrentHashMap<>(); // 각방의 사용자상태정보
	private ConcurrentHashMap<String, Boolean> friend_user_status = new ConcurrentHashMap<>();// 사용자의 친구상태정보

	private String current_room = ""; // 현재 나의 방
	Vector<DefaultMutableTreeNode> chat_new = new Vector<>();
	Vector<DefaultMutableTreeNode> chat_old = new Vector<>();
	Vector<DefaultMutableTreeNode> chat_mark = new Vector<>();
	private ConcurrentHashMap<String, String[]> group_chat_data;
	private ConcurrentHashMap<String, Vector<String>> group_result = new ConcurrentHashMap<>();
	DefaultTreeModel tree_model;
	private JPanel friend_list_panel;
	private JPanel group_list_panel;
	private Boolean list_change = false;
	String select_keyword_group;


	private String selected_tree_chat;
	ConcurrentHashMap<String, String> number_code = new ConcurrentHashMap<>();
	ConcurrentHashMap<String, String> number_chat = new ConcurrentHashMap<>();
	ConcurrentHashMap<String, String> group_id_to_name = new ConcurrentHashMap<>();
	ConcurrentHashMap<String, ConcurrentHashMap<String, Vector<String>>> all_group_result_root = new ConcurrentHashMap<>();
	ConcurrentHashMap<String, Vector<String>> group_keyword_root = new ConcurrentHashMap<>();
	private DefaultListModel<String> keyword_q_list_model = new DefaultListModel<>();

	// Network Source 있어야하는애들
	private Socket socket = null;
	private String ip = "127.0.0.1";
	private int port = 9000;
	private String id = "noname";
	private String pwd;
	// 있어야하는애들
	private InputStream inputStream;
	private OutputStream outputStream;
	private DataOutputStream dataOutputStream;
	private DataInputStream dataInputStream;
	private JButton btntochat;

	// etc valuable 있어야하는애들

	public Client() {
		Login_init(); // Login GUI
		Main_init();// Main GUI
		Join_init();

		start(); // ACTION
	}

	private void start() { // ACTION
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			// TODO: handle exception
		}
		button_login.addActionListener(this);
		button_send_note.setBounds(0, 388, 100, 23);
		button_send_note.setBackground(new Color(135, 206, 250));
		button_send_note.setUI(new StyledButtonUI());
		friend_list_panel.add(button_send_note);
		fadd_button.setBounds(0, 411, 100, 23);
		fadd_button.setBackground(new Color(135, 206, 250));
		fadd_button.setUI(new StyledButtonUI());
		friend_list_panel.add(fadd_button);

		button_send_note.addActionListener(this);
		button_create_room.setBounds(0, 400, 100, 23);
		button_create_room.setBackground(new Color(135, 206, 250));
		button_create_room.setUI(new StyledButtonUI());
		group_list_panel.add(button_create_room);
		button_create_room.addActionListener(this);
		button_send_message.addActionListener(this);
		button_send_invite.addActionListener(this);
		textField_message.addActionListener(this);
		change_to_board.addActionListener(this);
		button_search.addActionListener(this);
		btnchangelist.addActionListener(this);
		btntochat.addActionListener(this);
		comboBox.addActionListener(this);
		goto_keyword_set.addActionListener(this);
		button_join.addActionListener(this);
		submit.addActionListener(this);
		fadd_button.addActionListener(this);
		file_send_btn.addActionListener(this);

	}

	private void Main_init() { // Main GUI 메인 제이프레임
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(100, 100);
		this.setPreferredSize(dim);
		this.pack();
		contentpane = new JPanel();
		contentpane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentpane.setLayout(null);
		this.setContentPane(contentpane);
		main_p = new JPanel();
		main_p.setBackground(new Color(112, 128, 144));
		main_p.setLayout(null);
		board_p = new JPanel();
		board_p.setBackground(new Color(112, 128, 144));
		board_p.setLayout(null);
		card = new CardLayout(0, 0);
		contentpane.setLayout(card);

		contentpane.add("main_p", main_p);
		contentpane.add("board_p", board_p);

		search_txf = new JTextField();
		search_txf.setBounds(12, 20, 116, 21);
		board_p.add(search_txf);
		search_txf.setColumns(10);

		button_search.setBounds(140, 19, 72, 23);
		button_search.setBackground(new Color(135, 206, 250));
		button_search.setUI(new StyledButtonUI());
		board_p.add(button_search);

		goto_keyword_set.setBounds(224, 19, 106, 23);
		goto_keyword_set.setBackground(new Color(135, 206, 250));
		goto_keyword_set.setUI(new StyledButtonUI());
		board_p.add(goto_keyword_set);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(12, 58, 872, 372);
		board_p.add(scrollPane_1);

		tree = new JTree();
		tree.setBorder(new LineBorder(Color.black));
		tree.setModel(tree_model);
		tree.setCellRenderer(new KeywordTreeCellRenderer());

		scrollPane_1.setViewportView(tree);

		btntochat = new JButton("채팅창");
		btntochat.setBounds(451, 19, 97, 23);
		btntochat.setBackground(new Color(135, 206, 250));
		btntochat.setUI(new StyledButtonUI());

		board_p.add(btntochat);

		comboBox = new JComboBox();
		comboBox.setModel(combo_list_model);

		comboBox.setBounds(342, 20, 97, 21);
		board_p.add(comboBox);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(124, 38, 522, 365);
		main_p.add(scrollPane);

		scrollPane.setViewportView(textArea_chat);
		textArea_chat.setBorder(new LineBorder(Color.black));
		textArea_chat.setEditable(false);
		textField_message = new JTextField();
		textField_message.setBounds(124, 413, 300, 21);
		main_p.add(textField_message);
		textField_message.setColumns(10);
		textField_message.setEnabled(false);

		textField_message.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.getKeyCode() == KeyEvent.VK_ENTER) { // enter
					send_message("Chatting/" + current_room + "/" + textField_message.getText().trim());
					textField_message.setText("");
					textField_message.requestFocus();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});

		button_send_message.setBounds(446, 412, 95, 23);
		button_send_message.setBackground(new Color(135, 206, 250));
		button_send_message.setUI(new StyledButtonUI());
		button_send_message.setEnabled(false);
		main_p.add(button_send_message);

		button_send_invite.setBounds(551, 412, 95, 23);
		button_send_invite.setBackground(new Color(135, 206, 250));
		button_send_invite.setUI(new StyledButtonUI());
		button_send_invite.setEnabled(false);

		main_p.add(button_send_invite);

		JLabel label = new JLabel("대화상대목록");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(784, 10, 100, 15);
		main_p.add(label);

		room_list_user.setBounds(784, 33, 100, 369);
		room_list_user.setBorder(new LineBorder(Color.black));
		main_p.add(room_list_user);

		change_to_board.setBounds(616, 3, 30, 33);
		main_p.add(change_to_board);
		change_to_board.setEnabled(false);

		fg_list_panel = new JPanel();
		fg_list_panel.setBounds(12, 10, 100, 434);

		main_p.add(fg_list_panel);

		friend_list_panel = new JPanel();
		friend_list_panel.setBackground(new Color(112, 128, 144));
		friend_list_panel.setLayout(null);
		group_list_panel = new JPanel();
		group_list_panel.setBackground(new Color(112, 128, 144));
		group_list_panel.setLayout(null);

		fg_card = new CardLayout(0, 0);

		fg_list_panel.setLayout(fg_card);
		fg_list_panel.add("friend_list_panel", friend_list_panel);
		fg_list_panel.add("group_list_panel", group_list_panel);

		friend_list_user.setBounds(0, 25, 100, 364);
		friend_list_user.setBorder(new LineBorder(Color.black));
		friend_model.addElement(new UserInfo(id, true));

		friend_list_panel.add(friend_list_user);
		friend_list_user.setBackground(new Color(255, 255, 255));
		friend_list_user.setCellRenderer(new UserInfoRenderer());

		JLabel label_friend = new JLabel("친구목록");
		label_friend.setBackground(new Color(230, 230, 250));
		label_friend.setHorizontalAlignment(SwingConstants.CENTER);
		label_friend.setBounds(0, 0, 100, 15);

		friend_list_panel.add(label_friend);
		JLabel lblNewLabel_1 = new JLabel("채 팅 방 목 록");
		lblNewLabel_1.setBounds(0, 0, 100, 15);
		group_list_panel.add(lblNewLabel_1);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		group_list.setBackground(new Color(255, 255, 255));
		group_list.setBounds(0, 25, 100, 365);
		group_list.setBorder(new LineBorder(Color.black));
		group_list_panel.add(group_list);
		group_list.setModel(groupId_model);
		group_list.setCellRenderer(new RoomInfoRenderer());

		btnchangelist = new JButton(
				new ImageIcon("icon\\changelistbtn.png"));
		btnchangelist.setBounds(124, 4, 30, 32);
		btnchangelist.setEnabled(false);
		main_p.add(btnchangelist);

		JLabel lblNewLabel = new JLabel("키워드");
		lblNewLabel.setBounds(690, 10, 46, 15);
		main_p.add(lblNewLabel);

		keyword_quick_list.setBounds(658, 33, 100, 369);
		keyword_quick_list.setBorder(new LineBorder(Color.black));
		main_p.add(keyword_quick_list);
		
		file_send_btn = new JButton("파일전송");
		file_send_btn.setVisible(false);
		file_send_btn.setBackground(new Color(135, 206, 250));
		file_send_btn.setBounds(663, 412, 95, 23);
		main_p.add(file_send_btn);

		MouseListener mouseListener_keyword = new MouseAdapter() {
			public void mouseClicked(MouseEvent mouseEvent) {

				JList<String> theList = (JList) mouseEvent.getSource();
				if (mouseEvent.getClickCount() == 2) {
					int index = theList.locationToIndex(mouseEvent.getPoint());
					if (index >= 0) {
						Object o = theList.getModel().getElementAt(index);
						System.out.println("Double-clicked on: " + o.toString());
						String str = o.toString();
						card.show(contentpane, "board_p");
						StringTokenizer stringTokenizer;
						stringTokenizer = new StringTokenizer(str, ":");
						String count = stringTokenizer.nextToken();
						String keyword = stringTokenizer.nextToken();

						System.out.println("count확인용" + count);
						String group_name = group_id_to_name.get(current_room);
						KeywordResult keywordroot = new KeywordResult("title",
								group_name + "그룹의" + keyword + "키워드결과");
						DefaultMutableTreeNode root_all = new DefaultMutableTreeNode(keywordroot);

						// KeywordResult keywordResult_child = new KeywordResult(chat, check)
						Vector<String> chat_v = requestDB.group_keyword_result(current_room, keyword);
						number_code = requestDB.group_keyword_result_code(current_room, keyword);
						number_chat = requestDB.group_keyword_result_number(current_room, keyword);
						chat_new.clear();
						chat_old.clear();
						chat_mark.clear();

						Set set = number_chat.entrySet();
						Iterator iterator = set.iterator();
						ArrayList<Integer> number_al = new ArrayList<>();
						while (iterator.hasNext()) {
							Map.Entry entry = (Map.Entry) iterator.next();
							String number = (String) entry.getKey();
							number_al.add(Integer.parseInt(number));
						}
						Collections.sort(number_al);
						Collections.reverse(number_al);
						for (int k = 0; k < number_al.size(); k++) {
							String chatdata = number_chat.get(String.valueOf(number_al.get(k)));
							String code = number_code.get(String.valueOf(number_al.get(k)));
							KeywordResult keywordResult = new KeywordResult("chat",current_room, chatdata,
									code,String.valueOf(number_al.get(k)));
							DefaultMutableTreeNode chat_child = new DefaultMutableTreeNode(keywordResult);
							root_all.add(chat_child);
						}
					
					
						

						tree_model = new DefaultTreeModel(root_all);
						tree.setModel(tree_model);
						tree.setCellRenderer(new KeywordTreeCellRenderer());

					}
				}
			}

		};
		keyword_quick_list.addMouseListener(mouseListener_keyword);

		MouseListener mouseListener_group = new MouseAdapter() {
			public void mouseClicked(MouseEvent mouseEvent) {
				JList<String> theList = (JList) mouseEvent.getSource();
				if (mouseEvent.getClickCount() == 2) {

					int index = theList.locationToIndex(mouseEvent.getPoint());
					if (index >= 0) {
						Object o = theList.getModel().getElementAt(index);
						RoomInfo selected_roomInfo = (RoomInfo)o;
						String groupid = selected_roomInfo.getGroupid();
					    System.out.println("선택된 그룹"+groupid);
						if (current_room != null) {
							if (current_room.equals(groupid)) {
								JOptionPane.showMessageDialog(null, "현재 채팅방입니다.", "알림", JOptionPane.ERROR_MESSAGE);
								return;
							}
							send_message("ExitRoom/" + current_room);
							textArea_chat.setText("");
						}
						send_message("JoinRoom/" + groupid);
						System.out.println("join_room");
						current_room = groupid;

					}
				}

			}
		};
		group_list.addMouseListener(mouseListener_group);

		this.setVisible(false);
	}

	private void Login_init() { // Login GUI 로그인 제이프레임
		Login_GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Login_GUI.setBounds(100, 100, 318, 500);
		Login_panel = new JPanel();
		Login_panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		Login_GUI.setContentPane(Login_panel);
		Login_panel.setLayout(null);

		JLabel lblId = new JLabel("ID");
		lblId.setBounds(44, 141, 77, 15);
		Login_panel.add(lblId);

		textField_id = new JTextField();
		textField_id.setBounds(120, 138, 137, 21);
		Login_panel.add(textField_id);
		textField_id.setColumns(10);

		JLabel lblPwd = new JLabel("PWD");
		lblPwd.setBounds(44, 197, 77, 15);
		Login_panel.add(lblPwd);

		textField_pwd = new JPasswordField();
		textField_pwd.setBounds(120, 194, 137, 21);
		Login_panel.add(textField_pwd);
		textField_pwd.setColumns(10);

		button_login.setBounds(44, 300, 213, 23);
		Login_panel.add(button_login);

		button_join.setBounds(44, 350, 213, 23);
		Login_panel.add(button_join);
		Login_GUI.setVisible(true);
	}

	private void Join_init() { // Login GUI 로그인 제이프레임
		Join_GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Join_GUI.setBounds(100, 100, 318, 500);
		Join_panel = new JPanel();
		Join_panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		Join_GUI.setContentPane(Join_panel);
		Join_panel.setLayout(null);

		JLabel lblId = new JLabel("ID");
		lblId.setBounds(44, 141, 77, 15);
		Join_panel.add(lblId);

		Join_textField_id = new JTextField();
		Join_textField_id.setBounds(120, 138, 137, 21);
		Join_panel.add(Join_textField_id);
		Join_textField_id.setColumns(10);

		JLabel lblPwd = new JLabel("PWD");
		lblPwd.setBounds(44, 197, 77, 15);
		Join_panel.add(lblPwd);

		Join_textField_pwd = new JPasswordField();
		Join_textField_pwd.setBounds(120, 194, 137, 21);
		Join_panel.add(Join_textField_pwd);
		Join_textField_pwd.setColumns(10);

		submit.setBounds(44, 300, 213, 23);
		Join_panel.add(submit);

		Join_GUI.setVisible(false);
	}

	private void network() { // 있어야하는애들
		try {
			port = 9000;
			socket = new Socket(ip, port);
			if (socket != null) { // socket ok!!
				connection();
			}
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "연결 실패", "알림", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "연결 실패", "알림", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void join_network() {
		try {
			port = 9000;
			socket = new Socket(ip, port);
			if (socket != null) { // socket ok!!
				join_connection();
			}
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "연결 실패", "알림", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "연결 실패", "알림", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void connection() { // 있어야하는애들
		try {
			inputStream = socket.getInputStream();
			dataInputStream = new DataInputStream(inputStream);
			outputStream = socket.getOutputStream();
			dataOutputStream = new DataOutputStream(outputStream);
			this.setVisible(true);
			this.Login_GUI.setVisible(false);
			send_message("login/" + id + "/" + pwd); // first connect -> send id
			Thread thread = new Thread(new Socket_thread());
			thread.start();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "연결 실패", "알림", JOptionPane.ERROR_MESSAGE);
		}

	}

	private void join_connection() { // 있어야하는애들
		try {
			inputStream = socket.getInputStream();
			dataInputStream = new DataInputStream(inputStream);
			outputStream = socket.getOutputStream();
			dataOutputStream = new DataOutputStream(outputStream);
			send_message("join/" + id + "/" + pwd);
			Thread thread = new Thread(new Socket_thread());
			thread.start();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "연결 실패", "알림", JOptionPane.ERROR_MESSAGE);
		}
	}

	public class Socket_thread implements Runnable { // 있어야하는 애들
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				try {

					InMessage(dataInputStream.readUTF());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					try {
						outputStream.close();
						inputStream.close();
						dataInputStream.close();
						dataOutputStream.close();
						socket.close();

						JOptionPane.showMessageDialog(null, "서버와 접속 끊어짐", "알림", JOptionPane.ERROR_MESSAGE);
					} catch (IOException e1) {
					}
					break;

				}
			}
		}
	}

	private void changeFriendStatus() {
		friend_model.clear();
		friend_model.addElement(new UserInfo(id,true));
		Set set = friend_user_status.entrySet();
		Iterator iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String id = (String) entry.getKey();
			Boolean status = (Boolean) entry.getValue();

			friend_model.addElement(new UserInfo(id, status));

		}
		friend_list_user.setModel(friend_model);
		friend_list_user.setCellRenderer(new UserInfoRenderer());
	}
	private void changegroupidlist() {
		groupId_model.clear();
		
		group_id_to_name = requestDB.getgroup_id_to_name();
		Set set = group_id_to_name.entrySet();
		Iterator iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String groupid = (String) entry.getKey();
			String group_name = (String) entry.getValue();
         
			
			groupId_model.addElement(new RoomInfo(groupid,group_name));
        	  
		}
		group_list.setModel(groupId_model);
		group_list.setCellRenderer(new RoomInfoRenderer());
	}

	private void changegroupStatus(String groupid) {
		group_user_model.clear();
		RoomInfo roomInfo = group_user_status.get(groupid);
		ConcurrentHashMap<String, Boolean> map = roomInfo.user_status;

		group_user_model.addElement(new UserInfo(this.id,true));
		Set set = map.entrySet();
		Iterator iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String id = (String) entry.getKey();
			Boolean status = (Boolean) entry.getValue();
          if(!id.equals(this.id)) {
        	  group_user_model.addElement(new UserInfo(id, status));
          }

		}
		room_list_user.setModel(group_user_model);
		room_list_user.setCellRenderer(new UserInfoRenderer());
	}
	private void changeCombobox() {
		
		combo_list_model = new DefaultComboBoxModel<>();
		combo_list_model.addElement(new Combo_Keyword_Info("all_group_key", "전체그룹"));
		combo_list_model.addElement(new Combo_Keyword_Info("user_mark", "즐겨찾기"));
		Set set = requestDB.getgroup_id_to_name().entrySet();
		Iterator iterator = set.iterator();		
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String groupid = (String) entry.getKey();
			String group_name = (String) entry.getValue();
          combo_list_model.addElement(new Combo_Keyword_Info(groupid,group_name));
		}
		comboBox.setModel(combo_list_model);
		comboBox.setRenderer(new ComboRenderer());
	}

	private void get_keyword_q_list(String groupid) {
		int count = 0;
		keyword_q_list_model.clear();
		Vector<String> get_keyword = requestDB.get_user_keyword(groupid);

		for (int i = 0; i < get_keyword.size(); i++) {
			count = requestDB.get_room_new_keyword_count(groupid, get_keyword.elementAt(i));
			keyword_q_list_model.addElement("[" + count + "]:" + get_keyword.elementAt(i));
		}
		keyword_quick_list.setModel(keyword_q_list_model);
		
	}

	private void InMessage(String str) { // all message from server //서버에서 전달받음
		StringTokenizer stringTokenizer;
		stringTokenizer = new StringTokenizer(str, "/");
		Vector<String> data = new Vector<>();

		while (stringTokenizer.hasMoreTokens()) {

			data.add(stringTokenizer.nextToken());
		}

		String protocol = data.elementAt(0);
		System.out.println("----------------------");
		System.out.println("보내진 구조크기" + data.size());
		System.out.println("프로토콜 : " + protocol);

		if (protocol.equals("friend_list")) {
			String friendid = data.elementAt(1);
			System.out.println("내용 : " + friendid);
			friend_user_status.put(friendid, false);
			System.out.println("사용자 친구:" + friendid);
			changeFriendStatus();

		} else if (protocol.equals("online_friend")) {
			String online_friend = data.elementAt(1);
			System.out.println("내용 : " + online_friend);
			friend_user_status.put(online_friend, true);
			System.out.println("사용자 친구 온라인:" + online_friend);
			changeFriendStatus();

		} else if (protocol.equals("new_chat")) {
			String code = data.elementAt(1);
			String groupid = data.elementAt(2);
			System.out.println("그룹명 : " + groupid);
			String time = data.elementAt(3);
			String userid = data.elementAt(4);
			String chatdata = "";
			for (int i = 5; i < data.size(); i++) {
				chatdata = chatdata + data.elementAt(i);
			}

			String result = "[" + time + "]" + userid + " : " + chatdata;
			System.out.println("저장하는 메시지:" + result);
			requestDB.set_new_chat(groupid, result, code);

		}

		else if (protocol.equals("Note")) {
			String message = data.elementAt(1);
			System.out.println("내용 : " + message);
			String note = data.elementAt(2);
			System.out.println(message + " 사용자에게 온 쪽지 " + note);
			JOptionPane.showMessageDialog(null, note, message + "님으로 부터 온 쪽지", JOptionPane.CLOSED_OPTION); // basic
																											// support
																											// dialog

		} else if (protocol.equals("CreateRoomFail")) {
			JOptionPane.showMessageDialog(null, "방 만들기 실패", "알림", JOptionPane.ERROR_MESSAGE);
		} else if (protocol.equals("NewRoom")) { // 새로운그룹
			String groupid = data.elementAt(1);
			String group_name = data.elementAt(2);
			System.out.println("내용 : " + groupid);
			RoomInfo roominfo = new RoomInfo();
			roominfo.setGroupid(groupid);
			roominfo.setGroup_name(group_name);
			roominfo.user_status.put(id, true);
			group_user_status.put(groupid, roominfo);
			
			
			
			requestDB.creategroup(groupid);
			requestDB.creategroupkey(groupid);
			requestDB.addgrouplist(groupid,group_name);
			changeCombobox();
			changegroupidlist();
			

		} else if (protocol.equals("OldRoom")) {
			String groupid = data.elementAt(1);
			String group_name =data.elementAt(2);
			System.out.println("그룹이름 : " + groupid);
			RoomInfo roominfo = new RoomInfo();
			for (int i = 0; i < data.size() - 3; ++i) {
				String userid = data.elementAt(i + 3);
				System.out.println("해당 그룹 유저:" + userid);
				if (userid.equals(id)) {
					roominfo.user_status.put(userid, true);
				} else
					roominfo.user_status.put(userid, false);

			}
			if (requestDB.checktable(groupid)) {
				requestDB.creategroup(groupid);
				requestDB.creategroupkey(groupid);
				requestDB.addgrouplist(groupid,group_name);
			}
			
			int code = requestDB.get_code_number(groupid);
			String scode = String.valueOf(code);
			send_message("get_room_chat_data/" + groupid + "/" + scode);
			group_user_status.put(groupid, roominfo); // 들어온 그룹명과 그룹안에 상용자 저장
			room_list.add(groupid);
			

		} else if (protocol.equals("oldRoom_update")) {

		} else if (protocol.equals("JoinRoom")) { // 그룹입장
			String groupid = data.elementAt(1);
			current_room = data.elementAt(1);
			System.out.println("내용 : " + current_room);

			RoomInfo roomInfo = group_user_status.get(groupid);
			Vector vector = requestDB.get_chat_data(groupid);

			roomInfo.chat_data = vector;
			group_user_status.put(groupid, roomInfo);

			for (int i = 0; i < vector.size(); i++) {
				textArea_chat.append(vector.elementAt(i) + "\n");
			}
			textArea_chat.setCaretPosition(textArea_chat.getDocument().getLength());

			
			button_send_message.setEnabled(true);
			button_send_invite.setEnabled(true);
			textField_message.setEnabled(true);

			get_keyword_q_list(groupid);
			changegroupStatus(groupid);

		} else if (protocol.equals("group_online")) {
			if (data.size() > 2) {
				String groupid = data.elementAt(1);
				String userid = data.elementAt(2);
				System.out.println("그룹이름 : " + groupid);
				RoomInfo roomInfo = group_user_status.get(groupid);
				System.out.println("그룹온라인:" + userid);
				if (!userid.equals(id)) {
					roomInfo.user_status.put(userid, true);
					group_user_status.put(groupid, roomInfo);
					if (current_room.equals(groupid))
						changegroupStatus(groupid);
				}
			}

		} else if (protocol.equals("group_each_online")) {
			String groupid = data.elementAt(1);
			System.out.println("그룹이름 : " + groupid);
			RoomInfo roomInfo = group_user_status.get(groupid);
			for (int i = 0; i < data.size() - 2; i++) {
				String userid = data.elementAt(i + 2);
				System.out.println("해당 그룹 유저 온라인자:" + userid);
				roomInfo.user_status.put(userid, true);
			}
			group_user_status.put(groupid, roomInfo);

		}

		else if (protocol.equals("ExitRoom")) {
			String message = data.elementAt(1);
			System.out.println("내용 : " + message);
			room_list.remove(message);
			group_user_status.remove(message);
		} else if (protocol.equals("Chatting")) { // code + "/" + groupID + "/" + time + "/" + userid + "/" + chatdata;
			String code = data.elementAt(1);
			String groupid = data.elementAt(2);
			String time = data.elementAt(3);
			String userid = data.elementAt(4);
			System.out.println("채팅온그룹명 : " + groupid);
			String chatdata = "";
			for (int i = 5; i < data.size(); i++) {
				chatdata = chatdata + data.elementAt(i);
			}
			String result = "[" + time + "]" + userid + " : " + chatdata;
			requestDB.set_new_chat(groupid, result, code);
			if(current_room.equals(groupid)) {
				textArea_chat.append("[" + time + "]" + userid + " : " + chatdata + "\n");
				textArea_chat.setCaretPosition(textArea_chat.getDocument().getLength());
				get_keyword_q_list(groupid);
			}
			
		

		} else if (protocol.equals("userout")) {
			String protocol2 = data.elementAt(1);
			System.out.println("프로토콜2 : " + protocol2);

		} else if (protocol.equals("result_add_friend")) {
			String message = data.elementAt(1);
			System.out.println("내용 : " + message);
			if (message.equals("fadd_ok")) {
				JOptionPane.showMessageDialog(null, "추가완료", "알림", JOptionPane.INFORMATION_MESSAGE);
			} else if (message.equals("fadd_alread")) {
				JOptionPane.showMessageDialog(null, "이미친구추가되어있는 아이디입니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
			} else if (message.equals("fadd_fail")) {
				JOptionPane.showMessageDialog(null, "시스템오류, 다시시도해보세요", "알림", JOptionPane.INFORMATION_MESSAGE);
			}

		} else if (protocol.equals("join_sucess")) {
			JOptionPane.showMessageDialog(null, "회원가입성공", "알림", JOptionPane.INFORMATION_MESSAGE);
			Join_GUI.setVisible(false);
			Login_GUI.setVisible(true);
		} else if (protocol.equals("join_fail")) {
			JOptionPane.showMessageDialog(null, "회원가입실패", "알림", JOptionPane.ERROR_MESSAGE);
		} else if (protocol.equals("set_offline")) {
			String groupid = data.elementAt(1);
			String userid = data.elementAt(2);
			RoomInfo roomInfo = group_user_status.get(groupid);
			roomInfo.user_status.put(userid, false);
			group_user_status.put(groupid, roomInfo);
			changegroupStatus(groupid);
		} else if (protocol.equals("friend_user_offline")) {
			String userid = data.elementAt(1);
			friend_user_status.put(userid, false);
			changeFriendStatus();
		} else if (protocol.equals("group_user_offline")) {
			String groupid = data.elementAt(1);
			String userid = data.elementAt(2);

			RoomInfo roomInfo = group_user_status.get(groupid);
			roomInfo.user_status.put(userid, false);
			group_user_status.put(groupid, roomInfo);
			if (current_room.equals(groupid)) {
				changegroupStatus(groupid);
			}
		}
		else if(protocol.equals("end_update")) {
			change_to_board.setEnabled(true);
			changeFriendStatus();
			changegroupidlist();
			btnchangelist.setEnabled(true);
			//file_send_btn.setEnabled(true);
		}
	}

	private String getLocalServerIp() // 있어야하는애들
	{
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()
							&& inetAddress.isSiteLocalAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
		}
		return null;
	}

	private void send_message(String message) { // 서버에 전달
		try {
			System.out.println("전달한구조:" + message);
			dataOutputStream.writeUTF(message);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) { // 메인
		// TODO Auto-generated method stub
		new Client();
	}

	public void actionPerformed(ActionEvent e) { // 뜯어야할곳
		// TODO Auto-generated method stub
		if (e.getSource() == button_login) {
			System.out.println("login");

			if (textField_id.getText().length() == 0) {
				JOptionPane.showMessageDialog(null, "ID를 입력해주세요", "알림", JOptionPane.ERROR_MESSAGE);
				textField_id.requestFocus();
			} else if (textField_pwd.getText().length() == 0) {
				JOptionPane.showMessageDialog(null, "패스워드를 입력해주세요", "알림", JOptionPane.ERROR_MESSAGE);
				textField_pwd.requestFocus();
			} else {

				id = textField_id.getText().trim();
				pwd = textField_pwd.getText().trim();
				System.out.println("사용자" + id);
				network();
			}
		}
	
		else if (e.getSource() == button_create_room) {
			String roomname = null;

			roomname = JOptionPane.showInputDialog("생성할그룹명");

			if (!(roomname.trim().isEmpty())) {
				send_message("CreateRoom/" + roomname);

				System.out.println("create_room");
			}

		} else if (e.getSource() == button_send_message)

		{ // 메시지 전달.
			System.out.println("send_message");
			if (current_room == null) {
				JOptionPane.showMessageDialog(null, "채팅방에 참여해주세요", "알림", JOptionPane.ERROR_MESSAGE);
			} else {
				send_message("Chatting/" + current_room + "/" + textField_message.getText().trim());
				textField_message.setText("");
				textField_message.requestFocus();
			}
		} else if (e.getSource() == button_send_invite) {
			String id = JOptionPane.showInputDialog(null, "추가할 아이디를 입력하세요", "그룹초대", JOptionPane.OK_CANCEL_OPTION);
			System.out.println("사용자 초대");
			if (id != null) {
				send_message("invite_group/" + current_room + "/" + id);
			}
		} else if (e.getSource() == fadd_button) {
			System.out.println("add_friend");
			String id = JOptionPane.showInputDialog(null, "추가할 아이디를 입력하세요", "친구추가", JOptionPane.OK_CANCEL_OPTION);
			System.out.println(id);
			if (!id.isEmpty()) {
				send_message("add_friend/" + id);
			}

		} else if (e.getSource() == change_to_board) {
			if(!current_room.isEmpty()) {
				get_keyword_q_list(current_room);
			}
			changeCombobox();
			card.show(contentpane, "board_p");
			contentpane.updateUI();
		} else if (e.getSource() == btntochat) {
			if(!current_room.isEmpty()) {
				get_keyword_q_list(current_room);
			}
			card.show(contentpane, "main_p");
			this.setPreferredSize(dim);
			contentpane.updateUI();

		} else if (e.getSource() == button_search) {
			String keyword = search_txf.getText().trim();
			KeywordResult root = new KeywordResult("title", "검색결과");
			DefaultMutableTreeNode root_all = new DefaultMutableTreeNode(root);
			KeywordResult keyword_k = new KeywordResult("title", keyword);
			DefaultMutableTreeNode keyword_child = new DefaultMutableTreeNode(keyword_k);

			for (int i = 0; i < room_list.size(); i++) { // 전체 그룹에대한 키워드

				String group_name = group_id_to_name.get(room_list.elementAt(i));
				KeywordResult group = new KeywordResult("group", group_name); // 그룹 노드생성
				DefaultMutableTreeNode group_child_child = new DefaultMutableTreeNode(group); // 그룹노드트리초기화
				Vector<String> chat_v = requestDB.group_keyword_result(room_list.elementAt(i), keyword);
				number_code = requestDB.group_keyword_result_code(room_list.elementAt(i), keyword);
				number_chat = requestDB.group_keyword_result_number(room_list.elementAt(i), keyword);
				Set set = number_chat.entrySet();
				Iterator iterator = set.iterator();

				while (iterator.hasNext()) {
					Map.Entry entry = (Map.Entry) iterator.next();
					String number = (String) entry.getKey();
					String chatdata = (String) entry.getValue();
					String code = number_code.get(number);
					KeywordResult keywordResult = new KeywordResult("chat",room_list.elementAt(i), chatdata,
							code,number);
					DefaultMutableTreeNode chat_child = new DefaultMutableTreeNode(keywordResult);
					group_child_child.add(chat_child);
				}
				keyword_child.add(group_child_child);
			}
			root_all.add(keyword_child);

			tree_model = new DefaultTreeModel(root_all);
			tree.setModel(tree_model);
			tree.setCellRenderer(new KeywordTreeCellRenderer());

		} else if (e.getSource() == btnchangelist) {
			if (list_change == false) {
				fg_card.show(fg_list_panel, "group_list_panel");
				list_change = true;
			} else {
				fg_card.show(fg_list_panel, "friend_list_panel");
				list_change = false;
			}

		} else if (e.getSource() == comboBox) {
			JComboBox cb = (JComboBox) e.getSource();
			Object cell = cb.getSelectedItem();
			Combo_Keyword_Info combo_Keyword_Info = (Combo_Keyword_Info)cell;
			select_keyword_group = combo_Keyword_Info.Groupid;
			System.out.println(select_keyword_group);
			// DefaultMutableTreeNode root_group = new
			// DefaultMutableTreeNode(select_keyword_group);
			String title = "";
			if (select_keyword_group.equals("all_group_key")) {
				title = "그룹별키워드결과";
			}else if(select_keyword_group.equals("user_mark")) {
				title = "즐겨찾기";
			}else {
				
				title = group_id_to_name.get(select_keyword_group);
			}
			KeywordResult root = new KeywordResult("title", title);
			DefaultMutableTreeNode root_all = new DefaultMutableTreeNode(root);
			if (select_keyword_group.equals("all_group_key")) {
				Vector<String> keyword_v = requestDB.get_user_keyword(select_keyword_group);
				for (int i = 0; i < room_list.size(); i++) { // 전체 그룹에대한 키워드
					String group_name = group_id_to_name.get(room_list.elementAt(i));
					KeywordResult group = new KeywordResult("group", group_name); // 그룹 노드생성
					DefaultMutableTreeNode group_child = new DefaultMutableTreeNode(group); // 그룹노드트리초기화
					for (int j = 0; j < keyword_v.size(); j++) {

						KeywordResult keyword_k = new KeywordResult("title", keyword_v.elementAt(j));

						DefaultMutableTreeNode keyword_child_child = new DefaultMutableTreeNode(keyword_k);

						
					

						number_code = requestDB.group_keyword_result_code(room_list.elementAt(i),
								keyword_v.elementAt(j));
						number_chat = requestDB.group_keyword_result_number(room_list.elementAt(i), keyword_v.elementAt(j));
						Set set = number_chat.entrySet();
						Iterator iterator = set.iterator();

						ArrayList<Integer> number_al = new ArrayList<>();
						while (iterator.hasNext()) {
							Map.Entry entry = (Map.Entry) iterator.next();
							String number = (String) entry.getKey();
							number_al.add(Integer.parseInt(number));
						}
						Collections.sort(number_al);
						Collections.reverse(number_al);
						for (int k = 0; k < number_al.size(); k++) {
							String chatdata = number_chat.get(String.valueOf(number_al.get(k)));
							String code = number_code.get(String.valueOf(number_al.get(k)));
							KeywordResult keywordResult = new KeywordResult("chat",room_list.elementAt(i), chatdata,
									code,String.valueOf(number_al.get(k)));
							DefaultMutableTreeNode chat_child = new DefaultMutableTreeNode(keywordResult);
							keyword_child_child.add(chat_child);
						}
						
						group_child.add(keyword_child_child);
					}
					root_all.add(group_child);
				}
				tree_model = new DefaultTreeModel(root_all);
				tree.setModel(tree_model);
				tree.setCellRenderer(new KeywordTreeCellRenderer());

			} else if (select_keyword_group.equals("user_mark")) {

				for (int i = 0; i < room_list.size(); i++) { // 전체 그룹에대한 키워드
					String group_name = group_id_to_name.get(room_list.elementAt(i));
					KeywordResult group = new KeywordResult("group",group_name); // 그룹 노드생성
					DefaultMutableTreeNode group_child = new DefaultMutableTreeNode(group); // 그룹노드트리초기화

					
					number_code = requestDB.getmark_number_code(room_list.elementAt(i));
					number_chat = requestDB.getmark_number_chat(room_list.elementAt(i));
					Set set = number_chat.entrySet();
					Iterator iterator = set.iterator();

					ArrayList<Integer> number_al = new ArrayList<>();
					while (iterator.hasNext()) {
						Map.Entry entry = (Map.Entry) iterator.next();
						String number = (String) entry.getKey();
						number_al.add(Integer.parseInt(number));
					}
					Collections.sort(number_al);
					Collections.reverse(number_al);
					for (int k = 0; k < number_al.size(); k++) {
						String chatdata = number_chat.get(String.valueOf(number_al.get(k)));
						String code = number_code.get(String.valueOf(number_al.get(k)));
						KeywordResult keywordResult = new KeywordResult("chat",room_list.elementAt(i), chatdata,
								code,String.valueOf(number_al.get(k)));
						DefaultMutableTreeNode chat_child = new DefaultMutableTreeNode(keywordResult);
						group_child.add(chat_child);
					}
					root_all.add(group_child);
				}

				tree_model = new DefaultTreeModel(root_all);
				tree.setModel(tree_model);
				tree.setCellRenderer(new KeywordTreeCellRenderer());
			} else {
				Vector<String> keyword_v = requestDB.get_user_keyword(select_keyword_group);
				for (int i = 0; i < keyword_v.size(); i++) {
					KeywordResult keyword_k = new KeywordResult("title", keyword_v.elementAt(i));
					DefaultMutableTreeNode keyword_child = new DefaultMutableTreeNode(keyword_k);
					Vector<String> chat_v = requestDB.group_keyword_result(select_keyword_group,
							keyword_v.elementAt(i));
					number_code = requestDB.group_keyword_result_code(select_keyword_group, keyword_v.elementAt(i));
					number_chat = requestDB.group_keyword_result_number(select_keyword_group, keyword_v.elementAt(i));
					Set set = number_chat.entrySet();
					Iterator iterator = set.iterator();

					ArrayList<Integer> number_al = new ArrayList<>();
					while (iterator.hasNext()) {
						Map.Entry entry = (Map.Entry) iterator.next();
						String number = (String) entry.getKey();
						number_al.add(Integer.parseInt(number));
					}
					Collections.sort(number_al);
					Collections.reverse(number_al);
					for (int k = 0; k < number_al.size(); k++) {
						String chatdata = number_chat.get(String.valueOf(number_al.get(k)));
						String code = number_code.get(String.valueOf(number_al.get(k)));
						KeywordResult keywordResult = new KeywordResult("chat",select_keyword_group, chatdata,
								code,String.valueOf(number_al.get(k)));
						DefaultMutableTreeNode chat_child = new DefaultMutableTreeNode(keywordResult);
						keyword_child.add(chat_child);
					}
					root_all.add(keyword_child);
				}
				tree_model = new DefaultTreeModel(root_all);
				tree.setModel(tree_model);
			}
			
			// 그룹키워드 전달

		} else if (e.getSource() == goto_keyword_set) {
			Board_custom board_custom = new Board_custom();
			board_custom.setVisible(true);

		} else if (e.getSource() == button_join) {
			Join_GUI.setVisible(true);
			Login_GUI.setVisible(false);
		} else if (e.getSource() == submit) {
			if (Join_textField_id.getText().length() == 0) {
				JOptionPane.showMessageDialog(null, "ID를 입력해주세요", "알림", JOptionPane.ERROR_MESSAGE);
				Join_textField_id.requestFocus();
			} else if (Join_textField_pwd.getText().length() == 0) {
				JOptionPane.showMessageDialog(null, "패스워드를 입력해주세요", "알림", JOptionPane.ERROR_MESSAGE);
				Join_textField_pwd.requestFocus();
			} else {

				id = Join_textField_id.getText().trim();
				pwd = Join_textField_pwd.getText().trim();
				System.out.println("사용자" + id);
				join_network();
			}
		}
		else if(e.getSource() == file_send_btn) {
			FileChoose filename = new FileChoose();
			String filepath = filename.FileChoose_Start();
			
			try {
				
				 FileSender fs = new FileSender(socket,filepath);
		         fs.start();


		
			}catch (Exception e2) {
				// TODO: handle exception
			}

		}

	}
	class FileSender extends Thread {
	    Socket socket;
	    DataOutputStream dos;
	    FileInputStream fis;
	    BufferedInputStream bis;
	    String filename;
	    int control = 0;
	    public FileSender(Socket socket,String filestr) {
	        this.socket = socket;
	        this.filename = filestr;
	        try {
	            // 데이터 전송용 스트림 생성
	            dos = new DataOutputStream(socket.getOutputStream());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	  
	    @Override
	    public void run() {
	        try {
	  
	            String fName = filename;
	 
	  
	            // 파일 내용을 읽으면서 전송
	            File f = new File(fName);
	            fis = new FileInputStream(f);
	            bis = new BufferedInputStream(fis);
	  
	            int len;
	            int size = 4096;
	            byte[] data = new byte[size];
	            while ((len = bis.read(data)) != -1) {
	                control++;
	                if(control % 10000 == 0)
	                {
	                    System.out.println("전송중..." + control/10000);      
	                }
	                dos.write(data, 0, len);
	            }
	  
	            dos.flush();
	            dos.close();
	            bis.close();
	            fis.close();
	            System.out.println("완료");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
}
