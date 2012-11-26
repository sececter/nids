package ids.analyzer;

import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;

public class SMTPAnalyzer extends JDPacketAnalyzer
{
  public SMTPAnalyzer()
  {
    this.layer = APPLICATION_LAYER;
  }

  public boolean isAnalyzable(Packet p)
  {
    return ((p instanceof TCPPacket)) && (
      (((TCPPacket)p).src_port == 25) || (((TCPPacket)p).dst_port == 25));
  }

  public String getProtocolName()
  {
    return "SMTP";
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