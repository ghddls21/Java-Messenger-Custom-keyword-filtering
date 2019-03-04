package ChatProgram.Client;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

public class KeywordTreeCellRenderer implements TreeCellRenderer {
	private JLabel label;
	DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();

	Color backgroundSelectionColor;

	Color backgroundNonSelectionColor;

	KeywordTreeCellRenderer() {
		label = new JLabel();
		backgroundSelectionColor = defaultRenderer.getBackgroundSelectionColor();
		backgroundNonSelectionColor = defaultRenderer.getBackgroundNonSelectionColor();
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
		Object o = ((DefaultMutableTreeNode) value).getUserObject();
		if (o instanceof KeywordResult) {
			KeywordResult keywordResult = (KeywordResult) o;

			if(keywordResult.getKategory().equals("title")) {
				label.setIcon(null);
				label.setText(keywordResult.getKategory_data());
			}
				
		    if (keywordResult.getKategory().equals("group")) {
		    	ImageIcon imageIcon = new ImageIcon(getClass().getResource("group.png"));
		    	label.setIcon(imageIcon);
				label.setText(keywordResult.getKategory_data());
		    }
		    if(keywordResult.getKategory().equals("chat")) {
		    	if (keywordResult.getCheck().equals("0")) {
					ImageIcon imageIcon = new ImageIcon(getClass().getResource("new_keword_result.png"));
					label.setIcon(imageIcon);
					label.setText(keywordResult.getChat());
				} else if (keywordResult.getCheck().equals("1")) {
					ImageIcon imageIcon = new ImageIcon(getClass().getResource("keyword_result.png"));
					label.setIcon(imageIcon);
					label.setText(keywordResult.getChat());
				}else if (keywordResult.getCheck().equals("2")) {
					ImageIcon imageIcon = new ImageIcon(getClass().getResource("mark.png"));
					label.setIcon(imageIcon);
					label.setText(keywordResult.getChat());
				}else if (keywordResult.getCheck().equals("4")) {
					ImageIcon imageIcon = new ImageIcon(getClass().getResource("keyword_result.png"));
					label.setIcon(imageIcon);
					label.setText(keywordResult.getChat());
				}
		    }
			
			
			
		

			if (selected) {
				RequestDB requestDB = new RequestDB();
				label.setBackground(backgroundSelectionColor);
				System.out.println("선택됨");
				
				
				
				switch (keywordResult.getCheck()) {
				case "0":
					System.out.println("변경된사항 채팅번호"+keywordResult.getChat_number());
					ImageIcon imageIcon = new ImageIcon(getClass().getResource("keyword_result.png"));
					label.setIcon(imageIcon);
					label.setText(keywordResult.getChat());
					keywordResult.setCheck("1");
					requestDB.changeCheck(keywordResult.getGroupid(), "1", keywordResult.getChat_number());
					tree.repaint();
					System.out.println(1);
					tree.clearSelection();
					break;
				case "1":
					System.out.println("변경된사항 채팅번호"+keywordResult.getChat_number());
					ImageIcon imageIcon2 = new ImageIcon(getClass().getResource("mark.png"));
					label.setIcon(imageIcon2);
					label.setText(keywordResult.getChat());
					keywordResult.setCheck("2");
					requestDB.changeCheck(keywordResult.getGroupid(),"2",keywordResult.getChat_number());
					tree.repaint();
					tree.clearSelection();
					System.out.println(2);
					break;
				case "2":
					System.out.println("변경된사항 채팅번호"+keywordResult.getChat_number());
					ImageIcon imageIcon3 = new ImageIcon(getClass().getResource("keyword_result.png"));
					label.setIcon(imageIcon3);
					label.setText(keywordResult.getChat());
					keywordResult.setCheck("1");
					requestDB.changeCheck(keywordResult.getGroupid(),"1",keywordResult.getChat_number());
					tree.repaint();
					tree.clearSelection();
					System.out.println(1);
					break;
			
				default:
					break;
				}

			} else {
				label.setBackground(backgroundNonSelectionColor);
			}
			
		}

		label.setEnabled(tree.isEnabled());

		return label;
	}
}
