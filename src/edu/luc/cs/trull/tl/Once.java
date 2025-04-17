package edu.luc.cs.trull.tl;

import org.apache.log4j.Logger;

/**
 * The formula <code>once p</code> specifies that
 * <code>p</code> must have been true sometime
 * in the history of this system run.
 */

public class Once extends Expr { // Once p

  private static Logger cat = Logger.getLogger(Once.class);

  private Expr p;

  public Once(Expr p) {
    this.p = p;
    val = false;
  }

  public Once(Object p) {
    this(new Leaf(p));
  }

  public Object clone() {
    Once result = (Once) super.clone();
    result.p = (Expr) p.clone();
    return result;
  }

  public void reset () { val = false; p.reset(); } // added val = false;

  public void tick () { evaluated = false; p.tick(); }

  boolean ev () {
    boolean cp = p.valuate();

    cat.debug("ev() called");

    val = val ? true : cp;
    return val;
  }

  void accept(Visitor v) {
    v.visitOnce(this);
    p.accept(v);
  }
}

