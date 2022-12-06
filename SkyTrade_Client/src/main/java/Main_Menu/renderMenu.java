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

import ProductPage.ProductPageController;
import com.google.gson.internal.LinkedTreeMap;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;

import static ClientSide.Reader.Products;
import static ClientSide.Reader.productIDs;
import static Main_Menu.MenuController.*;

/**
 * Class that renders products onto the screen
 */
class renderMenu {
    /**
     * Renders and adds all products on the gridpane in the middle of the main menu
     *
     * @param products_Menu the gridpane
     */
    protected static void renderProductsMenu(GridPane products_Menu) {
        // Initialize by clearing the products menu and time_lefts for animation
        products_Menu.getChildren().clear();

        prices_all = new ArrayList<>();
        time_left_all = new ArrayList<>();
        numBids_all = new ArrayList<>();
        sold_ended_all = new ArrayList<>();

        // Render the products on the screen
        for (int i = 0, j = 0; i < productIDs.size(); i++) {
            // If the user searched nothing
            if (search_text.isEmpty()) {
                // if the product was added to the gridpane, increment gridpane row counter (j)
                if (checkCheckBoxes(i, j, products_Menu)) {
                    j++;
                }
            }
            // If the user searched something
            else {
                String title = (String) Products.get(productIDs.get(i)).get("title");
                String model = (String) Products.get(productIDs.get(i)).get("Model");
                // if the product has the keyword in their title and was added to the gridpane, increment gridpane row counter (j)
                if (title.toLowerCase().contains(search_text.toLowerCase()) && checkCheckBoxes(i, j, products_Menu)) {
                    j++;
                } else if (model != null && model.toLowerCase().contains(search_text.toLowerCase()) && checkCheckBoxes(i, j, products_Menu)) {
                    // if the product has the keyword in their model and was added to the gridpane, increment gridpane row counter (j)
                    j++;
                }
            }
        }
        // Remove the bottom line from the last produce pane
        if (products_Menu.getChildren().size() > 0) {
            ObservableList<Node> panes = products_Menu.getChildren();
            AnchorPane last = (AnchorPane) panes.get(panes.size() - 1);
            last.getChildren().remove(last.getChildren().size() - 1);
        }
    }

    /**
     * Initializes a product and adds it to the gridpane
     *
     * @param i             the index of the product
     * @param j             the index of the gridpane row
     * @param products_Menu the gridpane
     */
    private static void initializeProductPane(int i, int j, GridPane products_Menu) {
        // Renders product and adds it to an anchorpane
        AnchorPane product_Pane = renderProductPane(productIDs.get(i), Products.get(productIDs.get(i)));

        // Initialize line at the bottom of the information for separation
        Line l = new Line(0, 210, 782, 207);
        l.setStroke(Color.BLACK);
        product_Pane.getChildren().add(l);

        // Add pane to screen
        products_Menu.addRow(j, product_Pane);
    }

