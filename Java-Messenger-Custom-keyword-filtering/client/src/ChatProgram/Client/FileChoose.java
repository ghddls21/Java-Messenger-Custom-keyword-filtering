package ChatProgram.Client;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class FileChoose {

	String filePath = "";
	public String FileChoose_Start() {
		// TODO Auto-generated constructor stub
		
		JFileChooser chooser = new JFileChooser(); //��ü ����

		
		int ret = chooser.showOpenDialog(null);  //����â ����
		
		if (ret != JFileChooser.APPROVE_OPTION) {

			JOptionPane.showMessageDialog(null, "��θ� ���������ʾҽ��ϴ�.",

			"���", JOptionPane.WARNING_MESSAGE);

			return filePath;
			}else {
				filePath = chooser.getSelectedFile().getPath();  //���ϰ�θ� ������

				System.out.println(filePath);  //���
				
				return filePath;
			}
		}

			 
}
