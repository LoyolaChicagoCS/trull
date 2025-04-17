package edu.luc.cs.trull;

/**
 * A basic component does nothing but never terminates.
 * Only one instance of this component is required.
 */
public class Nil extends EmptyComponent {

  /**
   * Sole constructor.
   */
	protected Nil() { }

  /**
   * The instance of this component.
   */
	private static final Nil INSTANCE = new Nil();
	
  /**
   * Returns the sole instance of this component.
   * @return the sole instance of this component.
   */
	public static Nil getInstance() { return INSTANCE; }
}
