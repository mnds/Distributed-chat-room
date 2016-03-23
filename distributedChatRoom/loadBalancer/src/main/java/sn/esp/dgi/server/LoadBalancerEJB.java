/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sn.esp.dgi.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import sn.esp.dgi.core.ILoadBalancerRemote;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import sn.esp.dgi.core.IChatRoom;

/**
 *
 * @author mnds
 */
@Remote(ILoadBalancerRemote.class)
@LocalBean
@Singleton
@Startup
public class LoadBalancerEJB implements ILoadBalancerRemote {

    /**
     * List of available chat rooms
     */
    protected static Map<String, IChatRoom> chatRooms;

    /**
     * The class logger
     */
    private static final Logger logger = Logger.getLogger("loadBalancerEJBLogger");

    /**
     * Initialization of the session bean
     */
    @PostConstruct
    public void init() {
        // Initializing the chat rooms list
        chatRooms = new HashMap<>();
        logger.log(Level.INFO, "Aiguilleur démarré !");
    }

    @Override
    public void addChatRoom(String name, String address) {
        IChatRoom chatRoom;
        try {
            chatRoom = (IChatRoom) Naming.lookup(address);
            chatRooms.put(chatRoom.getName(), chatRoom);
            logger.log(Level.INFO, "Enregistrement du salon de discussion {0}", chatRoom.getName());
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            Logger.getLogger(LoadBalancerEJB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void removeChatRoom(String name) {
        // Removing a chat room from the list of available chat rooms
        chatRooms.remove(name);
        logger.log(Level.INFO, "Suppression du salon de discussion {0}", name);
    }

}
