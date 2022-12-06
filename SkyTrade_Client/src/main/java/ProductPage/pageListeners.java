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
import ClientSide.Reader;
import com.google.gson.internal.LinkedTreeMap;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

/**
 * Class that deals with the interaction of the product page screen
 */
class pageListeners {
    /**
     * If the user pressed escape in the product page, go back to the main menu
     *
     * @param event the keyevent (the key on the keyboard they pressed)
     */
    protected static void productPageKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            if (ProductPageController.toMainMenu) {
                Client.primaryStage.setScene(Client.menu);
            } else {
                try {
                    FXMLLoader MySkyTrade = new FXMLLoader(Client.class.getResource("/scenes/MySkyTrade.fxml"));
                    Scene MySkyTrade_scene = new Scene(MySkyTrade.load(), 1280, 720);
                    Client.primaryStage.setScene(MySkyTrade_scene);

                    MySkyTrade_scene.setOnKeyPressed(e -> {
                        if (event.getCode() == KeyCode.ESCAPE) {
                            Client.primaryStage.setScene(Client.menu);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * If the user pressed the bid in the product page, go back to the main menu
     *
     * @param bid_amount the amount of money they bid
     * @param bid_button the bid button
     * @param product    the product the user bid on
     * @param productID  the product's ID
     */
    protected static void BidButtonClicked(TextField bid_amount, Button bid_button, LinkedTreeMap product, String productID) {
        try {
            HashMap<String, String> bid = new HashMap<>();

            bid.put("CODE", "BID");
            bid.put("bid amount", bid_amount.getText());
            bid.put("bid time", Calendar.getInstance().getTime().toString());
            bid.put("title", (String) product.get("title"));
            bid.put("product ID", productID);

            Client.sendData(bid);

            // Wait for server to respond
            HashMap<String, String> bid_check = Reader.bid_q.take();

            // If bid amount isn't at least $10 more than the last bid, it's an invalid vid
            if (Objects.equals(bid_check.get("BID_RESULT"), "INVALID")) {
                InvalidBid(bid_button);
            }
        } catch (NumberFormatException e) {
            InvalidBid(bid_button);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * If the bid amount was invalid in any way
     *
     * @param bid_button the bid button
     */
    private static void InvalidBid(Button bid_button) {
        Timeline timer = new Timeline(new KeyFrame(Duration.seconds(2), b -> {
            bid_button.setText("Place Bid");
            bid_button.setStyle("-fx-background-color: #5C94F5;");
            bid_button.setDisable(false);
        }));

        bid_button.setDisable(true);
        bid_button.setText("INVALID BID");
        bid_button.setStyle("-fx-background-color: red;");
        timer.play();
    }
}
