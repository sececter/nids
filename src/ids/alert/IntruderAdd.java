package ids.alert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class IntruderAdd {


	public IntruderAdd(String src)
	{
		String mac="******";
		String count="**";
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
	                .getConnection("jdbc:mysql://localhost/addrule",
	                        "root", "");

	    } catch (SQLException e) {
	        System.out.println("Connection Failed! Check output console");
	        e.printStackTrace();

	    }
	    try {
//	 Statement s=connection.createStatement();

	    PreparedStatement ps=connection.prepareStatement("insert into intruder values (?,?,?)");
	    ps.setString(1,mac);
	    ps.setString(2,src);
	    ps.setString(3,count);
	    ps.executeUpdate();
	    ps.close();

	    }
	    catch(Exception e)
	    {
	    	System.out.println("error in prepared statement");
	    }

	}


	}




