package ClientHandling.Readers;
/*
 * EE422C Final Project submission by
 * Replace <...> with your actual data.
 * Kenneth Emeremnu
 * kie226
 * 17835
 * Slip days used: <1>
 * Spring 2021
 */

import ClientHandling.ClientHandler;
import ClientHandling.ClientData;
import ClientHandling.CodeProcessing;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Class that deals with the bidding process for each individual product
 */
public class BidReader implements Runnable {
    public LinkedBlockingQueue<ClientData> Q = new LinkedBlockingQueue<>();

    /**
     * Processes each bid one by one to achieve synchronization
     */
    @Override
    public void run() {
        // Wait for a new bid to come and process it
        while (true) {
            try {
                ClientData c_and_data = Q.take();

                ClientHandler c = c_and_data.clientHandler;
                HashMap<String, String> data = c_and_data.data;

                c.sendData(CodeProcessing.newBid(c, data));
            } catch (InterruptedException ignored) {
            }
        }
    }
}
