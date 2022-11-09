package net.torrydev.microservices.appuserservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Disabled
@SpringBootTest
@Slf4j
public class KlaviyoTest
{
    // Event Flow
    public static final String PLACED_ORDER_EVENTS = "Placed Order";
    // Klaviyo Data Key Properties
    public static final String TOKEN = "token";
    public static final String EVENT = "event";
    public static final String CUSTOMER_PROPERTIES = "customer_properties";
    public static final String PROPERTIES = "properties";
    public static final String ITEMS = "Items";
    public static final String PROPERTIES_EVENT_ID = "$event_id"; // Required - This must be unique per flow/track calls
    public static final String PROPERTIES_VALUE = "$value"; // Required
    public static final String PROPERTIES_EMAIL = "$email"; // Required
    private static final String BILLING_FIRST_NAME = "billing_first_name";
    private static final String BILLING_LAST_NAME = "billing_last_name";
    private static final String EVENT_CHOICE = "event_choice";
    private static final String EVENT_TITLE = "event_title";
    private static final String RETAILER_EMAIL_ADDRESS = "retailer_email_address";
    private static final String RETAILER_TELEPHONE_ADDRESS = "retailer_telephone_number";
    private static final String RETAILER_FAX_NUMBER = "retailer_fax_number";
    private static final String ORDER_REFERENCE = "order_reference";
    private static final String BOOKING_DETAILS_INTO = "booking_details_intro";
    private static final String KLAVIYO_FLOW_URL = "https://a.klaviyo.com/api/track";
    private static final String  KLAVIYO_PUBLIC_TOKEN="SM7WFW";

    public final ObjectMapper objectMapper = new ObjectMapper();
    public RestTemplate restTemplate;
    public Map<String, Object> testData = new HashMap<>();
    public Map<String, Object> customer_properties_data = new HashMap<>();
    public Map<String, Object> retailer_properties_data = new HashMap<>();
    public Map<String, Object> order_properties_data = new HashMap<>();
    public Map<String, Object> properties_data = new HashMap<>(); // This can be for order-related details
    public List<Map<String, Object>> ORDER_ITEMS = new ArrayList<>();
    static final String RECIPIENT_EMAIL = "test-orders@motorsporttickets.com";

    @BeforeEach
    void init(){
        restTemplate = buildTemplate();
        // Add Token
      //  testData.put(TOKEN, klaviyoConfig.getPublictoken());
    }

    // Using Apache HttClient
    @Test
    public void sendEmailUsingKalviyo() {
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
        testData.put(EVENT, PLACED_ORDER_EVENTS);


        String orderRef = "F1BE"+ (new Random().nextInt(1000) + 1000);
        // Other properties
        properties_data.put(PROPERTIES_EVENT_ID, orderRef);
        properties_data.put(PROPERTIES_VALUE, new Random().nextDouble());
        properties_data.put(EVENT_CHOICE, " ");
        properties_data.put(EVENT_TITLE, "F1-Belgium");
        properties_data.put(TOKEN, KLAVIYO_PUBLIC_TOKEN);

        // Items - e.g. Order Details
        Map<String, Object> orderDetails = getOrderDetails(orderRef);
        ORDER_ITEMS.add(orderDetails);

        // Add Items to Properties
        properties_data.put(ITEMS, ORDER_ITEMS);
        //Add as part of properties rather than nesting
        //properties_data.putAll(orderDetails);

        testData.put(CUSTOMER_PROPERTIES, getCustomerProperties());
        addRetailerProperties(properties_data);
        testData.put(PROPERTIES, properties_data);

        String strData = new Gson().toJson(testData);

        System.out.println("Payload ==> "+strData);

        // create client
        CloseableHttpClient client = HttpClients.createDefault();
        // create request object
        HttpPost post = new HttpPost(KLAVIYO_FLOW_URL);
//        post.setHeader("Content-Type","application/json");
        try {

            StringEntity entity =  new StringEntity(strData);
            post.setEntity(entity);
            // send request
            CloseableHttpResponse response = client.execute(post);
            log.trace(" CloseableHttpResponse ===>> {}", response);
            // read response
            //  String responseStr = EntityUtils.toString(response.getEntity());
            // System.out.println(responseStr);

            StringBuilder textBuilder = new StringBuilder();
            try (Reader reader = new BufferedReader(new InputStreamReader
                    (response.getEntity().getContent(), Charset.forName(StandardCharsets.UTF_8.name())))) {
                int c = 0;
                while ((c = reader.read()) != -1) {
                    textBuilder.append((char) c);
                }

                System.out.println(" Response ===>> "+textBuilder);
            }

            if (response.getStatusLine().getStatusCode() == 200)
                System.out.println("============ Data sent Successfully =================");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("============ Data sent UnSuccessfully =================");
        }
    }

