package edu.luc.cs.trull.tl;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 * The class <code>Expr</code> is the common abstract superclass for
 * all temporal logic formula classes.
 * TODO THEJUS look at java.util.regex.Pattern.compile and come up with something similar for TL
 * look here for syntax: http://www-step.stanford.edu/tutorial/temporal-logic/temporal-logic.html
 * I propose the following syntax:
 * !, &&, || for the logical operators
 * a=>b implies, a$b since, a#b backto 
 * =a sofar (our allpast), +a once, -a previous 
 */

public abstract class Expr implements Cloneable {

  private static Logger cat = Logger.getLogger(Expr.class.getName());

  boolean val = true;
  boolean evaluated = false;

  List fringe;

  public Expr() { }

  public Expr(boolean v) { val = v; }

  public Object clone() {
    try {
      return super.clone();
    } catch (CloneNotSupportedException e) {
      throw new InternalError(this + ".clone() failed although Cloneable is implemented");
    }
  }

/**
 *		Sets the value of an expression. for
 *		obvious reasons, this is only available to base Expr's.
 *		NOTE: at each time instant, set must be called with the
 *		appropriate value, otherwise, it maintains values across
 *		invocations of eval(). (this could be changed, if necessary, to
 *		automatically set all Exprs to false at the end of each eval.)
 */

  public final void set (boolean val) { this.val = val; }

/**
 *		Returns the value of the formula as it
 *		was last eval()uated. subformulas can be ckec()ed.
 */

  public final boolean check () { return val; }

/**
 *		return time to "zero" by resetting the local state
 *		of this formula and all its subformulas. use only
 *		on top-level formulas.
 */

  public void reset () { }

/**
 *		Evaluates the formula, by recursively evaluating
 *		its subformulas. it is important that only top-level
 *		formulas be eval()uated, and only once during each "tick"
 *		of the system. each time eval is used on a top-level formula
 *		a "tick" takes place. once a formula is evaluated, its value
 *		and all its subformulas' value can be obtained through
 *		the check() method. in other words, eval modifies
 *		the states of the formulas and recurses, check does not.
 */

  public final synchronized boolean eval () {
    tick();
    return valuate ();
  }

  /**
   * Evaluates the formula for the given label.  Leaves
   * evaluate to true if and only if their label is equal to the
   * given one.
   *
   * @see edu.luc.cs.trull.tl.Leaf
   */

  public final synchronized boolean eval(Object label) {
    setLeaves(label);
    return eval();
  }

  /**
   * Traverses the temporal logic formula using a visitor.
   */

  void accept(Visitor v) {
    v.visitExpr(this);
  }

  /**
   * Advances the clock by one step.  Subclasses
   * should override this method to advance their subformulas.
   */

  public void tick () { evaluated = false; }

  /**
   * Evaluates the formula.  It should be overridden
   * by subclasses that represent composite formulas.
   */

  boolean ev () {
    cat.debug("ev() called");
    return val;
  }

  /**
   * Recursively computes the value of the formula
   * if necessary or returns the previously computed value.
   */

  final boolean valuate () {

    cat.debug("valuate() called");

    if (! evaluated) {
      val = ev();	// this calls the real ev(), which overrides the one
                        // in this base class.
      evaluated = true;	// note it, so that you do not have to call it again
    }
    return val;
  }

  List getFringe() {
    if (fringe == null) {
      FringeVisitor f = new FringeVisitor();
      accept(f);
      fringe = f.getFringe();
    }
    return fringe;
  }

  void setLeaves(Object label) {
    for (Iterator e = getFringe().iterator(); e.hasNext(); ) {
      Leaf l = (Leaf) e.next();
      l.set(label.equals(l.getLabel()));
    }
  }

  /**
   * Returns the labels that occur in the leaves of this formula.
   */

  public Object[] getLabels() {
    Object[] labels = new Object[getFringe().size()];
    int i = 0;
    for (Iterator e = getFringe().iterator(); e.hasNext(); i ++) {
      Leaf l = (Leaf) e.next();
      labels[i] = l.getLabel();
    }
    return labels;
  }

  public String toString() {
    PrintVisitor p = new PrintVisitor();
    accept(p);
    return p.getString();
  }
}

/**
 * A visitor that walks along the fringe of a temporal logic expression.
 */
final class FringeVisitor extends VisitorAdapter {
    private List fringe = new ArrayList();

    public void visitLeaf(Leaf e) {
        fringe.add(e);
    }

    public List getFringe() {
        return fringe;
    }
}

/**
 * A visitor that applies the same visit method to its argument without
 * further traversal. 
 */
abstract class UniformVisitor implements Visitor {
    public void visitExpr(Expr e) { visit(e); }
    public void visitLeaf(Leaf e) { visit(e); }
    public void visitAllPast(AllPast e) { visit(e); }
    public void visitAnd(And e) { visit(e); }
    public void visitBackTo(BackTo e) { visit(e); }
    public void visitImplies(Implies e) { visit(e); }
    public void visitNot(Not e) { visit(e); }
    public void visitOnce(Once e) { visit(e); }
    public void visitOr(Or e) { visit(e); }
    public void visitPrevious(Previous e) { visit(e); }
    public void visitSince(Since e) { visit(e); }
    public abstract void visit(Expr e);
}

/**
 * A visitor for converting a temporal logic expression to a String.
 */
final class PrintVisitor extends UniformVisitor {

    private StringBuffer buffer = new StringBuffer();
    private int currentPos = 0;

    public synchronized void visit(Expr e) {
        String name = e.getClass().getName();
        String prefix = "(" + name.substring(name.lastIndexOf(".") + 1);
        buffer.insert(currentPos, prefix);
        currentPos += prefix.length();
        buffer.append(")");
    }

    public synchronized void visitLeaf(Leaf e) {
        String label = "[" + e.getLabel() + "]";
        buffer.insert(currentPos, label);
        currentPos += label.length();
    }

    public String getString() {
        return buffer.toString();
    }
}
