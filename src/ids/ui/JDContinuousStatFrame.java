package ids.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.Iterator;
import java.util.List;
import ids.stat.JDStatisticsTaker;
import ids.ui.graph.LineGraph;
import jpcap.packet.Packet;

public class JDContinuousStatFrame extends JDStatFrame
{
  LineGraph lineGraph;
  JDStatisticsTaker staker;
  int statType;
  boolean drawTimescale;
  int count;
  int currentCount = 0;
  long currentSec = 0L;

  public static JDContinuousStatFrame openWindow(List<Packet> packets, JDStatisticsTaker staker) {
    JDContinuousStatFrame frame = new JDContinuousStatFrame(packets, 5, true, staker, 0);
    frame.setVisible(true);
    return frame;
  }

  JDContinuousStatFrame(List<Packet> packets, int count, boolean isTime, JDStatisticsTaker staker, int type) {
    super(staker.getName() + " [" + staker.getStatTypes()[type] + "]");
    this.staker = staker;
    this.drawTimescale = isTime; this.count = count;
    this.statType = type;

    this.lineGraph = new LineGraph(staker.getLabels());

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(this.lineGraph, "Center");
    setSize(400, 400);

    if ((packets == null) || (packets.size() == 0)) return;

    Iterator it = packets.iterator();
    this.currentSec = ((Packet)packets.get(0)).sec;
    this.currentCount = 0;
    int index = 0;
    if (isTime)
      while (index < packets.size()) {
        Packet p = (Packet)packets.get(index++);

        while ((index < packets.size()) && (p.sec - this.currentSec <= count)) {
          staker.addPacket(p);
          p = (Packet)packets.get(index++);
        }
        if (index == packets.size()) break;
        this.currentSec += count;
        index--;
        this.lineGraph.addValue(staker.getValues(type));
        staker.clear();
      }
    else
      while (it.hasNext()) {
        for (int i = 0; (it.hasNext()) && (i < count); this.currentCount += 1) {
          staker.addPacket((Packet)it.next());

          i++;
        }
        if (!it.hasNext()) break;
        this.currentCount = 0;
        this.lineGraph.addValue(staker.getValues(type));
        staker.clear();
      }
  }

  public void addPacket(Packet p)
  {
    this.staker.addPacket(p);
    if (this.drawTimescale) {
      if (this.currentSec == 0L) this.currentSec = p.sec;
      if (p.sec - this.currentSec > this.count) {
        this.lineGraph.addValue(this.staker.getValues(this.statType));
        this.staker.clear();
        this.currentSec += this.count;
        if (p.sec - this.currentSec > this.count)
          for (long s = p.sec - this.currentSec - this.count; s > this.count; s -= this.count)
            this.lineGraph.addValue(this.staker.getValues(this.statType));
      }
    }
    else {
      this.currentCount += 1;
      if (this.currentCount == this.count) {
        this.lineGraph.addValue(this.staker.getValues(this.statType));
        this.staker.clear();
        this.currentCount = 0;
      }
    }
  }

  public void clear() {
    this.currentCount = 0;
    this.currentSec = 0L;
    this.lineGraph.clear();
  }

  void fireUpdate() {
    repaint();
  }
}