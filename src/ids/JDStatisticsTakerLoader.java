package ids;

import java.util.ArrayList;
import java.util.List;
import ids.stat.ApplicationProtocolStat;
import ids.stat.FreeMemStat;
import ids.stat.JDStatisticsTaker;
import ids.stat.NetworkProtocolStat;
import ids.stat.PacketStat;
import ids.stat.TransportProtocolStat;

public class JDStatisticsTakerLoader
{
  static ArrayList<JDStatisticsTaker> stakers = new ArrayList();

  static void loadStatisticsTaker() {
    stakers.add(new PacketStat());
    stakers.add(new NetworkProtocolStat());
    stakers.add(new TransportProtocolStat());
    stakers.add(new ApplicationProtocolStat());
    stakers.add(new FreeMemStat());
  }

  public static List<JDStatisticsTaker> getStatisticsTakers() {
    return stakers;
  }

  public static JDStatisticsTaker getStatisticsTakerAt(int index) {
    return (JDStatisticsTaker)stakers.get(index);
  }
}