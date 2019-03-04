package ChatProgram.Client;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ComboRenderer extends JLabel implements ListCellRenderer<Combo_Keyword_Info>{
	 String groupid = "";
	 String group_name = "";
	 public ComboRenderer() {
		// TODO Auto-generated constructor stub
		setOpaque(true);
	}
	  @Override
	    public Component getListCellRendererComponent(JList<? extends Combo_Keyword_Info> list, Combo_Keyword_Info combo_Keyword_Info, int index,
	        boolean isSelected, boolean cellHasFocus) {
	          
	        groupid = combo_Keyword_Info.getGroupid();
	        group_name = combo_Keyword_Info.getGroup_name();
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