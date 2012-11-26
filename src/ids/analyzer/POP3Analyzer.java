package ids.analyzer;

import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;

public class POP3Analyzer extends JDPacketAnalyzer
{
  public POP3Analyzer()
  {
    this.layer = APPLICATION_LAYER;
  }

  public boolean isAnalyzable(Packet p)
  {
    return ((p instanceof TCPPacket)) && (
      (((TCPPacket)p).src_port == 110) || (((TCPPacket)p).dst_port == 110));
  }

  public String getProtocolName()
  {
    return "POP3";
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