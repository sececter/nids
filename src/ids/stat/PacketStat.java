package ids.stat;

import java.util.Date;
import java.util.List;
import jpcap.packet.Packet;

public class PacketStat extends JDStatisticsTaker
{
  static final String[] types = { 
    "Total packet #", 
    "Total packet size", 
    "Average packet size", 
    "bits/s", 
    "pkts/s" };

  static final String[] label = { "Value" };
  long numOfPs;
  long sizeOfPs;
  Date first;
  Date last;

  public String getName()
  {
    return "Overall information";
  }

  public void analyze(List<Packet> packets) {
    if (packets.size() > 0) {
      Packet fp = (Packet)packets.get(0); Packet lp = (Packet)packets.get(packets.size() - 1);
      this.first = new Date(fp.sec * 1000L + fp.usec / 1000L);
      this.last = new Date(lp.sec * 1000L + lp.usec / 1000L);
    }

    for (int i = 0; i < packets.size(); i++) {
      this.numOfPs += 1L;
      this.sizeOfPs += ((Packet)packets.get(i)).len;
    }
  }

  public void addPacket(Packet p) {
    if (this.first == null) {
      this.first = new Date(p.sec * 1000L + p.usec / 1000L);
    }
    this.last = new Date(p.sec * 1000L + p.usec / 1000L);

    this.numOfPs += 1L;
    this.sizeOfPs += p.len;
  }
  public String[] getLabels() {
    return label; } 
  public String[] getStatTypes() { return types; }

  public long[] getValues(int index) {
    long[] ret = new long[1];
    switch (index) {
    case 0:
      ret[0] = this.numOfPs;
      return ret;
    case 1:
      ret[0] = this.sizeOfPs;
      return ret;
    case 2:
      if (this.numOfPs == 0L) ret[0] = 0L; else
        ret[0] = (this.sizeOfPs / this.numOfPs);
      return ret;
    case 3:
    case 4:
      if (this.first == null) { ret[0] = 0L;
      } else {
        long sec = (this.last.getTime() - this.first.getTime()) * 1000L;
        if (sec == 0L) ret[0] = 0L;
        else if (index == 3) ret[0] = (this.sizeOfPs * 8L / sec); else
          ret[0] = (this.numOfPs / sec);
      }
      return ret;
    }
    return null;
  }

  public void clear()
  {
    this.numOfPs = 0L; this.sizeOfPs = 0L;
    this.first = null; this.last = null;
  }
}