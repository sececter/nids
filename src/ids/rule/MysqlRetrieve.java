package ids.rule;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
public class MysqlRetrieve {
	JFrame frame;
	JTextArea ta ;


			public MysqlRetrieve(String name)
			{
				// frame attributes

				frame=new JFrame();
				frame.setSize(600,500);
				frame.setTitle(name);
				frame.setLocation(200,100);
				frame.setVisible(true);

				//frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
				ta = new JTextArea();
				frame.add(ta);


				// Connectivity to MySql Database

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
		                    .getConnection("jdbc:mysql://localhost/addrule",
		                            "root", "");

		        } catch (SQLException e) {
		            System.out.println("Connection Failed! Check output console");
		            e.printStackTrace();
		            return;
		        }
			ta.append("Rule_Name   " + "Rule_Action" + "Protocol" + " SOURCE_IP" + "S_PORT" + "Dest_IP" + "D_PORT" + "RULE_OPTION  ");

		        try
			{
		        Statement s=connection.createStatement();
		        ResultSet rs=s.executeQuery("select * from rule");
		        while(rs.next())
		        {
		        	ta.append("\n"+rs.getString(1)+"         "+rs.getString(2)+"      "+rs.getString(3)+"      "+rs.getString(4)+"    "+rs.getString(5)+"      "+rs.getString(6)+"      "+rs.getString(7)+"    "+rs.getString(8)+"\n");


		        }
		        }
		        catch(Exception c)
		        {
		        	System.out.println("problem with getting ResultSet");
		        }




}

/*public static void main(String args[]){
new MysqlRetrieve("jjjjjjj");
}*/
}
