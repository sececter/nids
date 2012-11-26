package ids.stat;

import java.util.List;
import jpcap.packet.Packet;

public class FreeMemStat extends JDStatisticsTaker
{
  String[] labels = { "Free Memory" };
  String[] types = { "Bytes" };

  public String getName() {
    return "Free Memory";
  }
  public void analyze(List<Packet> packets) {
  }
  public void addPacket(Packet p) {
  }
  public String[] getLabels() {
    return this.labels;
  }

  public String[] getStatTypes() {
    return this.types;
  }

  public long[] getValues(int index) {
    long[] ret = new long[1];
    ret[0] = Runtime.getRuntime().freeMemory();
    return ret;
  }

  public void clear()
  {
  }
}