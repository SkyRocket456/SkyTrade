package ClientHandling;
/*
 * EE422C Final Project submission by
 * Replace <...> with your actual data.
 * Kenneth Emeremnu
 * kie226
 * 17835
 * Slip days used: <1>
 * Spring 2021
 */

import ClientHandling.Readers.LoginReader;
import ClientHandling.Readers.SignUpReader;
import Items.Items;
import ServerSide.Log;
import ServerSide.Server;
import User.Account;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * Class that allows the server to communicate with each individual client
 */
public class ClientHandler implements Runnable {
    public Server server;
    public Socket socket;
    public ClientObserver client;
    public ObjectInputStream receiver;
    public ObjectOutputStream sender;
    public Account user;

    public ClientHandler(Server s, Socket clientSocket) {
        try {
            this.server = s;
            this.socket = clientSocket;
            this.client = new ClientObserver();
            server.addObserver(client);
            sender = new ObjectOutputStream(clientSocket.getOutputStream());
            receiver = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            server.deleteObserver(client);
        }
    }

    /**
     * Send data to client
     *
     * @param o the object being sent
     */
    public void sendData(Object o) {
        try {
            sender.writeObject(new Gson().toJson(o));
        } catch (SocketException | JsonSyntaxException e) {
            server.deleteObserver(client);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds owned products to the gridpane on MySkyTrade
     *
     * @return the data from the client
     */
    private Object receiveData() {
        try {
            return new Gson().fromJson((String) receiver.readObject(), HashMap.class);
        } catch (SocketException | JsonSyntaxException e) {
            server.deleteObserver(client);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("IO error or Class not Found");
        }
        return null;
    }


    /**
     * The communication between server and client, Only ends when the client closes
     */
    public void run() {
        sendData(new Object[]{Items.productIDs, Items.Products});
        while (true) {
            try {
                // data is only null is the user closes the program
                HashMap<String, String> data = (HashMap<String, String>) receiveData();
                switch (data.get("CODE")) {
                    case "IS_SIGNUP_VALID":
                        SignUpReader.Q.put(new ClientData(this, data));
                        break;
                    case "GUEST":
                        CodeProcessing.newGuest(this, data);
                        break;
                    case "NEW_ACCOUNT":
                        CodeProcessing.newAccount(this, data);
                        break;
                    case "LOGIN":
                        LoginReader.Q.put(new ClientData(this, data));
                        break;
                    case "BID":
                        // When a client sends a valid bid, process it
                        Server.BidReaders.get(data.get("product ID")).Q.put(new ClientData(this, data));
                        break;
                }
            } catch (NullPointerException e) {
                sendData(null);
                if (user != null && user.password == null) {
                    Server.Usernames.remove(user.username);
                    Server.Emails.remove(user.email);
                }
                Log.socketDisconnected(socket, user);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Class that only exists to notify each and every client of any changes in SkyTrade (new bid, an auction ended, etc)
     */
    class ClientObserver implements Observer {

        /**
         * Is called whenever SkyTrade is changed in some way
         *
         * @param arg the data to send to all clients
         * @param o   ignore
         */
        @Override
        public void update(Observable o, Object arg) {
            sendData(arg);
        }
    }
}
