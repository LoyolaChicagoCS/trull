package edu.luc.cs.trull.demo.test;

import java.beans.PropertyChangeEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import edu.luc.cs.trull.EmitComponent;
import edu.luc.cs.trull.Util;

/**
 * A tester for Trull components, which reads event labels from
 * the console and passes it to the component.
 * <p>
 * Example: 
 * <pre>
 * new Composite(new ConsoleTester(), new MyCounter(...))
 * </pre>
 */
public class ConsoleTester extends EmitComponent {

	public static final String STOP = "stop";

	private Thread thread;

	public void start(PropertyChangeEvent event) {
		super.start(event);
		thread = new Thread(new Runnable() {
			public void run() {
				BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
				while (!Thread.interrupted()) {
					try {
						System.out.println();
						System.out.print("enter event label or '" + STOP + "'>");
						String input = in.readLine().trim();

						if (input == null || STOP.equals(input)) {
							break;
						}
						if (input.length() == 0) {
							continue;
						}

						//parse input and generate approperiate event to emit
						StringTokenizer tokens = new StringTokenizer(input, "=,", false);
						String label = tokens.nextToken();
						String[] values = new String[tokens.countTokens()];
						for (int i = 0; i < values.length; i++) {
							values[i] = tokens.nextToken();
						}
						scheduleEvent(label, values);
					} catch (IOException e) {
						break;
					} //stop
				}
				ConsoleTester.this.scheduleTermination();
			}
		});
		thread.start();
	}

	public void stop() {
		thread.interrupt();
		thread = null;
	}

	public void propertyChange(PropertyChangeEvent event) {
		System.out.println("received " + Util.toString(event));
	}
}