package edu.luc.cs.trull.tl;

/**
 * The formula <code>leaf l</code> specifies that the current label
 * is <code>l</code>.
 */

public class Leaf extends Expr {

  private Object label;

  /**
   * The constructor of this class assumes that the label provided
   * for this leaf can be safely shared.
   */

  public Leaf(Object label) {
    this.label = label;
  }

  public Object clone() {
    Leaf result = (Leaf) super.clone();
    return result;
  }

  public Leaf(Object label, boolean v) {
    super(v);
    this.label = label;
  }

  public Object getLabel() {
    return label;
  }

  void accept(Visitor v) {
    v.visitLeaf(this);
  }
}
