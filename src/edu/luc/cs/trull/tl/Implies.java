package edu.luc.cs.trull.tl;

/**
 * The formula <code>p => q</code> is defined as <code>(! p) \/ q</code>.
 *
 * @see Not
 * @see Or
 */

public class Implies extends Expr {

  private Expr p, q;

  public Implies(Expr p, Expr q) {
    this.p = p;
    this.q = q;
  }

  public Implies(Object p, Object q) {
    this(new Leaf(p), new Leaf(q));
  }

  public Implies(Expr p, Object q) {
    this(p, new Leaf(q));
  }

  public Implies(Object p, Expr q) {
    this(new Leaf(p), q);
  }

  public Object clone() {
    Implies result = (Implies) super.clone();
    result.p = (Expr) p.clone();
    result.q = (Expr) q.clone();
    return result;
  }

  public void reset () { p.reset(); q.reset(); }

  public void tick () { evaluated = false; p.tick(); q.tick(); }

  boolean ev () {
    boolean tp = p.valuate();
    boolean tq = q.valuate();

    val = tp ? tq : true;
    return val;
  }

  void accept(Visitor v) {
    v.visitImplies(this);
    p.accept(v);
    q.accept(v);
  }
}

