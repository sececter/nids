package ids.analyzer;

import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;

public class FTPAnalyzer extends JDPacketAnalyzer
{
  public FTPAnalyzer()
  {
    this.layer = APPLICATION_LAYER;
  }

  public boolean isAnalyzable(Packet p)
  {
    return ((p instanceof TCPPacket)) && (
      (((TCPPacket)p).src_port == 20) || (((TCPPacket)p).dst_port == 20) || 
      (((TCPPacket)p).src_port == 21) || (((TCPPacket)p).dst_port == 21));
  }

  public String getProtocolName()
  {
    return "FTP";
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