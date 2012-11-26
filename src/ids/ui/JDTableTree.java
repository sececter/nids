package ids.ui;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import ids.JDPacketAnalyzerLoader;
import ids.analyzer.JDPacketAnalyzer;
import jpcap.packet.Packet;

class JDTableTree extends JComponent
{
  JTree tree;
  DefaultMutableTreeNode root = new DefaultMutableTreeNode();
  List<JDPacketAnalyzer> analyzers = JDPacketAnalyzerLoader.getAnalyzers();

  JDTableTree() {
    this.tree = new JTree(this.root);
    this.tree.setRootVisible(false);
    JScrollPane treeView = new JScrollPane(this.tree);

    setLayout(new BorderLayout());
    add(treeView, "Center");
  }

  void analyzePacket(Packet packet) {
    boolean[] isExpanded = new boolean[this.root.getChildCount()];
    for (int i = 0; i < this.root.getChildCount(); i++) {
      isExpanded[i] = this.tree.isExpanded(new TreePath(((DefaultMutableTreeNode)this.root.getChildAt(i)).getPath()));
    }
    this.root.removeAllChildren();

    for (JDPacketAnalyzer analyzer : this.analyzers) {
      if (analyzer.isAnalyzable(packet)) {
        analyzer.analyze(packet);
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(analyzer.getProtocolName());
        this.root.add(node);
        String[] names = analyzer.getValueNames();
        Object[] values = analyzer.getValues();
        if (names == null)
          continue;
        for (int j = 0; j < names.length; j++) {
          if ((values[j] instanceof Vector))
            addNodes(node, names[j], (Vector)values[j]);
          else if (values[j] != null) {
            addNode(node, names[j] + ": " + values[j]);
          }
        }
      }

    }

    ((DefaultTreeModel)this.tree.getModel()).nodeStructureChanged(this.root);

    for (int i = 0; i < Math.min(this.root.getChildCount(), isExpanded.length); i++) {
      if (isExpanded[i] == false) continue; this.tree.expandPath(new TreePath(((DefaultMutableTreeNode)this.root.getChildAt(i)).getPath()));
    }
  }

  private void addNode(DefaultMutableTreeNode node, String str) {
    node.add(new DefaultMutableTreeNode(str));
  }

  private void addNodes(DefaultMutableTreeNode node, String str, Vector v) {
    DefaultMutableTreeNode subnode = new DefaultMutableTreeNode(str);

    for (int i = 0; i < v.size(); i++) {
      subnode.add(new DefaultMutableTreeNode(v.elementAt(i)));
    }
    node.add(subnode);
  }

  private void setUserObject(TreeNode node, Object obj) {
    ((DefaultMutableTreeNode)node).setUserObject(obj);
  }
}