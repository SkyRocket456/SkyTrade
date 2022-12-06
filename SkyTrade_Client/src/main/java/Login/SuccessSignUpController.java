package Login;
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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Screen;

import java.io.IOException;

/**
 * Controller for when the client has a successful signup
 */
public class SuccessSignUpController {
    private boolean isGuest = false;

    @FXML
    private Button Return;

    /**
     * Initialize successful sign-up screen
     */
    @FXML
    private void initialize() {
        // Send User data to server
        Client.sendData(Client.User);

        // If guest go to main menu, if account go to login screen
        if (Client.User.get("password") == null) {
            isGuest = true;
            Return.setText("Enter SkyTrade");
        }
    }

    /**
     * Runs when user clicks return to login to enter SkyTrade
     */
    @FXML
    private void returnToLoginOrGoToMainMenu() {
        // If guest, go to main menu
        if (isGuest) {
            try {
                FXMLLoader MenuLoader = new FXMLLoader(Client.class.getResource("/scenes/Main_Menu.fxml"));
                Client.menu = new Scene(MenuLoader.load(), 1280, 720);
                Client.primaryStage.setScene(Client.menu);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            Client.primaryStage.setX((screenBounds.getWidth() - 1280) / 2);
            Client.primaryStage.setY((screenBounds.getHeight() - 720) / 2);
        }
        // If account, go to login screen
        else {
            Client.primaryStage.setScene(Client.Login);

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            Client.primaryStage.setX((screenBounds.getWidth() - 700) / 2);
            Client.primaryStage.setY((screenBounds.getHeight() - 550) / 2);
        }
    }
}



