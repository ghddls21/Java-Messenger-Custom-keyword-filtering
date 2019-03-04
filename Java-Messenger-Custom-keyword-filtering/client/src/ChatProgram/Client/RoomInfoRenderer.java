package ChatProgram.Client;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class RoomInfoRenderer extends JLabel implements ListCellRenderer<RoomInfo>{
	 String groupid = "";
	 String group_name = "";
	 public RoomInfoRenderer() {
		// TODO Auto-generated constructor stub
		setOpaque(true);
	}
	  @Override
	    public Component getListCellRendererComponent(JList<? extends RoomInfo> list, RoomInfo roomInfo, int index,
	        boolean isSelected, boolean cellHasFocus) {
	          
	        groupid = roomInfo.getGroupid();
	        group_name = roomInfo.getGroup_name();
	        this.setText(group_name);
	        if (isSelected) {
	            setBackground(list.getSelectionBackground());
	            setForeground(list.getSelectionForeground());
	        } else {
	            setBackground(list.getBackground());
	            setForeground(list.getForeground());
	        }
	        return this;
	        
	        
	    }
	     
}
