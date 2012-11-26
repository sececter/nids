package ids.stat;

import java.util.List;
import jpcap.packet.Packet;

public abstract class JDStatisticsTaker
{
  public abstract String getName();

  public abstract void analyze(List<Packet> paramList);

  public abstract void addPacket(Packet paramPacket);

  public abstract String[] getLabels();

  public abstract String[] getStatTypes();

  public abstract long[] getValues(int paramInt);

  public abstract void clear();

  public JDStatisticsTaker newInstance()
  {
    try
    {
      return (JDStatisticsTaker)getClass().newInstance(); } catch (Exception e) {
    }
    return null;
  }
}