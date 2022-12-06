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
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * Class that animates the product page
 */
class animatePage {
    /**
     * Animates the product page
     *
     * @param p           the controller of the product page
     * @param product     the product on the page
     * @param bids        the gridpane showing the bid history
     * @param bid_num     the number of bids
     * @param current_bid the current price
     * @param bid_minimum the minimum bid
     */
    protected static void animateProductPage(ProductPageController p, LinkedTreeMap product, GridPane bids, Text bid_num, Text current_bid, Text bid_minimum) {
        // Update bid price, bid minimum, and number of bids
        renderPage.renderBidInfo(product, current_bid, bid_minimum, bid_num);

        // Update time left
        renderPage.renderTimeLeft(p, product);

        // Update bid history
        ObservableList<Node> o = bids.getChildren();
        o.remove(4, o.size());
        renderPage.renderBidHistory(p, product);

        // Initialize bid button and bid amount textfield
        renderPage.renderBidButtonAndAmount(p, product);
    }
}
