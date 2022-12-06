package ClientSide;
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

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Class that deals with counting down the timer on each product
 */
class countDown {
    public static ArrayBlockingQueue<Boolean> abort_q = new ArrayBlockingQueue<>(1);

    /**
     * Starts the timer for each product on the menu
     *
     * @param product the product
     */
    protected static void startTimer(LinkedTreeMap product) {

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            Double d = (Double) product.get("time_left_long");

            public void run() {
                ArrayList<Double> time_left_num = (ArrayList<Double>) product.get("time_left_num");

                Double days = time_left_num.get(0);
                Double hours = time_left_num.get(1);
                Double minutes = time_left_num.get(2);
                Double seconds = time_left_num.get(3);

                d--;
                product.put("time_left_long", d);

                // Count down until time's up!
                if (d > -1 && abort_q.isEmpty()) {
                    // Decrement seconds
                    seconds--;
                    if (seconds == -1) {
                        time_left_num.set(3, 59.0);
                    } else {
                        time_left_num.set(3, seconds);
                    }

                    // Decrement minutes
                    minutes--;
                    if (seconds == -1 && minutes > -1) {
                        time_left_num.set(2, minutes);
                    } else if ((days > 0 || hours > 0) && seconds == -1 && minutes == -1) {
                        time_left_num.set(2, 59.0);
                    }


                    // Decrement hours
                    hours--;
                    if (seconds == -1 && minutes == -1 && hours > -1) {
                        time_left_num.set(1, hours);
                    } else if (days > 0 && seconds == -1 && minutes == -1 && hours == -1) {
                        time_left_num.set(1, 23.0);
                    }

                    // Decrement days
                    days--;
                    if (seconds == -1 && minutes == -1 && hours == -1 && days > -1) {
                        time_left_num.set(0, days);
                    }
                }
                // When time is up, see if there is a winner or not
                else {
                    timer.cancel();
                }
            }
        }, 1000, 1000);
    }
}

