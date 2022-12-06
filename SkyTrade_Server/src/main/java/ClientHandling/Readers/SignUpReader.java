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
import User.Account;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Class that deals with the signup process for each individual user
 */
public class SignUpReader implements Runnable {
    public static LinkedBlockingQueue<ClientData> Q = new LinkedBlockingQueue<>();

    /**
     * Processes each user login one by one to achieve synchronization
     */
    @Override
    public void run() {
        // Wait for a new signup to come and process it
        while (true) {
            try {
                ClientData c_and_data = Q.take();
                ClientHandler c = c_and_data.clientHandler;
                HashMap<String, String> data = c_and_data.data;

                HashMap<String, String> signup_check = CodeProcessing.ValidateSignUpInfo(data.get("username"), data.get("email"));

                // Make temporary new user instance just in case client closes window in the middle of sign-up
                c.user = new Account(signup_check.get("username"), null, signup_check.get("email"), null, null, null,
                        null, null, null, null, null);

                c.sendData(signup_check);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
