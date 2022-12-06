package User;
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
import ServerSide.MongoDB;
import ServerSide.Server;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class that deals with initializing accounts for the clients
 */
public class Account {
    public String ID;
    public String username;
    public String password;
    public String email;
    public String shipping_address;
    public String city;
    public String state;
    public String zip_code;
    public String card_number;
    public String name_on_card;
    public String expdate;
    public String CVV;
    public ArrayList<ProductInfo> bidded_items;
    public ArrayList<ProductInfo> owned_items;

    public Account(String username, String password, String email, String shipping_address, String city, String state, String zip_code,
                   String card_number, String name_on_card, String expdate, String CVV) {
        this.ID = String.format("%06d", new Random().nextInt(999999999));
        while (Server.AccountIDs.contains(this.ID)) {
            this.ID = String.format("%06d", new Random().nextInt(999999999));
        }
        Server.AccountIDs.add(this.ID);
        this.username = username;
        this.password = password;
        this.email = email;
        this.shipping_address = shipping_address;
        this.city = city;
        this.state = state;
        this.zip_code = zip_code;
        this.card_number = card_number;
        this.name_on_card = name_on_card;
        this.expdate = expdate;
        this.CVV = CVV;
        this.bidded_items = new ArrayList<>();
        this.owned_items = new ArrayList<>();
    }

    /**
     * Add a product to the user's owned items
     *
     * @param productID the ID of the product
     * @param product   the product
     */
    public void addProduct(String productID, Item product) {
        owned_items.add(new ProductInfo(productID, product, product.price));
        MongoDB.addToOwnedItemsDataBase(this, productID, product);
    }
}
