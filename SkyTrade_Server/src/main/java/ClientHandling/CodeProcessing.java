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

import Items.Item;
import Items.Items;
import Items.countDown;
import ServerSide.Log;
import ServerSide.MongoDB;
import ServerSide.Server;
import User.Account;
import User.Email;
import User.ProductInfo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;


/**
 * Class that deals with processing data from the client
 */
public class CodeProcessing {

    /**
     * Check to see if the client's desired username and email is not already in use
     *
     * @param username the username client wants
     * @param email    the email the client wants to use
     * @return data to send back to the client regarding their desired credentiials
     */
    public static HashMap<String, String> ValidateSignUpInfo(String username, String email) {
        HashMap<String, String> data = new HashMap<>();
        data.put("CODE", "IS_SIGNUP_VALID");
        if (Server.Usernames.contains(username)) {
            data.put("username_check", "Username is already in use");
        }
        if (Server.Emails.contains(email)) {
            data.put("email_check", "Email is already in use");
        } else if (!Email.validateEmail(email)) {
            data.put("email_check", "Invalid email address");
        }
        if (data.get("username_check") == null && data.get("email_check") == null) {
            String OTP = String.format("%06d", new Random().nextInt(999999));
            data.put("OTP", OTP);
            String subject = "Verify your email for SkyTrade";
            Email.SendEmail(email, subject, Email.OTPMessage(OTP));

            data.put("username", username);
            data.put("email", email);

            Server.Usernames.add(username);
            Server.Emails.add(email);
        }
        return data;
    }

    /**
     * When a client successfully logs in as a guest, update everything on the server
     *
     * @param clientHandler the client that is now a guest
     * @param data          the data from the client
     */
    public static void newGuest(ClientHandler clientHandler, HashMap<String, String> data) {
        Account user = new Account(data.get("username"), null, data.get("email"),
                data.get("shipping_address"), data.get("city"), data.get("state"), data.get("zip_code"),
                data.get("card_number"), data.get("name_on_card"), data.get("expdate"), data.get("CVV"));

        countDown.countDown_Accounts.put(user.username, user);
        clientHandler.user = user;
        Log.newGuestJoined(clientHandler.socket, data.get("username"));
    }

    /**
     * When a client successfully logs into their account, update everything on the server
     *
     * @param clientHandler the client that is now a guest
     * @param data          the data from the client
     */
    public static void newAccount(ClientHandler clientHandler, HashMap<String, String> data) {
        HashMap<String, String> credentials = new HashMap<>();
        credentials.put("CODE", "LOGIN");
        credentials.put("username", data.get("username"));
        credentials.put("password", data.get("password"));

        Account user = new Account(data.get("username"), data.get("password"), data.get("email"),
                data.get("shipping_address"), data.get("city"), data.get("state"), data.get("zip_code"),
                data.get("card_number"), data.get("name_on_card"), data.get("expdate"), data.get("CVV"));

        countDown.countDown_Accounts.put(user.username, user);

        Server.Accounts.put(credentials, user);
        Log.newAccountCreated(clientHandler.socket, data.get("username"));
    }

    /**
     * When a client logs in to their account, send them their account information
     *
     * @param clientHandler the client that is now a guest
     * @param data          the data from the client
     * @return their account data
     */
    public static HashMap<String, String> checkLogin(ClientHandler clientHandler, HashMap<String, String> data) {
        HashMap<String, String> account_check = new HashMap<>();
        account_check.put("CODE", "LOGIN");
        Account user = Server.Accounts.get(data);

        if (user != null) {
            account_check.put("username", user.username);
            account_check.put("email", user.email);
            account_check.put("password", user.password);
            account_check.put("shipping_address", user.shipping_address);
            account_check.put("city", user.city);
            account_check.put("state", user.state);
            account_check.put("zip_code", user.zip_code);
            account_check.put("card_number", user.card_number);
            account_check.put("name_on_card", user.name_on_card);
            account_check.put("expdate", user.expdate);
            account_check.put("CVV", user.CVV);
            account_check.put("bidded_items", new Gson().toJson(user.bidded_items));
            account_check.put("owned_items", new Gson().toJson(user.owned_items));
            clientHandler.user = user;
            Log.userHasLoggedIn(clientHandler.socket, user.username);

            MongoDB.addCustomerToDatabase(user);
        }
        return account_check;
    }

    /**
     * When a client enters a new valid bid, update it on the server and on the database
     *
     * @param clientHandler the client that is now a guest
     * @param data          the data from the client
     * @return the data to send to the client for the new bid
     */
    public static HashMap<String, String> newBid(ClientHandler clientHandler, HashMap<String, String> data) {
        HashMap<String, String> bid_result = new HashMap<>();
        bid_result.put("CODE", "BID_RESULT");

        Item product = Items.Products.get(data.get("product ID"));

        try {
            if (Double.parseDouble(data.get("bid amount")) > product.price + 9) {
                product.bid_history.add(new ArrayList<>(Arrays.asList(clientHandler.user.username, "$" + data.get("bid amount"), data.get("bid time"))));
                product.numberBids++;
                product.price = Double.parseDouble(data.get("bid amount"));

                HashMap<String, String> bid = new HashMap<>();

                bid.put("CODE", "BID");
                bid.put("bidder", clientHandler.user.username);
                bid.put("bid amount", data.get("bid amount"));
                bid.put("bid time", data.get("bid time"));
                bid.put("productID", data.get("product ID"));

                Server.server.notifyObservers(bid);
                Log.newBid(clientHandler.user.username, data.get("bid amount"), data.get("title"), data.get("product ID"));

                MongoDB.updateProductOnDatabase(clientHandler, data, product);
                Double[] s = new Double[1];
                s[0] = product.price;
                clientHandler.user.bidded_items.add(new ProductInfo(data.get("product ID"), product, s[0]));

                bid_result.put("BID_RESULT", "VALID");
            } else {
                bid_result.put("BID_RESULT", "INVALID");
            }
            return bid_result;
        } catch (NumberFormatException e) {
            bid_result.put("BID_RESULT", "INVALID");
            return bid_result;
        }
    }
}

