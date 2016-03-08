/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sn.esp.dgi.server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import sn.esp.dgi.sever.IChatRoom;

/**
 *
 * @author mnds
 */
@Remote(ILoadBalancer.class)
@Singleton
public class LoadBalancer implements ILoadBalancer {

    /**
     * List of available chat rooms
     */
    private List<IChatRoom> chatRooms;
    
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
        chatRooms = new ArrayList<IChatRoom>();
    }
    
    public void addChatRoom(IChatRoom chatRoom) {
        // Adding a new chat room to the list of available chat rooms
        chatRooms.add(chatRoom);
        try {
            logger.log(Level.INFO, "Enregistrement d'un salon de discussion {0}", chatRoom.getName());
        } catch (RemoteException ex) {
            Logger.getLogger(LoadBalancer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removeChatRoom(IChatRoom chatRoom) {
        // Removing a chat room from the list of available chat rooms
        chatRooms.remove(chatRoom);
        try {
            logger.log(Level.INFO, "Suppression du salon de discussion {0}", chatRoom.getName());
        } catch (RemoteException ex) {
            Logger.getLogger(LoadBalancer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
