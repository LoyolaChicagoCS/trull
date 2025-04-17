package edu.luc.cs.trull.test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.AssertionFailedError;

import org.apache.log4j.Logger;


/**
 * Support for unit-testing a component.
 */
public class Tester implements PropertyChangeListener {

	private static final Logger logger = Logger.getLogger(Tester.class);
	
    List history = new ArrayList();
    /*eventmap maps an event name to the most recent event*/
    Map eventMap = new HashMap();

    public synchronized void propertyChange(PropertyChangeEvent event) {
        logger.info("entering propertyChange()");
    	history.add(event.getPropertyName());
        String eventLabel = event.getPropertyName();
        /*check if eventLabel already exists in the eventMap as a key*/
        /*store the label as the key and event as the value*/
        if (eventMap.containsKey(eventLabel)) {
        	/*key exists, update the value*/
        	eventMap.put(eventLabel,event);
        } else {
        	/*key doesn't exist*/
        	eventMap.put(eventLabel,event);
        }
        /*iterate through and print the eventMap for debugging purposes*/
        logger.debug("event map:");
        for (Iterator i=eventMap.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry e = (Map.Entry) i.next();
            PropertyChangeEvent event1 = (PropertyChangeEvent)e.getValue();
            Object obj = event1.getNewValue();
            logger.debug(e.getKey() + ": " + obj);
        }
    }
    
    public synchronized void clear() { history.clear(); /*time = -1;*/ }

    /**
     * Returns the value associated with an eventlabel
     */
    public synchronized Object getValue(String eventLabel) {
    	if (eventMap.containsKey(eventLabel)) {
        	/*key exists, return the value of the event as an object*/
        	PropertyChangeEvent event = (PropertyChangeEvent)eventMap.get(eventLabel);
        	
        	Object mapEventValue = event.getNewValue();
        	return mapEventValue;
        } else {
        	/*key doesn't exist*/
        	throw new AssertionFailedError("Event " + eventLabel +  " was not emitted");
        }
    }

    public synchronized List getLabels() { return history; }

    /**
     * Matches the value associated with an eventlabel with the value 
     * existing in the event map
     */
    public synchronized void matchValue(String eventLabel, Object eventValue) {
    	/*check if eventLabel already exists in the eventMap as a key*/
        if (eventMap.containsKey(eventLabel)) {
        	/*key exists, check the value to see if it matches*/
        	PropertyChangeEvent event = (PropertyChangeEvent)eventMap.get(eventLabel);
        	
        	Object mapEventValue = event.getNewValue();
        	/*check to see if the value of the parameter event value object matches*/
        	if (!(eventValue.equals(mapEventValue))) {
        		throw new AssertionFailedError("FAIL: value = " + eventValue.toString() + " expected = " + mapEventValue.toString());
        	}  
        } else {
        	/*key doesn't exist*/
        	throw new AssertionFailedError("Event " + eventLabel +  " was not emitted");
        }	
    }

    public synchronized void matchLast(Object label) {
        matchOrdered(new Object[] { label });
    }

    public synchronized void matchOrdered(Object[] labels) {
        logger.info("inside matchOrdered()");
    	List theLabels = Arrays.asList(labels);
        if (! theLabels.equals(history)) {
            throw new AssertionFailedError("FAIL: labels = " + history + " expected ordered = " + theLabels);
        }
        //iterate through the key set of eventmap for debugging purposes*/
        logger.debug("keys from eventMap");
        Iterator iter = eventMap.keySet().iterator();
        while (iter.hasNext()) {
        	logger.debug(iter.next() + ",");
        }
        logger.debug("labels from history");
        logger.debug(theLabels);
    }

    public synchronized void matchAll(Object[] labels) {
    	logger.info("inside matchAll()");
        List theLabels = Arrays.asList(labels);
        if (! (new HashSet(theLabels)).equals(new HashSet(history))) {
        	  throw new AssertionFailedError("FAIL: labels = " + history + " expected all = " + theLabels);
        }
        /*iterate through the keyset for debugging purposes*/
        Iterator iter = eventMap.keySet().iterator();
        while (iter.hasNext()) {
        	logger.debug(iter.next());
        }
        if (! (new HashSet(theLabels)).equals(eventMap.keySet())) {
      	  throw new AssertionFailedError("FAIL: labels = " + history + " expected all = " + eventMap.keySet());
        }
    }

    public synchronized void matchSome(Object[] labels) {
    	logger.info("Entering MatchSome()");
        Set theLabels = new HashSet(Arrays.asList(labels));
        if (! history.containsAll(theLabels)) {
            theLabels.removeAll(history);
            throw new AssertionFailedError("FAIL: labels = " + history + " expected missing = " + theLabels);
        }
        if (! eventMap.keySet().containsAll(theLabels)) {
            theLabels.removeAll(eventMap.keySet());
            throw new AssertionFailedError("FAIL: labels = " + history + " expected missing = " + theLabels);
        }
    }

    public synchronized void matchSome(Object label) {
        matchSome(new Object[] { label });
    }
}
