package edu.luc.cs.trull.tl;

/**
 * An empty visitor implementation. 
 */
class VisitorAdapter implements Visitor {
    public void visitExpr(Expr e) { }
    public void visitLeaf(Leaf e) { }
    public void visitAllPast(AllPast e) { }
    public void visitAnd(And e) { }
    public void visitBackTo(BackTo e) { }
    public void visitImplies(Implies e) { }
    public void visitNot(Not e) { }
    public void visitOnce(Once e) { }
    public void visitOr(Or e) { }
    public void visitPrevious(Previous e) { }
    public void visitSince(Since e) { }
}
