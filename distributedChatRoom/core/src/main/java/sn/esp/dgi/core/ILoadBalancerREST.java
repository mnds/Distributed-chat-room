/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sn.esp.dgi.core;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 *
 * @author mnds
 */
public interface ILoadBalancerREST {
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
    public Response postMessage( @PathParam("chatroom") String chatRoomName,
            @PathParam("pseudo") String pseudo, String message);

    /**
     * Forwards a request to get messages sent since last retrieval by the client whose pseudo is given
     * @param chatRoomName
     * @param pseudo
     * @param sequenceNumber
     * @return The list of new messages
     */
    @GET
    @Path("{chatroom}/{pseudo}")
    public Response getNewMessages( @PathParam("chatroom") String chatRoomName,
            @PathParam("pseudo") String pseudo, @QueryParam("sequence") int sequenceNumber );
    
    /**
     * 
     * @return the set of chat rooms registered in the load balancer
     */
    @GET
    public Response getChatRoomsList();
    
    /**
     * Allows the client whose pseudo is given to subscribe to a chat room
     * @param pseudo
     * @param chatRoomName
     * @return 
     */
    @POST
    @Path("join/{chatroom}/{pseudo}")
    public Response joinChatRoom( @PathParam("chatroom") String chatRoomName,
            @PathParam("pseudo") String pseudo );
    
    @PUT
    @Path("quit/{chatroom}/{pseudo}")
    public Response quitChatRoom( @PathParam("chatroom") String chatRoomName,
            @PathParam("pseudo") String pseudo );
    
    @GET
    @Path("sequence/{chatroom}/{pseudo}")
    public Response getSequenceNumber( @PathParam("chatroom") String chatRoomName,
            @PathParam("pseudo") String pseudo);
}
