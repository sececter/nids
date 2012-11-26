package ids.stat;

import java.util.List;
import ids.JDPacketAnalyzerLoader;
import ids.analyzer.JDPacketAnalyzer;
import jpcap.packet.Packet;

public class NetworkProtocolStat extends JDStatisticsTaker
{
  List<JDPacketAnalyzer> analyzers;
  long[] numOfPs;
  long totalPs;
  long[] sizeOfPs;
  long totalSize;
  String[] labels;
  static final String[] types = { "# of packets", "% of packets", "total packet size", "% of size" };

  public NetworkProtocolStat() {
    this.analyzers = JDPacketAnalyzerLoader.getAnalyzersOf(JDPacketAnalyzer.NETWORK_LAYER);
    this.numOfPs = new long[this.analyzers.size() + 1];
    this.sizeOfPs = new long[this.analyzers.size() + 1];

    this.labels = new String[this.analyzers.size() + 1];
    for (int i = 0; i < this.analyzers.size(); i++)
      this.labels[i] = ((JDPacketAnalyzer)this.analyzers.get(i)).getProtocolName();
    this.labels[this.analyzers.size()] = "Other";
  }

  public String getName() {
    return "Netowrk Layer Protocol Ratio";
  }

  public void analyze(List<Packet> packets) {
    for (int i = 0; i < packets.size(); i++) {
      Packet p = (Packet)packets.get(i);
      this.totalPs += 1L;
      this.totalSize += p.len;

      boolean flag = false;
      for (int j = 0; j < this.analyzers.size(); j++)
        if (((JDPacketAnalyzer)this.analyzers.get(j)).isAnalyzable(p)) {
          this.numOfPs[j] += 1L;
          this.totalPs += 1L;
          this.sizeOfPs[j] += p.len;
          flag = true;
          break;
        }
      if (!flag) {
        this.numOfPs[(this.numOfPs.length - 1)] += 1L;
        this.sizeOfPs[(this.sizeOfPs.length - 1)] += p.len;
      }
    }
  }

  public void addPacket(Packet p) {
    boolean flag = false;
    this.totalPs += 1L;
    this.totalSize += p.len;
    for (int j = 0; j < this.analyzers.size(); j++)
      if (((JDPacketAnalyzer)this.analyzers.get(j)).isAnalyzable(p)) {
        this.numOfPs[j] += 1L;
        this.sizeOfPs[j] += p.len;
        flag = true;
        break;
      }
    if (!flag) {
      this.numOfPs[(this.numOfPs.length - 1)] += 1L;
      this.sizeOfPs[(this.sizeOfPs.length - 1)] += p.len;
    }
  }

  public String[] getLabels() {
    return this.labels;
  }

  public String[] getStatTypes() {
    return types;
  }

  public long[] getValues(int index) {
    switch (index) {
    case 0:
      if (this.numOfPs == null) return new long[0];
      return this.numOfPs;
    case 1:
      long[] percents = new long[this.numOfPs.length];
      if (this.totalPs == 0L) return percents;
      for (int i = 0; i < this.numOfPs.length; i++)
        percents[i] = (this.numOfPs[i] * 100L / this.totalPs);
      return percents;
    case 2:
      if (this.sizeOfPs == null) return new long[0];
      return this.sizeOfPs;
    case 3:
      long[] percents2 = new long[this.sizeOfPs.length];
      if (this.totalSize == 0L) return percents2;
      for (int i = 0; i < this.sizeOfPs.length; i++)
        percents2[i] = (this.sizeOfPs[i] * 100L / this.totalSize);
      return percents2;
    }
    return null;
  }

  public void clear()
  {
    this.numOfPs = new long[this.analyzers.size() + 1];
    this.sizeOfPs = new long[this.analyzers.size() + 1];
    this.totalPs = 0L;
    this.totalSize = 0L;
  }
}