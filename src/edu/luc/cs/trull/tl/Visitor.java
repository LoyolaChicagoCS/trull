package edu.luc.cs.trull.tl;

/**
 * A visitor for temporal logic expressions.
 */
interface Visitor {
    void visitExpr(Expr e);
    void visitLeaf(Leaf e);
    void visitAllPast(AllPast e);
    void visitAnd(And e);
    void visitBackTo(BackTo e);
    void visitImplies(Implies e);
    void visitNot(Not e);
    void visitOnce(Once e);
    void visitOr(Or e);
    void visitPrevious(Previous e);
    void visitSince(Since e);
}
