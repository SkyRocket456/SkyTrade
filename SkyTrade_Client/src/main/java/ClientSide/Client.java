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

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LinkedTreeMap;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Central class that begins everything, including creating and starting the Application GUI for the client
 */
public class Client extends Application {
    public static HashMap<String, String> User;
    public static ArrayList<LinkedTreeMap> bidded_items;
    public static ArrayList<LinkedTreeMap> owned_items;
    private static ObjectInputStream receiver;
    private static ObjectOutputStream sender;

    public static Stage primaryStage;
    public static Scene Login;
    public static Scene menu;

    public static void main(String[] args) {
        initialize();
        launch();
    }

    /**
     * Send data to the server
     *
     * @param o Object we are sending
     */
    public static void sendData(Object o) {
        try {
            // Convert Object to JSON Format then send to server
            sender.writeObject(new Gson().toJson(o));
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Receive data from the server
     *
     * @param o Object type we are expecting
     * @return Object from server
     */
    protected static Object receiveData(Object o) {
        try {
            // Convert JSON to recognizable Object then receive
            return new Gson().fromJson((String) receiver.readObject(), (Type) o);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            return null;
        }
        return null;
    }

    /**
     * Initialize everything the client needs to operate
     */
    private static void initialize() {
        try {
            // Connect to the server and establish receiving and sending objects

            // Socket for cloud server
            //Socket socket = new Socket("3.142.204.111", 5000);

            // Socket for localhost
            Socket socket = new Socket("localhost", 5000);
            receiver = new ObjectInputStream(socket.getInputStream());
            sender = new ObjectOutputStream(socket.getOutputStream());

            // Receive information about products (their IDs and the products themselves)
            ArrayList ProductsInfo = (ArrayList) receiveData(ArrayList.class);
            Reader.productIDs = (ArrayList<String>) ProductsInfo.get(0);
            Reader.Products = (LinkedTreeMap<String, LinkedTreeMap>) ProductsInfo.get(1);

            // Start the timer for all the products
            for (int i = 0; i < Reader.productIDs.size(); i++) {
                countDown.startTimer(Reader.Products.get(Reader.productIDs.get(i)));
            }
            // Initialize information about the user
            User = new HashMap<>();

            // Start thread to wait for information from the server
            new Thread(new Reader()).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Start the program window
     *
     * @param primaryStage the stage the GUI will be on
     */
    @Override
    public void start(Stage primaryStage) {
        Client.primaryStage = primaryStage;
        primaryStage.getIcons().add(new Image("https://i.imgur.com/p5jy5I9.png"));
        try {
            // Set title on the window
            primaryStage.setTitle("SkyTrade");

            // Load login screen
            FXMLLoader LoginLoader = new FXMLLoader(Client.class.getResource("/scenes/Login/LoginScreen.fxml"));
            Login = new Scene(LoginLoader.load(), 700, 550);
        } catch (IOException e) {
            e.printStackTrace();
        }

        primaryStage.setScene(Client.Login);
        primaryStage.setOnCloseRequest(event -> {
            Client.sendData(null);
        });
        primaryStage.show();
        primaryStage.setResizable(false);
    }
}






