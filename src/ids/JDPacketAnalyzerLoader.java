package ids;

import java.util.ArrayList;
import java.util.List;
import ids.analyzer.ARPAnalyzer;
import ids.analyzer.EthernetAnalyzer;
import ids.analyzer.FTPAnalyzer;
import ids.analyzer.HTTPAnalyzer;
import ids.analyzer.ICMPAnalyzer;
import ids.analyzer.IPv4Analyzer;
import ids.analyzer.IPv6Analyzer;
import ids.analyzer.JDPacketAnalyzer;
import ids.analyzer.POP3Analyzer;
import ids.analyzer.PacketAnalyzer;
import ids.analyzer.SMTPAnalyzer;
import ids.analyzer.SSHAnalyzer;
import ids.analyzer.TCPAnalyzer;
import ids.analyzer.TelnetAnalyzer;
import ids.analyzer.UDPAnalyzer;

public class JDPacketAnalyzerLoader
{
  static List<JDPacketAnalyzer> analyzers = new ArrayList();
  static List<List<JDPacketAnalyzer>> layerAnalyzers = new ArrayList();

  static void loadDefaultAnalyzer() {
    analyzers.add(new PacketAnalyzer());
    analyzers.add(new EthernetAnalyzer());
    analyzers.add(new IPv4Analyzer());
    analyzers.add(new IPv6Analyzer());
    analyzers.add(new TCPAnalyzer());
    analyzers.add(new UDPAnalyzer());
    analyzers.add(new ICMPAnalyzer());
    analyzers.add(new HTTPAnalyzer());
    analyzers.add(new FTPAnalyzer());
    analyzers.add(new TelnetAnalyzer());
    analyzers.add(new SSHAnalyzer());
    analyzers.add(new SMTPAnalyzer());
    analyzers.add(new POP3Analyzer());
    analyzers.add(new ARPAnalyzer());

    for (int i = 0; i < 10; i++) {
      layerAnalyzers.add(new ArrayList());
    }
    for (JDPacketAnalyzer a : analyzers)
      ((List)layerAnalyzers.get(a.layer)).add(a);
  }

  public static List<JDPacketAnalyzer> getAnalyzers() {
    return analyzers;
  }

  public static List<JDPacketAnalyzer> getAnalyzersOf(int layer) {
    return (List)layerAnalyzers.get(layer);
  }
}