package Main_Menu;
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
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Controller for the main menu
 */
public class MenuController {
    private static Timeline timeLeftMenuTimeline;

    protected static ArrayList<Text> prices_all;

    protected static ArrayList<Text> numBids_all;

    protected static ArrayList<Text> time_left_all;

    protected static ArrayList<Text> sold_ended_all;

    protected static Double BoxSetting = -1.0;

    protected static String search_text = "";

    @FXML
    private ScrollPane main_menu_screen;

    @FXML
    private GridPane products_Menu;

    @FXML
    private CheckBox UsedBox;

    @FXML
    private CheckBox RefurbishedBox;

    @FXML
    private CheckBox NewBox;

    @FXML
    private TextField search;

    @FXML
    private Button MySkyTrade;

    /**
     * Initialize main menu screen
     */
    @FXML
    private void initialize() {
        // If the password is null that means the user is a guest. Disable MySkyTrade and make it not Visible
        if (Client.User.get("password") == null) {
            MySkyTrade.setDisable(true);
            MySkyTrade.setVisible(false);
        }

        // Disable horizontal scroll bar
        main_menu_screen.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Render products on screen
        renderMenu.renderProductsMenu(products_Menu);

        // Animate the number of bids, time left, and auction result (if any) on each product
        initializeTimeLineMenu();
        timeLeftMenuTimeline.play();
    }

    /**
     * Runs when user clicks the user checkbox
     */
    @FXML
    private void showOnlyUsed() {
        // If the box is selected, disable the other boxes and only show used items
        if (UsedBox.isSelected()) {
            BoxSetting = 0.0;
            RefurbishedBox.setDisable(true);
            NewBox.setDisable(true);
        }
        // If the box is not selected, enable the other boxes and show all items
        else {
            BoxSetting = -1.0;
            RefurbishedBox.setDisable(false);
            NewBox.setDisable(false);
        }
        // Update the menu
        renderMenu.renderProductsMenu(products_Menu);
    }

    /**
     * Runs when user clicks the refurbished checkbox
     */
    @FXML
    private void showOnlyRefurbished() {
        // If the box is selected, disable the other boxes and only show refurbished items
        if (RefurbishedBox.isSelected()) {
            BoxSetting = 1.0;
            UsedBox.setDisable(true);
            NewBox.setDisable(true);
        }
        // If the box is not selected, enable the other boxes and show all items
        else {
            BoxSetting = -1.0;
            UsedBox.setDisable(false);
            NewBox.setDisable(false);
        }
        // Update the menu
        renderMenu.renderProductsMenu(products_Menu);
    }

    /**
     * Runs when user clicks the new checkbox
     */
    @FXML
    private void showOnlyNew() {
        // If the box is selected, disable the other boxes and only show new items
        if (NewBox.isSelected()) {
            BoxSetting = 2.0;
            UsedBox.setDisable(true);
            RefurbishedBox.setDisable(true);
        }
        // If the box is not selected, enable the other boxes and show all items
        else {
            BoxSetting = -1.0;
            UsedBox.setDisable(false);
            RefurbishedBox.setDisable(false);
        }
        // Update the menu
        renderMenu.renderProductsMenu(products_Menu);
    }

    /**
     * Runs when user presses enter on the search field
     *
     * @param event the keyevent for the listener
     */
    @FXML
    private void SearchField(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            // Only show items with the keyword and update the menu
            search_text = search.getText();
            renderMenu.renderProductsMenu(products_Menu);
        }
    }

    /**
     * Runs when user clicks my SkyTrade
     */
    @FXML
    private void GoToMySkyTrade() {
        try {
            // Go to MySkyTrade
            FXMLLoader MySkyTrade = new FXMLLoader(Client.class.getResource("/scenes/MySkyTrade.fxml"));
            Scene MySkyTrade_scene = new Scene(MySkyTrade.load(), 1280, 720);
            Client.primaryStage.setScene(MySkyTrade_scene);

            MySkyTrade_scene.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                    Client.primaryStage.setScene(Client.menu);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize the timeline for the menu animation
     */
    private void initializeTimeLineMenu() {
        timeLeftMenuTimeline = new Timeline();
        timeLeftMenuTimeline.setCycleCount(Animation.INDEFINITE);
        timeLeftMenuTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(1), event -> {
            animateMenu.animateMainMenu();
        }));
    }
}
