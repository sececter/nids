package ids.rule;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;

public class MysqlConnect {

public boolean addRule(String rulename, String ruleaction, String protocol, String sip, String sport, String dip, String dport,String ruleoption) {
// TODO Auto-generated method stub





//	 Connectivity to MySql Database

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
// Statement s=connection.createStatement();

    PreparedStatement ps=connection.prepareStatement("insert into rule values (?,?,?,?,?,?,?,?)");
    ps.setString(1,rulename);
    ps.setString(2,ruleaction);
    ps.setString(3,protocol);
    ps.setString(4,sip);
    ps.setString(5,sport);
    ps.setString(6,dip);
    ps.setString(7,dport);
    ps.setString(8,ruleoption);
    ps.executeUpdate();
    ps.close();

    }
    catch(Exception e)
    {
    	System.out.println("error in prepared statement");
    }
	return true;
}

}
