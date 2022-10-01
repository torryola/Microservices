package net.torrydev.microservices.reviewsservice.constants;

public interface ServiceConstants {

    public static final String HTTP = "http://";
    public static final String APP_VERSION = "v1";
    public static final String REST_API = "/api/"+APP_VERSION;

    static final String API_VERSION_WITH_FEIGN = REST_API + "/reviews-feign";

    static final int API_GATEWAY_PORT = 9001;

    public static final String API_GATEWAY = "API-GATEWAY";

    /*
    App-Users-Service Related URLs - This can be externalised to app properties from Maintainability/Flexibility
    E.g. It will be easier to change the port number. endpoints etc.
    */
    public static final String APP_USER_SERVICE = API_GATEWAY; //"APP-USER-SERVICE";
    public static final String APP_USER_SERVICE_BASE_URL = HTTP+APP_USER_SERVICE+"/api/v1/users/"; //"http://localhost:8081/api/v1/users/";
    public static final String APP_USER_SERVICE_ALL_USERS = APP_USER_SERVICE_BASE_URL;
    public static final String APP_USER_BY_USER_ID = APP_USER_SERVICE_BASE_URL+"user/id/";

    /*
     Product-Service Related Url to be externalized to Spring-Cloud-Config
     */
    public static final String PRODUCT_SERVICE = API_GATEWAY; //"PRODUCTS-SERVICE";
    public static final String PRODUCT_SERVICE_BASE_URL = HTTP+PRODUCT_SERVICE+"/api/v1/products/product/"; //"http://localhost:8381/api/v1/products/product/";
    public static final String PRODUCT_BY_ID = PRODUCT_SERVICE_BASE_URL;
    public static final String PRODUCT_API_GATEWAY = HTTP+PRODUCT_SERVICE+"/api/v1/products/";


    /*
     COMMENT-SERVICE related urls
     */
    public static final String COMMENTS_SERVICE = API_GATEWAY; // "COMMENTS-SERVICE";
    public static final String COMMENT_API_GATEWAY = HTTP+ COMMENTS_SERVICE +"/api/v1/comments/"; //"http://localhost:8281/api/v1/comments/";
    public static final String COMMENTS_BY_PRODUCT_ID_URI = COMMENT_API_GATEWAY +"product/";

    /*
    RATING-SERVICE URLS
     */
    public static final String RATINGS_SERVICE = API_GATEWAY; // "RATINGS-SERVICE";
    public static final String RATINGS_API_GATEWAY = HTTP+RATINGS_SERVICE+"/api/v1/ratings/"; //"http://localhost:8181/api/v1/ratings/";
    public static final String RATINGS_BY_PRODUCT_ID_URL = RATINGS_API_GATEWAY+"product/";


}
