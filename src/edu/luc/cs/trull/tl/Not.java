package edu.luc.cs.trull.tl;

import org.apache.log4j.Logger;

/**
 * The formula <code>! p</code> specifies that
 * <code>p</code> must be false in the current time unit.
 */

public class Not extends Expr {

  private static Logger cat = Logger.getLogger(Not.class);

  private Expr e;

  public Not(Expr e) {
    this.e = e;
  }

  public Not(Object label) {
    this(new Leaf(label));
  }

  public Object clone() {
    Not result = (Not) super.clone();
    result.e = (Expr) e.clone();
    return result;
  }

  public void reset () { e.reset(); }

  public void tick () { evaluated = false; e.tick(); }

  boolean ev () {
    cat.debug("ev() called");

    boolean te = e.valuate();

    val = ! te; //(te ? false : true);
    return val;
  }

  void accept(Visitor v) {
    v.visitNot(this);
    e.accept(v);
  }
}

