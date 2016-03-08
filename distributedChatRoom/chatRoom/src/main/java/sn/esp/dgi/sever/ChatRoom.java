/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sn.esp.dgi.sever;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sn.esp.dgi.server.ILoadBalancer;

/**
 *
 * @author mnds
 */
public class ChatRoom extends UnicastRemoteObject implements IChatRoom {

    /**
     * The name of the chat room
     */
    private String name;
    
    /**
     * List of users registered to the chat room
     */
    private List<String> users;
    
    /**
     * Collection of (pseudo, message) pairs sent by the clients of the chat room
     */
    private List<String> messages;
    
    private static final Logger logger = Logger.getLogger("chatRoomLogger");
    
    /**
     * A reference to the load balancer
     */
    private ILoadBalancer loadBalancer;
    
    /**
     * Default constructor
     * @param name The name to give to the chat room
     * @throws java.rmi.RemoteException
     */
    public ChatRoom( String name ) throws RemoteException {
        this.name = name;
        users = new ArrayList<String>();
        messages = new ArrayList<String>();
        
        // TODO Retrieve a reference of the loadBalancer
    }
    
    public int postMessage(String pseudo, String message) throws RemoteException {
        logger.log(Level.INFO, "Nouveau message de {0}", pseudo);
        messages.add(pseudo + ": " + message);
        
        return messages.size();
    }

    public int subscribe(String pseudo) throws RemoteException {
        users.add(pseudo);
        logger.log(Level.INFO, "{0} vient de rejoindre le salon de discussion", pseudo);
        
        return messages.size();
    }

    public List<String> getNewMessages(String pseudo, int sequenceNumber) throws RemoteException {
        if( sequenceNumber > messages.size() ) {
            System.out.println("Récupération de nouveaux messages par " + pseudo);
            return messages.subList(sequenceNumber, messages.size());
        }
        
        return null;
    }

    public String getName() throws RemoteException {
        return name;
    }
    
}
