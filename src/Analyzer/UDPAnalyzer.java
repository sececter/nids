/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Analyzer;

import jpcap.packet.Packet;
import jpcap.packet.UDPPacket;

/**
 *
 * @author suman
 */
public class UDPAnalyzer extends JpacketAnalyzer{
    private static final String[] valueNames={
		"Source Port(0)",
		"Destination Port(1)",
		"Packet Length(2)"
	};
	private UDPPacket udp;
	
	public UDPAnalyzer(){
		layer=TRANSPORT_LAYER;
	}
	
	public boolean isAnalyzable(Packet p){
		return (p instanceof UDPPacket);
	}
	
	public String getProtocolName(){
		return "UDP";
	}
	
	public String[] getValueNames(){
		return valueNames;
	}
	
	public void analyze(Packet p){
		if(!isAnalyzable(p)) return;
		udp=(UDPPacket)p;
	}
	
	public Object getValue(String valueName){
		for(int i=0;i<valueNames.length;i++)
			if(valueNames[i].equals(valueName))
				return getValueAt(i);
		
		return null;
	}
	
	public Object getValueAt(int index){
		switch(index){
			case 0: return new Integer(udp.src_port);
			case 1: return new Integer(udp.dst_port);
			case 2: return new Integer(udp.length);
			default: return null;
		}
	}
	
	public Object[] getValues(){
		Object[] v=new Object[3];
		for(int i=0;i<3;i++)
			v[i]=getValueAt(i);
		
		return v;
	}
}

    

