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
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Controller for when the client asks for the shipping information of the user
 */
public class AskShippingController {

    @FXML
    private TextField shipping_address;

    @FXML
    private Text shipping_addressError;

    @FXML
    private TextField city;

    @FXML
    private Text cityError;

    @FXML
    private ComboBox states;

    @FXML
    private Text statesError;

    @FXML
    private TextField zip_code;

    @FXML
    private Text zipCodeError;

    /**
     * Initialize the shipping screen
     */
    @FXML
    private void initialize() {
        try {
            // Initialize the states drop down menu
            InputStream in = new FileInputStream("States.csv");
            CSVReader csvReader = new CSVReader(new InputStreamReader(in));
            csvReader.skip(1);
            List<String[]> states_abbr = csvReader.readAll();
            ObservableList<String> states_list = FXCollections.observableArrayList();
            for (int i = 0; i < states_abbr.size(); i++) {
                states_list.add(states_abbr.get(i)[0] + " - " + states_abbr.get(i)[1]);
            }
            states.setItems(states_list);
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    /**
     * Runs when Continue is pressed
     */
    @FXML
    private void ContinueShippingClick() {
        // If the information looks good, continue to ask for payment method (go to payinfo screen)
        if (doesInfoLookGood()) {
            try {
                Client.User.put("shipping_address", shipping_address.getText());
                Client.User.put("city", city.getText());
                Client.User.put("state", states.getValue().toString().substring(0, 2));
                Client.User.put("zip_code", zip_code.getText());

                FXMLLoader PayInfo = new FXMLLoader(Client.class.getResource("/scenes/Login/AskForPayInfo.fxml"));
                Scene p = new Scene(PayInfo.load(), 500, 650);
                Client.primaryStage.setScene(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Check if zip code is valid
     *
     * @param zip the zip code
     * @return valid, true, invalid, false
     */
    private boolean isZipCodeValid(String zip) {
        String regex = "^[0-9]{5}(?:-[0-9]{4})?$";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(zip);
        return matcher.matches();
    }

    /**
     * Check if information is valid
     *
     * @return valid, true, invalid, false
     */
    private boolean doesInfoLookGood() {
        // Initialize
        shipping_addressError.setText("");
        cityError.setText("");
        statesError.setText("");
        zipCodeError.setText("");

        // Check shipping address
        if (shipping_address.getText().isEmpty()) {
            shipping_addressError.setText("Please enter your shipping address");
        }

        // Check city
        if (city.getText().isEmpty()) {
            cityError.setText("Please enter your city of shipping");
        }

        // Check state
        if (states.getValue() == null) {
            statesError.setText("Please select your state of shipping");
        }

        // Check zip code
        if (zip_code.getText().isEmpty()) {
            zipCodeError.setText("Please enter your zipcode of shipping");
        } else if (!isZipCodeValid(zip_code.getText())) {
            zipCodeError.setText("Zip code is in incorrect format");
        }

        // Return true if no errors, false otherwise
        return shipping_addressError.getText().isEmpty() && cityError.getText().isEmpty() && statesError.getText().isEmpty() && zipCodeError.getText().isEmpty();
    }
}
