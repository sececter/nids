package ids.analyzer;

import jpcap.packet.EthernetPacket;
import jpcap.packet.Packet;

public class EthernetAnalyzer extends JDPacketAnalyzer
{
  private static final String[] valueNames = { 
    "Frame Type", 
    "Source MAC", 
    "Destination MAC" };
  private EthernetPacket eth;

  public EthernetAnalyzer()
  {
    this.layer = DATALINK_LAYER;
  }

  public boolean isAnalyzable(Packet p) {
    return (p.datalink != null) && ((p.datalink instanceof EthernetPacket));
  }

  public String getProtocolName() {
    return "Ethernet Frame";
  }

  public String[] getValueNames() {
    return valueNames;
  }

  public void analyze(Packet p) {
    if (!isAnalyzable(p)) return;
    this.eth = ((EthernetPacket)p.datalink);
  }

  public Object getValue(String valueName) {
    for (int i = 0; i < valueNames.length; i++) {
      if (valueNames[i].equals(valueName))
        return getValueAt(i);
    }
    return null;
  }

  Object getValueAt(int index) {
    switch (index) { case 0:
      return new Integer(this.eth.frametype);
    case 1:
      return this.eth.getSourceAddress();
    case 2:
      return this.eth.getDestinationAddress(); }
    return null;
  }

  public Object[] getValues()
  {
    Object[] v = new Object[3];
    for (int i = 0; i < 3; i++) {
      v[i] = getValueAt(i);
    }
    return v;
  }
}