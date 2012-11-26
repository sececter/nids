/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Analyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Vector;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;

/**
 *
 * @author suman
 */
public class HTTPAnalyzer extends JpacketAnalyzer{
    private static final String[] valueNames={
		"Method(0)",
		"Header(1)"
	};
	String method;
	Vector headers=new Vector();
	
	public HTTPAnalyzer(){
		layer=APPLICATION_LAYER;
	}
	
	public boolean isAnalyzable(Packet p){
		if(p instanceof TCPPacket &&
		   (((TCPPacket)p).src_port==80 || ((TCPPacket)p).dst_port==80))
			return true;
		else return false;
	}
	
	public String getProtocolName(){
		return "HTTP";
	}
	
	public String[] getValueNames(){
		return valueNames;
	}
	
	public void analyze(Packet p){
		method="";
		headers.removeAllElements();
		if(!isAnalyzable(p)) return;
		
		try{
			BufferedReader in=new BufferedReader(new StringReader(new String(p.data)));
			
			method=in.readLine();
			if(method==null || method.indexOf("HTTP")==-1){
				// this packet doesn't contain HTTP header
				method="Not HTTP Header";
				return;
			}
			
			String l;
			//read headers
			while((l=in.readLine()).length()>0)
				headers.addElement(l);
		}catch(IOException e){}
	}
	
	public Object getValue(String valueName){
		if(valueNames[0].equals(valueName)) return method;
		if(valueNames[1].equals(valueName)) return headers;
		return null;
	}
	
	Object getValueAt(int index){
		if(index==0) return method;
		if(index==1) return headers;
		return null;
	}
	
	public Object[] getValues(){
		Object[] values=new Object[2];
		values[0]=method;
		values[1]=headers;
		
		return values;
	}
}

    

