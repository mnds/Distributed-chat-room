/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sn.esp.dgi.sever;

import java.rmi.Remote;
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
     */
    public void saveMessage( String pseudo, String message );
    
    /**
     * Registering of a new client to the chat room
     * @param pseudo 
     */
    public void addClient( String pseudo );
    
    /**
     * 
     * @param sequenceNumber the sequence number stored by the client
     * @return the list of the new messages, since last retrieval of the client
     */
    public List<String> getNewMessages(int sequenceNumber);
}
