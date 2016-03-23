/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sn.esp.dgi.server;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import sn.esp.dgi.core.IChatRoom;
import sn.esp.dgi.core.ILoadBalancerREST;
import sn.esp.dgi.core.Strings;

/**
 *
 * @author mnds
 */
@Path("/methods")
@Produces(MediaType.APPLICATION_XML)
public class LoadBalancerREST implements ILoadBalancerREST {

    /**
     * The class logger
     */
    private static final Logger logger = Logger.getLogger("loadBalancerRESTLogger");

    /**
     * Forwards a message to a chat room
     *
     * @param chatRoomName
     * @param pseudo
     * @param message
     * @return the sequence number of the chat room
     */
    @Override
    @POST
    @Path("{chatroom}/{pseudo}")
    public Response postMessage(@PathParam("chatroom") String chatRoomName,
            @PathParam("pseudo") String pseudo, String message) {

        IChatRoom chatRoom = LoadBalancerEJB.chatRooms.get(chatRoomName);
        int chatRoomSequenceNumber = 0;

        if (chatRoom != null) {
            try {
                chatRoomSequenceNumber = chatRoom.postMessage(pseudo, message);
                logger.log(Level.INFO, "Forwarding a message of {0}", pseudo);
            } catch (RemoteException ex) {
                Logger.getLogger(LoadBalancerREST.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return Response.accepted(String.valueOf(chatRoomSequenceNumber)).build();
    }

    /**
     * Forwards a request to get messages sent since last retrieval by the
     * client whose pseudo is given
     *
     * @param chatRoomName
     * @param pseudo
     * @param sequenceNumber
     * @return The list of new messages
     */
    @Override
    @GET
    @Path("{chatroom}/{pseudo}")
    public Response getNewMessages(@PathParam("chatroom") String chatRoomName,
            @PathParam("pseudo") String pseudo, @QueryParam("sequence") int sequenceNumber) {

        IChatRoom chatRoom = LoadBalancerEJB.chatRooms.get(chatRoomName);
        String messages = null;

        if (chatRoom != null) {
            try {
                messages = chatRoom.getNewMessages(pseudo, sequenceNumber);
            } catch (RemoteException ex) {
                Logger.getLogger(LoadBalancerREST.class.getName()).log(Level.SEVERE, null, ex);
            }
            logger.log(Level.INFO, "Retrieving new messages received by {0}", pseudo);
        }

        return Response.ok(messages).build();
    }

    /**
     *
     * @return the set of chat rooms registered in the load balancer
     */
    @Override
    @GET
    public Response getChatRoomsList() {
        Strings chatRoomsList = null;
        if( LoadBalancerEJB.chatRooms != null ) {
            chatRoomsList = new Strings(LoadBalancerEJB.chatRooms.keySet());
        }
        
        return Response.ok(chatRoomsList).build();
    }

    /**
     * Allows the client whose pseudo is given to subscribe to a chat room
     *
     * @param pseudo
     * @param chatRoomName
     * @return
     */
    @Override
    @POST
    @Path("join/{chatroom}/{pseudo}")
    public Response joinChatRoom(@PathParam("chatroom") String chatRoomName,
            @PathParam("pseudo") String pseudo) {

        int chatRoomSequenceNumber = -1; // Initialized to the error value

        if (LoadBalancerEJB.chatRooms != null && !LoadBalancerEJB.chatRooms.isEmpty()) {
            IChatRoom chatRoom = LoadBalancerEJB.chatRooms.get(chatRoomName);

            if (chatRoom != null) {
                try {
                    chatRoomSequenceNumber = chatRoom.subscribe(pseudo);
                    logger.log(Level.INFO, "Forwarding a subscription request of {0}", pseudo);
                } catch (RemoteException ex) {
                    Logger.getLogger(LoadBalancerREST.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return Response.accepted(String.valueOf(chatRoomSequenceNumber)).build();
    }
    
    @Override
    @PUT
    @Path("quit/{chatroom}/{pseudo}")
    public Response quitChatRoom( @PathParam("chatroom") String chatRoomName,
            @PathParam("pseudo") String pseudo ) {

        int status = -1; // Initialized to an error value
        if (LoadBalancerEJB.chatRooms != null && !LoadBalancerEJB.chatRooms.isEmpty()) {
            IChatRoom chatRoom = LoadBalancerEJB.chatRooms.get(chatRoomName);

            if (chatRoom != null) {
                try {
                    chatRoom.unsubscribe(pseudo);
                    logger.log(Level.INFO, "Forwarding an unsubscription request of {0}", pseudo);
                    status = 0; // Everything is ok
                } catch (RemoteException ex) {
                    Logger.getLogger(LoadBalancerREST.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return Response.accepted(String.valueOf(status)).build(); // in case of error
    }
    
    @Override
    @GET
    @Path("sequence/{chatroom}/{pseudo}")
    public Response getSequenceNumber( @PathParam("chatroom") String chatRoomName,
            @PathParam("pseudo") String pseudo) {

        int chatRoomSequenceNumber = -1; // Initialized to the error value
        if (LoadBalancerEJB.chatRooms != null && !LoadBalancerEJB.chatRooms.isEmpty()) {
            IChatRoom chatRoom = LoadBalancerEJB.chatRooms.get(chatRoomName);

            if (chatRoom != null) {
                try {
                    chatRoomSequenceNumber = chatRoom.getSequenceNumber(pseudo);
                    logger.log(Level.INFO, "Forwarding a sequence number retrieval of {0}", pseudo);
                } catch (RemoteException ex) {
                    Logger.getLogger(LoadBalancerREST.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return Response.accepted(String.valueOf(chatRoomSequenceNumber)).build();
    }
}
