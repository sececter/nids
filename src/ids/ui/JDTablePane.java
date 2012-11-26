package ids.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.StringTokenizer;
import java.util.prefs.Preferences;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import ids.JDCaptor;
import ids.JDPacketAnalyzerLoader;
import ids.JpcapDumper;
import ids.analyzer.JDPacketAnalyzer;
import jpcap.packet.Packet;

class JDTablePane extends JPanel
  implements ActionListener, ListSelectionListener
{
  JDTable table;
  JDTableTree tree;
  JDTableTextArea text;
  JDCaptor captor;
  List<JDPacketAnalyzer> analyzers;
  JMenu[] tableViewMenu = new JMenu[4];

  JDTablePane(JDCaptor captor) { this.captor = captor;
    this.table = new JDTable(this, captor);
    this.tree = new JDTableTree();
    this.text = new JDTableTextArea();

    JSplitPane splitPane = new JSplitPane(0);
    JSplitPane splitPane2 = new JSplitPane(1);
    splitPane.setTopComponent(this.table);
    splitPane2.setTopComponent(this.tree);
    splitPane2.setBottomComponent(new JScrollPane(this.text));
    splitPane.setBottomComponent(splitPane2);
    splitPane.setDividerLocation(200);
    splitPane2.setDividerLocation(200);

    this.tableViewMenu[0] = new JMenu("Datalink Layer");
    this.tableViewMenu[1] = new JMenu("Network Layer");
    this.tableViewMenu[2] = new JMenu("Transport Layer");
    this.tableViewMenu[3] = new JMenu("Application Layer");
    this.analyzers = JDPacketAnalyzerLoader.getAnalyzers();

    for (int i = 0; i < this.analyzers.size(); i++) {
      JDPacketAnalyzer analyzer = (JDPacketAnalyzer)this.analyzers.get(i);
      JMenuItem item = new JMenu(analyzer.getProtocolName());
      String[] valueNames = analyzer.getValueNames();
      if (valueNames != null) {
        for (int j = 0; j < valueNames.length; j++) {
          JMenuItem subitem = new JCheckBoxMenuItem(valueNames[j]);
          subitem.setActionCommand("TableView" + i);
          subitem.addActionListener(this);
          item.add(subitem);
        }
        this.tableViewMenu[analyzer.layer].add(item);
      }
    }
    setLayout(new BorderLayout());
    add(splitPane, "Center");

    loadProperty();
    setSize(400, 200); }

  void fireTableChanged()
  {
    this.table.fireTableChanged();
  }

  void clear() {
    this.table.clear();
  }

  public void setTableViewMenu(JMenu menu) {
    menu.add(this.tableViewMenu[0]);
    menu.add(this.tableViewMenu[1]);
    menu.add(this.tableViewMenu[2]);
    menu.add(this.tableViewMenu[3]);
  }

  public void actionPerformed(ActionEvent evt) {
    String cmd = evt.getActionCommand();

    if (cmd.startsWith("TableView")) {
      int index = Integer.parseInt(cmd.substring(9));
      JCheckBoxMenuItem item = (JCheckBoxMenuItem)evt.getSource();
      this.table.setTableView((JDPacketAnalyzer)this.analyzers.get(index), item.getText(), item.isSelected());
    }
  }

  public void valueChanged(ListSelectionEvent evt) {
    if (evt.getValueIsAdjusting()) return;

    int index = ((ListSelectionModel)evt.getSource()).getMinSelectionIndex();
    if (index >= 0) {
      Packet p = (Packet)this.captor.getPackets().get(this.table.sorter.getOriginalIndex(index));
      this.tree.analyzePacket(p);
      this.text.showPacket(p);
    }
  }

  void loadProperty()
  {
    Component[] menus = new Component[this.analyzers.size()];
    int k = 0;
    for (int j = 0; j < this.tableViewMenu[0].getMenuComponents().length; j++)
      menus[(k++)] = this.tableViewMenu[0].getMenuComponents()[j];
    for (int j = 0; j < this.tableViewMenu[1].getMenuComponents().length; j++)
      menus[(k++)] = this.tableViewMenu[1].getMenuComponents()[j];
    for (int j = 0; j < this.tableViewMenu[2].getMenuComponents().length; j++)
      menus[(k++)] = this.tableViewMenu[2].getMenuComponents()[j];
    for (int j = 0; j < this.tableViewMenu[3].getMenuComponents().length; j++) {
      menus[(k++)] = this.tableViewMenu[3].getMenuComponents()[j];
    }

    StringTokenizer status=new StringTokenizer(JpcapDumper.JDProperty.getProperty("TableView", 
      "Ethernet Frame:Source MAC,Ethernet Frame:Destination MAC,IPv4:Source IP,IPv4:Destination IP"), ",");

    while (status.hasMoreTokens()) {
      StringTokenizer s = new StringTokenizer(status.nextToken(), ":");
      if (s.countTokens() == 2) {
        String name = s.nextToken(); String valueName = s.nextToken();
        Component[] vn;
        for (int i = 0; i < menus.length; i++) {
          if ((((JMenu)menus[i]).getText() == null) || (name == null) || 
            (!((JMenu)menus[i]).getText().equals(name))) continue;
          vn = ((JMenu)menus[i]).getMenuComponents();

          for (int j = 0; j < vn.length; j++)
            if (valueName.equals(((JCheckBoxMenuItem)vn[j]).getText())) {
              ((JCheckBoxMenuItem)vn[j]).setState(true);
              break;
            }
          break;
        }

        for (JDPacketAnalyzer analyzer : this.analyzers)
          if (analyzer.getProtocolName().equals(name)) {
            this.table.setTableView(analyzer, valueName, true);
            break;
          }
      }
    }
  }

  void saveProperty() {
    String[] viewStatus = this.table.getTableViewStatus();
    if (viewStatus.length > 0) {
      StringBuffer buf = new StringBuffer(viewStatus[0]);
      for (int i = 1; i < viewStatus.length; i++) {
        buf.append("," + viewStatus[i]);
      }
     JpcapDumper.JDProperty.put("TableView",buf.toString());
    }
  }
}