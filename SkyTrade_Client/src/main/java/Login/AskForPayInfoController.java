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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Controller for when the client asks for the payment method of the user
 */
public class AskForPayInfoController {
    @FXML
    private TextField card_number;

    private Set<Integer> spacing;

    @FXML
    private Text card_numberError;

    @FXML
    private TextField name;

    @FXML
    private Text nameError;

    @FXML
    private ComboBox expiration_date_month;

    @FXML
    private ComboBox expiration_date_year;

    @FXML
    private Text expiration_dateError;

    @FXML
    private TextField CVV;

    @FXML
    private Text CVVError;

    /**
     * Initialize the payment information screen
     */
    @FXML
    private void initialize() {
        // Initialize the months drop down menu for the expiration date
        ObservableList<String> month_list = FXCollections.observableArrayList();
        for (int i = 0; i < 12; i++) {
            month_list.add("0" + (i + 1));
        }
        expiration_date_month.setItems(month_list);

        // Initialize the years drop down menu for the expiration date
        ObservableList<Integer> year_list = FXCollections.observableArrayList();
        for (int i = 0; i < 21; i++) {
            year_list.add(Calendar.getInstance().get(Calendar.YEAR) + i);
        }
        expiration_date_year.setItems(year_list);

        // Initialize HashSet for card number spacing
        spacing = new HashSet<>(Arrays.asList(4, 9, 14, 19));

        // Space the numbers of the card number every 4 numbers
        card_number.setOnKeyPressed(event -> {
            if (spacing.contains(card_number.getText().length()) && !card_number.getText().isEmpty() && event.getCode() != KeyCode.BACK_SPACE) {
                card_number.setText(card_number.getText() + " ");
                card_number.positionCaret(card_number.getText().length() + 2);
            }
        });
    }

    /**
     * Runs when the user clicks Finalize
     */
    @FXML
    private void FinalizeClick() {
        // If the information looks good, the sign-up is successful. Move on to the last screen
        if (doesInfoLookGood()) {
            try {
                Client.User.put("card_number", card_number.getText());
                Client.User.put("name_on_card", name.getText());
                Client.User.put("expdate", expiration_date_month.getValue() + "/" + expiration_date_year.getValue());
                Client.User.put("CVV", CVV.getText());

                FXMLLoader Finished = new FXMLLoader(Client.class.getResource("/scenes/Login/SuccessSignUp.fxml"));
                Scene f = new Scene(Finished.load(), 500, 650);
                Client.primaryStage.setScene(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks if the CVV is valid
     *
     * @return valid, true, invalid, false
     */
    private boolean isCVVValid() {
        // Tries to parse the String into a size 3 integer. If it fails, the CVV is invalid
        try {
            int seg = Integer.parseInt(CVV.getText().substring(0, 3));
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the card number is valid
     *
     * @return valid, true, invalid, false
     */
    private boolean isCardNumberValid() {
        // Tries to parse the String into a size 4 integer for each segment of the card number. If it fails, the card number is invalid
        for (int i = 0, j = 0; j < 4; i = i + 5, j++) {
            try {
                int seg = Integer.parseInt(card_number.getText().substring(i, i + 4));
            } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the information given the user is valid
     *
     * @return valid, true, invalid, false
     */
    private boolean doesInfoLookGood() {
        // Initialize
        card_numberError.setText("");
        nameError.setText("");
        expiration_dateError.setText("");
        CVVError.setText("");

        // Check card number
        if (card_number.getText().isEmpty()) {
            card_numberError.setText("Please enter your card number");
        } else if (!isCardNumberValid() || card_number.getText().length() > 19) {
            card_numberError.setText("Invalid card number");
        }
        // Check the name field
        if (name.getText().isEmpty()) {
            nameError.setText("Please enter the name on the card");
        }
        // Check expiration date
        if (expiration_date_month.getValue() == null || expiration_date_year.getValue() == null) {
            expiration_dateError.setText("Please completely enter your expiration date");
        }
        // Check CVV
        if (CVV.getText().isEmpty()) {
            CVVError.setText("Please enter your CVV");
        } else if (!isCVVValid() || CVV.getText().length() > 3) {
            CVVError.setText("Invalid CVV");
        }

        // Return true if no errors, false otherwise
        return card_numberError.getText().isEmpty() && nameError.getText().isEmpty() && expiration_dateError.getText().isEmpty() && CVVError.getText().isEmpty();
    }
}
