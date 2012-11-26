/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Analyzer;

import jpcap.packet.EthernetPacket;
import jpcap.packet.Packet;

/**
 *
 * @author suman
 */
public class EthernetAnalyzer extends JpacketAnalyzer{
    
    
	private static final String[] valueNames={
		"Frame Type(0)",
		"Source MAC(1)",
		"Destination MAC(2)"
	};
	private EthernetPacket eth;

	public EthernetAnalyzer(){
		layer=DATALINK_LAYER;
	}
	
	public boolean isAnalyzable(Packet p){
		return (p.datalink!=null && p.datalink instanceof EthernetPacket);
	}

	public String getProtocolName(){
		return "Ethernet Frame";
	}

	public String[] getValueNames(){
		return valueNames;
	}

	public void analyze(Packet p){
		if(!isAnalyzable(p)) return;
		eth=(EthernetPacket)p.datalink;
	}

	public Object getValue(String valueName){
		for(int i=0;i<valueNames.length;i++)
			if(valueNames[i].equals(valueName))
				return getValueAt(i);

		return null;
	}

	Object getValueAt(int index){
		switch(index){
		case 0: return new Integer(eth.frametype);
		case 1: return eth.getSourceAddress();
		case 2: return eth.getDestinationAddress();
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

