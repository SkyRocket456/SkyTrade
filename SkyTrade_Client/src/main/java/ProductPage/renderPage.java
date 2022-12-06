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

import com.google.gson.internal.LinkedTreeMap;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;

import java.util.ArrayList;

/**
 * Class that renders product-specific information of the product page
 */
class renderPage {

    /**
     * Class that renders product-specific information of the product page
     *
     * @param p       the controller for the product page
     * @param product the product
     */
    protected static void renderProductPage(ProductPageController p, LinkedTreeMap product) {
        // Initialize product image
        Image product_image = new Image((String) product.get("imageURL"));
        p.ProductImage.setImage(product_image);

        // Initialize description
        p.Description.setWrapText(true);
        p.Description.setText((String) product.get("Description"));

        // Initialize product seller
        p.Seller.setText((String) product.get("seller"));

        // Initialize title of post
        p.Title.setText((String) product.get("title"));

        // Initialize product condition
        switch ((int) (double) product.get("Condition")) {
            case 0:
                p.Condition.setText("Used");
                break;
            case 1:
                p.Condition.setText("Seller Refurbished");
                break;
            default:
                p.Condition.setText("New");
                break;
        }

        // Initialize time left
        renderTimeLeft(p, product);

        // Initialize current bid, the minimum bid price, and the number of bids
        renderBidInfo(product, p.current_bid, p.bid_minimum, p.bid_num);

        // Initialize bid button and bid amount textfield
        renderBidButtonAndAmount(p, product);

        // Initialize product shipping cost
        p.shippingCost.setText("US $" + product.get("shippingPrice"));

        // Initialize bid history
        p.bids = new GridPane();
        p.bids.setGridLinesVisible(true);
        p.bid_history.setContent(p.bids);

        p.bids.addRow(0, renderBidPane(true, "Bidder", false),
                renderBidPane(true, "Bid Amount", false),
                renderBidPane(true, "Bid Time", false));

        // Initialize the bid history
        renderBidHistory(p, product);
    }

    /**
     * Renders a pane containing title information for a single row on the bid history gridpane
     *
     * @param style_and_font if true, give the pane a specific color
     * @param title_name     the name in the middle of the pane
     * @param winner         if the title in the row of the winner is passed
     * @return a borderpane a title in the middle
     */
    private static BorderPane renderBidPane(Boolean style_and_font, String title_name, boolean winner) {
        BorderPane b = new BorderPane();
        b.setMinSize(195, 24);
        Text text = new Text(title_name);
        b.setCenter(text);

        if (style_and_font) {
            if (winner) {
                b.setStyle("-fx-background-color: #4fa876;");
            } else {
                b.setStyle("-fx-background-color: #C9C5C5;");
                text.setFont(Font.font("System", FontPosture.REGULAR, 18));
            }
        }

        return b;
    }

    /**
     * Renders a pane containing bid information on the screen
     *
     * @param product     the product
     * @param current_bid the current price of the product
     * @param bid_minimum the minimum price to bid
     * @param bid_num     the number of bids on the item
     */
    protected static void renderBidInfo(LinkedTreeMap product, Text current_bid, Text bid_minimum, Text bid_num) {
        // Initialize the current bid
        current_bid.setText("US $" + product.get("price"));

        // Initialize bid requirements
        if (!product.get("winner").equals(".")) {
            bid_minimum.setText("");
        } else {
            Double price = (Double) product.get("price");
            bid_minimum.setText("Enter US $" + (price + 10) + " or more");
        }

        // Update number of bids
        Double y = (Double) product.get("numberBids");
        bid_num.setText(String.valueOf(y.intValue()));

    }

    /**
     * Renders the time left on the product auction
     *
     * @param p       the product page controller
     * @param product the product
     */
    protected static void renderTimeLeft(ProductPageController p, LinkedTreeMap product) {
        // Initialize time left
        ArrayList<Double> time_left_al = (ArrayList<Double>) product.get("time_left_num");
        int[] time_left_a = new int[4];
        for (int i = 0; i < time_left_a.length; i++) {
            time_left_a[i] = time_left_al.get(i).intValue();
        }
        p.Time_Left.setText(time_left_a[0] + "d " + time_left_a[1] + "h " + time_left_a[2] + "m " + time_left_a[3] + "s | " + product.get("time_left_str"));
    }

    /**
     * Renders the bid history of the product
     *
     * @param p       the product page controller
     * @param product the product
     */
    protected static void renderBidHistory(ProductPageController p, LinkedTreeMap product) {
        ArrayList<ArrayList<String>> history = (ArrayList<ArrayList<String>>) product.get("bid_history");
        for (int i = history.size() - 1; i > -1; i--) {
            ArrayList<String> bid = history.get(i);

            if (i == history.size() - 1 && !product.get("winner").equals(".") && !product.get("winner").equals("*")) {
                p.bids.addRow(history.size() - i, renderBidPane(true, bid.get(0), true),
                        renderBidPane(true, bid.get(1), true),
                        renderBidPane(true, bid.get(2), true));
            } else {
                p.bids.addRow(history.size() - i, renderBidPane(false, bid.get(0), false),
                        renderBidPane(false, bid.get(1), false),
                        renderBidPane(false, bid.get(2), false));
            }
        }
    }

    /**
     * Renders the bid button and the bid amount. This method is only useful when the auction for that product ends
     *
     * @param p       the product page controller
     * @param product the product
     */
    protected static void renderBidButtonAndAmount(ProductPageController p, LinkedTreeMap product) {
        if (!product.get("winner").equals(".")) {
            if (product.get("winner").equals("*")) {
                p.bid_button.setText("ENDED");
            } else {
                p.bid_button.setText("SOLD");
            }

            p.bid_button.setStyle("-fx-background-color: #B9B9B9;");
            p.bid_button.setDisable(true);
            p.bid_button.setLayoutX(780);
            p.bid_button.setLayoutY(294);

            p.current_bid_string.setText("Final Bid:");
            p.current_bid_string.setLayoutX(563.5);
            p.current_bid_string.setLayoutY(239);

            p.bid_amount.setVisible(false);
        }
    }
}
