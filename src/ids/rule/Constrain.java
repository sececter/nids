package ids.rule;

import javax.swing.JLabel;

public class Constrain {

	boolean check(JLabel error,String sip,String dip,String protocol)
	{
		if(checkIP(sip))
		{
			error.setText("Please enter the correct source IP");
		}
		else if(checkIP(dip))
		{
			error.setText("Please enter the correct Destination IP");
		}
		else if(proto(protocol))
		{
			error.setText("Please enter the correct protocol");
		}
		return true;
	}
	boolean checkIP(String ip){

		return false;
	}
	boolean proto(String protocol){


		return false;
	}

}
