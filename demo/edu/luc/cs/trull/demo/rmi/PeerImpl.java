package edu.luc.cs.trull.demo.rmi;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import org.apache.log4j.Logger;

import EDU.oswego.cs.dl.util.concurrent.Latch;
import EDU.oswego.cs.dl.util.concurrent.LinkedQueue;
import edu.luc.cs.trull.Component;
import edu.luc.cs.trull.DefaultTopLevelComponent;
import edu.luc.cs.trull.TopLevelComponent;
import edu.luc.cs.trull.swing.FastSwingScheduler;

/**
 * An RMI-based component suitable for a peer-to-peer architecture.  
 * The peer's behavior is provided by a Trull component.  
 * The peer implementation
 * creates its own registry on the local host and registers itself.
 * @see edu.luc.cs.trull.demo.rmi.Peer
 * @see edu.luc.cs.trull.Component
 */
public class PeerImpl extends UnicastRemoteObject implements Peer {

    private static Logger cat = Logger.getLogger(PeerImpl.class.getName());

    String selfName, peerName, selfHost, peerHost;
    Peer peer;
    Component component;
    TopLevelComponent top;
    Latch peerReady = new Latch();

    /**
     * An unbounded buffer to store events going to the remote peer.
     */
    final LinkedQueue remoteEventQueue = new LinkedQueue();

    /**
     * A consumer thread for asynchronous sending of events from the buffer
     * to the remote peer.
     */
    final Thread consumer = new Thread(new Runnable() {
        public void run() {
            while (! Thread.currentThread().isInterrupted()) {
                try {
                		PropertyChangeEvent event = (PropertyChangeEvent) remoteEventQueue.take();
                    try {
                        peer.propertyChange(event);
                    } catch (RemoteException r) {
                        while (! remoteEventQueue.isEmpty()) {
                            remoteEventQueue.take();
                        }
                        cat.error("peer update exception", r);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    });


    /**
     * @param component the Trull component that provides this peer's behavior.
     * @param selfHost the host name where this local peer is running.
     * @param selfName the name of this local peer object.
     * @param peerHost the host name where the remote peer is running.
     * @param peerName the name of the remote peer object.
     */
    public PeerImpl(Component component, String selfHost, String selfName, String peerHost, String peerName) throws RemoteException {
        this.component = component;
        this.selfName = selfName;
        this.peerName = peerName;
        this.selfHost = selfHost;
        this.peerHost = peerHost;
        top = new DefaultTopLevelComponent(component, new FastSwingScheduler());
    }

    /**
     * This method first sets up an RMI registry on selfHost and
     * registers the newly created peer on the registry under selfName.
     * It then waits for the peer peerName on peerHost to become
     * available.  Finally, it hooks the remote peer up as a listener to
     * the events sent out by the local peer.
     */
    public void start() throws InterruptedException {
        bindSelf();
        waitForPeer();
        addPeerListener();
        consumer.start();
        top.start();
    }

    /**
     * Add the peer as a listener to the expr.  Events are sent to the
     * peer asynchronously via the remoteEventQueue.
     */
    void addPeerListener() {
        component.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
            		try {
            			remoteEventQueue.put(event);
            		} catch (InterruptedException e) {
            			throw new RuntimeException("interrupted");
            		}
            }
        });
    }

    /**
     * Send any incoming events from outside to the expr.
     */
    public void propertyChange(PropertyChangeEvent event) throws RemoteException {
        try {
        		final PropertyChangeEvent localEvent = 
        			new PropertyChangeEvent(this, event.getPropertyName(), event.getOldValue(), event.getNewValue());
          top.propertyChange(localEvent);
        } catch (Exception e) {
            throw new RemoteException("self propertyChange exception: " + e.getMessage(), e);
        }
    }

    /**
     * Create a registry on the local host and register myself.
     */
    protected void bindSelf() {
        try {
            LocateRegistry.createRegistry(1099);
        } catch (RemoteException e) {
            // Perhaps our peer already created the registry on the local host
            cat.info("Using existing registry");
        }
        try {
            Naming.rebind("//" + selfHost + "/" + selfName, this);
            cat.info(selfName + " bound in registry on " + selfHost);
        } catch (Exception e) {
            cat.error("binding exception", e);
            System.exit(2);
        }
    }

    /**
     * Wait for the peer to become available on its host.  Once the peer
     * becomes available, tell the peer that we are available.
     */
    protected void waitForPeer() {
        if (! bindPeer()) {
            try {
                cat.info(selfName + " waiting for peer");
                peerReady.acquire();
            } catch (InterruptedException e) {
                cat.error("interrupted while waiting for peer", e);
                System.exit(2);
            }
        }
        cat.info(selfName + " found peer " + peerName + " on " + peerHost);
        bindPeer();
        try {
            peer.peerReady();
        } catch (RemoteException e) {
            cat.error("peer notification exception", e);
            System.exit(4);
        }
    }

    /**
     * Try to bind the peer and report whether the peer is ready.
     */
    protected boolean bindPeer() {
        try {
            peer = (Peer) Naming.lookup("//" + peerHost + "/" + peerName);
        } catch (Exception e) {
            cat.info(selfName + " could not find peer " + peerName + " on " + peerHost);
            return false;
        }
        return true;
    }

    /**
     * Invoked when the remote peer is ready.
     */
    public synchronized void peerReady() throws RemoteException {
        peerReady.release();
    }
}
