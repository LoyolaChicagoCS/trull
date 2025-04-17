package edu.luc.cs.trull.tl;

import org.apache.log4j.Logger;

/**
 * The formula <code>always p</code> specifies that <code>p</code>
 * must have been true for the entire past history of this system run.
 */

public class AllPast extends Expr { // has-always-been

  private static Logger cat = Logger.getLogger(AllPast.class);

  private Expr p;

  public AllPast(Expr p) {
    this.p = p;
    val = true;
  }

  public AllPast(Object label) {
    this(new Leaf(label));
  }

  public Object clone() {
    AllPast result = (AllPast) super.clone();
    result.p = (Expr) p.clone();
    return result;
  }

  public void reset () { p.reset(); }

  public void tick () { evaluated = false; p.tick(); }

  boolean ev () {
    boolean cp = p.valuate();

    cat.debug("ev() called");

    val = val ? cp : false;
    return val;
  }

  void accept(Visitor v) {
    v.visitAllPast(this);
    p.accept(v);
  }
}
