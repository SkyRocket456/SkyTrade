package ProductPage;
/*
 * EE422C Final Project submission by
 * Replace <...> with your actual data.
 * Kenneth Emeremnu
 * kie226
 * 17835
 * Slip days used: <1>
 * Spring 2021
 */

import ClientSide.Client;
import com.google.gson.internal.LinkedTreeMap;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;

import static javafx.animation.Animation.INDEFINITE;

/**
 * Controller for the product page
 */
public class ProductPageController {
    public static String productID;

    public static LinkedTreeMap product;

    public static boolean toMainMenu = true;

    private static Timeline timeLeftPageTimeline;

    @FXML
    protected AnchorPane product_screen;

    @FXML
    protected ImageView ProductImage;

    @FXML
    protected TextArea Description;

    @FXML
    protected Text Seller;

    @FXML
    protected Text Title;

    @FXML
    protected Text Condition;

    @FXML
    protected Text Time_Left;

    @FXML
    protected Text shippingCost;

    @FXML
    protected Text current_bid_string;

    @FXML
    protected Text current_bid;

    @FXML
    protected Text bid_minimum;

    @FXML
    protected Button bid_button;

    @FXML
    protected TextField bid_amount;

    @FXML
    protected ScrollPane bid_history;

    @FXML
    protected GridPane bids;

    @FXML
    protected Text bid_num;

    /**
     * Initialize the product page
     */
    @FXML
    private void initialize() {
        // Initialize the listener for the product screen
        product_screen.setOnKeyPressed(pageListeners::productPageKeyPressed);

        // Initialize the listener for the bid button
        bid_button.setOnMouseClicked(event -> {
            pageListeners.BidButtonClicked(bid_amount, bid_button, product, productID);
        });

        // Render the product page
        renderPage.renderProductPage(this, product);

        // Animate the time left on product
        initializeTimelinePage();
        timeLeftPageTimeline.play();
    }

    /**
     * Initialize the animation timeline
     */
    private void initializeTimelinePage() {
        timeLeftPageTimeline = new Timeline();
        timeLeftPageTimeline.setCycleCount(INDEFINITE);
        timeLeftPageTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(100), event -> animatePage.animateProductPage(this, product, bids, bid_num, current_bid, bid_minimum)));
    }

    /**
     * Runs when user clicks any of the products on the screen
     */
    public static void GoToProductPage() {
        try {
            // Goes to that product's page
            FXMLLoader productPageLoader = new FXMLLoader(Client.class.getResource("/scenes/Product_Page.fxml"));
            Scene productPage = new Scene(productPageLoader.load(), 1280, 720);
            Client.primaryStage.setScene(productPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



