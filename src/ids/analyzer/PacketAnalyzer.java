package ids.analyzer;

import java.util.Date;
import jpcap.packet.Packet;

public class PacketAnalyzer extends JDPacketAnalyzer
{
  private static final String[] valueNames = { "Captured Time", "Captured Length" };
  private Packet packet;

  public boolean isAnalyzable(Packet packet)
  {
    return true;
  }

  public String getProtocolName() {
    return "Packet Information";
  }

  public String[] getValueNames() {
    return valueNames;
  }

  public void analyze(Packet p) {
    this.packet = p;
  }

  public Object getValue(String name) {
    if (name.equals(valueNames[0]))
      return new Date(this.packet.sec * 1000L + this.packet.usec / 1000L).toString();
    if (name.equals(valueNames[1]))
      return new Integer(this.packet.caplen);
    return null;
  }

  Object getValueAt(int index) {
    switch (index) { case 0:
      return new Date(this.packet.sec * 1000L + this.packet.usec / 1000L).toString();
    case 1:
      return new Integer(this.packet.caplen); }
    return null;
  }

  public Object[] getValues()
  {
    Object[] v = new Object[2];
    v[0] = new Date(this.packet.sec * 1000L + this.packet.usec / 1000L).toString();
    v[1] = new Integer(this.packet.caplen);

    return v;
  }
}