package User;
/*
 * EE422C Final Project submission by
 * Replace <...> with your actual data.
 * Kenneth Emeremnu
 * kie226
 * 17835
 * Slip days used: <1>
 * Spring 2021
 */

import Items.Item;

/**
 * Class whose purpose is to hold a productID and a product
 */
public class ProductInfo {
    public String productID;
    public Item product;
    public Double price;

    public ProductInfo(String productID, Item product, Double price) {
        this.productID = productID;
        this.product = product;
        this.price = price;
    }
}
