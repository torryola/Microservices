package net.torrydev.microservices.appuserservice;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

@Disabled
@SpringBootTest
public class KlaviyoToolTestBuggy {

    @Test
    public void testKlaviyo() throws URISyntaxException {
        sendEmailUsingKalviyo();
    }


    public void sendEmailUsingKalviyo() throws URISyntaxException {
//        Result result = new Result();
        // Convert Mapped Data to Json String
        // Sent Header Content-Type - application/json
        // Create HttpPost
        // Attach data and header
        // Send Post
        /*
            KLAVIYO_ENVIRONMENT=Sandbox
            KLAVIYO_ENABLED=true
            KLAVIYO_PUBLIC_TOKEN=SM7WFW
            KLAVIYO_PRIVATE_KEY=pk_2d3ff17090fcdeb30ed64d47dcd9c1903e
            KLAVIYO_TRACK_URL=https://a.klaviyo.com/api/track
         */
        String strData = getSampleData(); //new Gson().toJson(emailData);

        System.out.println("Payload ==> "+strData);

        // create client
        CloseableHttpClient client = HttpClients.createDefault();
        // create request object
        HttpPost post = new HttpPost(new URI("https://a.klaviyo.com/api/track"));
        post.setHeader("Content-Type","application/json");
        try {

            StringEntity stringEntity = new StringEntity(strData);
            post.setEntity(stringEntity);
            // send request
            //CloseableHttpResponse response = client.execute(post);
            // read response
//            String responseStr = EntityUtils.toString(response.getEntity());
//            System.out.println(responseStr);
            ResponseHandler< String > responseHandler = response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                String res = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                System.out.println(res);
                return res;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            };

            String responseBody = client.execute(post, responseHandler);
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

//
//        if (response.getStatusLine().getStatusCode() == 200 && responseStr.equals("1"))
//                System.out.println("============ Data sent Successfully =================");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("============ Data sent UnSuccessfully =================");
//        }
    }

    private String getSampleData(){
       return "{\"customer_properties\":{\"billing_address_city\":\"Reading\",\"billing_last_name\":\"Ola\",\"contact_account_details\":\"You have used the guest checkout\\n\\n\",\"billing_primary_email_address\":\"torryselfdev@gmail.com\",\"$email\":\"torryselfdev@gmail.com\",\"billing_address_line_2\":\"Amesthyst Lane\",\"billing_address_postcode\":\"RG30 2EZ\",\"billing_address_line_1\":\"K Arun Court\",\"billing_address_country\":\"United Kingdom\",\"contact_account_downloads\":\"\",\"billing_first_name\":\"Tori\",\"billing_primary_telephone\":\"+44 789 902 93\"}," +
               "\"event\":\"Placed Order\",\"properties\":{\"venue_name\":\"Red Bull Ring, Austria\",\"gift_voucher_code\":[],\"delivery_address\":\"torryselfdev@gmail.com\",\"order_reference\":\"F1AT350633\",\"delivery_information\":\"\",\"delivery_address_city\":\"Reading\",\"event_choice\":\" for the Austrian Formula 1 Grand Prix 2022\",\"delivery_address_line_1\":\"K Arun Court\",\"delivery_address_line_2\":\"Amesthyst Lane\",\"charity\":\" 10.00\",\"$value\":408.0,\"delivery_method\":\"D E L I V E R Y    E M A I L\\n\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\",\"retailer_website_url\":\"http://www.website1300.com\",\"Items\":[{\"order_reference\":\"F1AT350633\",\"currency_symbol\":\"£\",\"currency\":\"Great British Pounds (GBP £)\",\"order_details\":[{\"unit\":\"1 \",\"price\":\" 222.00\",\"description\":\" T8 (Sunday) Adult @ 222.00 \"},{\"unit\":\"1 \",\"price\":\" 111.00\",\"description\":\" T8 (Sunday) Disabled @ 111.00 \"},{\"unit\":\"1 \",\"price\":\" 23.00\",\"description\":\" T8 (Sunday) Child @ 23.00 \"},{\"unit\":\"1\",\"price\":\" 27.00\",\"description\":\"Service Fee and Email Delivery \"},{\"unit\":\"1 \",\"price\":\" 0.00\",\"description\":\" Free Content Pack 3-Month Subscription @ 0.00 \"}],\"status\":\"Your booking is confirmed. Approximately 10 days before the race, we will email you your e-tickets to print out and present at the event for entry.\\n\\n\"}],\"grand_total\":\" 408.00\",\"booking_protection\":\" 15.00\",\"employee_first_name\":\"Joe324499\",\"payment_method\":\"Credit/Debit Card\",\"delivery_last_name\":\"Ola\",\"travel_affiliate_call_to_action\":\"http://www.website1300.com/f1-austria/accommodation.htm\",\"card_last_4_digits\":\"1111\",\"remaining_balance\":\" 408.0\",\"employee_last_name\":\"Bloggs324499\",\"delivery_address_postcode\":\"RG30 2EZ\",\"event_title\":\"Austrian Formula 1 Grand Prix 2022\",\"booking_status\":\"Confirmed\",\"sales_order_protection_terms\":\"B O O K I N G    P R O T E C T I O N\\n\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\nYou have chosen to take out optional Booking Protection cover. For further information including the terms and conditions and claims procedure, please visit http://www.website1300.com/help/booking-protection.htm\\n\\n\",\"payment_reference\":\"F1AT350633-220207154846\",\"delivery_address_country\":\"United Kingdom\",\"subtotal\":\" 356.00\",\"delivery_first_name\":\"Tori\"," +
               "\"$event_id\":\"F1AT350633\"," +
               "\"retailer_fax_number\":\"\"," +
               "\"retailer_address\":\"Motorsport Tickets, \\nSuite 31 Beaufort Court, \\nAdmirals Way, \\nLondon, \\nE14 9XL, \\nUnited Kingdom\",\"retailer_email_address\":\"yourorder@motorsporttickets.com\",\"retailer_name\":\"Motorsport Tickets\",\"loyalty_point\":0,\"retailer_telephone_number\":\"123816988\"},\"token\":\"SM7WFW\"}";

    }
}
