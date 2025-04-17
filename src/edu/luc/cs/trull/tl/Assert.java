package edu.luc.cs.trull.tl;

import java.awt.AWTEventMulticaster;
import java.awt.ItemSelectable;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.luc.cs.trull.EmptyComponent;

/**
 * An item-selectable component that wraps around a temporal logic assertion.
 * For each <code>PropertyChangeEvent</code> of interest to the temporal logic
 * expression, an <code>ItemEvent</code> indicating the current status 
 * of the temporal logic expression is sent to the item listeners of the
 * component.
 *
 * @see edu.luc.cs.trull.tl.Expr
 * @see java.awt.ItemSelectable
 * @see java.awt.event.ItemEvent
 */
public class Assert extends EmptyComponent implements ItemSelectable {

	private static Logger logger = Logger.getLogger(Assert.class);

	private Expr assertion;
	private ItemListener itemListener;
	private boolean previous;
	private Set labels;

	/**
	 * @param assertion the temporal logic expression for this assertion.
	 * @exception IllegalArgumentException if <code>expr</code> is null.
	 */
	public Assert(Expr assertion, boolean initial) {
		if (assertion == null) {
			throw new IllegalArgumentException(
					"triveni.Assertion.<init>: expr null");
		}
		this.assertion = assertion;
		this.previous = initial;
		this.labels = new HashSet(Arrays.asList(assertion.getLabels()));
	}

	public Assert(Expr assertion) {
		this(assertion, true);
	}

	public void addItemListener(ItemListener il) {
		itemListener = AWTEventMulticaster.add(itemListener, il);
	}

	public void removeItemListener(ItemListener il) {
		itemListener = AWTEventMulticaster.remove(itemListener, il);
	}

	public Object[] getSelectedObjects() {
		return null;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String label = evt.getPropertyName();
		if (!labels.contains(label)) {
			if (logger.isDebugEnabled()) logger.debug(this + ": ignoring " + label);
			return;
		}
		boolean current = assertion.eval(label);
		if (logger.isDebugEnabled()) logger.debug(this + ": " + label + " -> " + current);
		if (current != previous && itemListener != null) {
			int stateChange = current ? ItemEvent.SELECTED : ItemEvent.DESELECTED;
			itemListener.itemStateChanged(new ItemEvent(this,
					ItemEvent.ITEM_STATE_CHANGED, label, stateChange));
		}
		previous = current;
	}
}