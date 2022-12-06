package Items;
/*
 * EE422C Final Project submission by
 * Replace <...> with your actual data.
 * Kenneth Emeremnu
 * kie226
 * 17835
 * Slip days used: <1>
 * Spring 2021
 */

import ServerSide.Log;
import ServerSide.MongoDB;
import ServerSide.Server;
import User.Account;
import User.Email;

import java.util.*;

/**
 * Class that deals with the timers of each product
 */
public class countDown {
    public static HashMap<String, Account> countDown_Accounts = new HashMap<>();

    /**
     * Starts the timer for each product
     *
     * @param productID the product iD
     * @param product   the product itself
     */
    public static void startTimer(String productID, Item product) {

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                //System.out.println(product.time_left_num.get(0) + "," + product.time_left_num.get(1) + "," + product.time_left_num.get(2) + "," + product.time_left_num.get(3));

                // Get product time left
                int days = product.time_left_num.get(0);
                int hours = product.time_left_num.get(1);
                int minutes = product.time_left_num.get(2);
                int seconds = product.time_left_num.get(3);

                product.time_left_long--;

                // If the timer isn't finished yet
                if (product.time_left_long > -1) {
                    // Decrement seconds
                    seconds--;
                    if (seconds == -1) {
                        product.time_left_num.set(3, 59);
                    } else {
                        product.time_left_num.set(3, seconds);
                    }

                    // Decrement minutes
                    minutes--;
                    if (seconds == -1 && minutes > -1) {
                        product.time_left_num.set(2, minutes);
                    } else if ((days > 0 || hours > 0) && seconds == -1 && minutes == -1) {
                        product.time_left_num.set(2, 59);
                    }


                    // Decrement hours
                    hours--;
                    if (seconds == -1 && minutes == -1 && hours > -1) {
                        product.time_left_num.set(1, hours);
                    } else if (days > 0 && seconds == -1 && minutes == -1 && hours == -1) {
                        product.time_left_num.set(1, 23);
                    }

                    // Decrement days
                    days--;
                    if (seconds == -1 && minutes == -1 && hours == -1 && days > -1) {
                        product.time_left_num.set(0, days);
                    }
                }
                // If the timer has finished
                else {
                    // Cancel the timer and find the winner (if any)
                    timer.cancel();
                    if (!Objects.equals(product.bid_history.get(product.bid_history.size() - 1).get(0), "STARTING PRICE")) {
                        product.winner = product.bid_history.get(product.bid_history.size() - 1).get(0);
                    } else {
                        product.winner = "*";
                    }

                    // Send data of the end of this product's auction to all clients
                    HashMap<String, String> winner = new HashMap<>();
                    winner.put("CODE", "PRODUCT_WINNER");
                    winner.put("productID", productID);
                    winner.put("WINNER", product.winner);

                    Server.server.notifyObservers(winner);
                    Log.productAuctionEnded(productID);

                    // Update product winner on the database
                    MongoDB.addWinnerToProductDatabase(productID, product);

                    // If there was a winner, update product winner and order on the database and add this product to their owned_items and send them a confirmation email
                    if (countDown_Accounts.get(product.winner) != null) {

                        // If the winner wasn't a guest, add product to user's owned items
                        Account winner_a = countDown_Accounts.get(product.winner);
                        if (winner_a.password != null) {
                            winner_a.addProduct(productID, product);
                        }

                        // Generate order number and make sure it is unique
                        String orderNumber = String.format("%06d", new Random().nextInt(999999999));
                        while (Server.OrderNumbers.contains(orderNumber)) {
                            orderNumber = String.format("%06d", new Random().nextInt(999999999));
                        }
                        Server.OrderNumbers.add(orderNumber);

                        // Add order to collection
                        MongoDB.addOrderToDatabase(orderNumber, winner_a, productID, product);

                        // Send confirmation email
                        Email.SendEmail(winner_a.email, "Order Confirmation", Email.OrderConfirmation(orderNumber, winner_a, productID, product));
                    }
                }
            }
        }, 1000, 1000);
    }
}
