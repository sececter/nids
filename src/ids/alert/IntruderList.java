package ids.alert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import jpcap.packet.Packet;

public class IntruderList {
	JFrame alert;
	JTextArea ta;
	Packet p;

	public IntruderList(String str) {
		// TODO Auto-generated constructor stub
	alert=new JFrame(str);
	ta=new JTextArea();
	alert.add(ta);

	alert.setSize(200,500);
	alert.setVisible(true);


	 try {

         Class.forName("com.mysql.jdbc.Driver");

     } catch (ClassNotFoundException e) {

         System.out.println("Where is your MySQL JDBC Driver?");
         e.printStackTrace();
         return;

     }

     System.out.println("MySQL JDBC Driver Registered!");
     Connection connection = null;

     try {
         connection = DriverManager
                 .getConnection("jdbc:mysql://localhost:3309/signature",
                         "testuser", "test623");

     } catch (SQLException e) {
         System.out.println("Connection Failed! Check output console");
         e.printStackTrace();
         return;
     }
    ta.append("#########################"+"\n");
	ta.append("\n ##MAC##"+"#SOURCE_IP#"+"count#"+"\n" );
	ta.append("\n #########################"+"\n");

     try
	{
     Statement s=connection.createStatement();
     ResultSet rs=s.executeQuery("select * from intruder");
     while(rs.next())
     {
     	ta.append("\n"+rs.getString(1)+"         "+rs.getString(2)+"      "+rs.getString(3)+"\n");


     }
     }
     catch(Exception c)
     {
     	System.out.println("problem with getting ResultSet");
     }


}
}
