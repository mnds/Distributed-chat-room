/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sn.esp.dgi.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import sn.esp.dgi.core.IChatRoom;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import sn.esp.dgi.core.ILoadBalancerRemote;

/**
 *
 * @author mnds
 */
public class ChatRoomLauncher {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java ChatRoomLauncher <nomChatRoom>");
            System.exit(-1);
        }

        String chatRoomName = args[0];
        int port = 6672;
        String chatRoomAddress = "//127.0.0.1:" + port + "/" + chatRoomName;

        try {
            IChatRoom chatRoom = new ChatRoom(chatRoomName, chatRoomAddress);
            LocateRegistry.createRegistry(port);
            Naming.rebind(chatRoomAddress, chatRoom);

            // Retrieving a reference to the loadBalancer
            Properties props = new Properties();
            props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.impl.SerialInitContextFactory");
            props.setProperty(Context.STATE_FACTORIES, "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
            props.setProperty(Context.URL_PKG_PREFIXES, "com.sun.enterprise.naming");
            props.setProperty("org.omg.CORBA.ORBInitialHost", "localhost");
            props.setProperty("org.omg.CORBA.ORBInitialPort", "3700");

            Context ctx = new InitialContext(props);
            ILoadBalancerRemote loadBalancer = (ILoadBalancerRemote) ctx.lookup("java:global/loadBalancer/LoadBalancerEJB!sn.esp.dgi.core.ILoadBalancerRemote");
            loadBalancer.addChatRoom(chatRoomName, chatRoomAddress);
            
            Logger.getLogger(ChatRoomLauncher.class.getName())
                    .log(Level.INFO, "Salon de discussion {0} démarré", chatRoomName);
        } catch (NamingException ex) {
            Logger.getLogger(ChatRoomLauncher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(ChatRoomLauncher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ChatRoomLauncher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
