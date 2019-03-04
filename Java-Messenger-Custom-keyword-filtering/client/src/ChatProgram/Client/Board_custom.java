package ChatProgram.Client;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

public class Board_custom extends JFrame implements ActionListener, KeyListener, ItemListener,ListSelectionListener{


	private RequestDB requestDB = new RequestDB();
	private JTextField textField;
	private JList list = new JList();
	private JButton btn_add_keyword;
	private JButton btn_delete_keyword;
	private JComboBox comboBox;
	JRadioButton keyword_kind_chat;
	private String groupid = "all_group";
	DefaultListModel model = new DefaultListModel();
	DefaultComboBoxModel<Combo_Keyword_Info> combo_list_model = new DefaultComboBoxModel<>();
	String keyword="";
	
	
	
	ButtonGroup buttonGrp = new ButtonGroup();
	public Board_custom() {
		getContentPane().setBackground(new Color(112, 128, 144));

	
		getContentPane().setLayout(null);
		this.setBounds(100, 100, 452, 300);
		
		
		list = new JList(model);
		list.setBounds(12, 10, 181, 241);
		getContentPane().add(list);
		
		textField = new JTextField();
		textField.setBounds(205, 193, 116, 21);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		btn_add_keyword = new JButton("키워드 추가");
		btn_add_keyword.setBounds(325, 192, 97, 23);
		
		btn_add_keyword.setBackground(new Color(135, 206, 250));
		btn_add_keyword.setUI(new StyledButtonUI());
		getContentPane().add(btn_add_keyword);
		
		btn_delete_keyword = new JButton("키워드삭제");
		btn_delete_keyword.setBounds(325, 228, 97, 23);
	
		btn_delete_keyword.setBackground(new Color(135, 206, 250));
		btn_delete_keyword.setUI(new StyledButtonUI());
		getContentPane().add(btn_delete_keyword);
		comboBox = new JComboBox();
		changeCombobox();
		comboBox.setBounds(265, 23, 136, 21);
		getContentPane().add(comboBox);
		
		JLabel lblNewLabel = new JLabel("그룹명 :");
		lblNewLabel.setBounds(216, 26, 49, 15);
		getContentPane().add(lblNewLabel);
		
		keyword_kind_chat = new JRadioButton("대화내용" , true);
		keyword_kind_chat.setBackground(new Color(112, 128, 144));
		keyword_kind_chat.setBounds(201, 164, 79, 23);
		getContentPane().add(keyword_kind_chat);
		buttonGrp.add(keyword_kind_chat);
		

		btn_add_keyword.addActionListener(this);
		btn_delete_keyword.addActionListener(this);
		
		keyword_kind_chat.addItemListener(this);
		comboBox.addActionListener(this);
		keyword_kind_chat.isSelected();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addListSelectionListener(this);
		
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btn_add_keyword){
			
			String keyword = textField.getText().trim();
			requestDB.set_user_keyword(groupid,keyword);
			model.clear();
			Vector<String> vector = requestDB.get_user_keyword(groupid);
			for(int i = 0;i<vector.size();i++) {
				model.addElement(vector.elementAt(i));
			}
			list.setModel(model);
			
		}
		else if(e.getSource() == btn_delete_keyword) {
			model.clear();
			System.out.println("지울 키워드 그룹:"+groupid + "/키워드:"+ keyword);
			requestDB.delte_keyword(groupid, keyword);
			Vector<String> vector = requestDB.get_user_keyword(groupid);
			for(int i = 0;i<vector.size();i++) {
				model.addElement(vector.elementAt(i));
			}
			list.setModel(model);
		}
		else if(e.getSource() == comboBox) {
			Combo_Keyword_Info cell = (Combo_Keyword_Info)comboBox.getSelectedItem();
			groupid = cell.Groupid;
			String group_name = cell.Group_name;
			model.clear();

			Vector<String> vector = requestDB.get_user_keyword(groupid);
			for(int i = 0;i<vector.size();i++) {
				model.addElement(vector.elementAt(i));
			}
			list.setModel(model);

		}
	}
	
	private void changeCombobox() {
		combo_list_model.removeAllElements();
		
		combo_list_model.addElement(new Combo_Keyword_Info("all_group_key", "전체그룹"));
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


	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		AbstractButton sel = (AbstractButton)e.getItemSelectable();
		
		if(e.getStateChange() == ItemEvent.SELECTED) {// 사용자 아이디,키워드 선택
			
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {


	if (list.getSelectedValue()!=null) { //getSelectedValue() : 선택된 항목(Object 타입) 반환

		keyword = (String)list.getSelectedValue();
		System.out.println(keyword);
	//하나라도 선택된 경우

	} else {

	
	//하나도 선택되지 않은 경우

	}

	}


	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	
}
