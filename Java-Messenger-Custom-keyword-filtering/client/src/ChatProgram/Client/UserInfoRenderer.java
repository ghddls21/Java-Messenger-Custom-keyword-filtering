package ChatProgram.Client;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class UserInfoRenderer extends JLabel implements ListCellRenderer<UserInfo>{
	
	 public UserInfoRenderer() {
		// TODO Auto-generated constructor stub
		setOpaque(true);
	}
	  @Override
	    public Component getListCellRendererComponent(JList<? extends UserInfo> list, UserInfo userinfo, int index,
	        boolean isSelected, boolean cellHasFocus) {
	          
	        String id = userinfo.getId();
	        Boolean status = userinfo.getStatus();
	        String st="";
	        if(status == false) {
	        	st = "offline";
	        }
	        else if(status == true)
	        {
	        	st = "online";
	        }
	        ImageIcon imageIcon = new ImageIcon(getClass().getResource( st + ".png"));
	         
	        setIcon(imageIcon);
	        setText(id);
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