    /**
     * Renders a product and adds all of its values onto an anchorpane
     *
     * @param productID the product iD
     * @param product   the product
     * @return the anchorpane holding all product information
     */
    private static AnchorPane renderProductPane(String productID, LinkedTreeMap product) {
        // Initialize pane for rendering
        AnchorPane product_pane = new AnchorPane();
        product_pane.setPrefSize(800, 211);

        // Initialize product image
        Image product_image = new Image((String) product.get("imageURL"));
        ImageView productImageView = new ImageView(product_image);

        productImageView.setFitWidth(150);
        productImageView.setFitHeight(150);
        productImageView.setLayoutX(44);
        productImageView.setLayoutY(30);

        productImageView.setOnMouseClicked(event -> {
            ProductPageController.productID = productID;
            ProductPageController.product = product;
            ProductPageController.toMainMenu = true;
            ProductPageController.GoToProductPage();
        });

        product_pane.getChildren().add(productImageView);

        // Initialize product title
        Text Title = new Text((String) product.get("title"));
        Title.setWrappingWidth(560);
        Title.setFont(Font.font("Arial", FontPosture.REGULAR, 22));
        Title.setLayoutX(225);
        Title.setLayoutY(50);

        Title.setOnMouseClicked(event -> {
            ProductPageController.productID = productID;
            ProductPageController.product = product;
            ProductPageController.toMainMenu = true;
            ProductPageController.GoToProductPage();
        });

        product_pane.getChildren().add(Title);

        // Initialize product condition
        TextField Condition;
        switch ((int) (double) product.get("Condition")) {
            case 0:
                Condition = new TextField("Pre Owned");
                break;
            case 1:
                Condition = new TextField("Refurbished");
                break;
            default:
                Condition = new TextField("Brand New");
                break;
        }
        Condition.setAlignment(Pos.CENTER);
        Condition.setEditable(false);
        Condition.setPrefSize(80, 25);

        if (Title.getText().length() > 50) {
            Condition.setLayoutX(Title.getLayoutX() - 1);
            Condition.setLayoutY(Title.getLayoutY() + 35);
        } else {
            Condition.setLayoutX(Title.getLayoutX() - 1);
            Condition.setLayoutY(Title.getLayoutY() + 9);
        }

        product_pane.getChildren().add(Condition);

        // Initialize product model name
        String product_model = (String) product.get("Model");
        if (product_model != null) {
            Text p = new Text(product_model);
            TextField Product_Model = new TextField(product_model);
            Product_Model.setEditable(false);
            Product_Model.setPrefWidth(TextUtils.computeTextWidth(p.getFont(), p.getText(), 0.0D) + 15);
            Product_Model.setAlignment(Pos.CENTER);
            Product_Model.setLayoutX(Condition.getLayoutX() + 90);
            Product_Model.setLayoutY(Condition.getLayoutY());
            product_pane.getChildren().add(Product_Model);
        }

        // Initialize product price
        Text price = new Text("$" + product.get("price"));
        price.setLayoutX(225);
        price.setLayoutY(130);

        price.setFont(Font.font("System", FontPosture.REGULAR, 19));
        product_pane.getChildren().add(price);
        prices_all.add(price);


        // Initialize number of bids on product
        Text num_bids = new Text((int) (double) product.get("numberBids") + " bids");
        num_bids.setLayoutX(225);
        num_bids.setLayoutY(145);

        product_pane.getChildren().add(num_bids);
        numBids_all.add(num_bids);

        // Initialize time left on the bid
        ArrayList<Double> tl = (ArrayList<Double>) product.get("time_left_num");
        int[] time_left_a = new int[4];
        for (int i = 0; i < time_left_a.length; i++) {
            time_left_a[i] = (tl.get(i)).intValue();
        }

        Text time_left = new Text(time_left_a[0] + "d " + time_left_a[1] + "h " + time_left_a[2] + "m " + "left" + " (" + product.get("time_left_str") + ")");
        time_left.setFont(Font.font("System", FontWeight.BOLD, 12));
        time_left.setLayoutX(225);
        time_left.setLayoutY(161);

        product_pane.getChildren().add(time_left);
        time_left_all.add(time_left);

        // Initialize shipping cost
        Text shippingCost = new Text("+$" + product.get("shippingPrice") + " shipping");
        shippingCost.setLayoutX(225);
        shippingCost.setLayoutY(177);

        product_pane.getChildren().add(shippingCost);

        // Initialize if product has been sold or auction ended
        Text sold_ended = new Text();
        sold_ended.setLayoutX(225);
        sold_ended.setLayoutY(195);

        product_pane.getChildren().add(sold_ended);
        sold_ended_all.add(sold_ended);

        return product_pane;
    }

    /**
     * Check the checkboxes to see if the user only wanted to display certain conditions (products)
     *
     * @param i             index for product
     * @param j             the gridpane row
     * @param products_Menu the gridpane
     * @return if the product was added to the gridpane return true, false otherwise
     */
    private static boolean checkCheckBoxes(int i, int j, GridPane products_Menu) {
        if (Products.get(productIDs.get(i)).get("Condition").equals(BoxSetting) && BoxSetting != -1.0) {
            initializeProductPane(i, j, products_Menu);
            return true;
        }
        else if (BoxSetting == -1.0) {
            initializeProductPane(i, j, products_Menu);
            return true;
        }
        prices_all.add(new Text());
        time_left_all.add(new Text());
        numBids_all.add(new Text());
        sold_ended_all.add(new Text());
        return false;
    }
}

