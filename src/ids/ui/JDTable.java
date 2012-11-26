package ids.ui;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;



import ids.JDCaptor;
import ids.analyzer.JDPacketAnalyzer;
import jpcap.packet.Packet;

class JDTable extends JComponent
{
  JDTableModel model;
  TableSorter sorter;
  Vector views = new Vector();
  JDCaptor captor;

  JDTable(JDTablePane parent, JDCaptor captor)
  {
    this.captor = captor;
    this.model = new JDTableModel();
    this.sorter = new TableSorter(this.model);

    JTable table = new JTable(this.sorter);
    this.sorter.addMouseListenerToHeaderInTable(table);

    table.setSelectionMode(0);
    table.getSelectionModel().addListSelectionListener(parent);
    table.setDefaultRenderer(Object.class, new JDTableRenderer());
    JScrollPane tableView = new JScrollPane(table);

    setLayout(new BorderLayout());
    add(tableView, "Center");
  }

  void fireTableChanged()
  {
    this.model.fireTableRowsInserted(this.captor.getPackets().size() - 1, this.captor.getPackets().size() - 1);
  }

  void clear() {
    this.model.fireTableStructureChanged();
    this.model.fireTableDataChanged();
  }

  void setTableView(JDPacketAnalyzer analyzer, String name, boolean set) {
    if (set)
      this.views.addElement(new TableView(analyzer, name));
    else {
      for (int i = 0; i < this.views.size(); i++) {
        TableView view = (TableView)this.views.elementAt(i);
        if ((view.analyzer == analyzer) && (view.valueName.equals(name)))
          this.views.removeElement(view);
      }
    }
    this.model.fireTableStructureChanged();
  }

  String[] getTableViewStatus() {
    String[] status = new String[this.views.size()];

    for (int i = 0; i < status.length; i++) {
      TableView view = (TableView)this.views.elementAt(i);
      status[i] = (view.analyzer.getProtocolName() + ":" + view.valueName);
    }

    return status;
  }
  class TableView {
    JDPacketAnalyzer analyzer;
    String valueName;

    TableView(JDPacketAnalyzer analyzer, String name) { this.analyzer = analyzer; this.valueName = name; }
  }

  class JDTableModel extends AbstractTableModel {
    JDTableModel() {
    }

    public int getRowCount() {
      return JDTable.this.captor.getPackets().size();
    }

    public int getColumnCount() {
      return JDTable.this.views.size() + 1;
    }

    public Object getValueAt(int row, int column) {
      if (JDTable.this.captor.getPackets().size() <= row) return "";
      Packet packet = (Packet)JDTable.this.captor.getPackets().get(row);

      if (column == 0) return new Integer(row);
      JDTable.TableView view = (JDTable.TableView)JDTable.this.views.elementAt(column - 1);

      if (view.analyzer.isAnalyzable(packet)) {
        synchronized (view.analyzer) {
          view.analyzer.analyze(packet);
          Object obj = view.analyzer.getValue(view.valueName);

          if ((obj instanceof Vector)) {
            if (((Vector)obj).size() > 0) {
              return ((Vector)obj).elementAt(0);
            }
            return null;
          }
          return obj;
        }
      }
      return null;
    }

    public boolean isCellEditable(int row, int column)
    {
      return false;
    }

    public String getColumnName(int column) {
      if (column == 0) return "No.";

      return ((JDTable.TableView)JDTable.this.views.elementAt(column - 1)).valueName;
    }
  }
}