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

import ClientHandling.ClientHandler;
import Items.Item;
import Items.Items;
import User.Account;
import User.Order;
import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.*;

/**
 * Class that deals writing data to the database
 */
public class MongoDB {
    public static MongoCollection<Document> Product_Activity;
    public static MongoCollection<Document> Customer_Activity;
    public static MongoCollection<Document> Order_Activity;
    public static Set<Account> ActivityAccounts = new HashSet<>();


    /**
     * Add customer to the database
     *
     * @param user customer to add
     */
    public static void addCustomerToDatabase(Account user) {
        if (!ActivityAccounts.contains(user)) {
            Customer_Activity.insertOne(Document.parse(new Gson().toJson(user)));
            ActivityAccounts.add(user);
        }
    }

    /**
     * Add product to the database
     *
     * @param productID ID of the product to add
     */
    public static void addProductToDatabase(String productID) {
        // Add new item to collection
        String s = new Gson().toJson(Items.Products.get(productID));
        Document doc = Document.parse(s);
        doc.put("productID", productID);
        MongoDB.Product_Activity.insertOne(doc);
    }

    /**
     * Add order to the database
     *
     * @param orderNumber the ID of the order
     * @param winner_a    the winner of the product that is being order
     * @param productID   the ID of the product being ordered
     * @param product     the product being ordered
     */
    public static void addOrderToDatabase(String orderNumber, Account winner_a, String productID, Item product) {
        Order o = new Order(orderNumber, winner_a.username, winner_a.ID, winner_a.shipping_address, (winner_a.city + ", " + winner_a.state + " " + winner_a.zip_code),
                productID, product.title, String.valueOf(product.price), Calendar.getInstance().getTime().toString());
        MongoDB.Order_Activity.insertOne(Document.parse(new Gson().toJson(o)));
    }

    /**
     * update product information on the database
     *
     * @param clientHandler the client that updated the product by bidding on it
     * @param data          the data that the product is being updated upon
     * @param product       the product being updated
     */
    public static void updateProductOnDatabase(ClientHandler clientHandler, HashMap<String, String> data, Item product) {
        MongoDB.Product_Activity.updateOne(Filters.eq("productID", data.get("product ID")), Updates.combine(
                Updates.set("price", Double.parseDouble(data.get("bid amount"))),
                Updates.set("numberBids", product.numberBids),
                Updates.push("bid_history", new ArrayList<>(Arrays.asList(clientHandler.user.username, data.get("bid amount"), data.get("bid time")))))
        );

        MongoDB.Customer_Activity.updateOne(Filters.eq("ID", clientHandler.user.ID), Updates.push("bidded_items",
                new ArrayList<>(Arrays.asList(data.get("product ID"), product.title, product.price))));
    }

    /**
     * update owned items of a user on the database
     *
     * @param user      the user being updated
     * @param productID the ID of the product being added
     * @param product   the product being added
     */
    public static void addToOwnedItemsDataBase(Account user, String productID, Item product) {
        MongoDB.Customer_Activity.updateOne(Filters.eq("ID", user.ID), Updates.push("owned_items",
                new ArrayList<>(Arrays.asList(productID, product.title, product.price))));
    }

    /**
     * update winner (if any) of a product on the database
     *
     * @param productID the ID of the product being added
     * @param product   the product being added
     */
    public static void addWinnerToProductDatabase(String productID, Item product) {
        if (!Objects.equals(product.winner, "*")) {
            MongoDB.Product_Activity.updateOne(Filters.eq("productID", productID), Updates.set("winner", product.winner));
        } else {
            MongoDB.Product_Activity.updateOne(Filters.eq("productID", productID), Updates.set("winner", null));
        }
    }
}
