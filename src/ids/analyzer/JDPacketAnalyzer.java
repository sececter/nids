package ids.analyzer;

import jpcap.packet.Packet;

public abstract class JDPacketAnalyzer
{
  public int layer = DATALINK_LAYER;
  public static int DATALINK_LAYER = 0;
  public static int NETWORK_LAYER = 1;
  public static int TRANSPORT_LAYER = 2;
  public static int APPLICATION_LAYER = 3;

  public abstract boolean isAnalyzable(Packet paramPacket);

  public abstract void analyze(Packet paramPacket);

  public abstract String getProtocolName();

  public abstract String[] getValueNames();

  public abstract Object getValue(String paramString);

  abstract Object getValueAt(int paramInt);

  public abstract Object[] getValues();
}