    @Test
    void sendSampleTestWithData() throws JsonProcessingException {
        testData.put(EVENT, PLACED_ORDER_EVENTS);

        String orderRef = "F1BE"+ (new Random().nextInt(1000) + 1000);
        // Other properties
        properties_data.put(PROPERTIES_EVENT_ID, orderRef);
        properties_data.put(PROPERTIES_VALUE, new Random().nextDouble());
        properties_data.put(EVENT_CHOICE, " ");
        properties_data.put(EVENT_TITLE, "F1-Belgium");
        properties_data.put(TOKEN, KLAVIYO_PUBLIC_TOKEN);

        // Items - e.g. Order Details
        Map<String, Object> orderDetails = getOrderDetails(orderRef);
        ORDER_ITEMS.add(orderDetails);

        // Add Items to Properties
        properties_data.put(ITEMS, ORDER_ITEMS);
        //Add as part of properties rather than nesting
        //properties_data.putAll(orderDetails);

        testData.put(CUSTOMER_PROPERTIES, getCustomerProperties());
         addRetailerProperties(properties_data);
        testData.put(PROPERTIES, properties_data);

        String strData = objectMapper.writeValueAsString(testData);

        System.out.printf(" Test Data : %s%n",strData); //payload);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // Required

        HttpEntity<String> httpEntity = new HttpEntity<>(payload, headers);
        ResponseEntity<String> response = restTemplate.exchange(KLAVIYO_FLOW_URL, HttpMethod.POST, httpEntity, String.class);
        log.trace(" Response ===>> {}", response);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertEquals("1", response.getBody());
    }

    Map<String, Object> getOrderDetails(String orderRef){
        order_properties_data.put(ORDER_REFERENCE, orderRef);
        order_properties_data.put(BOOKING_DETAILS_INTO, "Event: Abu Dhabi Formula 1 Grand Prix 2021\n" +
                "Venue: Abu Dhabi\n" +
                "Booking reference: "+orderRef+"\n" +
                "\n" +
                "All prices are in Euro (EUR €)\n" +
                "\n" +
                "1 x South Upper Rows (Saturday-Sunday) Adult @ 486.00 = 486.00\n" +
                "\n" +
                "Subtotal = 486.00\n" +
                "\n" +
                "Service Fee and Delivery = 37.00\n" +
                "Charity = 10.00\n" +
                "Booking Protection = 17.00\n" +
                "\n" +
                "GRAND TOTAL = 550.00\n" +
                "\n" +
                "\n" +
                "\n" +
                "Remaining balance = 550.0\n");
        return order_properties_data;
    }

    // Create Customer Properties Object to Bundle with Data
    private Map<String, Object> getCustomerProperties() {
        customer_properties_data.put(BILLING_FIRST_NAME, "AAAA");
        customer_properties_data.put(BILLING_LAST_NAME, "BBBB");
        customer_properties_data.put(PROPERTIES_EMAIL, RECIPIENT_EMAIL);
        return customer_properties_data;
    }

    // retailer details
    private void addRetailerProperties(Map<String, Object> properties_data) {
        properties_data.put(RETAILER_EMAIL_ADDRESS, "yourorder@motorsporttickets.com");
        properties_data.put(RETAILER_TELEPHONE_ADDRESS, "080037382837");
        properties_data.put(RETAILER_FAX_NUMBER, "retailer_fax_number");
    }

    // retailer details
    private Map<String, Object> getRetailerProperties() {
        retailer_properties_data.put(RETAILER_EMAIL_ADDRESS, "retailer_email_address");
        retailer_properties_data.put(RETAILER_TELEPHONE_ADDRESS, "retailer_telephone_number");
        retailer_properties_data.put(RETAILER_FAX_NUMBER, "retailer_fax_number");
        return retailer_properties_data;
    }

