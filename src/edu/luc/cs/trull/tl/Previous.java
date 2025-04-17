package edu.luc.cs.trull.tl;

import org.apache.log4j.Logger;

/**
 * The formula <code>previous p</code> specifies
 * that <code>p</code> must have been true in the
 * time unit immediately preceding the current one.
 */

public class Previous extends Expr {

  private static Logger cat = Logger.getLogger(Previous.class);

  private Expr p;
  private boolean prev=false;

  public Previous(Expr p) {
    this.p = p;
  }

  public Previous(Object p) {
    this(new Leaf(p));
  }

  public Object clone() {
    Previous result = (Previous) super.clone();
    result.p = (Expr) p.clone();
    return result;
  }

  public void reset () { prev = false; p.reset(); }

  public void tick () { evaluated = false; p.tick(); }

  boolean ev () {
    boolean pp = prev;
    prev = p.valuate();

    cat.debug("ev() called");

    val = pp;
    return val;
  }

  void accept(Visitor v) {
    v.visitPrevious(this);
    p.accept(v);
  }
}

