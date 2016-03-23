/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sn.esp.dgi.core;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author mnds
 */
public interface IChatRoom extends Remote {
    
    /**
     * Saves the given message
     * @param pseudo
     * @param message 
     * @throws java.rmi.RemoteException 
     * @return the sequence number 
     */
    public int postMessage( String pseudo, String message) throws RemoteException;
    
    /**
     * Registering of a new client to the chat room
     * @param pseudo
     * @throws java.rmi.RemoteException
     * @return the sequence number
     */
    public int subscribe( String pseudo) throws RemoteException;
    
    /**
     * Unsubscribing the client whose pseudo is given
     * @param pseudo
     * @throws RemoteException 
     */
    public void unsubscribe( String pseudo ) throws RemoteException;
    
    /**
     * 
     * @param pseudo the value of pseudo
     * @param sequenceNumber the sequence number stored by the client
     * @throws java.rmi.RemoteException
     * @return the list of new messages posted since last retrieval
     */
    public String getNewMessages(String pseudo, int sequenceNumber) throws RemoteException;
    
    /**
     * 
     * @param pseudo
     * @return the sequence number of the chat room, i.e the number of messages sent to it
     * @throws RemoteException 
     */
    public int getSequenceNumber( String pseudo ) throws RemoteException;
    
    /**
     * 
     * @return the name of the chat room
     * @throws java.rmi.RemoteException
     */
    public String getName() throws RemoteException;
    
    /**
     * @return the address of the chat room
     * @throws RemoteException 
     */
    public String getAddress() throws RemoteException;
}
