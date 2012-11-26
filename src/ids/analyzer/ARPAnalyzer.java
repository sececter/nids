package ids.analyzer;

import jpcap.packet.ARPPacket;
import jpcap.packet.Packet;

public class ARPAnalyzer extends JDPacketAnalyzer
{
  private static final String[] valueNames = { 
    "Hardware Type", 
    "Protocol Type", 
    "Hardware Address Length", 
    "Protocol Address Length", 
    "Operation", 
    "Sender Hardware Address", 
    "Sender Protocol Address", 
    "Target Hardware Address", 
    "Target Protocol Address" };
  private ARPPacket arp;

  public ARPAnalyzer()
  {
    this.layer = NETWORK_LAYER;
  }

  public boolean isAnalyzable(Packet p) {
    return p instanceof ARPPacket;
  }

  public String getProtocolName() {
    return "ARP/RARP";
  }

  public String[] getValueNames() {
    return valueNames;
  }

  public void analyze(Packet p) {
    if (!isAnalyzable(p)) return;
    this.arp = ((ARPPacket)p);
  }

  public Object getValue(String valueName) {
    for (int i = 0; i < valueNames.length; i++) {
      if (valueNames[i].equals(valueName))
        return getValueAt(i);
    }
    return null;
  }

  Object getValueAt(int index) {
    switch (index) {
    case 0:
      switch (this.arp.hardtype) { case 1:
        return "Ethernet (" + this.arp.hardtype + ")";
      case 6:
        return "Token ring (" + this.arp.hardtype + ")";
      case 15:
        return "Frame relay (" + this.arp.hardtype + ")"; }
      return new Integer(this.arp.hardtype);
    case 1:
      switch (this.arp.prototype) { case 2048:
        return "IP (" + this.arp.prototype + ")"; }
      return new Integer(this.arp.prototype);
    case 2:
      return new Integer(this.arp.hlen);
    case 3:
      return new Integer(this.arp.plen);
    case 4:
      switch (this.arp.operation) { case 1:
        return "ARP Request";
      case 2:
        return "ARP Reply";
      case 3:
        return "Reverse ARP Request";
      case 4:
        return "Reverse ARP Reply";
      case 8:
        return "Identify peer Request";
      case 9:
        return "Identify peer Reply";
      case 5:
      case 6:
      case 7: } return new Integer(this.arp.operation);
    case 5:
      return this.arp.getSenderHardwareAddress();
    case 6:
      return this.arp.getSenderProtocolAddress();
    case 7:
      return this.arp.getTargetHardwareAddress();
    case 8:
      return this.arp.getTargetProtocolAddress();
    }return null;
  }

  public Object[] getValues()
  {
    Object[] v = new Object[valueNames.length];
    for (int i = 0; i < valueNames.length; i++) {
      v[i] = getValueAt(i);
    }
    return v;
  }
}