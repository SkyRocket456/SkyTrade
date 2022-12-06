package Main_Menu;
/*
 * EE422C Final Project submission by
 * Replace <...> with your actual data.
 * Kenneth Emeremnu
 * kie226
 * 17835
 * Slip days used: <1>
 * Spring 2021
 */

import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.Objects;

import static ClientSide.Reader.Products;
import static ClientSide.Reader.productIDs;
import static Main_Menu.MenuController.*;

/**
 * Class that deals with the animation of the menu
 */
class animateMenu {
    /**
     * Animate the main menu
     */
    protected static void animateMainMenu() {
        // Update the prices of each product
        animatePrices();

        // Update number of bids on products
        animateNumBids();

        // Update time left on all products
        animateTimeLeft();
    }

    /**
     * Animate the number of bids for each product on the menu
     */
    private static void animatePrices() {
        for (int i = 0; i < prices_all.size(); i++) {
            // If the user didn't search anything
            if (search_text.isEmpty()) {
                CheckBoxesPrices(i);
            }
            // If the user searched something
            else {
                String title = (String) Products.get(productIDs.get(i)).get("title");
                String model = (String) Products.get(productIDs.get(i)).get("Model");
                if (title.toLowerCase().contains(search_text.toLowerCase())) {
                    CheckBoxesPrices(i);
                } else if (model != null && model.toLowerCase().contains(search_text.toLowerCase())) {
                    CheckBoxesPrices(i);
                }
            }
        }
    }


    /**
     * Animate the number of bids for each product on the menu
     */
    private static void animateNumBids() {
        for (int i = 0; i < numBids_all.size(); i++) {
            // If the user didn't search anything
            if (search_text.isEmpty()) {
                CheckBoxesNumBids(i);
            }
            // If the user searched something
            else {
                String title = (String) Products.get(productIDs.get(i)).get("title");
                String model = (String) Products.get(productIDs.get(i)).get("Model");
                if (title.toLowerCase().contains(search_text.toLowerCase())) {
                    CheckBoxesNumBids(i);
                } else if (model != null && model.toLowerCase().contains(search_text.toLowerCase())) {
                    CheckBoxesNumBids(i);
                }
            }
        }
    }

    /**
     * Animate the time left for each product on the menu
     */
    private static void animateTimeLeft() {
        for (int i = 0; i < time_left_all.size(); i++) {
            // If the user didn't search anything
            if (search_text.isEmpty()) {
                CheckBoxesTimeLeft(i);
            }
            // If the user searched something
            else {
                String title = (String) Products.get(productIDs.get(i)).get("title");
                String model = (String) Products.get(productIDs.get(i)).get("Model");
                if (title.toLowerCase().contains(search_text.toLowerCase())) {
                    CheckBoxesTimeLeft(i);
                } else if (model != null && model.toLowerCase().contains(search_text.toLowerCase())) {
                    CheckBoxesTimeLeft(i);
                }
            }
        }
    }

    /**
     * Animate the auction result (if any) for each product on the menu
     *
     * @param i index for product
     */
    private static void animateAuctionResult(int i) {
        // Update if product has been sold or auction ended
        if (!Products.get(productIDs.get(i)).get("winner").equals(".")) {
            String winner = (String) Products.get(productIDs.get(i)).get("winner");
            if (Objects.equals(winner, "*")) {
                sold_ended_all.get(i).setText("ENDED " + Products.get(productIDs.get(i)).get("time_left_str"));
                sold_ended_all.get(i).setFill(Paint.valueOf("#9e3c2f"));
            } else {
                sold_ended_all.get(i).setText("SOLD " + Products.get(productIDs.get(i)).get("time_left_str"));
                sold_ended_all.get(i).setFill(Paint.valueOf("#4fa876"));
            }
        }
    }

    /**
     * Check the checkboxes to see if the user only wanted to display certain conditions (prices)
     *
     * @param i index for product
     */
    private static void CheckBoxesPrices(int i) {
        if (Products.get(productIDs.get(i)).get("Condition").equals(BoxSetting) && BoxSetting != -1.0) {
            prices_all.get(i).setText("$" + Products.get(productIDs.get(i)).get("price"));
        } else if (BoxSetting == -1.0) {
            prices_all.get(i).setText("$" + Products.get(productIDs.get(i)).get("price"));
        }
    }

    /**
     * Check the checkboxes to see if the user only wanted to display certain conditions (bids)
     *
     * @param i index for product
     */
    private static void CheckBoxesNumBids(int i) {
        if (Products.get(productIDs.get(i)).get("Condition").equals(BoxSetting) && BoxSetting != -1.0) {
            numBids_all.get(i).setText((int) (double) Products.get(productIDs.get(i)).get("numberBids") + " bids");
        } else if (BoxSetting == -1.0) {
            numBids_all.get(i).setText((int) (double) Products.get(productIDs.get(i)).get("numberBids") + " bids");
        }
    }

    /**
     * Check the checkboxes to see if the user only wanted to display certain conditions (time left)
     *
     * @param i index for product
     */
    private static void CheckBoxesTimeLeft(int i) {
        if (Products.get(productIDs.get(i)).get("Condition").equals(BoxSetting) && BoxSetting != -1.0) {
            updateTimeLeft(i);
        } else if (BoxSetting == -1.0) {
            updateTimeLeft(i);
        }
    }

    /**
     * Update time left for each product on the menu
     *
     * @param i index for product
     */
    private static void updateTimeLeft(int i) {
        ArrayList<Double> tl = (ArrayList<Double>) Products.get(productIDs.get(i)).get("time_left_num");
        int[] time_left_a = new int[4];
        for (int j = 0; j < time_left_a.length; j++) {
            time_left_a[j] = (tl.get(j)).intValue();
        }

        time_left_all.get(i).setText(time_left_a[0] + "d " + time_left_a[1] + "h " + time_left_a[2] + "m " + "left"
                + " (" + Products.get(productIDs.get(i)).get("time_left_str") + ")");

        animateAuctionResult(i);
    }
}
