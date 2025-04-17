package edu.luc.cs.trull.demo.wristwatch;

import javax.swing.JFrame;
import javax.swing.Timer;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import edu.luc.cs.trull.TopLevelComponent;

/**
 * A driver for running this demo.
 */
public class Main implements EventLabels {

  public static void main(String[] args) throws Exception {
    // access component factory
    XmlBeanFactory factory = new XmlBeanFactory(
      new ClassPathResource("edu/luc/cs/trull/demo/wristwatch/wristwatch.xml"));

    // top-level control logic (translation)
    TopLevelComponent controller = (TopLevelComponent) factory.getBean("controller");
    // timer providing the clock ticks
    Timer timer = (Timer) factory.getBean("timer");
    // frame for presentation
    JFrame view = (JFrame) factory.getBean("frame");
    
    // start wristwatch
    controller.start();
    // start timer
    timer.start();
    // start GUI
    view.pack();
    view.setVisible(true);
  }
}