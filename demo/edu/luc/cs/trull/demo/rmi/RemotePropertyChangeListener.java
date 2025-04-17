package edu.luc.cs.trull.demo.rmi;

// TODO BENEDYKT port ClickCounterRmi from 337 to Trull and add as example
// TODO BENEDYKT remote example using xmlrpc  

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.beans.PropertyChangeEvent;

/**
 * A remote listener for PropertyChangeEvents.
 */
public interface RemotePropertyChangeListener extends Remote {

    /**
     * Invoked when a labeled event occurs.
     *
     * @exception RemoteException if any exception occurs on the remote side.
     */

    void propertyChange(PropertyChangeEvent event) throws RemoteException;
}
