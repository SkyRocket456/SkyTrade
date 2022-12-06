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
import ProductPage.ProductPageController;
import com.google.gson.internal.LinkedTreeMap;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;

import java.util.ArrayList;

/**
 * Class that renders owned products onto the screen
 */
class renderYourSkyTrade {
    /**
     * Adds owned products to the gridpane on MySkyTrade
     *
     * @param owned_products  the gridpane for owned productss
     * @param bidded_products the gridpane for bidded products
     */
    public static void renderProducts(GridPane owned_products, GridPane bidded_products) {
        owned_products.getChildren().clear();
        bidded_products.getChildren().clear();

        for (int i = Client.owned_items.size() - 1, j = 0; i > -1; i--, j++) {
            owned_products.addRow(j, renderProduct(Client.owned_items, i));
        }

        for (int i = Client.bidded_items.size() - 1, j = 0; i > -1; i--, j++) {
            bidded_products.addRow(j, renderProduct(Client.bidded_items, i));
        }
    }

    /**
     * Adds products to the gridpane(s) on MySkyTrade
     *
     * @param items the list of items to get from
     * @param i     the index of the product in items
     * @return the pane with all the product-specific information
     */
    private static AnchorPane renderProduct(ArrayList<LinkedTreeMap> items, int i) {
        LinkedTreeMap item = (LinkedTreeMap) items.get(i).get("product");
        String itemID = (String) items.get(i).get("productID");

        AnchorPane product_pane = new AnchorPane();
        product_pane.setMinSize(298, 85);
        product_pane.setStyle("-fx-background-color: #ebebeb; -fx-border-color: black");
        product_pane.setOnMouseClicked(event -> {
            ProductPageController.productID = itemID;
            ProductPageController.product = item;
            ProductPageController.toMainMenu = false;
            ProductPageController.GoToProductPage();
        });

        Image product_image = new Image((String) item.get("imageURL"));
        ImageView productImageView = new ImageView(product_image);
        productImageView.setLayoutX(14);
        productImageView.setLayoutY(8);
        productImageView.setFitHeight(70);
        productImageView.setFitWidth(70);

        product_pane.getChildren().add(productImageView);

        Text product_title = new Text((String) item.get("title"));
        product_title.setFont(Font.font("System", FontPosture.REGULAR, 12));
        product_title.setWrappingWidth(200);
        product_title.setLayoutX(91);
        product_title.setLayoutY(21);

        product_pane.getChildren().add(product_title);

        Double d = (Double) items.get(i).get("price");
        Text product_price = new Text("$" + d);
        product_price.setFont(Font.font("System", FontPosture.REGULAR, 12));
        product_price.setLayoutX(91);
        product_price.setLayoutY(76);

        product_pane.getChildren().add(product_price);

        return product_pane;
    }

}
