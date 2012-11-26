package ids.ui;

import java.awt.Container;
import java.awt.Dimension;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import ids.stat.JDStatisticsTaker;
import ids.ui.graph.PieGraph;
import jpcap.packet.Packet;

public class JDCumlativeStatFrame extends JDStatFrame
  implements ListSelectionListener
{
  JTable table;
  TableModel model = null;
  PieGraph pieGraph = null;
  JDStatisticsTaker staker;
  int statType = 0;

  public static JDCumlativeStatFrame openWindow(List<Packet> packets, JDStatisticsTaker staker) {
    JDCumlativeStatFrame frame = new JDCumlativeStatFrame(packets, staker);
    frame.setVisible(true);
    return frame;
  }

  JDCumlativeStatFrame(List<Packet> packets, JDStatisticsTaker staker) {
    super(staker.getName());
    this.staker = staker;
    staker.analyze(packets);

    getContentPane().setLayout(new BoxLayout(getContentPane(), 1));

    this.model = new TableModel();
    this.table = new JTable(this.model);
    this.table.setSelectionMode(0);
    JTableHeader header = this.table.getTableHeader();
    Dimension dim = header.getPreferredSize();
    dim.height = 20;
    header.setPreferredSize(dim);
    JScrollPane tablePane = new JScrollPane(this.table);
    dim = this.table.getMinimumSize();
    dim.height += 25;
    tablePane.setPreferredSize(dim);

    if (staker.getLabels().length > 1) {
      this.pieGraph = new PieGraph(staker.getLabels(), staker.getValues(0));
      JSplitPane splitPane = new JSplitPane(0);
      splitPane.setTopComponent(tablePane);
      splitPane.setBottomComponent(this.pieGraph);

      getContentPane().add(splitPane);

      this.table.getSelectionModel().addListSelectionListener(this);
    } else {
      getContentPane().add(tablePane);
    }

    setSize(400, 400);
  }

  void fireUpdate()
  {
    int sel = this.table.getSelectedRow();
    if (this.pieGraph != null) this.pieGraph.changeValue(this.staker.getValues(this.statType));
    if (this.model != null) this.model.update();
    if (sel >= 0) this.table.setRowSelectionInterval(sel, sel);
    repaint();
  }

  public void addPacket(Packet p) {
    this.staker.addPacket(p);
  }

  public void clear() {
    this.staker.clear();
    if (this.pieGraph != null) this.pieGraph.changeValue(this.staker.getValues(this.statType));
    if (this.model != null) this.model.update(); 
  }

  public void valueChanged(ListSelectionEvent evt)
  {
    if (evt.getValueIsAdjusting()) return;

    ListSelectionModel lsm = (ListSelectionModel)evt.getSource();
    if (lsm.isSelectionEmpty()) this.statType = 0; else
      this.statType = lsm.getMinSelectionIndex();
    this.pieGraph.changeValue(this.staker.getValues(this.statType));
  }
  class TableModel extends AbstractTableModel { String[] labels;
    Object[][] values;

    TableModel() { this.labels = new String[JDCumlativeStatFrame.this.staker.getLabels().length + 1];
      this.labels[0] = new String();
      System.arraycopy(JDCumlativeStatFrame.this.staker.getLabels(), 0, this.labels, 1, JDCumlativeStatFrame.this.staker.getLabels().length);

      String[] types = JDCumlativeStatFrame.this.staker.getStatTypes();
      this.values = new Object[types.length][JDCumlativeStatFrame.this.staker.getLabels().length + 1];
      for (int i = 0; i < this.values.length; i++) {
        this.values[i][0] = types[i];
        long[] v = JDCumlativeStatFrame.this.staker.getValues(i);
        for (int j = 0; j < v.length; j++)
          this.values[i][(j + 1)] = new Long(v[j]); 
      } }

    public String getColumnName(int c) {
      return this.labels[c]; } 
    public int getColumnCount() { return this.labels.length; } 
    public int getRowCount() { return this.values.length; } 
    public Object getValueAt(int row, int column) { return this.values[row][column]; } 
    void update() {
      String[] types = JDCumlativeStatFrame.this.staker.getStatTypes();
      this.values = new Object[types.length][JDCumlativeStatFrame.this.staker.getLabels().length + 1];
      for (int i = 0; i < this.values.length; i++) {
        this.values[i][0] = types[i];
        long[] v = JDCumlativeStatFrame.this.staker.getValues(i);
        for (int j = 0; j < v.length; j++)
          this.values[i][(j + 1)] = new Long(v[j]);
      }
      fireTableDataChanged();
    }
  }
}