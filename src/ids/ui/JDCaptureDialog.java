package ids.ui;
import jpcap.*;
import  jpcap.packet.*;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.packet.ICMPPacket;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;
import jpcap.packet.UDPPacket;
import ids.alert.*;

public class JDCaptureDialog extends JDialog
  implements ActionListener
{
  static JpcapCaptor jpcap = null;
  NetworkInterface[] devices;
  JComboBox adapterComboBox;
  JTextField filterField;
  JTextField caplenField;
  JRadioButton wholeCheck;
  JRadioButton headCheck;
  JRadioButton userCheck;
  JCheckBox promiscCheck;
  static String protocol;

  public JDCaptureDialog(JFrame parent)
  {
    super(parent, "Choose Device and Options", true);

    this.devices = JpcapCaptor.getDeviceList();
    if (this.devices == null) {
      JOptionPane.showMessageDialog(parent, "No device found.");
      dispose();
      return;
    }
    String[] names = new String[this.devices.length];
    for (int i = 0; i < names.length; i++)
      names[i] = (this.devices[i].description == null ? this.devices[i].name : this.devices[i].description);
    this.adapterComboBox = new JComboBox(names);

    JPanel adapterPane = new JPanel();
    adapterPane.add(this.adapterComboBox);
    adapterPane.setBorder(BorderFactory.createTitledBorder("Choose capture device"));
    adapterPane.setAlignmentX(0.0F);

    this.promiscCheck = new JCheckBox("Put into promiscuous mode");
    this.promiscCheck.setSelected(true);
    this.promiscCheck.setAlignmentX(0.0F);

    this.filterField = new JTextField(20);

    JPanel filterPane = new JPanel();
    filterPane.add(new JLabel("Filter"));
    filterPane.add(this.filterField);
    filterPane.setBorder(BorderFactory.createTitledBorder("Capture filter"));
    filterPane.setAlignmentX(0.0F);

    JPanel caplenPane = new JPanel();
    caplenPane.setLayout(new BoxLayout(caplenPane, 1));
    this.caplenField = new JTextField("1514");
    this.caplenField.setEnabled(false);
    this.caplenField.setMaximumSize(new Dimension(32767, 20));
    this.wholeCheck = new JRadioButton("Whole packet");
    this.wholeCheck.setSelected(true);
    this.wholeCheck.setActionCommand("Whole");
    this.wholeCheck.addActionListener(this);
    this.headCheck = new JRadioButton("Header only");
    this.headCheck.setActionCommand("Head");
    this.headCheck.addActionListener(this);
    this.userCheck = new JRadioButton("Other");
    this.userCheck.setActionCommand("Other");
    this.userCheck.addActionListener(this);
    ButtonGroup group = new ButtonGroup();
    group.add(this.wholeCheck);
    group.add(this.headCheck);
    group.add(this.userCheck);
    caplenPane.add(this.caplenField);
    caplenPane.add(this.wholeCheck);
    caplenPane.add(this.headCheck);
    caplenPane.add(this.userCheck);
    caplenPane.setBorder(BorderFactory.createTitledBorder("Max capture length"));
    caplenPane.setAlignmentX(1.0F);

    JPanel buttonPane = new JPanel(new FlowLayout(2));
    JButton okButton = new JButton("OK");
    okButton.setActionCommand("OK");
    okButton.addActionListener(this);
    JButton cancelButton = new JButton("Cancel");
    cancelButton.setActionCommand("Cancel");
    cancelButton.addActionListener(this);
    buttonPane.add(okButton);
    buttonPane.add(cancelButton);
    buttonPane.setAlignmentX(1.0F);

    JPanel westPane = new JPanel(); JPanel eastPane = new JPanel();
    westPane.setLayout(new BoxLayout(westPane, 1));
    westPane.add(Box.createRigidArea(new Dimension(5, 5)));
    westPane.add(adapterPane);
    westPane.add(Box.createRigidArea(new Dimension(0, 10)));
    westPane.add(this.promiscCheck);
    westPane.add(Box.createRigidArea(new Dimension(0, 10)));
    westPane.add(filterPane);
    westPane.add(Box.createVerticalGlue());
    eastPane.add(Box.createRigidArea(new Dimension(5, 5)));
    eastPane.setLayout(new BoxLayout(eastPane, 1));
    eastPane.add(caplenPane);
    eastPane.add(Box.createRigidArea(new Dimension(5, 30)));
    eastPane.add(buttonPane);
    eastPane.add(Box.createRigidArea(new Dimension(5, 5)));

    getContentPane().setLayout(new BoxLayout(getContentPane(), 0));
    getContentPane().add(Box.createRigidArea(new Dimension(10, 10)));
    getContentPane().add(westPane);
    getContentPane().add(Box.createRigidArea(new Dimension(10, 10)));
    getContentPane().add(eastPane);
    getContentPane().add(Box.createRigidArea(new Dimension(10, 10)));
    pack();

    setLocation(parent.getLocation().x + 100, parent.getLocation().y + 100);
  }

  public void actionPerformed(ActionEvent evt) {
    String cmd = evt.getActionCommand();

    if (cmd.equals("Whole")) {
      this.caplenField.setText("1514");
      this.caplenField.setEnabled(false);
    } else if (cmd.equals("Head")) {
      this.caplenField.setText("68");
      this.caplenField.setEnabled(false);
    } else if (cmd.equals("Other")) {
      this.caplenField.setText("");
      this.caplenField.setEnabled(true);
      this.caplenField.requestFocus();
    } else if (cmd.equals("OK")) {
      try {
        int caplen = Integer.parseInt(this.caplenField.getText());
        if ((caplen < 68) || (caplen > 1514)) {
          JOptionPane.showMessageDialog(null, "Capture length must be between 68 and 1514.");
          return;
        }
        //int caplen;
        jpcap = JpcapCaptor.openDevice(this.devices[this.adapterComboBox.getSelectedIndex()], caplen,
          this.promiscCheck.isSelected(), 50);
    //    capture(jpcap);

        if ((this.filterField.getText() != null) && (this.filterField.getText().length() > 0))
          jpcap.setFilter(this.filterField.getText(), true);
      }
      catch (NumberFormatException e) {
      }
      catch (IOException e) {
        JOptionPane.showMessageDialog(null, e.toString());
      }
      finally {
        dispose();
      }return;

      //dispose();
    }
    else if (cmd.equals("Cancel")) {
      dispose();
    }
  }


   private void capture(JpcapCaptor jpcap) {
	// TODO Auto-generated method stub



	  while(true)
	  {

		  Packet p=jpcap.getPacket();
		   IPPacket ip=(IPPacket)p;
		   String src=(ip.src_ip).getHostAddress();
		   String dst=(ip.dst_ip).getHostAddress();

		  if((p instanceof TCPPacket) && ((((TCPPacket)p).src_port == 80) || (((TCPPacket)p).dst_port == 80)))
		  {
			      protocol="http";
		  }
		  else if(((p instanceof TCPPacket)) && ((((TCPPacket)p).src_port == 20) || (((TCPPacket)p).dst_port == 20) ||(((TCPPacket)p).src_port == 21) || (((TCPPacket)p).dst_port == 21)))
		  {
			  	  protocol="ftp";

		  }
		  else if(p instanceof ICMPPacket)
		  {
			  	protocol="icmp";

		  }
		  else if((p instanceof TCPPacket)&& ((((TCPPacket)p).src_port == 23) || (((TCPPacket)p).dst_port == 23)))
		  {
			  protocol="telnet";

		  }
		  else if((p instanceof TCPPacket)&&((((TCPPacket)p).src_port == 22) || (((TCPPacket)p).dst_port == 22)))
		  {
			  protocol="ssh";
		  }
		  else if(((p instanceof TCPPacket)) && ((((TCPPacket)p).src_port == 25) || (((TCPPacket)p).dst_port == 25)))
		  {
			  protocol="smtp";

		  }
		  else if(((p instanceof TCPPacket)) && ((((TCPPacket)p).src_port == 110) || (((TCPPacket)p).dst_port == 110)))
		  {

			  protocol="pop3";
		  }


		  {
		  try {
			if(match(src,dst,protocol))
				  {
					new ids.alert.GenerateAlert(src);
					new ids.alert.IntruderAdd(src);

				  }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  }

		  }




	  }



  // method to match with mysql database record

   boolean match(String source,String destination,String protocol) throws Exception
	{
		try {

           Class.forName("com.mysql.jdbc.Driver");

       } catch (ClassNotFoundException e) {

           System.out.println("Where is your MySQL JDBC Driver?");
           e.printStackTrace();


       }

       System.out.println("MySQL JDBC Driver Registered!");
       Connection connection = null;

       try {
           connection = DriverManager
                   .getConnection("jdbc:mysql://localhost:3309/testdb",
                           "testuser", "test623");

       } catch (SQLException e) {
           System.out.println("Connection Failed! Check output console");
           e.printStackTrace();

       }
//preparing readymade query for matching...

       PreparedStatement ps=connection.prepareStatement("select * from rule where protocol=? and sip=? and dip=? ");
       ps.setString(1,protocol);
       ps.setString(2,source);
       ps.setString(2,destination);
       if( ps.executeUpdate()==1)
       {

       	return true;
       }
       else
       {
       return false;

       }

	}




public static JpcapCaptor getJpcap(JFrame parent) {
    new JDCaptureDialog(parent).setVisible(true);
    return jpcap;
  }
}