package ServerSide;
/*
 * EE422C Final Project submission by
 * Replace <...> with your actual data.
 * Kenneth Emeremnu
 * kie226
 * 17835
 * Slip days used: <1>
 * Spring 2021
 */

import Items.Items;
import User.Account;

import java.net.Socket;
import java.util.Objects;

/**
 * Class that prints everything that happens in the server
 */
public class Log {
    /**
     * Prints if a new client has connected
     *
     * @param socket the client that joined
     */
    public static void socketConnected(Socket socket) {
        System.out.println("User has connected: " + socket);
        System.out.println();
    }

    /**
     * Prints if an existing client has disconnected
     *
     * @param socket the client that joined
     * @param user   the Account of the user (if any)
     */
    public static void socketDisconnected(Socket socket, Account user) {
        if (user != null) {
            System.out.println("User has disconnected: (" + user.username + "," + socket + ")");
        } else {
            System.out.println("User has disconnected: " + socket);
        }
        System.out.println();
    }

    /**
     * Prints if an existing client has become a guest
     *
     * @param socket   the client that joined
     * @param username the username of the user
     */
    public static void newGuestJoined(Socket socket, String username) {
        System.out.println(socket + " has joined as a guest on SkyTrade! Their username is: " + username);
        System.out.println();
    }

    /**
     * Prints if an existing client has made an account
     *
     * @param socket   the client that joined
     * @param username the username of the account
     */
    public static void newAccountCreated(Socket socket, String username) {
        System.out.println(socket + " has created a new account on SkyTrade! Their username is: " + username);
        System.out.println();
    }

    /**
     * Prints if an existing client has logged into their account
     *
     * @param socket   the client that joined
     * @param username the username of the account
     */
    public static void userHasLoggedIn(Socket socket, String username) {
        System.out.println(socket + " has logged in to their account: " + username);
        System.out.println();
    }

    /**
     * Prints if an existing client has entered a new bid
     *
     * @param username  the username of the account
     * @param price     the price they bid
     * @param title     the title of the product they bid on
     * @param productID the productID of the product they bid
     */
    public static void newBid(String username, String price, String title, String productID) {
        System.out.println(username + " just played a bid on product " + "(" + title + "," + productID + ")" + " for " + "$" + price);
        System.out.println();
    }

    /**
     * Prints if a product's auction ended
     *
     * @param productID the productID of the product
     */
    public static void productAuctionEnded(String productID) {
        if (Objects.equals(Items.Products.get(productID).winner, "*")) {
            System.out.println("The auction for (" + (Items.Products.get(productID).title + "," + productID) + ") has ended with no winner.");
        } else {
            System.out.println("Congrats to " + Items.Products.get(productID).winner + " for winning the auction for the (" + (Items.Products.get(productID).title + "," + productID) + ")!");
        }
        System.out.println();
    }
}
