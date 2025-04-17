package edu.luc.cs.trull.tl;

import org.apache.log4j.Logger;

/**
 * The formula <code>p backto q</code> specifies that
 * either <code>p since q</code> is true or <code>always p</code> is true.
 *
 * @see Since
 * @see AllPast
 */

public class BackTo extends Expr { // p BackTo q (the lazy approach)

  private static Logger cat = Logger.getLogger(BackTo.class);

  private Expr p, q;

  public BackTo(Expr p, Expr q) {
    this.p = p;
    this.q = q;
    val = true;
  }

  public BackTo(Object p, Object q) {
    this(new Leaf(p), new Leaf(q));
  }

  public BackTo(Expr p, Object q) {
    this(p, new Leaf(q));
  }

  public BackTo(Object p, Expr q) {
    this(new Leaf(p), q);
  }

  public Object clone() {
    BackTo result = (BackTo) super.clone();
    result.p = (Expr) p.clone();
    result.q = (Expr) q.clone();
    return result;
  }

  public void reset () { val = true; p.reset(); q.reset(); }

  public void tick () { evaluated = false; p.tick(); q.tick(); }

  boolean ev () {
    boolean cp = p.valuate();
    boolean cq = q.valuate();

    cat.debug("ev() called");

    val = cq || (val && cp);
    return val;
  }

  void accept(Visitor v) {
    v.visitBackTo(this);
    p.accept(v);
    q.accept(v);
  }
}

