/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sn.esp.dgi.core;

import java.util.Map;

/**
 *
 * @author mnds
 */
public interface ILoadBalancerRemote{
    
    /**
     * Adds the given chat room to the list of handled chat rooms
     * @param name The name of the chat room
     * @param address the address of the chat room
     */
    public void addChatRoom( String name, String address );
    
    /**
     * Removes the given chat room from the list of handled chat rooms
     * @param name The name of the chat room to remove
     */
    public void removeChatRoom( String name );

}
