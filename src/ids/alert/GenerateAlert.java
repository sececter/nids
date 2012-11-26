package ids.alert;
import javax.swing.*;

import jpcap.packet.Packet;
public class GenerateAlert {

	//public

	public GenerateAlert(String str) {
		// TODO Auto-generated constructor stub
		JFrame f=new JFrame("alert");
		JOptionPane.showMessageDialog(f, str);
		f.setVisible(true);


	}


/*public static void main (String args[])
{
new GenerateAlert("Intruders");
}
*/
}