/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Analyzer;

import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;

/**
 *
 * @author suman
 */
public class SMTPAnalyzer extends JpacketAnalyzer {
    
public SMTPAnalyzer()
{
		layer=APPLICATION_LAYER;
	}
	
	public boolean isAnalyzable(Packet p){
		if(p instanceof TCPPacket &&
		   (((TCPPacket)p).src_port==25 || ((TCPPacket)p).dst_port==25))
			return true;
		else return false;
	}
	
	public String getProtocolName(){
		return "SMTP";
	}
	
	public String[] getValueNames(){return null;}
	
	public void analyze(Packet p){}
	
	public Object getValue(String s){ return null; }
	public Object getValueAt(int i){ return null; }
	public Object[] getValues(){ return null; }
}