    // Sample Payload
    private String payload = "{\"customer_properties\":{\"billing_address_city\":\"Reading\",\"billing_last_name\":\"Ola\",\"contact_account_details\":\"You have used the guest checkout\\n\\n\",\"billing_primary_email_address\":\"torryselfdev@gmail.com\",\"$email\":\"torryselfdev@gmail.com\",\"billing_address_line_2\":\"Amesthyst Lane\",\"billing_address_postcode\":\"RG30 2EZ\",\"billing_address_line_1\":\"K Arun Court\",\"billing_address_country\":\"United Kingdom\",\"contact_account_downloads\":\"\",\"billing_first_name\":\"Tori\",\"billing_primary_telephone\":\"+44 789 902 93\"},\"event\":\"Placed Order\",\"properties\":{\"venue_name\":\"Red Bull Ring, Austria\",\"gift_voucher_code\":[],\"delivery_address\":\"torryselfdev@gmail.com\",\"order_reference\":\"F1AT350633\",\"delivery_information\":\"\",\"delivery_address_city\":\"Reading\",\"event_choice\":\" for the Austrian Formula 1 Grand Prix 2022\",\"delivery_address_line_1\":\"K Arun Court\",\"delivery_address_line_2\":\"Amesthyst Lane\",\"charity\":\" 10.00\",\"$value\":408.0,\"delivery_method\":\"D E L I V E R Y    E M A I L\\n\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\",\"retailer_website_url\":\"http://www.website1300.com\",\"Items\":[{\"order_reference\":\"F1AT350633\",\"currency_symbol\":\"£\",\"currency\":\"Great British Pounds (GBP £)\",\"order_details\":[{\"unit\":\"1 \",\"price\":\" 222.00\",\"description\":\" T8 (Sunday) Adult @ 222.00 \"},{\"unit\":\"1 \",\"price\":\" 111.00\",\"description\":\" T8 (Sunday) Disabled @ 111.00 \"},{\"unit\":\"1 \",\"price\":\" 23.00\",\"description\":\" T8 (Sunday) Child @ 23.00 \"},{\"unit\":\"1\",\"price\":\" 27.00\",\"description\":\"Service Fee and Email Delivery \"},{\"unit\":\"1 \",\"price\":\" 0.00\",\"description\":\" Free Content Pack 3-Month Subscription @ 0.00 \"}],\"status\":\"Your booking is confirmed. Approximately 10 days before the race, we will email you your e-tickets to print out and present at the event for entry.\\n\\n\"}],\"grand_total\":\" 408.00\",\"booking_protection\":\" 15.00\",\"employee_first_name\":\"Joe324499\",\"payment_method\":\"Credit/Debit Card\",\"delivery_last_name\":\"Ola\",\"travel_affiliate_call_to_action\":\"http://www.website1300.com/f1-austria/accommodation.htm\",\"card_last_4_digits\":\"1111\",\"remaining_balance\":\" 408.0\",\"employee_last_name\":\"Bloggs324499\",\"delivery_address_postcode\":\"RG30 2EZ\",\"event_title\":\"Austrian Formula 1 Grand Prix 2022\",\"booking_status\":\"Confirmed\",\"sales_order_protection_terms\":\"B O O K I N G    P R O T E C T I O N\\n\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\u003d\\nYou have chosen to take out optional Booking Protection cover. For further information including the terms and conditions and claims procedure, please visit http://www.website1300.com/help/booking-protection.htm\\n\\n\",\"payment_reference\":\"F1AT350633-220207154846\",\"delivery_address_country\":\"United Kingdom\",\"subtotal\":\" 356.00\",\"delivery_first_name\":\"Tori\",\"$event_id\":\"F1AT350633\",\"retailer_fax_number\":\"\",\"retailer_address\":\"Motorsport Tickets, \\nSuite 31 Beaufort Court, \\nAdmirals Way, \\nLondon, \\nE14 9XL, \\nUnited Kingdom\",\"retailer_email_address\":\"yourorder@motorsporttickets.com\",\"retailer_name\":\"Motorsport Tickets\",\"loyalty_point\":0,\"retailer_telephone_number\":\"123816988\"},\"token\":\"SM7WFW\"}";
    @Bean
    RestTemplate buildTemplate(){
        return new RestTemplateBuilder().build();
    }
}
