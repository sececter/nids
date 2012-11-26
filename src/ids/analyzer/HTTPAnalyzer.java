package ids.analyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Vector;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;

public class HTTPAnalyzer extends JDPacketAnalyzer
{
  private static final String[] valueNames = { 
    "Method", 
    "Header" };
  String method;
  Vector headers = new Vector();

  public HTTPAnalyzer() {
    this.layer = APPLICATION_LAYER;
  }

  public boolean isAnalyzable(Packet p)
  {
    return ((p instanceof TCPPacket)) && (
      (((TCPPacket)p).src_port == 80) || (((TCPPacket)p).dst_port == 80));
  }

  public String getProtocolName()
  {
    return "HTTP";
  }

  public String[] getValueNames() {
    return valueNames;
  }

  public void analyze(Packet p) {
    this.method = "";
    this.headers.removeAllElements();
    if (!isAnalyzable(p)) return;
    try
    {
      BufferedReader in = new BufferedReader(new StringReader(new String(p.data)));

      this.method = in.readLine();
      if ((this.method == null) || (this.method.indexOf("HTTP") == -1))
      {
        this.method = "Not HTTP Header";
        return;
      }
      String l;
      while ((l = in.readLine()).length() > 0)
      {
       // String l;
        this.headers.addElement(l);
      } } catch (IOException localIOException) {
    }
  }

  public Object getValue(String valueName) {
    if (valueNames[0].equals(valueName)) return this.method;
    if (valueNames[1].equals(valueName)) return this.headers;
    return null;
  }

  Object getValueAt(int index) {
    if (index == 0) return this.method;
    if (index == 1) return this.headers;
    return null;
  }

  public Object[] getValues() {
    Object[] values = new Object[2];
    values[0] = this.method;
    values[1] = this.headers;

    return values;
  }
}