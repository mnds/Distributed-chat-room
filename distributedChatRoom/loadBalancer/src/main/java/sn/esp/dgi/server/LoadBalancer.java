/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sn.esp.dgi.server;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import sn.esp.dgi.sever.IChatRoom;

/**
 *
 * @author mnds
 */
@Remote(ILoadBalancer.class)
@Singleton
@Path("/loadbalancer")
public class LoadBalancer implements ILoadBalancer {

    /**
     * List of available chat rooms
     */
    private Map<String, IChatRoom> chatRooms;

    /**
     * The class logger
     */
    private static final Logger logger = Logger.getLogger("loadBalancerLogger");

    /**
     * Initialization of the session bean
     */
    @PostConstruct
    public void init() {
        // Initializing the chat rooms list
        chatRooms = new HashMap<String, IChatRoom>();
    }

    public void addChatRoom(IChatRoom chatRoom) {
        // Adding a new chat room to the list of available chat rooms
        try {
            chatRooms.put(chatRoom.getName(), chatRoom);
            logger.log(Level.INFO, "Enregistrement d'un salon de discussion {0}", chatRoom.getName());
        } catch (RemoteException ex) {
            Logger.getLogger(LoadBalancer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removeChatRoom(IChatRoom chatRoom) {
        // Removing a chat room from the list of available chat rooms
        try {
            chatRooms.remove(chatRoom.getName());
            logger.log(Level.INFO, "Suppression du salon de discussion {0}", chatRoom.getName());
        } catch (RemoteException ex) {
            Logger.getLogger(LoadBalancer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Forwards a message to a chat room
     *
     * @param chatRoomName
     * @param pseudo
     * @param message
     * @return the sequence number of the chat room
     */
    @POST
    @Path("{chatroom}/{pseudo}")
    public int postMessage( @PathParam("chatroom") String chatRoomName,
            @PathParam("pseudo") String pseudo, String message) {
        
        IChatRoom chatRoom = chatRooms.get(chatRoomName);
        int chatRoomSequenceNumber = 0;
        
        if (chatRoom != null) {
            try {
                chatRoomSequenceNumber = chatRoom.postMessage(pseudo, message);
                logger.log(Level.INFO, "Forwarding a message of {0}", pseudo);
            } catch (RemoteException ex) {
                Logger.getLogger(LoadBalancer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return chatRoomSequenceNumber;
    }
    
    /**
     * Forwards a request to get messages sent since last retrieval by the client whose pseudo is given
     * @param chatRoomName
     * @param pseudo
     * @param sequenceNumber
     * @return The list of new messages
     */
    @GET
    @Path("{chatroom}/{pseudo}")
    public List<String> getNewMessages( @PathParam("chatroom") String chatRoomName,
            @PathParam("pseudo") String pseudo, @QueryParam("sequence") int sequenceNumber ) {
        
        IChatRoom chatRoom = chatRooms.get(chatRoomName);
        List<String> messages = null;
        
        if (chatRoom != null) {
            try {
                messages = chatRoom.getNewMessages(pseudo, sequenceNumber);
                logger.log(Level.INFO, "Retrieving new messages received by {0}", pseudo);
            } catch (RemoteException ex) {
                Logger.getLogger(LoadBalancer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return messages;
    } 
    
    /**
     * 
     * @return the set of chat rooms registered in the load balancer
     */
    @GET
    public Set<String> getChatRoomsList() {
        return chatRooms.keySet();
    }
    
    /**
     * Allows the client whose pseudo is given to subscribe to a chat room
     * @param pseudo
     * @param chatRoomName
     * @return 
     */
    @POST
    @Path("join/{chatroom}/{pseudo}")
    public int joinChatRoom( @PathParam("chatroom") String chatRoomName,
            @PathParam("pseudo") String pseudo ) {
        
        IChatRoom chatRoom = chatRooms.get(chatRoomName);
        int chatRoomSequenceNumber = 0;
        
        if (chatRoom != null) {
            try {
                chatRoomSequenceNumber = chatRoom.subscribe(pseudo);
                logger.log(Level.INFO, "Forwarding a subscription request of {0}", pseudo);
            } catch (RemoteException ex) {
                Logger.getLogger(LoadBalancer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return chatRoomSequenceNumber;
    }
}
