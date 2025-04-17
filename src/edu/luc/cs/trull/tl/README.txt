//
// tl.java - Carlos Puchol, June 1997.
//
//	$Id: README.txt,v 1.1 2004/06/10 21:24:08 laufer Exp $
//
// This package implements temporal logic expressions in java.
//
// It provides a base class called Expr (for atomic expressions or
// events in the system) and a set of classes, NotExpr, AndExpr,
// etc, each corresponding to a temporal logic formula, which is
// built on top of other formulas.
//
// Each expression has the following "signature":
//
//	eval: void -> boolean
//		a method that evaluates the formula, by recursively evaluating
//		its subformulas. it is important that only top-level
//		formulas be eval()uated, and only once during each "tick"
//		of the system. each time eval is used on a top-level formula
//		a "tick" takes place. once a formula is evaluated, its value
//		and all its subformulas' value can be obtained through
//		the check() method. in other words, eval modifies
//		the states of the formulas and recurses, check does not.
//
//	set: boolean -> void
//		a method to set the value of an expression. for
//		obvious reasons, this is only available to base Expr's.
//		NOTE: at each time instant, set must be called with the
//		appropriate value, otherwise, it maintains values across
//		invocations of eval(). (this could be changed, if necessary, to
//		automatically set all Exprs to false at the end of each eval.)
//
//	reset: void -> void
//		return time to "zero" by resetting the local state
//		of this formula and all its subformulas. use only
//		on top-level formulas.
//
//	check: void -> boolean
//		returns the value of the formula as it
//		was last eval()uated. subformulas can be ckec()ed.
//
//	checkStr: void -> String
//		exactly like check, but returns "true" or "false", as strings.
//
// For instance, to create a formula such as: F: A => Prev B, do this:
//
//		Expr B = new Expr();
//		PreviousExpr Aux0 = new PreviousExpr(B);
//		Expr A = new Expr();
//		ImpliesExpr F = new ImpliesExpr(A, Aux0);
//
// To evaluate this formula over time, you typically do this:
//
//		F.reset();
//		for each tick:
//			A.set(some bool value);
//			B.set(some other bool value);
//			boolean v = F.eval();
//			<use v, or F.check() repeatedly>
//		end for
//
// Note that it is possible to use a formula more than once
// in building new formulas, even if it has "internal state",
// formulas are evaluated *exactly* once per each eval() to
// top-level formulas.
