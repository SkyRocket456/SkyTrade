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
import ClientSide.Reader;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Screen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Controller for when the client asks the user to log in
 */
public class LoginScreenController {
    @FXML
    private TextField username;

    @FXML
    private Text usernameError;

    @FXML
    private TextField password;

    @FXML
    private Text passwordError;

    @FXML
    private Text LoginError;

    @FXML
    private TextField usernameGuest;

    @FXML
    private Text usernameGuestError;

    @FXML
    private TextField emailGuest;

    @FXML
    private Text emailGuestError;

    /**
     * Runs when user clicks Login
     */
    @FXML
    private void LoginClick() {
        // If information looks good,
        if (doesInfoLookGood()) {
            try {
                HashMap<String, String> login_info = new HashMap<>();
                login_info.put("CODE", "LOGIN");
                login_info.put("username", username.getText());
                login_info.put("password", password.getText());
                Client.sendData(login_info);

                // Wait for server to respond
                HashMap<String, String> account_check = Reader.account_q.take();

                // Check if account exists
                if (account_check.get("username") == null) {
                    LoginError.setText("Account does not exist");
                }
                // If account exists, give the user their information and enter the main menu
                else {
                    Client.User.put("username", account_check.get("username"));
                    Client.User.put("password", account_check.get("password"));
                    Client.User.put("email", account_check.get("email"));
                    Client.User.put("shipping_address", account_check.get("shipping_address"));
                    Client.User.put("city", account_check.get("city"));
                    Client.User.put("state", account_check.get("state"));
                    Client.User.put("zip_code", account_check.get("zip_code"));
                    Client.User.put("card_number", account_check.get("card_number"));
                    Client.User.put("name_on_card", account_check.get("name_on_card"));
                    Client.User.put("expdate", account_check.get("expdate"));
                    Client.User.put("CVV", account_check.get("CVV"));
                    Client.bidded_items = (ArrayList<LinkedTreeMap>) new Gson().fromJson(account_check.get("bidded_items"), ArrayList.class);
                    Client.owned_items = (ArrayList<LinkedTreeMap>) new Gson().fromJson(account_check.get("owned_items"), ArrayList.class);

                    FXMLLoader MenuLoader = new FXMLLoader(Client.class.getResource("/scenes/Main_Menu.fxml"));
                    Client.menu = new Scene(MenuLoader.load(), 1280, 720);
                    Client.primaryStage.setScene(Client.menu);

                    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                    Client.primaryStage.setX((screenBounds.getWidth() - 1280) / 2);
                    Client.primaryStage.setY((screenBounds.getHeight() - 720) / 2);

                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Runs when user clicks in Guest Login
     */
    @FXML
    private void GuestLoginClick() {
        // If information looks good,
        if (doesInfoLookGoodGuest()) {
            try {
                HashMap<String, String> signup_data = new HashMap<>();
                signup_data.put("CODE", "IS_SIGNUP_VALID");
                signup_data.put("username", usernameGuest.getText());
                signup_data.put("email", emailGuest.getText());
                Client.sendData(signup_data);

                // Wait for server to respond
                HashMap<String, String> signup_check = Reader.username_and_email_q.take();

                // If the username and email they want to use are not in use by somebody else, ask to verify email (go to verification screen)
                if (AskCredAndEmailController.areUsernameAndEmailNotInUse(signup_check, usernameGuestError, emailGuestError)) {
                    ConfirmEmailController.OTP = signup_check.get("OTP");
                    Client.User.put("CODE", "GUEST");
                    Client.User.put("username", usernameGuest.getText());
                    Client.User.put("email", emailGuest.getText());
                    ConfirmEmailController.receiver = emailGuest.getText();
                    FXMLLoader ConfirmEmailLoader = new FXMLLoader(Client.class.getResource("/scenes/Login/ConfirmEmail.fxml"));
                    Scene ConfirmEmail = new Scene(ConfirmEmailLoader.load(), 500, 650);
                    Client.primaryStage.setScene(ConfirmEmail);

                    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                    Client.primaryStage.setX((screenBounds.getWidth() - 500) / 2);
                    Client.primaryStage.setY((screenBounds.getHeight() - 650) / 2);
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Runs when user clicks in Create Account
     */
    @FXML
    private void onSignUpButtonClick() {
        try {
            // Go to username, password, and email screen
            Client.User.put("CODE", "NEW_ACCOUNT");

            FXMLLoader SignUpLoader = new FXMLLoader(Client.class.getResource("/scenes/Login/AskCredAndEmail.fxml"));
            Scene SignUp = new Scene(SignUpLoader.load(), 500, 650);
            Client.primaryStage.setScene(SignUp);

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            Client.primaryStage.setX((screenBounds.getWidth() - 500) / 2);
            Client.primaryStage.setY((screenBounds.getHeight() - 650) / 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if information on login looks valid
     *
     * @return valid, true, invalid, false
     */
    private boolean doesInfoLookGood() {
        // Initialize
        usernameError.setText("");
        passwordError.setText("");
        LoginError.setText("");

        if (username.getText().isEmpty()) {
            usernameError.setText("Please enter your username");
        }
        if (password.getText().isEmpty()) {
            passwordError.setText("Please enter your password");
        }
        return usernameError.getText().isEmpty() && passwordError.getText().isEmpty();
    }

    /**
     * Check if information on guest login looks valid
     *
     * @return valid, true, invalid, false
     */
    private boolean doesInfoLookGoodGuest() {
        // Initialize
        usernameGuestError.setText("");
        emailGuestError.setText("");

        if (usernameGuest.getText().isEmpty()) {
            usernameGuestError.setText("Please enter your desired username");
        } else if (Objects.equals(usernameGuest.getText(), ".") || Objects.equals(usernameGuest.getText(), "*")
                || usernameGuest.getText().contains(" ") || Objects.equals(usernameGuest.getText(), "null")) {
            usernameGuestError.setText("Invalid desired username");
        }
        if (emailGuest.getText().isEmpty()) {
            emailGuestError.setText("Please enter your email address");
        }

        return usernameGuestError.getText().isEmpty() && emailGuestError.getText().isEmpty();
    }
}

