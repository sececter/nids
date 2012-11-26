package ids.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.Timer;
import jpcap.packet.Packet;

public abstract class JDStatFrame extends JFrame
{
  Timer JDStatFrameUpdater = new Timer(500, new ActionListener() {
    public void actionPerformed(ActionEvent evt) {
      JDStatFrame.this.fireUpdate();
      JDStatFrame.this.repaint();
    }
  });

  JDStatFrame(String title)
  {
    super(title);
    this.JDStatFrameUpdater.start();
    addWindowListener(new WindowAdapter()
    {
      public void windowClosed(WindowEvent evt) {
        JDStatFrame.this.setVisible(false); }  } ); } 
  abstract void fireUpdate();

  public abstract void addPacket(Packet paramPacket);

  public abstract void clear();

  public void startUpdating() { this.JDStatFrameUpdater.setRepeats(true);
    this.JDStatFrameUpdater.start(); }

  public void stopUpdating()
  {
    this.JDStatFrameUpdater.stop();
    this.JDStatFrameUpdater.setRepeats(false);
    this.JDStatFrameUpdater.start();
  }
}