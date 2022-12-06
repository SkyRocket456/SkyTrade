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

/**
 * Class that deals with putting everything in an order in an orderly fashion : )
 */
public class Order {
    public String Order_Number;
    public String User;
    public String customerID;
    public String Shipping_Address;
    public String City;
    public String productID;
    public String Product_Title;
    public String Product_Price;
    public String date;

    public Order(String orderNumber, String username, String customerID, String shippingAddress,
                 String city, String productID, String product_title, String product_final_price, String date) {
        this.Order_Number = orderNumber;
        this.User = username;
        this.customerID = customerID;
        this.Shipping_Address = shippingAddress;
        this.City = city;
        this.productID = productID;
        this.Product_Title = product_title;
        this.Product_Price = product_final_price;
        this.date = date;

    }
}
