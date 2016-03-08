/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sn.esp.dgi.sever;

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
     * 
     * @param pseudo the value of pseudo
     * @param sequenceNumber the sequence number stored by the client
     * @throws java.rmi.RemoteException
     * @return the list of new messages posted since last retrieval
     */
    public List<String> getNewMessages(String pseudo, int sequenceNumber) throws RemoteException;
    
    /**
     * 
     * @return the name of the chat room
     * @throws java.rmi.RemoteException
     */
    public String getName() throws RemoteException;
}
