package ids.analyzer;

import java.util.Hashtable;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;

public class TCPAnalyzer extends JDPacketAnalyzer
{
  private static final String[] valueNames = { 
    "Source Port", 
    "Destination Port", 
    "Sequence Number", 
    "Ack Number", 
    "URG Flag", 
    "ACK Flag", 
    "PSH Flag", 
    "RST Flag", 
    "SYN Flag", 
    "FIN Flag", 
    "Window Size" };

  Hashtable values = new Hashtable();

  public TCPAnalyzer() {
    this.layer = TRANSPORT_LAYER;
  }

  public boolean isAnalyzable(Packet p) {
    return p instanceof TCPPacket;
  }

  public String getProtocolName() {
    return "TCP";
  }

  public String[] getValueNames() {
    return valueNames;
  }

  public void analyze(Packet p) {
    this.values.clear();
    if (!isAnalyzable(p)) return;
    TCPPacket tcp = (TCPPacket)p;
    this.values.put(valueNames[0], new Integer(tcp.src_port));
    this.values.put(valueNames[1], new Integer(tcp.dst_port));
    this.values.put(valueNames[2], new Long(tcp.sequence));
    this.values.put(valueNames[3], new Long(tcp.ack_num));
    this.values.put(valueNames[4], new Boolean(tcp.urg));
    this.values.put(valueNames[5], new Boolean(tcp.ack));
    this.values.put(valueNames[6], new Boolean(tcp.psh));
    this.values.put(valueNames[7], new Boolean(tcp.rst));
    this.values.put(valueNames[8], new Boolean(tcp.syn));
    this.values.put(valueNames[9], new Boolean(tcp.fin));
    this.values.put(valueNames[10], new Integer(tcp.window));
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