/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Analyzer;

import java.net.InetAddress;
import java.util.Hashtable;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;

/**
 *
 * @author suman
 */
public class IPv4Analyzer extends JpacketAnalyzer{
    private static final String[] valueNames={"Version(0)",
		"TOS: Priority(1)",
		"TOS: Throughput(2)",
		"TOS: Reliability(3)",
		"Length(4)",
		"Identification(5)",
		"Fragment: Don't Fragment(6)",
		"Fragment: More Fragment(7)",
		"Fragment Offset(8)",
		"Time To Live(9)",
		"Protocol(10)",
		"Source IP(11)",
		"Destination IP(12)"
    };
    private Hashtable values=new Hashtable();
	
public IPv4Analyzer(){
		layer=NETWORK_LAYER;
	}
	
	public boolean isAnalyzable(Packet p){
		if(p instanceof IPPacket && ((IPPacket)p).version==4) return true;
		else return false;
	}
	
	public String getProtocolName(){
		return "IPv4";
	}
	
	public String[] getValueNames(){
		return valueNames;
	}
	
	public void analyze(Packet packet){
		values.clear();
		if(!isAnalyzable(packet))	return;
		IPPacket ip=(IPPacket)packet;
		values.put(valueNames[0],new Integer(4));//ipv4
		values.put(valueNames[1],new Integer(ip.priority));//priority of packet
		values.put(valueNames[2],new Boolean(ip.t_flag));
		values.put(valueNames[3],new Boolean(ip.r_flag));
		values.put(valueNames[4],new Integer(ip.length));
		values.put(valueNames[5],new Integer(ip.ident));//identification
		values.put(valueNames[6],new Boolean(ip.dont_frag));//don't fragment
		values.put(valueNames[7],new Boolean(ip.more_frag));//more fragment
		values.put(valueNames[8],new Integer(ip.offset));
		values.put(valueNames[9],new Integer(ip.hop_limit));
		values.put(valueNames[10],new Integer(ip.protocol));
		values.put(valueNames[11],ip.src_ip.getHostAddress());//src_ip=source ip address
		values.put(valueNames[12],ip.dst_ip.getHostAddress());//dst_ip=destination ip address
		
	}
	
	public Object getValue(String valueName){
		

		return values.get(valueName);
	}
	
	Object getValueAt(int index){
		if(index<0 || index>=valueNames.length) return null;

		if((index==13 && values.get(valueNames[index]) instanceof InetAddress) ||
		   (index==14 && values.get(valueNames[index]) instanceof InetAddress)){
			values.put(valueNames[index],((InetAddress)values.get(valueNames[index])).getHostName());
		}

		return values.get(valueNames[index]);
	}
	
	public Object[] getValues(){
		Object[] v=new Object[valueNames.length];
		
		for(int i=0;i<valueNames.length;i++)
			v[i]=getValueAt(i);
		
		return v;
	}
}
