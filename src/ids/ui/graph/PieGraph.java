package ids.ui.graph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

public class PieGraph extends JPanel
{
  private String[] labels;
  private long[] values;
  private Color[] colors = { 
    Color.blue, Color.green, Color.yellow, Color.red, Color.cyan, Color.pink, Color.orange };

  public PieGraph(String[] labels, long[] values)
  {
    this.labels = labels;
    this.values = values;
  }

  public void changeValue(long[] values) {
    this.values = values;
    repaint();
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    if ((this.labels == null) || (this.values == null)) return;

    int r = Math.min(getWidth(), getHeight()) / 2 - 20;
    int x = getWidth() / 2; int y = getHeight() / 2;
    int sum = 0;
    for (int i = 0; i < this.values.length; i++) sum = (int)(sum + this.values[i]);

    double startAngle = 90.0D;
    for (int i = 0; i < this.values.length; i++) {
      if (this.values[i] != 0L) {
        double angle = this.values[i] * 360.0D / sum;

        Color c = this.colors[(i % this.colors.length)];
        for (int j = 0; j < i / this.colors.length; j++) c.darker();
        g.setColor(c);
        g.fillArc(x - r, y - r, r * 2, r * 2, (int)startAngle, (int)(-angle));

        startAngle -= angle;
      }
    }
    startAngle = 90.0D;
    for (int i = 0; i < this.values.length; i++)
      if (this.values[i] != 0L) {
        double angle = this.values[i] * 360.0D / sum;

        int sx = (int)(Math.cos(6.283185307179586D * (startAngle - angle / 2.0D) / 360.0D) * (r + 10));
        int sy = (int)(Math.sin(6.283185307179586D * (startAngle - angle / 2.0D) / 360.0D) * (r + 10));
        g.setColor(Color.black);
        g.drawString(this.labels[i], x + sx, y - sy);

        startAngle -= angle;
      }
  }

  public Dimension getPreferredSize() {
    return new Dimension(100, 100);
  }
}