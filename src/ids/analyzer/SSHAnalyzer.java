package ids.analyzer;

import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;

public class SSHAnalyzer extends JDPacketAnalyzer
{
  public SSHAnalyzer()
  {
    this.layer = APPLICATION_LAYER;
  }

  public boolean isAnalyzable(Packet p)
  {
    return ((p instanceof TCPPacket)) && (
      (((TCPPacket)p).src_port == 22) || (((TCPPacket)p).dst_port == 22));
  }

  public String getProtocolName()
  {
    return "SSH";
  }
  public String[] getValueNames() {
    return null;
  }
  public void analyze(Packet p) {
  }
  public Object getValue(String s) { return null; } 
  public Object getValueAt(int i) { return null; } 
  public Object[] getValues() { return null;
  }
}