/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sn.esp.dgi.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import sn.esp.dgi.core.Strings;

/**
 *
 * @author mnds
 */
public class ChatRoomClient extends TimerTask {

    private String title = "Logiciel de discussion en ligne";
    private String pseudo = null;
    private boolean connected = false;
    private final static Logger logger = Logger.getLogger(ChatRoomClient.class.getName());
    
    Strings chatRoomsList;
    int selectedChatRoomIndex = -1;

    private Client restClient;
    private WebTarget target;

    private javax.swing.JButton btnChoose = new JButton("Choisir le salon de discussion");
    private javax.swing.JList jChatRoomsList = new JList();
    private javax.swing.JScrollPane scroller = new JScrollPane();
    private JFrame window = new JFrame(this.title);
    private JTextArea txtOutput = new JTextArea();
    private JTextField txtMessage = new JTextField();
    private JButton btnSend = new JButton("Envoyer");

    private int sequenceNumber = 0;

    public ChatRoomClient() {
        restClient = ClientBuilder.newClient();

        displayChatRoomsList();
        this.requestPseudo();
    }

    /**
     * GUI for choosing a chat room
     */
    private void displayChatRoomsList() {

        String host = "http://localhost:8080/loadBalancer/ws/methods";
        target = restClient.target(host);

        // Requesting the list of chat rooms
        Invocation.Builder builder = target.request();
        builder.header("Accept", "application/xml");
        Invocation invocation = builder.buildGet();
        Response response = invocation.invoke();
        chatRoomsList = response.readEntity(Strings.class);

        jChatRoomsList.setModel(new AbstractListModel() {

            public int getSize() {
                return chatRoomsList.size();
            }

            public Object getElementAt(int index) {
                return chatRoomsList.get(index);
            }
        });

        jChatRoomsList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                selectedChatRoomIndex = lsm.getMinSelectionIndex();
            }
        });

        scroller.setViewportView(jChatRoomsList);

        btnChoose.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                chooseChatRoom();
            }
        });

        JPanel panel = (JPanel) this.window.getContentPane();
        panel.add(jChatRoomsList, BorderLayout.NORTH);
        panel.add(btnChoose, BorderLayout.SOUTH);
        this.window.setSize(500, 400);
        this.window.setVisible(true);
    }

    private void chooseChatRoom() {
        String selectedChatRoom = chatRoomsList.get(selectedChatRoomIndex);

        try {
            String host = "http://localhost:8080/loadBalancer/ws/methods/join/" + URLEncoder.encode(selectedChatRoom, "UTF8") + "/" + pseudo;
            target = restClient.target(host);

            // Requesting the list of chat rooms
            Invocation.Builder builder = target.request();
            builder.header("Accept", "application/xml");
            Invocation invocation = builder.buildPost(null);
            Response response = invocation.invoke();
            sequenceNumber = Integer.valueOf(response.readEntity(String.class));

            if (sequenceNumber == -1) { // There is an error
                JOptionPane.showMessageDialog(window, "Erreur lors de la connexion au salon de discussion " + selectedChatRoom,
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(window, "Vous êtes maintenant connecté au chat " + selectedChatRoom,
                        "Info", JOptionPane.INFORMATION_MESSAGE);
                connected = true;
                createIHM(); // Showing the textbox for writing messages
            }
        } catch (UnsupportedEncodingException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    private void createIHM() {
        // Assemblage des composants
        JPanel panel = (JPanel) this.window.getContentPane();
        panel.removeAll();
        JScrollPane sclPane = new JScrollPane(txtOutput);
        panel.add(sclPane, BorderLayout.CENTER);
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(this.txtMessage, BorderLayout.CENTER);
        southPanel.add(this.btnSend, BorderLayout.EAST);
        panel.add(southPanel, BorderLayout.SOUTH);

        // Gestion des évènements
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    window_windowClosing(e);
                } catch (RemoteException ex) {
                    Logger.getLogger(ChatRoomClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnSend_actionPerformed(e);
            }
        });
        txtMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent event) {
                if (event.getKeyChar() == '\n') {
                    btnSend_actionPerformed(null);
                }
            }
        });

        // Initialisation des attributs
        this.txtOutput.setBackground(new Color(220, 220, 220));
        this.txtOutput.setEditable(false);
        this.window.setSize(500, 400);
        this.window.setVisible(true);
        this.txtMessage.requestFocus();
    }

    private void requestPseudo() {
        this.pseudo = JOptionPane.showInputDialog(
                this.window, "Entrez votre pseudo : ",
                this.title, JOptionPane.OK_OPTION
        );
        if (this.pseudo == null) {
            System.exit(0);
        }
    }

    public void window_windowClosing(WindowEvent e) throws RemoteException {
        String selectedChatRoom = chatRoomsList.get(selectedChatRoomIndex);
        int exitStatus = -1;
        
        try {
            String host = "http://localhost:8080/loadBalancer/ws/methods/quit/" + URLEncoder.encode(selectedChatRoom, "UTF8") + "/" + pseudo;
            target = restClient.target(host);
            Invocation invocation = target.request(MediaType.APPLICATION_XML).buildPut(Entity.entity(pseudo, MediaType.WILDCARD_TYPE));
            Response response = invocation.invoke();
            exitStatus = Integer.valueOf(response.readEntity(String.class));

            if (exitStatus == -1) { // There is an error
                JOptionPane.showMessageDialog(window, "Erreur lors de la déconnexion du salon de discussion " + selectedChatRoom,
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(window, "Vous êtes maintenant déconnecté du salon de discussion " + selectedChatRoom,
                        "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (UnsupportedEncodingException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        
        System.exit(exitStatus);
    }

    public void btnSend_actionPerformed(ActionEvent e) {
        try {
            String message = this.txtMessage.getText();
            if (message == null || message.isEmpty()) {
                return;
            }

            String selectedChatRoom = chatRoomsList.get(selectedChatRoomIndex);
            String host = "http://localhost:8080/loadBalancer/ws/methods/" + URLEncoder.encode(selectedChatRoom, "UTF8") + "/" + pseudo;

            target = restClient.target(host);

            // Requesting the list of chat rooms
            Invocation.Builder builder = target.request();
            builder.header("Accept", "application/xml");
            Invocation invocation = builder.buildPost(Entity.entity(message, MediaType.WILDCARD_TYPE));
            Response response = invocation.invoke();

            this.txtMessage.setText("");
            this.txtMessage.requestFocus();
        } catch (UnsupportedEncodingException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        
        // Nothing to do if the user hasn't joined a chat room yet
        if( !connected ) {
            return;
        }
        
        String host;
        String selectedChatRoom = chatRoomsList.get(selectedChatRoomIndex);
        int serverSequenceNumber;

        try {
            // Checking if the client sequence number and the server sequence number are synchronized
            host = "http://localhost:8080/loadBalancer/ws/methods/sequence/" + URLEncoder.encode(selectedChatRoom, "UTF8") + "/" + pseudo;
            target = restClient.target(host);
            Invocation getSequenceNumberInvocation = target.request(MediaType.APPLICATION_XML).buildGet();
            Response getSequenceNumberResponse = getSequenceNumberInvocation.invoke();
            serverSequenceNumber = Integer.valueOf(getSequenceNumberResponse.readEntity(String.class));

            if (serverSequenceNumber > sequenceNumber) { // Retrieving new messages if desynchronized
                host = "http://localhost:8080/loadBalancer/ws/methods/" + URLEncoder.encode(selectedChatRoom, "UTF8") + "/" + pseudo;
                target = restClient.target(host);

                // Requesting the list of chat rooms
                Invocation invocation = target.queryParam("sequence", sequenceNumber).request(MediaType.APPLICATION_XML).buildGet();
                Response response = invocation.invoke();
                String newMessages = response.readEntity(String.class);
                displayMessage(newMessages);

                // Synchronizing the sequence numbers
                sequenceNumber = serverSequenceNumber;
            }
        } catch (UnsupportedEncodingException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public void displayMessage(String messages) {
        this.txtOutput.append(messages + "\n");
    }

    public static void main(String[] args) {
        Timer timer = new Timer();
        // Search for new messages every 5 seconds
        timer.schedule(new ChatRoomClient(), 0, 5000);
    }

}
