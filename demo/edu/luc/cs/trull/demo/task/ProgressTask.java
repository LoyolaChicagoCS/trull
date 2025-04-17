package edu.luc.cs.trull.demo.task;

import javax.swing.JProgressBar;

import edu.luc.cs.trull.task.Task;

/**
 * A wrapper class for showing the progress of any task in a progress bar.
 * The original task is expected to return its current progress as the
 * result of the next() method.
 */
public class ProgressTask implements Task {

  JProgressBar progressBar;

  Task task;

  public ProgressTask(Task task, JProgressBar progressBar, int max) {
    this.task = task;
    this.progressBar = progressBar;
    progressBar.setMaximum(max);
    progressBar.setValue(0);
    progressBar.setStringPainted(true);
  }

  public void restart() {
    task.restart();
    progressBar.setValue(0);
  }

  public boolean hasNext() { return task.hasNext(); }

  public Object next() {
    Object result = task.next();
//    System.out.println(task + ": " + result);
    progressBar.setValue(((Integer) result).intValue());
    return result;
  }
}