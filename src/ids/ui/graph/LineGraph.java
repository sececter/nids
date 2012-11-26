package ids.ui.graph;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public class LineGraph extends JPanel
{
  private String[] labels;
  private Vector values = new Vector();

  long maxValue = -9223372036854775808L; long minValue = 9223372036854775807L;
  boolean autoMax;
  boolean autoMin;
  int marginY = 20; int marginX = 20;

  private Color[] colors = { 
    Color.blue, Color.green, Color.yellow.darker(), Color.red, Color.cyan, Color.pink, Color.orange };

  public LineGraph(String[] labels)
  {
    this(labels, null, 2147483647L, -2147483648L, true, true);
  }

  LineGraph(String[] labels, long[][] values) {
    this(labels, values, 2147483647L, -2147483648L, true, true);
  }

  LineGraph(String[] labels, long[][] values, long minValue, long maxValue) {
    this(labels, values, minValue, maxValue, false, false);
  }

  LineGraph(String[] labels, long[][] values, long minValue, long maxValue, boolean autoMin, boolean autoMax) {
    this.labels = labels;
    this.autoMax = autoMax; this.autoMin = autoMin;
    this.minValue = minValue; this.maxValue = maxValue;

    if (values != null) {
      for (int i = 0; i < values.length; i++) {
        this.values.addElement(values[i]);

        if ((autoMin) || (autoMax)) {
          for (int j = 0; j < values[i].length; j++) {
            if ((autoMax) && (values[i][j] > maxValue)) maxValue = values[i][j];
            if ((!autoMin) || (values[i][j] >= minValue)) continue; minValue = values[i][j];
          }
        }
      }
    }

    setLayout(new BoxLayout(this, 0));
    add(new GraphPane());
    add(new LabelPane());
  }

  public void addValue(long[] values) {
    this.values.addElement(values);

    if ((this.autoMin) || (this.autoMax)) {
      for (int i = 0; i < values.length; i++) {
        if ((this.autoMax) && (values[i] > this.maxValue)) this.maxValue = values[i];
        if ((!this.autoMin) || (values[i] >= this.minValue)) continue; this.minValue = values[i];
      }
    }
    repaint();
  }

  public void clear() {
    this.values.removeAllElements();
    this.maxValue = -9223372036854775808L; this.minValue = 9223372036854775807L;
    repaint();
  }
  void setMinValue(int minValue) {
    this.minValue = minValue; } 
  void setMaxValue(int maxValue) { this.maxValue = maxValue; } 
  void setMinValueAutoSet(boolean autoMin) { this.autoMin = autoMin; } 
  void setMaxValueAutoSet(boolean autoMax) { this.autoMax = autoMax;
  }

  public Dimension getPreferredSize()
  {
    return new Dimension(300, 200);
  }

  public static void main(String[] args) {
    String[] labels = { "layout", "box" };
    long[][] data = { { 1L, 1L }, { 2L, 4L }, { 3L, 2L } };

    JFrame f = new JFrame();
    f.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) { System.exit(0);
      }
    });
    LineGraph l = new LineGraph(labels, null, 0L, 10L);
    f.getContentPane().add(l);
    f.pack();
    f.setVisible(true);
  }

  private class GraphPane extends JPanel
  {
    private GraphPane()
    {
    }

    public void paintComponent(Graphics g)
    {
      super.paintComponent(g);

      setBackground(Color.white);
      if ((LineGraph.this.labels == null) || (LineGraph.this.values == null) || (LineGraph.this.values.size() == 0)) return;

      int ylabelw = 0;
      for (int i = 0; i < 4; i++) {
        int w = g.getFontMetrics().stringWidth(String.valueOf(LineGraph.this.maxValue - (LineGraph.this.maxValue - LineGraph.this.minValue) * i / 4.0D));
        if (w <= ylabelw) continue; ylabelw = w;
      }

      long h = getHeight() - LineGraph.this.marginY - LineGraph.this.marginY; long w = getWidth(); long h2 = LineGraph.this.maxValue - LineGraph.this.minValue;
      double d = (w - LineGraph.this.marginX - LineGraph.this.marginX) / (LineGraph.this.values.size() - 1.0D); double x = d + LineGraph.this.marginX + ylabelw;

      g.setColor(Color.black);

      g.drawLine(LineGraph.this.marginX + ylabelw, 0, LineGraph.this.marginX + ylabelw, getHeight());
      g.setColor(Color.gray);
      for (int i = 0; i < 5; i++) {
        int y = LineGraph.this.marginY + (getHeight() - LineGraph.this.marginY - LineGraph.this.marginY) / 4 * i;
        g.drawLine(LineGraph.this.marginX + ylabelw, y, getWidth(), y);
        g.drawString(String.valueOf(LineGraph.this.maxValue - (LineGraph.this.maxValue - LineGraph.this.minValue) * i / 4.0D), LineGraph.this.marginX - 5, y);
      }

      long[] vv = (long[])LineGraph.this.values.firstElement();
      for (int i = 1; i < LineGraph.this.values.size(); x += d) {
        long[] v = (long[])LineGraph.this.values.elementAt(i);

        for (int j = 0; j < v.length; j++) {
          Color c = LineGraph.this.colors[(j % LineGraph.this.colors.length)];
          for (int k = 0; k < j / LineGraph.this.colors.length; k++) c.darker();
          g.setColor(c);

          g.drawLine((int)(x - d), (int)(h + LineGraph.this.marginY - (vv[j] - LineGraph.this.minValue) * h / h2), (int)x, (int)(h + LineGraph.this.marginY - (v[j] - LineGraph.this.minValue) * h / h2));
        }

        vv = v;

        i++;
      }
    }
  }

  private class LabelPane extends JPanel
  {
    LabelPane()
    {
      setLayout(new BoxLayout(this, 1));
      setBackground(Color.white);

      for (int i = 0; i < LineGraph.this.labels.length; i++) {
        JPanel cont = new JPanel();
        cont.setLayout(new BoxLayout(cont, 0));
        cont.setBackground(Color.white);
        JLabel label = new JLabel(LineGraph.this.labels[i], 2);
        label.setForeground(Color.black);
        JLabel box = new JLabel("    ");
        box.setOpaque(true);

        Color c = LineGraph.this.colors[(i % LineGraph.this.colors.length)];
        for (int j = 0; j < i / LineGraph.this.colors.length; j++) c.darker();
        box.setBackground(c);

        cont.add(box);
        cont.add(Box.createRigidArea(new Dimension(5, 0)));
        cont.add(label);
        cont.setAlignmentX(0.0F);
        add(cont);
        add(Box.createRigidArea(new Dimension(0, 5)));
      }

      setBorder(
        new CompoundBorder(BorderFactory.createLineBorder(Color.black, 1), 
        new EmptyBorder(10, 10, 10, 10)));
    }
    public Dimension getMinimumSize() { return new Dimension(50, 1);
    }
  }
}