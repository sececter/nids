package ids.ui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

class JDTableRenderer extends JLabel
  implements TableCellRenderer
{
  protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

  public JDTableRenderer() {
    setOpaque(true);
  }

  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
  {
    if (isSelected) {
      super.setForeground(table.getSelectionForeground());
      super.setBackground(table.getSelectionBackground());
    } else {
      super.setForeground(table.getForeground());
      super.setBackground(table.getBackground());
    }

    setFont(table.getFont());

    if (hasFocus)
      setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
    else {
      setBorder(noFocusBorder);
    }

    if (value == null) {
      setText("Not Available");
      return this;
    }

    setText(value.toString());

    if ((value.getClass().equals(Integer.class)) || (value.getClass().equals(Long.class))) {
      setHorizontalAlignment(4);
    }

    Color back = getBackground();
    boolean colorMatch = (back != null) && (back.equals(table.getBackground())) && (table.isOpaque());
    setOpaque(!colorMatch);

    return this;
  }
}