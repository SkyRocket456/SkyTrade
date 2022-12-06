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

import ServerSide.MongoDB;
import ServerSide.Server;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

/**
 * Class that deals with the products held on SkyTrade
 */

public class Items {
    public static ArrayList<String> productIDs = new ArrayList<>();
    public static HashMap<String, Item> Products = new HashMap<>();

    /**
     * Read products from the SkyTrade MongoDB Database and
     * Write new product information into Product Activity
     *
     * @param mongo_products the products that are being read
     */
    public static void getProducts(MongoCollection<Document> mongo_products) {
        // Iterate through documents
        FindIterable<Document> iterDoc = mongo_products.find();
        Iterator<Document> it = iterDoc.iterator();
        while (it.hasNext()) {
            // Add product to server database
            Document p = it.next();
            String productID = String.format("%06d", new Random().nextInt(999999999));
            while (Server.ProductIDs.contains(productID)) {
                productID = String.format("%06d", new Random().nextInt(999999999));
            }
            Server.ProductIDs.add(productID);
            productIDs.add(productID);

            Item item = new Item((String) p.get("Title"), (String) p.get("Description"), (String) p.get("Condition"), (String) p.get("Model"), (String) p.get("Price"),
                    (String) p.get("imageURL"), (String) p.get("Shipping Cost"), (String) p.get("Time Left"), (String) p.get("Seller"));
            Products.put(productID, item);

            MongoDB.addProductToDatabase(productID);
        }
    }
}
