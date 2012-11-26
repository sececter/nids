package ids.analyzer;

import java.io.PrintStream;
import java.net.InetAddress;
import java.util.Hashtable;
import java.util.Map;
import ids.JDCaptor;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;

public class IPv4Analyzer extends JDPacketAnalyzer
{
  private static final String[] valueNames = { "Version", 
    "TOS: Priority", 
    "TOS: Throughput", 
    "TOS: Reliability", 
    "Length", 
    "Identification", 
    "Fragment: Don't Fragment", 
    "Fragment: More Fragment", 
    "Fragment Offset", 
    "Time To Live", 
    "Protocol", 
    "Source IP", 
    "Destination IP", 
    "Source Host Name", 
    "Destination Host Name" };

  private Hashtable values = new Hashtable();

  public IPv4Analyzer() {
    this.layer = NETWORK_LAYER;
  }

  public boolean isAnalyzable(Packet p) {
    return ((p instanceof IPPacket)) && (((IPPacket)p).version == 4);
  }

  public String getProtocolName()
  {
    return "IPv4";
  }

  public String[] getValueNames() {
    return valueNames;
  }

  public void analyze(Packet packet) {
    this.values.clear();
    if (!isAnalyzable(packet)) return;
    IPPacket ip = (IPPacket)packet;
    this.values.put(valueNames[0], new Integer(4));
    this.values.put(valueNames[1], new Integer(ip.priority));
    this.values.put(valueNames[2], new Boolean(ip.t_flag));
    this.values.put(valueNames[3], new Boolean(ip.r_flag));
    this.values.put(valueNames[4], new Integer(ip.length));
    this.values.put(valueNames[5], new Integer(ip.ident));
    this.values.put(valueNames[6], new Boolean(ip.dont_frag));
    this.values.put(valueNames[7], new Boolean(ip.more_frag));
    this.values.put(valueNames[8], new Integer(ip.offset));
    this.values.put(valueNames[9], new Integer(ip.hop_limit));
    this.values.put(valueNames[10], new Integer(ip.protocol));
    this.values.put(valueNames[11], ip.src_ip.getHostAddress());
    this.values.put(valueNames[12], ip.dst_ip.getHostAddress());
    this.values.put(valueNames[13], ip.src_ip);
    this.values.put(valueNames[14], ip.dst_ip);
  }

  public Object getValue(String valueName) {
    if (((valueNames[13].equals(valueName)) && ((this.values.get(valueName) instanceof InetAddress))) || (
      (valueNames[14].equals(valueName)) && ((this.values.get(valueName) instanceof InetAddress))))
    {
      InetAddress addr = (InetAddress)this.values.get(valueName);
      if (JDCaptor.hostnameCache.containsKey(addr)) {
        this.values.put(valueName, JDCaptor.hostnameCache.get(addr));
      } else {
        this.values.put(valueName, addr.getHostName());
        System.out.println("miss");
      }
    }

    return this.values.get(valueName);
  }

  Object getValueAt(int index) {
    if ((index < 0) || (index >= valueNames.length)) return null;

    return getValue(valueNames[index]);
  }

  public Object[] getValues() {
    Object[] v = new Object[valueNames.length];

    for (int i = 0; i < valueNames.length; i++) {
      v[i] = getValueAt(i);
    }
    return v;
  }
}