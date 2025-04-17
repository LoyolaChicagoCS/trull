package edu.luc.cs.trull.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import edu.luc.cs.trull.tl.Expr;

/**
 * A tracker of temporal logic assertions in Trull.
 * When an assertion fails, an orange popup window appears.
 * The user is
 * given the option of aborting the program or ignoring the
 * failed assertion.  
 * Assertions can be given names for identification.
 *
 * @see edu.luc.cs.trull.tl.Assert
 * @see edu.luc.cs.trull.tl.Expr
 */
public class AssertViolationListener implements ItemListener {

	private static Logger cat = Logger.getLogger(AssertViolationListener.class);

	private static final int HISTORY_LIMIT = 10;
	private int historyLimit;
	private List history = new ArrayList();
	private String name;
	boolean ignore = false;
	boolean dismiss = false;
	Expr assertion;

	public AssertViolationListener(String name, Expr assertion, int historyLimit) {
		if (name == null) {
			throw new IllegalArgumentException(
					"triveni.AssertionListener.<init>: name null");
		}
		this.name = name;
		this.assertion = assertion;
		this.historyLimit = historyLimit;
	}

	public AssertViolationListener(String name, Expr assertion) {
		this(name, assertion, HISTORY_LIMIT);
	}

	public AssertViolationListener(String name) {
		this(name, null, HISTORY_LIMIT);
	}

	public void itemStateChanged(ItemEvent evt) {
		Object label = evt.getItem();
		history.add(label);
		if (history.size() > historyLimit) {
			history.remove(0);
		}
		boolean valid = evt.getStateChange() == ItemEvent.SELECTED;
		if (cat.isInfoEnabled()) cat.info(name + ": " + label + " -> " + valid);
		if (! valid && ! dismiss) {
			reportViolation();
		}
	}

	private void reportViolation() {
		final JFrame frame = new JFrame(name);

		JTextField assertionText = new JTextField(assertion == null
				? "not available"
				: assertion.toString(), 40);
		assertionText.setEditable(false);
		assertionText.setBackground(Color.white);

		JPanel assertionPanel = new JPanel();
		assertionPanel.setOpaque(true);
		assertionPanel.setBackground(Color.ORANGE);
		assertionPanel.add(new JLabel("Assertion " + name + " failed:"));
		assertionPanel.add(assertionText);

		JList historyList = new JList(history.toArray());
		historyList.setSelectedIndex(history.size() - 1);
		JScrollPane scrollableList = new JScrollPane(historyList);

		JButton abortButton = new JButton("Abort");
		abortButton.setBackground(Color.red);
		abortButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				System.exit(1);
			}
		});

		JButton ignoreButton = new JButton("Ignore");
		ignoreButton.setBackground(Color.yellow);
		ignoreButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				ignore = true;
				frame.dispose();
			}
		});

		JButton dismissButton = new JButton("Ignore all");
		dismissButton.setBackground(Color.yellow);
		dismissButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				dismiss = true;
				frame.dispose();
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 0));
		buttonPanel.add(abortButton);
		buttonPanel.add(ignoreButton);
		buttonPanel.add(dismissButton);

		Container pane = frame.getContentPane();

		pane.setLayout(new BorderLayout());
		pane.setBackground(Color.orange);
		pane.add(BorderLayout.NORTH, assertionPanel);
		pane.add(BorderLayout.WEST, new JLabel("Event history:"));
		pane.add(BorderLayout.CENTER, scrollableList);
		pane.add(BorderLayout.SOUTH, buttonPanel);

		frame.setContentPane(pane);

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		frame.pack();
		frame.setVisible(true);
	}
}