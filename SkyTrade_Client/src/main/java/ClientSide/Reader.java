package ClientSide;
/*
 * EE422C Final Project submission by
 * Replace <...> with your actual data.
 * Kenneth Emeremnu
 * kie226
 * 17835
 * Slip days used: <1>
 * Spring 2021
 */

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.SynchronousQueue;

/**
 * Class whose job is to wait for information from the server
 */
public class Reader implements Runnable {
    public static ArrayList<String> productIDs;
    public static LinkedTreeMap<String, LinkedTreeMap> Products;

    public static SynchronousQueue<HashMap<String, String>> username_and_email_q = new SynchronousQueue<>();
    public static SynchronousQueue<HashMap<String, String>> account_q = new SynchronousQueue<>();
    public static SynchronousQueue<HashMap<String, String>> bid_q = new SynchronousQueue<>();

    /**
     * Starts the Reader
     */
    @Override
    public void run() {
        try {
            while (true) {
                // If this try catch ever receives null, then that means the user closed the window
                try {
                    HashMap<String, String> data = (HashMap<String, String>) Client.receiveData(HashMap.class);
                    switch (data.get("CODE")) {
                        // Server checked to see if username and email weren't in use
                        case "IS_SIGNUP_VALID":
                            username_and_email_q.put(data);
                            break;
                        // Server checked to see if you have an account
                        case "LOGIN":
                            account_q.put(data);
                            break;
                        // When a new bid from another client or this client is read from server, update this client
                        case "BID":
                            CodeProcessing.newBid(data);
                            break;
                        // When a new bid from another client or this client is read from server, update this client
                        case "BID_RESULT":
                            bid_q.put(data);
                            break;
                        // When an auction ends on a product, update this client
                        case "PRODUCT_WINNER":
                            CodeProcessing.ProductWinner(data);
                            break;
                    }
                } catch (NullPointerException e) {
                    // Cancel all timers and exit
                    countDown.abort_q.put(true);
                    break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
