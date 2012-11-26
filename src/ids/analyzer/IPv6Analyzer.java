package ids.analyzer;

import java.net.InetAddress;
import java.util.Hashtable;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;

public class IPv6Analyzer extends JDPacketAnalyzer
{
  private static final String[] valueNames = { 
    "Version", 
    "Class", 
    "Flow Label", 
    "Length", 
    "Protocol", 
    "Hop Limit", 
    "Source IP", 
    "Destination IP", 
    "Source Host Name", 
    "Destination Host Name" };

  Hashtable values = new Hashtable();

  public IPv6Analyzer() {
    this.layer = NETWORK_LAYER;
  }

  public boolean isAnalyzable(Packet p) {
    return ((p instanceof IPPacket)) && (((IPPacket)p).version == 6);
  }

  public String getProtocolName()
  {
    return "IPv6";
  }

  public String[] getValueNames() {
    return valueNames;
  }

  public void analyze(Packet packet) {
    this.values.clear();
    if (!isAnalyzable(packet)) return;
    IPPacket ip = (IPPacket)packet;
    this.values.put(valueNames[0], new Integer(6));
    this.values.put(valueNames[1], new Integer(ip.priority));
    this.values.put(valueNames[2], new Integer(ip.flow_label));
    this.values.put(valueNames[3], new Integer(ip.length));
    this.values.put(valueNames[4], new Integer(ip.protocol));
    this.values.put(valueNames[5], new Integer(ip.hop_limit));
    this.values.put(valueNames[6], ip.src_ip.getHostAddress());
    this.values.put(valueNames[7], ip.dst_ip.getHostAddress());
    this.values.put(valueNames[8], ip.src_ip.getHostName());
    this.values.put(valueNames[9], ip.dst_ip.getHostName());
  }

  public Object getValue(String valueName) {
    return this.values.get(valueName);
  }

  Object getValueAt(int index) {
    if ((index < 0) || (index >= valueNames.length)) return null;
    return this.values.get(valueNames[index]);
  }

  public Object[] getValues() {
    Object[] v = new Object[valueNames.length];

    for (int i = 0; i < valueNames.length; i++) {
      v[i] = this.values.get(valueNames[i]);
    }
    return v;
  }
}