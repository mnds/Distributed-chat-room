/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sn.esp.dgi.server;

import java.rmi.Remote;
import sn.esp.dgi.sever.IChatRoom;

/**
 *
 * @author mnds
 */
public interface ILoadBalancer extends Remote {
    
    /**
     * Adds the given chat room to the list of handled chat rooms
     * @param chatRoom 
     */
    public void addChatRoom( IChatRoom chatRoom );
    
    /**
     * Removes the given chat room from the list of handled chat rooms
     * @param chatRoom 
     */
    public void removeChatRoom( IChatRoom chatRoom );
}
