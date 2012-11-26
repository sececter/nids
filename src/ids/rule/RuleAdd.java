package ids.rule;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
public class RuleAdd implements ActionListener {


	JLabel name,action,protocol,sip,sport,dip,dport,error,option;
	JTextField tname,taction,tprotocol,tsip,tsport,tdip,tdport,toption;
	JButton button;
	JFrame addRuleFrame;



	public RuleAdd(String s) {
		// TODO Auto-generated constructor stub
	addRuleFrame = new JFrame(s);
	// instatntaniate all the component
	name=new JLabel("Rule Name");
	action=new JLabel("Rule Action");
	protocol=new JLabel("Protocol");
	sip=new JLabel("Source IP");
	sport=new JLabel("Source Port");
	dip=new JLabel("Destination IP");
	dport=new JLabel("Destination Port");
	option=new JLabel("Rule Option");
	toption=new JTextField();

	tname=new JTextField();
	taction=new JTextField();
	tprotocol = new JTextField();
	tsip=new JTextField();
	tsport=new JTextField();
	tdip=new JTextField();
	tdport=new JTextField();
	button=new JButton("submit");
	error=new JLabel();

	//set bounds of every component




	//add components to the frame
	addRuleFrame.add(name);     addRuleFrame.add(tname);
	addRuleFrame.add(action);   addRuleFrame.add(taction);
	addRuleFrame.add(protocol); addRuleFrame.add(tprotocol);
	addRuleFrame.add(sip); 	    addRuleFrame.add(tsip);
	addRuleFrame.add(sport); 	addRuleFrame.add(tsport);
	addRuleFrame.add(dip);      addRuleFrame.add(tdip);
	addRuleFrame.add(dport); 	addRuleFrame.add(tdport);
	addRuleFrame.add(option);	addRuleFrame.add(toption);
	addRuleFrame.add(button);	addRuleFrame.add(error);

	button.addActionListener(this);
	addRuleFrame.setVisible(true);
	addRuleFrame.setLayout(new GridLayout(9,2));
	//addRuleFrame.setLayout(null);
	addRuleFrame.setSize(500,250);
	//addRuleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}



	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String rulename=tname.getText();
		String ruleaction=taction.getText();
		String protocol=tprotocol.getText();
		String sip=tsip.getText();
		String sport=tsport.getText();
		String dip=tdip.getText();
		String dport=tdport.getText();
		String ruleoption=toption.getText();
		//error.setText("hi");
		if(new Constrain().check(error,sip,dip,protocol))
		{
				if(new MysqlConnect().addRule(rulename,ruleaction,protocol,sip,sport,dip,dport,ruleoption))
				{
				error.setText("your rule has beeen added successfully");

				}

		}


	}

}
