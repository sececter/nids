/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Analyzer;

import jpcap.packet.Packet;

/**
 *
 * @author suman
 */
public abstract class JpacketAnalyzer {
    public int layer=DATALINK_LAYER;
	public static int DATALINK_LAYER=0;
	public static int NETWORK_LAYER=1;
	public static int TRANSPORT_LAYER=2;
	public static int APPLICATION_LAYER=3;
	
	public abstract boolean isAnalyzable(Packet packet);//packet checking
	public abstract void analyze(Packet packet);//for analyze packet
	public abstract String getProtocolName();//for packet information or protocol name
	public abstract String[] getValueNames();//packet valuename or component of packet
	public abstract Object getValue(String valueName);//for getting info in packet length per second
	abstract Object getValueAt(int index);
	public abstract Object[] getValues();
}
    

