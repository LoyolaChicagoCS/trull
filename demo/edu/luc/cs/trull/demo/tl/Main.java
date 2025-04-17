package edu.luc.cs.trull.demo.tl;

import edu.luc.cs.trull.Component;
import edu.luc.cs.trull.Composite;
import edu.luc.cs.trull.DefaultTopLevelComponent;
import edu.luc.cs.trull.TopLevelComponent;
import edu.luc.cs.trull.demo.AssertViolationListener;
import edu.luc.cs.trull.demo.test.SwingTester;
import edu.luc.cs.trull.swing.FastSwingScheduler;
import edu.luc.cs.trull.tl.Assert;
import edu.luc.cs.trull.tl.Expr;
import edu.luc.cs.trull.tl.Implies;
import edu.luc.cs.trull.tl.Once;
import edu.luc.cs.trull.tl.Previous;
import edu.luc.cs.trull.tl.Since;

/**
 * A demonstration of how to use Assert.
 */
public class Main {

  public static final String WORLD = "World";
  public static final String HELLO = "Hello";
  public static final String WHATUP = "WhatUp";
    
  public static void main(String[] args) throws Exception {
//    Expr e = new Leaf(WORLD);
//    Expr e = new Implies(WORLD, new Previous(HELLO));
//    Expr e = new Since(WORLD, HELLO);
//    Expr e = new Implies(WHATUP, new Not(new Since(new Not(WORLD), HELLO)));  
    Expr e = new Implies(WHATUP, new Implies(new Once(WORLD), new Previous(new Since(WORLD, HELLO))));
    Assert ass = new Assert(e, true);
    ass.addItemListener(new AssertViolationListener("ass", e));
    SwingTester tester = new SwingTester("Assertion Test");
    Component top = new Composite(ass, tester);
    TopLevelComponent wrapper = 
    	new DefaultTopLevelComponent(top, new FastSwingScheduler());
    wrapper.start();
  }
}
