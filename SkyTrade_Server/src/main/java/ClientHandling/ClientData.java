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

import java.util.HashMap;

public class ClientData {
    public ClientHandler clientHandler;
    public HashMap<String, String> data;

    public ClientData(ClientHandler clientHandler, HashMap<String, String> data) {
        this.clientHandler = clientHandler;
        this.data = data;
    }
}