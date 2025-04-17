package edu.luc.cs.trull.tl;

/**
 * The formula <code>l /\ r</code> specifies that
 * both <code>l</code> and <code>r</code> must be true in the
 * current time unit.
 */

public class And extends Expr {

  private Expr l, r;

  public And(Expr l, Expr r) {
    this.l = l;
    this.r = r;
  }

  public And(Object l, Object r) {
    this(new Leaf(l), new Leaf(r));
  }

  public And(Expr l, Object r) {
    this(l, new Leaf(r));
  }

  public And(Object l, Expr r) {
    this(new Leaf(l), r);
  }

  public Object clone() {
    And result = (And) super.clone();
    result.l = (Expr) l.clone();
    result.r = (Expr) r.clone();
    return result;
  }

  public void reset () {  l.reset(); r.reset(); }

  public void tick () { evaluated = false; l.tick(); r.tick(); }

  boolean ev () {
    boolean tl = l.valuate();
    boolean tr = r.valuate();

    val = tl && tr;
    return val;
  }

  void accept(Visitor v) {
    v.visitAnd(this);
    l.accept(v);
    r.accept(v);
  }
}
