package edu.luc.cs.trull.tl;

import org.apache.log4j.Logger;

/**
 * The formula <code>p since q</code>
 * specifies that <code>q</code> must have been true sometime in the past
 * history of this system run, and that <code>p</code> must have been true
 * in every time unit since the last time <code>q</code> was true.
 *
 * @see edu.luc.cs.trull.tl.BackTo
 */

public class Since extends Expr { // p Since q

  private static Logger cat = Logger.getLogger(Since.class);

  private Expr p, q;

  public Since(Expr p, Expr q) {
    this.p = p;
    this.q = q;
    val = false; // KL: this seems to be required
  }

  public Since(Object p, Object q) {
    this(new Leaf(p), new Leaf(q));
  }

  public Since(Expr p, Object q) {
    this(p, new Leaf(q));
  }

  public Since(Object p, Expr q) {
    this(new Leaf(p), q);
  }

  public Object clone() {
    Since result = (Since) super.clone();
    result.p = (Expr) p.clone();
    result.q = (Expr) q.clone();
    return result;
  }

  public void reset () { val = false; p.reset(); q.reset(); }

  public void tick () { evaluated = false; p.tick(); q.tick(); }

  boolean ev () {
    boolean cp = p.valuate();
    boolean cq = q.valuate();

    cat.debug("ev() called");

    val = cq || (val && cp);
    return val;
  }

  void accept(Visitor v) {
    v.visitSince(this);
    p.accept(v);
    q.accept(v);
  }
}

