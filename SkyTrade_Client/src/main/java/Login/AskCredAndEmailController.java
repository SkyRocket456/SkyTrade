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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

/**
 * Controller for when the client asks for the username, password, and email of the user
 */

public class AskCredAndEmailController {
    @FXML
    private TextField username;

    @FXML
    private Text usernameError;

    @FXML
    private TextField email;

    @FXML
    private Text emailError;

    @FXML
    private TextField password;

    @FXML
    private Text passwordError;

    @FXML
    private TextField confirmPassword;

    @FXML
    private Text confirmPasswordError;

    /**
     * Runs when the user presses Continue
     */
    @FXML
    private void ContinueCredentialsClick() {
        try {
            // If information is good to go, send to server to check username and email are already in use
            if (doesInfoLookGood()) {
                HashMap<String, String> signup_data = new HashMap<>();
                signup_data.put("CODE", "IS_SIGNUP_VALID");
                signup_data.put("username", username.getText());
                signup_data.put("email", email.getText());
                Client.sendData(signup_data);

                // Wait for server to respond
                HashMap<String, String> signup_check = Reader.username_and_email_q.take();

                // If the username and email aren't already used, continue to the next screen (verify email)
                if (areUsernameAndEmailNotInUse(signup_check, usernameError, emailError)) {
                    ConfirmEmailController.OTP = signup_check.get("OTP");
                    Client.User.put("username", username.getText());
                    Client.User.put("password", password.getText());
                    ConfirmEmailController.receiver = email.getText();
                    FXMLLoader ConfirmEmailLoader = new FXMLLoader(Client.class.getResource("/scenes/Login/ConfirmEmail.fxml"));
                    Scene ConfirmEmail = new Scene(ConfirmEmailLoader.load(), 500, 650);
                    Client.primaryStage.setScene(ConfirmEmail);
                }
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if information given by user is valid
     *
     * @return if good, true, if not false
     */
    private boolean doesInfoLookGood() {
        // Initialize
        usernameError.setText("");
        emailError.setText("");
        passwordError.setText("");
        confirmPasswordError.setText("");

        // Check if username is valid
        if (username.getText().isEmpty()) {
            usernameError.setText("Please enter your desired username");
        } else if (Objects.equals(username.getText(), ".") || Objects.equals(username.getText(), "*")
                || username.getText().contains(" ") || Objects.equals(username.getText(), "null")) {
            usernameError.setText("Desired username is invalid");
        } else if (username.getText().length() > 16) {
            usernameError.setText("Desired username is too long");
        }
        // Check if email is valid
        if (email.getText().isEmpty()) {
            emailError.setText("Please enter your email address");
        }
        // Check if password is valid
        if (password.getText().isEmpty()) {
            passwordError.setText("Please enter your desired password");
        } else if (password.getText().length() > 32) {
            passwordError.setText("Desired password is too long");
        } else if (password.getText().length() < 8) {
            passwordError.setText("Desired password is too short");
        }
        // Confirm that the user's passwords match
        if (!Objects.equals(password.getText(), confirmPassword.getText())) {
            confirmPasswordError.setText("Passwords do not match");
        }

        // Return true if there were no errors, false otherwise
        return usernameError.getText().isEmpty() && emailError.getText().isEmpty() && passwordError.getText().isEmpty() && confirmPasswordError.getText().isEmpty();
    }

    /**
     * Check if the username or email are in use by somebody else
     *
     * @param signup_check  the data from the server with the results
     * @param usernameError text displaying the error for the username (if any)
     * @param emailError    text displaying the error for the email (if any)
     * @return valid, true, invalid, false
     */
    protected static boolean areUsernameAndEmailNotInUse(HashMap<String, String> signup_check, Text usernameError, Text emailError) {
        // Initialize
        usernameError.setText("");
        emailError.setText("");

        // Check if username and email were not used
        if (signup_check.get("username_check") != null) {
            usernameError.setText(signup_check.get("username_check"));
        }
        if (signup_check.get("email_check") != null) {
            emailError.setText(signup_check.get("email_check"));
        }

        // Return true if no errors, false otherwise
        return usernameError.getText().isEmpty() && emailError.getText().isEmpty();
    }

}
