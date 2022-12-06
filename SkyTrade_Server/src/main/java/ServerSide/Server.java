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
import ClientHandling.Readers.BidReader;
import ClientHandling.Readers.LoginReader;
import ClientHandling.Readers.SignUpReader;
import Items.Items;
import Items.countDown;
import User.Account;
import User.Email;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO connect server to a database

/**
 * Class that creates and runs the server that will accept client connections
 */
public class Server extends Observable {

    public static Server server;
    public static Set<String> Usernames = new HashSet<>();
    public static Set<String> Emails = new HashSet<>();

    public static HashMap<HashMap<String, String>, Account> Accounts = new HashMap<>();
    public static Set<String> ProductIDs = new HashSet<>();
    public static Set<String> OrderNumbers = new HashSet<>();
    public static Set<String> AccountIDs = new HashSet<>();
    public static HashMap<String, BidReader> BidReaders = new HashMap<>();

    /**
     * Starts the server
     *
     * @param args ignore
     */
    public static void main(String[] args) {
        server = new Server();
        server.initialize();
        server.start();
    }

    /**
     * Notifies the clients of any changes to SkyTrade (bew bid, an auction ended, etc)
     *
     * @param o the object to send to the clients
     */
    @Override
    public void notifyObservers(Object o) {
        setChanged();
        super.notifyObservers(o);
    }

    /**
     * Initializes and starts everything on the server
     */
    public void initialize() {
        System.out.println("Initializing...");
        // Show log for MongoDB only when there is a warning
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);

        System.out.println("Connecting to MongoDB...");
        // Connect to SkyTrade MongoDB Database
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb+srv://SkyRocket456:10394689830ut@skytradecluster.qb7ee.mongodb.net/test"));

        // Connect to database to read
        MongoDatabase data = mongoClient.getDatabase("data");

        System.out.println("Creating new database...");
        // Get current auction number
        MongoCollection<Document> auction_number = data.getCollection("Current Auction Number");
        FindIterable<Document> iterDoc = auction_number.find();
        Iterator<Document> it = iterDoc.iterator();
        Document d = it.next();

        // Create new database to store of the current SkyTrade Auction + give it a number
        MongoDatabase new_auction = mongoClient.getDatabase("SkyTrade_Auction_No_" + d.get("number"));

        // Increment the auction number we are on
        auction_number.updateOne(Filters.eq("number", d.get("number")), Updates.set("number", (Integer) d.get("number") + 1));

        System.out.println("Creating new collections to store information...");
        // Create collection for storing customer activity
        new_auction.createCollection("Customer Activity");
        MongoDB.Customer_Activity = new_auction.getCollection("Customer Activity");

        // Create collection for storing product activity
        new_auction.createCollection("Product Activity");
        MongoDB.Product_Activity = new_auction.getCollection("Product Activity");

        // Create collection for storing order activity
        new_auction.createCollection("Order Activity");
        MongoDB.Order_Activity = new_auction.getCollection("Order Activity");

        System.out.println("Reading products to sell from MongoDB...");
        // Generate products HashMap and products ID ArrayList, Add products to Product Activity
        TimeZone.setDefault(TimeZone.getTimeZone("America/Chicago"));
        Items.getProducts(data.getCollection("Products"));

        System.out.println("Initializing Email server and threads to read data from clients...");

        new Thread(new LoginReader()).start();

        new Thread(new SignUpReader()).start();

        for (int i = 0; i < Items.productIDs.size(); i++) {
            BidReader b = new BidReader();
            BidReaders.put(Items.productIDs.get(i), b);
            new Thread(b).start();
        }

        Email.initializeEmailServer();
        initializeAdminAccount();

        for (int i = 0; i < Items.productIDs.size(); i++) {
            countDown.startTimer(Items.productIDs.get(i), Items.Products.get(Items.productIDs.get(i)));
        }
    }

    /**
     * MEANT FOR DEBUGGING AND EASY ACCESS TO CHANGES
     * <p>
     * Created so I can enter SkyTrade easily to debug code
     */
    private void initializeAdminAccount() {
        // FIRST ACCOUNT
        HashMap<String, String> admin_credentials = new HashMap<>();
        admin_credentials.put("CODE", "LOGIN");
        admin_credentials.put("username", "SkyRocket");
        admin_credentials.put("password", "SkyRocket");

        Account admin = new Account("SkyRocket", "SkyRocket", "skyrocket456@gmail.com",
                "899 Bordfish Road", "New York City", "NY", "58294", "1111 1111 1111 1111",
                "KENNETH EMEREMNU", "11/24", "111");

        Usernames.add("SkyRocket");
        Emails.add("skyrocket456@gmail.com");
        Accounts.put(admin_credentials, admin);

        countDown.countDown_Accounts.put(admin.username, admin);

        // SECOND ACCOUNT
        HashMap<String, String> other = new HashMap<>();
        other.put("CODE", "LOGIN");
        other.put("username", "Sky");
        other.put("password", "Sky");

        Account other_a = new Account("Sky", "Sky", "skyrock567@gmail.com",
                "334 Kahlo Loop", "Laredo", "TX", "78045", "1111 1111 1111 1111",
                "KENNETH EMEREMNU", "11/24", "111");

        Usernames.add("Sky");
        Emails.add("skyrock567@gmail.com");
        Accounts.put(other, other_a);

        countDown.countDown_Accounts.put(other_a.username, other_a);
    }

    public void start(){
        try {
            ServerSocket ss = new ServerSocket(5000);
            System.out.println("Server has completed loading. Now accepting client connections...");
            System.out.println();
            while (true) {
                Socket clientSocket = ss.accept();
                new Thread(new ClientHandler(server, clientSocket)).start();
                Log.socketConnected(clientSocket);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
