package edu.luc.cs.trull;

/*
 * TODO IMPORTANT JavaBeans requirements such as serialization
 * TODO IMPORTANT check method args for validity
 * TODO IMPORTANT figure out how to use CVS keywords in conjunction with Javadoc 
 * TODO IMPORTANT massive JUnit testing 
 * TODO IMPORTANT improve introductory documentation incl. TUTORIAL 
 * TODO IMPORTANT make task progress reporting event-based
 * TODO RESEARCH deal properly with exceptions in user code
 * TODO RESEARCH investigate J2ME compatibility
 * TODO RESEARCH investigate middleware uses
 * TODO RESEARCH priorities (see if this is possible in an integrated way with Swing thread)
 * TODO RESEARCH relationship with jcc (Radha) and e (http://www.erights.org/) 
 * TODO RESEARCH dynamic changes to component structure
 * TODO parameterize task workers with task factories to make tasks parameterized (like predicates etc.)
 * TODO benchmarks for task framework (vary along various dimensions for different executors) 
 * TODO improve GUI for task demos: allow choices of workload, worker, etc. 
 * TODO consider throwing IllegalStateException if e.g. start is invoked twice
 * TODO clean up logging from within inner classes: this -> XYZ.this
 * DONE IMPORTANT finalize web start 
 * DONE IMPORTANT propagate javadoc comments from Triveni
 * DONE IMPORTANT non-GUI execution engines
 * DONE IMPORTANT parameterization by incoming events as in original Triveni
 * DONE IMPORTANT container-like interface: getComponent(i) etc.
 * DONE IMPORTANT general support for preemtable/suspendable emission
 * DONE IMPORTANT put back constructors for tree-style coding
 * DONE IMPORTANT subclasses of Control etc. for backward-compatibility
 * DONE IMPORTANT getters/setters for events and actions
 * DONE IMPORTANT branching await/watching using switch
 * DONE AwaitAll with empty event list: wait for any event
 * DONE consider hiding execution strategy in EmitComponent
 * DONE consider factoring out more commonalities from various combinators
 * DONE IMPORTANT implement TL with ASSERT combinator
 * and using reflection to check whether the component is also an event source
 * NOT DONE integrated source browsing from javadoc 
 * NOT DONE cloning (questionable -- Swing doesn't have it)
 * NOT DONE consider adding local/rename to all combinators (put in AbstractCombinator)
 * NOT DONE (use ActionComponent instead) consider using PropertyChangeListener as Trull component type
 */

import java.beans.PropertyChangeListener;

/**
 * A building block for a concurrent software system.  
 * A Trull component is a visual or nonvisual 
 * JavaBean that has event-driven reactive behavior in the form of 
 * zero or more of the following aspects:
 * <ul>
 * <li>Communication in the form of incoming and outgoing events 
 * (of type PropertyChangeEvent).</li>
 * <li>Lifecycle methods start, stop, suspend, and resume.</li>
 * <li>Termination in the form of outgoing events sent to  
 * another component, usually the parent in the component tree.</li>
 * </ul>
 * A class for a custom Trull component is usually declared
 * by subclassing from <code>Composite</code> and providing any
 * necessary instance variables for the object-oriented aspects
 * of the expression.  The event-driven aspects of the component
 * are then provided, usually in the constructor, by using the
 * method <code>addComponent</code> with Trull components that
 * provide the desired behavior.  Code that interacts with
 * the object-oriented aspects of a Trull component may be
 * embedded in the event-driven behavior in the form of actions,
 * event functions, predicates, and valuators.
 * @see java.beans.PropertyChangeEvent
 * @see edu.luc.cs.trull.Composite
 * @see edu.luc.cs.trull.EventFunction
 * @see edu.luc.cs.trull.EventPredicate
 * @see edu.luc.cs.trull.EventValuator
 */
public interface Component 
extends PropertyChangeListener, PropertyChangeSource, 
	Startable, Suspendable, Terminating {
}
