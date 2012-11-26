/*
 *
 this class is for just comparing the captured packet information to the  exinsting signature of the intuders
*/
package ids.alert;
import java.sql.*;
public class MysqlMatch {

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
                    .getConnection("jdbc:mysql://localhost/addrule",
                            "root", "");

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
}
