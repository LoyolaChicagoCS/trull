package edu.luc.cs.trull.demo.stopwatch;

import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import edu.luc.cs.trull.demo.wristwatch.DateConstants;
import edu.luc.cs.trull.demo.wristwatch.EventLabels;

/**
 * The visual interface of the stopwatch.
 */
public class Presentation extends JPanel 
    implements EventLabels, DateConstants, PropertyChangeListener {
  JPanel swUpPnl = new JPanel();
  JPanel swDownPnl = new JPanel();
  JTextField hourTxt = new JTextField();
  JLabel commaLbl = new JLabel();
  JTextField secTxt = new JTextField();
  JButton startBtn = new JButton();
  JButton resetBtn = new JButton();

  /**
   * List of listeners for making this presentation object an event source.
   * In this way, the presentation knows nothing about the translation.  The
   * translation is simply added as one of the listeners.
   */
  ActionListener listeners = null;

  public Presentation() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    swUpPnl.setLayout(new BoxLayout(swUpPnl, BoxLayout.X_AXIS));
    swDownPnl.setLayout(new BoxLayout(swDownPnl, BoxLayout.X_AXIS));
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    swUpPnl.setMaximumSize(new Dimension(200, 200));
    hourTxt.setBackground(Color.white);
    hourTxt.setForeground(UIManager.getColor("ToolBar.dockingForeground"));
    hourTxt.setEditable(false);
    secTxt.setBackground(Color.white);
    secTxt.setForeground(UIManager.getColor("ToolBar.dockingForeground"));
    secTxt.setEditable(false);
    swDownPnl.add(startBtn);
    swDownPnl.add(resetBtn);

    swUpPnl.add(Box.createHorizontalGlue());
    swUpPnl.add(hourTxt);
    swUpPnl.add(commaLbl);
    swUpPnl.add(secTxt);
    swUpPnl.add(Box.createHorizontalGlue());

    //swPnl.add(Box.createRigidArea(new Dimension(0, 120)));
    add(Box.createVerticalGlue());
    add(swUpPnl);
    add(Box.createRigidArea(new Dimension(0, 20)));
    add(swDownPnl);
    add(Box.createVerticalGlue());

    hourTxt.setFont(new java.awt.Font("Dialog", 1, 22));
    hourTxt.setText("00");
    hourTxt.setHorizontalAlignment(SwingConstants.CENTER);
    hourTxt.setBounds(new Rectangle(103, 43, 34, 35));

    commaLbl.setFont(new java.awt.Font("Dialog", 1, 24));
    commaLbl.setHorizontalAlignment(SwingConstants.CENTER);
    commaLbl.setText(":");
    commaLbl.setBounds(new Rectangle(142, 44, 7, 25));
    secTxt.setFont(new java.awt.Font("Dialog", 1, 22));
    secTxt.setText("00");
    secTxt.setHorizontalAlignment(SwingConstants.CENTER);
    secTxt.setBounds(new Rectangle(154, 43, 34, 35));

    startBtn.setBounds(new Rectangle(156, 115, 83, 23));
    startBtn.setFont(new java.awt.Font("Dialog", 0, 9));
    startBtn.setText("Start/Stop");
    startBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        startBtn_actionPerformed(e);
      }
    });
    startBtn.setActionCommand(START);

    resetBtn.setBounds(new Rectangle(63, 116, 83, 23));
    resetBtn.setFont(new java.awt.Font("Dialog", 0, 9));
    resetBtn.setText("Reset/Lap");
    resetBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        resetBtn_actionPerformed(e);
      }
    });
    resetBtn.setActionCommand(RESET);
  }

  /**
   * Adds an ActionListener to this presentation object.
   */
  public synchronized void addActionListener(ActionListener l) {
    listeners = AWTEventMulticaster.add(l, listeners);
  }

  /**
   * Removes an ActionListener from this presentation object.
   */
  public synchronized void removeActionListener(ActionListener l) {
    listeners = AWTEventMulticaster.remove(l, listeners);
  }
  
  private void updateTime(String min, String sec){
    hourTxt.setText(min);
    secTxt.setText(sec);
  }
  
  public void updateTime(int time) {
  	updateTime(int2string(time / SEC_PER_MIN), int2string(time % SEC_PER_MIN));
  }
  
  public void propertyChange(PropertyChangeEvent event) {
  	updateTime(((Integer) event.getNewValue()).intValue());
  }

  private String int2string(int v) {
    DecimalFormat df = new DecimalFormat("##");
    df.setMinimumIntegerDigits(2);
    return df.format((double)v);
  }

  void startBtn_actionPerformed(ActionEvent e) {
    listeners.actionPerformed(e);
  }

  void resetBtn_actionPerformed(ActionEvent e) {
    listeners.actionPerformed(e);
  }
}