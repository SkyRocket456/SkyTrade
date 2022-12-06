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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

/**
 * Class that deals with processing information from the server
 */
class CodeProcessing {

    /**
     * When new bid from a client comes from the server, process it
     *
     * @param data the data from the server
     */
    protected static void newBid(HashMap<String, String> data) {
        // Find product that got a new bid
        LinkedTreeMap product = Reader.Products.get(data.get("productID"));

        // Update the number of bids by +1
        product.put("numberBids", (Double) product.get("numberBids") + 1);

        // Update the new price to bid
        product.put("price", Double.parseDouble(data.get("bid amount")));

        // If the person who bid on it was this client, add it to their bidded items
        if (Objects.equals(data.get("bidder"), Client.User.get("username")) && Client.User.get("password") != null) {
            LinkedTreeMap item = new LinkedTreeMap();
            item.put("productID", data.get("productID"));
            item.put("product", product);
            Double[] s = new Double[1];
            s[0] = (Double) product.get("price");
            item.put("price", s[0]);
            Client.bidded_items.add(item);
        }

        // Add new bid to the bid history
        ArrayList<String> new_bid = new ArrayList<>(Arrays.asList(new String[]{data.get("bidder"), "$" + data.get("bid amount"), data.get("bid time")}));
        ArrayList<ArrayList<String>> bid_history = (ArrayList<ArrayList<String>>) product.get("bid_history");
        bid_history.add(new_bid);
    }

    /**
     * When the server sends data that a product's auction has ended, process it
     *
     * @param data the data from the server
     */
    protected static void ProductWinner(HashMap<String, String> data) {
        // Find product that's auction ended
        LinkedTreeMap product = Reader.Products.get(data.get("productID"));

        // Update the winner (if any)
        product.put("winner", data.get("WINNER"));

        // If the user was the one who won, update owned_items
        if (Objects.equals(data.get("WINNER"), Client.User.get("username")) && Client.User.get("password") != null) {
            LinkedTreeMap item = new LinkedTreeMap();
            item.put("productID", data.get("productID"));
            item.put("product", product);
            item.put("price", product.get("price"));
            Client.owned_items.add(item);
        }
    }
}
