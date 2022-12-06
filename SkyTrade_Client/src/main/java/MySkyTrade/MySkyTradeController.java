package MySkyTrade;
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
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * Controller for MySkyTrade
 */
public class MySkyTradeController {

    @FXML
    private Text username;

    @FXML
    private Text password;

    @FXML
    private Text email;

    @FXML
    private Text shipping_address;

    @FXML
    private Text city;

    @FXML
    private Text state;

    @FXML
    private Text zip_code;

    @FXML
    private Text card_number;

    @FXML
    private Text name_on_card;

    @FXML
    private Text expdate;

    @FXML
    private Text CVV;

    @FXML
    private GridPane owned_products;

    @FXML
    private GridPane bidded_products;

    /**
     * Initializes MySkyTrade
     */
    @FXML
    private void initialize() {

        // Set the user info on the screen
        username.setText(Client.User.get("username"));
        password.setText(Client.User.get("password"));
        email.setText(Client.User.get("email"));

        shipping_address.setText(Client.User.get("shipping_address"));
        city.setText(Client.User.get("city"));
        state.setText(Client.User.get("state"));
        zip_code.setText(Client.User.get("zip_code"));

        card_number.setText(Client.User.get("card_number"));
        name_on_card.setText(Client.User.get("name_on_card"));
        expdate.setText(Client.User.get("expdate"));
        CVV.setText(Client.User.get("CVV"));

        // Render the user's owned items
        renderYourSkyTrade.renderProducts(owned_products, bidded_products);
    }

    @FXML
    private void RefreshClick() {
        renderYourSkyTrade.renderProducts(owned_products, bidded_products);
    }
}
