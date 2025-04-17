package edu.luc.cs.trull.demo.rmi;

import java.rmi.RemoteException;

/**
 * An  interface for an RMI-based peer suitable for
 * a peer-to-peer architecture.
 *
 * @see edu.luc.cs.trull.demo.rmi.PeerImpl
 */
public interface Peer extends RemotePropertyChangeListener {

    /**
     * Invoked when this peer's remote peer is ready.
     */
    void peerReady() throws RemoteException;

    void start() throws InterruptedException, RemoteException;
}
