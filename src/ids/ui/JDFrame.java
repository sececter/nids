package ids.ui;
import ids.alert.GenerateAlert;
import ids.alert.IntruderList;
import ids.rule.*;
import ids.rule.*;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.Timer;
import ids.JDCaptor;
import ids.JDStatisticsTakerLoader;
import ids.JpcapDumper;
import ids.stat.JDStatisticsTaker;

public class JDFrame extends JFrame
  implements ActionListener
{
  public JDCaptor captor;
  JLabel statusLabel;
  JMenuItem openMenu;
  JMenuItem saveMenu;
  JMenuItem captureMenu;
  JMenuItem stopMenu;
  JMenuItem ruleAdd;
  JMenuItem ruleShow;
  JMenuItem alertMessage;
  JMenu statMenu;

  JButton openButton;
  JButton saveButton;
  JButton captureButton;
  JButton stopButton;
  public JDTablePane tablePane;
  Timer JDFrameUpdater = new Timer(500, new ActionListener() {
    public void actionPerformed(ActionEvent evt) {
      JDFrame.this.tablePane.fireTableChanged();
      JDFrame.this.statusLabel.setText("Captured " + JDFrame.this.captor.getPackets().size() + " packets.");

      JDFrame.this.repaint();
    }
  });

  public static JDFrame openNewWindow(JDCaptor captor)
  {
    JDFrame frame = new JDFrame(captor);
    frame.setVisible(true);

    return frame;
  }

  public JDFrame(JDCaptor captor) {
    this.captor = captor;
    this.tablePane = new JDTablePane(captor);
    captor.setJDFrame(this);

    setTitle("Network Intrusion Detection Syetem");

    JMenuBar menuBar = new JMenuBar();
    setJMenuBar(menuBar);

    JMenu menu = new JMenu("System");
    menuBar.add(menu);
    JMenuItem item = new JMenuItem("New Window");
    item.setActionCommand("NewWin");
    item.addActionListener(this);
    menu.add(item);
    item = new JMenuItem("Exit");
    item.setActionCommand("Exit");
    item.addActionListener(this);
    menu.add(item);

    menu = new JMenu("File");
    menuBar.add(menu);
    this.openMenu = new JMenuItem("Open");
    
    this.openMenu.setActionCommand("Open");
    this.openMenu.addActionListener(this);
    menu.add(this.openMenu);
    this.saveMenu = new JMenuItem("Save");
    
    this.saveMenu.setActionCommand("Save");
    this.saveMenu.addActionListener(this);
    this.saveMenu.setEnabled(false);
    menu.add(this.saveMenu);

// this is for Capture Operations
    menu = new JMenu("Capture");
    menuBar.add(menu);
    this.captureMenu = new JMenuItem("Start");
    
    this.captureMenu.setActionCommand("Start");
    this.captureMenu.addActionListener(this);
    menu.add(this.captureMenu);
    this.stopMenu = new JMenuItem("Stop");
    
    this.stopMenu.setActionCommand("Stop");
    this.stopMenu.addActionListener(this);
    this.stopMenu.setEnabled(false);
    menu.add(this.stopMenu);

//to know the statistics like pie graph and X-Y graph
    this.statMenu = new JMenu("Statistics");
    menuBar.add(this.statMenu);
    menu = new JMenu("Cumulative");
    this.statMenu.add(menu);
    List stakers = JDStatisticsTakerLoader.getStatisticsTakers();
    for (int i = 0; i < stakers.size(); i++) {
      item = new JMenuItem(((JDStatisticsTaker)stakers.get(i)).getName());
      item.setActionCommand("CUMSTAT" + i);
      item.addActionListener(this);
      menu.add(item);
    }
    menu = new JMenu("Continuous");
    this.statMenu.add(menu);
    for (int i = 0; i < stakers.size(); i++) {
      item = new JMenuItem(((JDStatisticsTaker)stakers.get(i)).getName());
      item.setActionCommand("CONSTAT" + i);
      item.addActionListener(this);
      menu.add(item);
    }

//  this is for all statistics of the system
    menu = new JMenu("View");
    menuBar.add(menu);
    this.tablePane.setTableViewMenu(menu);

//  this is for the Signature Operations
    menu=new JMenu("Signature Operations");
    menuBar.add(menu);
    ruleAdd=new JMenuItem("Add New Rules");
    menu.add(ruleAdd);
    this.ruleAdd.addActionListener(this);

    ruleShow=new JMenuItem("Show All Rules");
    menu.add(ruleShow);
    this.ruleShow.addActionListener(this);


//  this is for alet info
    menu=new JMenu("Intruder Info");
    menuBar.add(menu);
    alertMessage=new JMenuItem("Intruder");
    menu.add(alertMessage);
    this.alertMessage.addActionListener(this);



    JToolBar toolbar = new JToolBar();
    toolbar.setFloatable(false);
    this.openButton = new JButton(getImageIcon("/image/open.gif"));
    this.openButton.setActionCommand("Open");
    this.openButton.addActionListener(this);
    toolbar.add(this.openButton);
    this.saveButton = new JButton(getImageIcon("/image/save.gif"));
    this.saveButton.setActionCommand("Save");
    this.saveButton.addActionListener(this);
    this.saveButton.setEnabled(false);
    toolbar.add(this.saveButton);
    toolbar.addSeparator();
    this.captureButton = new JButton(getImageIcon("/image/capture.gif"));
    this.captureButton.setActionCommand("Start");
    this.captureButton.addActionListener(this);
    toolbar.add(this.captureButton);
    this.stopButton = new JButton(getImageIcon("/image/stopcap.gif"));
    this.stopButton.setActionCommand("Stop");
    this.stopButton.addActionListener(this);
    this.stopButton.setEnabled(false);
    toolbar.add(this.stopButton);

    this.statusLabel = new JLabel(" Intrusion Detection System started.");

    getContentPane().setLayout(new BorderLayout());

    getContentPane().add(this.statusLabel, "South");
    getContentPane().add(this.tablePane, "Center");
    //getContentPane().add(toolbar, "North");

    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent evt) {
        JDFrame.this.saveProperty();
        JpcapDumper.closeWindow((JDFrame)evt.getSource());
      }
    });
    loadProperty();
  }

  public void actionPerformed(ActionEvent evt)
  {
    String cmd = evt.getActionCommand();

    if (cmd.equals("Open")) {

    	this.captor.loadPacketsFromFile();

    } else if (cmd.equals("Save")) {

    	this.captor.saveToFile();

    } else if (cmd.equals("NewWin")) {

    	JpcapDumper.openNewWindow();
    } else if (cmd.equals("Exit")) {

    	saveProperty();
      System.exit(0);

    } else if (cmd.equals("Start")) {

    	this.captor.capturePacketsFromDevice();

    } else if (cmd.equals("Stop")) {

    	this.captor.stopCapture();
    } else if (cmd.startsWith("CUMSTAT")) {

    	int index = Integer.parseInt(cmd.substring(7));
      this.captor.addCumulativeStatFrame(JDStatisticsTakerLoader.getStatisticsTakerAt(index));

    } else if (cmd.startsWith("CONSTAT")) {

    	int index = Integer.parseInt(cmd.substring(7));
      this.captor.addContinuousStatFrame(JDStatisticsTakerLoader.getStatisticsTakerAt(index));

    } else if(cmd.equals("Add New Rules"))
    {
    	new RuleAdd("Add new rule");

    }
    else if(cmd.equals("Show All Rules"))
    {
    	new MysqlRetrieve("All Rules");

    }
    else if(cmd.equals("Intruder"))
    {
    	new IntruderList("Warning!!!!");

    }
  }

  public void clear()
  {
    this.tablePane.clear();
  }

  public void startUpdating()
  {
    this.JDFrameUpdater.setRepeats(true);
    this.JDFrameUpdater.start();
  }

  public void stopUpdating() {
    this.JDFrameUpdater.stop();
    this.JDFrameUpdater.setRepeats(false);
    this.JDFrameUpdater.start();
  }

  void loadProperty()
  {
   setSize(Integer.parseInt(JpcapDumper.JDProperty.getProperty("WinWidth","400")),
		        Integer.parseInt(JpcapDumper.JDProperty.getProperty("WinHeight","400")));
		setLocation(Integer.parseInt(JpcapDumper.JDProperty.getProperty("WinX","0")),
			Integer.parseInt(JpcapDumper.JDProperty.getProperty("WinY","0")));
  }

  void saveProperty()
  {
    
		JpcapDumper.JDProperty.put("WinWidth",String.valueOf(getBounds().width));
		JpcapDumper.JDProperty.put("WinHeight",String.valueOf(getBounds().height));
		JpcapDumper.JDProperty.put("WinX",String.valueOf(getBounds().x));
		JpcapDumper.JDProperty.put("WinY",String.valueOf(getBounds().y));

    this.tablePane.saveProperty();

    JpcapDumper.saveProperty();
  }

  public void enableCapture() {
    this.openMenu.setEnabled(true);
    this.openButton.setEnabled(true);
    this.saveMenu.setEnabled(true);
    this.saveButton.setEnabled(true);
    this.captureMenu.setEnabled(true);
    this.captureButton.setEnabled(true);
    this.stopMenu.setEnabled(false);
    this.stopButton.setEnabled(false);
  }

  public void disableCapture() {
    this.openMenu.setEnabled(false);
    this.openButton.setEnabled(false);
    this.captureMenu.setEnabled(false);
    this.captureButton.setEnabled(false);
    this.saveMenu.setEnabled(true);
    this.saveButton.setEnabled(true);
    this.stopMenu.setEnabled(true);
    this.stopButton.setEnabled(true);
  }

  private ImageIcon getImageIcon(String path) {
    return new ImageIcon(getClass().getResource(path));
  }
}