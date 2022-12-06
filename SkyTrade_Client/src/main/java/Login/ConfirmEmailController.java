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
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Objects;

/**
 * Controller for when the client asks to verify the email of the user
 */
public class ConfirmEmailController {

    public static String receiver;

    protected static String OTP;

    @FXML
    private Text confirmEmailText;

    @FXML
    private TextField UserOTP;

    @FXML
    private Text UserOTPError;

    /**
     * Initialize the verification screen
     */
    @FXML
    private void initialize() {
        confirmEmailText.setText("To verify your email, we've sent a One Time Password (OTP) to " + receiver);
    }

    /**
     * Runs when verify email is pressed
     */
    @FXML
    private void VerifyEmailClick() {
        // If the OTP was correct, move on to shipping information screen
        if (isOTPCorrect()) {
            try {
                Client.User.put("email", receiver);
                FXMLLoader AskShipping = new FXMLLoader(Client.class.getResource("/scenes/Login/AskShipping.fxml"));
                Scene a = new Scene(AskShipping.load(), 500, 650);
                Client.primaryStage.setScene(a);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks if OTP is correct
     *
     * @return OTP is correct, true, false otherwise
     */
    private boolean isOTPCorrect() {
        if (UserOTP.getText().isEmpty()) {
            UserOTPError.setText("Please enter the OTP found in your email");
        } else if (!Objects.equals(OTP, UserOTP.getText())) {
            UserOTPError.setText("OTP does not match");
        }
        return UserOTPError.getText().isEmpty();
    }
}
