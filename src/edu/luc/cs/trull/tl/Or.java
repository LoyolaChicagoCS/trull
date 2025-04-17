package edu.luc.cs.trull.tl;

/**
 * The formula <code>p \/ q</code> specifies that either <code>p</code>
 * or <code>q</code> or both are true in the current time unit.
 */

public class Or extends Expr {

  private Expr l, r;

  public Or(Expr l, Expr r) {
    this.l = l;
    this.r = r;
  }

  public Or(Object l, Object r) {
    this(new Leaf(l), new Leaf(r));
  }

  public Or(Expr l, Object r) {
    this(l, new Leaf(r));
  }

  public Or(Object l, Expr r) {
    this(new Leaf(l), r);
  }

  public Object clone() {
    Or result = (Or) super.clone();
    result.l = (Expr) l.clone();
    result.r = (Expr) r.clone();
    return result;
  }

  public void reset () { l.reset(); r.reset(); }

  public void tick () { evaluated = false; l.tick(); r.tick(); }

  boolean ev () {
    boolean tl = l.valuate();
    boolean tr = r.valuate();

    val = tl || tr;
    return val;
  }

  void accept(Visitor v) {
    v.visitOr(this);
    l.accept(v);
    r.accept(v);
  }
}

