/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sn.esp.dgi.server;

import java.io.Serializable;
import sn.esp.dgi.core.IChatRoom;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;

/**
 *
 * @author mnds
 */
public class ChatRoom extends UnicastRemoteObject implements IChatRoom, Serializable {

    /**
     * The name of the chat room
     */
    private String name;

    /**
     * The address of the chat room
     */
    private String address;
    
    /**
     * List of users registered to the chat room
     */
    private List<String> users;

    /**
     * Collection of (pseudo, message) pairs sent by the clients of the chat
     * room
     */
    private List<String> messages;

    private static final Logger logger = Logger.getLogger("chatRoomLogger");

    /**
     * Default constructor
     *
     * @param name The name to give to the chat room
     * @param address
     * @throws java.rmi.RemoteException
     * @throws javax.naming.NamingException
     */
    public ChatRoom(String name, String address) throws RemoteException, NamingException {
        this.name = name;
        this.address = address;
        users = new ArrayList<String>();
        messages = new ArrayList<String>();
    }

    @Override
    public int postMessage(String pseudo, String message) throws RemoteException {
        logger.log(Level.INFO, "Nouveau message de {0}", pseudo);
        messages.add(pseudo + ": " + message);

        return messages.size();
    }

    @Override
    public int subscribe(String pseudo) throws RemoteException {
        users.add(pseudo);
        logger.log(Level.INFO, "{0} vient de rejoindre le salon de discussion", pseudo);

        return messages.size();
    }

    @Override
    public void unsubscribe(String pseudo) throws RemoteException {
        users.remove(pseudo);
        logger.log(Level.INFO, "{0} vient de quitter le salon de discussion", pseudo);
    }
        
    @Override
    public String getNewMessages(String pseudo, int sequenceNumber) throws RemoteException {
        if (sequenceNumber < messages.size()) {
            logger.log(Level.INFO, "Récupération de nouveaux messages par {0} ", pseudo);
            StringBuilder sb = new StringBuilder();
            
            for( int i = sequenceNumber; i < messages.size(); i++ ) {
                sb.append(messages.get(sequenceNumber)).append("\n");
                return sb.toString();
            }
        }

        return null;
    }

    @Override
    public int getSequenceNumber(String pseudo) throws RemoteException {
        logger.log(Level.INFO, "Récupération de numéro de séquence par {0} ", pseudo);
        return messages.size();
    }
    
    @Override
    public String getName() throws RemoteException {
        return name;
    }

    @Override
    public String getAddress() throws RemoteException {
        return address;
    }

}
