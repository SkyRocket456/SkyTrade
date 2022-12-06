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

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Class deals with sending emails to the client
 */
public class Email {
    public static Session session;

    /**
     * Checks if the email of the user is valid or not
     *
     * @param email the email of the user
     * @return if email is valid, true, false otherwise
     */
    public static boolean validateEmail(String email) {
        try {
            InternetAddress internetAddress = new InternetAddress(email);
            internetAddress.validate();
        } catch (AddressException e) {
            return false;
        }
        return true;
    }

    /**
     * Initializes and connects the server to the email server that sends the emails
     */
    public static void initializeEmailServer() {
        // Mention the SMTP server address. Below Gmail's SMTP server is being used to send email
        String host = "smtp.gmail.com";
        // Get system properties
        Properties properties = System.getProperties();
        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        // Get the Session object.// and pass username and password

        session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("noreplyskytrade@gmail.com", "gxldhocnkerddmvh");
            }
        });
    }

    /**
     * Email the user's email
     *
     * @param receiver the user's email that will recieve the email
     * @param subject  the subject of the email
     * @param content  the content of the email
     */
    public static void SendEmail(String receiver, String subject, String content) {
        // Used to debug SMTP issues
        Email.session.setDebug(false);
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(Email.session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress("noreplyskytrade@gmail.com"));
            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            // Set Subject: header field
            message.setSubject(subject);

            // Send the actual HTML message, as big as you like
            message.setContent(content, "text/html");

            // Send message
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    /**
     * The message for the one time password
     *
     * @param OTP the one time password
     * @return the message to be sent to the user email
     */
    public static String OTPMessage(String OTP) {
        return "<p style=\"text-align: left;\"><img style=\"display: block; margin-left: auto; margin-right: auto;\" src=\"https://i.imgur.com/MSfPXBY.png\" alt=\"Logo\" width=\"120\" height=\"120\" /></p>\n" +
                "<h2 style=\"text-align: center;\">Verify your new SkyTrade account</h2>\n" +
                "<p>To verify your email address, please use the following One Time Password (OTP):</p>\n" +
                "<p><span style=\"text-decoration: underline;\">" + OTP + "</span></p>\n" +
                "<p>Do not share this OTP with anyone. SkyTrade takes your account security very seriously. SkyTrade Services will never ask you to disclose or verify your SkyTrade password or OTP. If you receive a suspicious email with a link to update your account information, do not click on the link</p>\n" +
                "<p>Thank you!</p>";
    }

    /**
     * The message for the order confirmation
     *
     * @param orderNumber the order number of the winning bid
     * @param winner_a    the user who won
     * @param product     the product the winner won
     * @param productID   the ID of the product
     * @return the message to be sent to the user
     */
    public static String OrderConfirmation(String orderNumber, Account winner_a, String productID, Item product) {
        return "<h2 style=\"text-align: center;\"><img style=\"display: block; margin-left: auto; margin-right: auto;\" src=\"https://i.imgur.com/MSfPXBY.png\" alt=\"Logo\" width=\"120\" height=\"120\" />Congratulations on the winning bid!</h2>\n" +
                "<p style=\"font-family: Arial; text-align: center;\">A receipt of your winning bid is below. Be sure to keep it in a safe place for future reference</p>\n" +
                "<table style=\"border-collapse: collapse; width: 100%; height: 71px;\" border=\"1\">\n" +
                "<tbody>\n" +
                "<tr style=\"height: 18px;\">\n" +
                "<td style=\"width: 50%; height: 18px;\">Order Number</td>\n" +
                "<td style=\"width: 50%; height: 18px;\">" + orderNumber + "</td>\n" +
                "</tr>\n" +
                "<tr style=\"height: 18px;\">\n" +
                "<td style=\"width: 50%; height: 18px;\">Username</td>\n" +
                "<td style=\"width: 50%; height: 18px;\">" + winner_a.username + "</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td style=\"width: 50%;\">Customer ID</td>\n" +
                "<td style=\"width: 50%;\">" + winner_a.ID + "</td>\n" +
                "</tr>\n" +
                "<tr style=\"height: 17px;\">\n" +
                "<td style=\"width: 50%; height: 17px;\">Shipping Address</td>\n" +
                "<td style=\"width: 50%; height: 17px;\">" + winner_a.shipping_address + "</td>\n" +
                "</tr>\n" +
                "<tr style=\"height: 18px;\">\n" +
                "<td style=\"width: 50%; height: 18px;\">City</td>\n" +
                "<td style=\"width: 50%; height: 18px;\">" + winner_a.city + ", " + winner_a.state + " " + winner_a.zip_code + "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<p style=\"text-align: left;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</p>\n" +
                "<p>Product won:</p>\n" +
                "<table style=\"height: 36px; width: 100%; border-collapse: collapse; float: left;\" border=\"1\">\n" +
                "<tbody>\n" +
                "<tr style=\"height: 18px;\">\n" +
                "<td style=\"width: 14.5908%; height: 18px;\">Product ID</td>\n" +
                "<td style=\"width: 85.4092%; text-align: left; height: 18px;\">" + productID + "</td>\n" +
                "</tr>\n" +
                "<tr style=\"height: 18px;\">\n" +
                "<td style=\"width: 14.5908%; height: 18px;\"><img style=\"display: block; margin-left: auto; margin-right: auto;\" src=\"" + product.imageURL + "\" alt=\"\" width=\"63\" height=\"63\" /></td>\n" +
                "<td style=\"width: 85.4092%; height: 18px; text-align: left;\">" + product.title + "<br />$" + product.price + "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p style=\"text-align: left;\">&nbsp;</p>\n" +
                "<p style=\"text-align: left;\">&nbsp;</p>\n" +
                "<p style=\"text-align: left;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</p>\n" +
                "<p style=\"text-align: left;\">&nbsp;</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p style=\"text-align: left;\">&nbsp;</p>";
    }
}

