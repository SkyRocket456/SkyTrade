package Items;
/*
 * EE422C Final Project submission by
 * Replace <...> with your actual data.
 * Kenneth Emeremnu
 * kie226
 * 17835
 * Slip days used: <1>
 * Spring 2021
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Class that defines the information of an item on SkyTrade
 * Note: price is in dollars
 */
public class Item {
    public String title;
    public String Description;
    public int Condition;
    public String Model;
    public double price;
    public String imageURL;
    public double shippingPrice;
    public int numberBids;
    public ArrayList<Integer> time_left_num = new ArrayList();
    public String time_left_str;
    public long time_left_long;
    public String seller;
    public ArrayList<ArrayList<String>> bid_history;
    public String winner;

    /**
     * Initializes a new Item/Product
     *
     * @param price         the starting price of the product
     * @param Condition     the condition the product is in
     *                      0 - Pre Owned
     *                      1 - Refurbished
     *                      2 - New
     * @param Description   the product description
     * @param imageURL      the URL the product image is at
     * @param Model         the model of a product (if any)
     * @param seller        the seller of the product
     * @param shippingPrice the shipping price of the product
     * @param time_left     the time limit on the product
     * @param Title         the title of the post displaying the product for sale
     */
    protected Item(String Title, String Description, String Condition, String Model, String price,
                   String imageURL, String shippingPrice, String time_left, String seller) {
        this.title = Title;
        this.Description = Description;
        this.Condition = Integer.parseInt(Condition);
        this.Model = Model;
        this.price = Double.parseDouble(price);
        this.imageURL = imageURL;
        this.shippingPrice = Double.parseDouble(shippingPrice);
        this.numberBids = 0;

        String[] s = time_left.split(",");
        for (String value : s) {
            this.time_left_num.add(Integer.parseInt(value));
        }
        this.time_left_str = generateNextDate();
        this.time_left_long = TimeUnit.DAYS.toSeconds(Long.parseLong(time_left_num.get(0).toString())) +
                TimeUnit.HOURS.toSeconds(Long.parseLong(time_left_num.get(1).toString())) +
                TimeUnit.MINUTES.toSeconds(Long.parseLong(time_left_num.get(2).toString())) +
                TimeUnit.SECONDS.toSeconds(Long.parseLong(time_left_num.get(3).toString()));

        this.seller = seller;
        this.bid_history = new ArrayList<>();
        this.bid_history.add(new ArrayList<>(Arrays.asList("STARTING PRICE", "$" + this.price, Calendar.getInstance().getTime().toString())));
        this.winner = ".";
    }

    /**
     * Generates the date on which the product's auction will end
     *
     * @return the date
     */
    private String generateNextDate() {
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.DAY_OF_WEEK, time_left_num.get(0));
        cal.add(Calendar.HOUR, time_left_num.get(1));
        cal.add(Calendar.MINUTE, time_left_num.get(2));

        String day = cal.getTime().toString().substring(0, cal.getTime().toString().length() - 25);
        switch (day) {
            case "Mon":
                day = "Monday";
                break;
            case "Tue":
                day = "Tuesday";
                break;
            case "Wed":
                day = "Wednesday";
                break;
            case "Thu":
                day = "Thursday";
                break;
            case "Fri":
                day = "Friday";
                break;
            case "Sat":
                day = "Saturday";
                break;
            case "Sun":
                day = "Sunday";
                break;
        }

        String amPM;
        if (cal.get(Calendar.AM_PM) == Calendar.AM) {
            amPM = "AM";
        } else {
            amPM = "PM";
        }

        String time = new SimpleDateFormat("hh:mm").format(cal.getTime());

        return day + ", " + time + amPM;
    }
}