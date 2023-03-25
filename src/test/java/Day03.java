import bookingPojos.Bookingdates;
import bookingPojos.GetBookingResponse;
import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import post_booking_response.PostResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class Day03 {
    @Before
    public void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    public void makeJsonSchemaValidationForBody() {
        /**
         * base url = "http://www.boredapi.com/api/";
         * use GET method
         * use /activity endpoint
         * validate response body
         *
         */

        // manual method
//        Response response = RestAssured
//                .given().baseUri("http://www.boredapi.com/api")
//                .when().accept(ContentType.JSON)
//                .and().get("activity")
//                .then().assertThat().contentType(ContentType.JSON)
//                .and().assertThat().statusCode(200).log().body()
//                .and().assertThat().body("activity", Matchers.instanceOf(String.class))
//                .and().assertThat().body("type", Matchers.instanceOf(String.class))
//                .and().assertThat().body("participants", Matchers.instanceOf(Integer.class))
//                .and().assertThat().body("price", Matchers.anyOf(Matchers.instanceOf(Integer.class), Matchers.instanceOf(Float.class)))
//                .extract().response();


        Response response = RestAssured
                .given().baseUri("http://www.boredapi.com/api")
                .when().accept(ContentType.JSON)
                .and().get("activity")
                .then().assertThat().contentType(ContentType.JSON)
                .and().assertThat().statusCode(200).log().body()
                .and().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("activity-schema.json"))
                .extract().response();



    }

    @Test
    public void anotherJsonSchemaValidation() {
        /**
         * base url = "https://jsonplaceholder.typicode.com/comments/90";
         * use GET method
         * use /comments/randomNumber endpoint
         * validate response body
         */
        int num = new Random().nextInt(501);

        Response response= RestAssured.given().baseUri("https://jsonplaceholder.typicode.com")
                .and().pathParam("randomNumber",num)
                .when().accept(ContentType.JSON)
                .and().get("comments/{randomNumber}")
                .then().assertThat().statusCode(200).log().body()
                .and().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("comments-schema.json"))
                .extract().response();

    }

    public void getbooking900(){

        RestAssured
                .given().accept(ContentType.ANY)
                .and().baseUri("https://restful-booker.herokuapp.com")
                .and().log().method().log().uri()
                .and().pathParam("id", "900")
                .when().get("/booking/{id}")
                .then().assertThat().statusCode(200).log().body();

    }

    public Response getbooking(int id){

        return  RestAssured
                .given().accept(ContentType.ANY)
                .and().baseUri("https://restful-booker.herokuapp.com")
                .and().log().method().log().uri()
                .and().pathParam("id", id)
                .when().get("/booking/{id}")
                .then().assertThat().statusCode(200).log().body()
                .extract().response();


    }

    @Test
    public void test1() {

        Response response = getbooking(13);
        System.out.println("response.path(\"price\") = " + response.path("totalprice"));
    }

    @Test
    public void getABookingAndDeserializeToJavaObj() {
//        Response getbooking = getbooking(45);

        Map<String,Object> javaObject = getbooking(45).body().as(Map.class);

        assertEquals("John",javaObject.get("firstname"));
        assertEquals("Smith",javaObject.get("lastname"));
        assertEquals(111,javaObject.get("totalprice"));
        assertEquals(true,javaObject.get("depositpaid"));
        assertEquals("Breakfast",javaObject.get("additionalneeds"));
        assertEquals("2018-01-01",((Map)(javaObject.get("bookingdates"))).get("checkin"));
        assertEquals("2019-01-01",((Map)(javaObject.get("bookingdates"))).get("checkout"));


        Map<String,String> bookingsDates= (Map<String,String>)javaObject.get("bookingdates");

        assertEquals("2018-01-01",bookingsDates.get("checkin"));
        assertEquals("2019-01-01",bookingsDates.get("checkout"));

        // how to use Gson
        //toJsom serilization
        Gson gson= new Gson();

        String serializedJson= gson.toJson(javaObject);
        System.out.println("serializedJson = " + serializedJson);


    }
    @Test
    public void getBookingAndDeserializetoPOJO() {

        GetBookingResponse pojoResponse = getbooking(156).body().as(GetBookingResponse.class);

        assertEquals(111, pojoResponse.getTotalprice());

        assertEquals("2018-01-01", pojoResponse.getBookingdates().getCheckin());
    }


    @Test
    public void postABookingUsingStringJson() {

        String randomBookingData = "{\n" +
                "    \"firstname\" : \"test-1\",\n" +
                "    \"lastname\" : \"user\",\n" +
                "    \"totalprice\" : 120,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2022-07-20\",\n" +
                "        \"checkout\" : \"2022-08-05\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n" +
                "}";

        Response response= postbooking(randomBookingData);

        PostResponse pojoObj = response.body().as(PostResponse.class);
        System.out.println("pojoObj.getBookingid() = " + pojoObj.getBookingid());


    }

    public Response postbooking(String requestBodyJson){

        return RestAssured
                .given().baseUri("https://restful-booker.herokuapp.com")
                .and().contentType(ContentType.JSON)
                .and().accept(ContentType.ANY)
                .body(requestBodyJson)
                .when().post("booking")
                .then().statusCode(200).extract().response();
    }


    public Response postBooking(GetBookingResponse pojoForPost) {

        return RestAssured
                .given().baseUri("https://restful-booker.herokuapp.com")
                .and().contentType(ContentType.JSON)
                .and().accept(ContentType.ANY)
                .body(pojoForPost).log().all()
                .when().post("booking")
                .then().statusCode(200)
                .and().log().ifError()
                .extract().response();
    }

    @Test
    public void postBookingUsingPOJO() {
        // create and fill pojo
        GetBookingResponse postObj = new GetBookingResponse();
        postObj.setAdditionalneeds("Parking Area");
        postObj.setFirstname("Mustafa");
        postObj.setLastname("Yilmaz");

        Bookingdates dateObj = new Bookingdates();
        dateObj.setCheckin("01.05.2023");
        dateObj.setCheckout("2023.04.15");

        postObj.setBookingdates(dateObj);
        postObj.setTotalprice(2000);
        postObj.setDepositpaid(true);

        // make request and print response
        Response response = postBooking(postObj);

        String createdBodyString = response.body().asString();
        System.out.println("createdBodyString = " + createdBodyString);


    }

    public Map<String, String> generateBodyForAuthToken() {
        Map<String, String> authBody = new HashMap<>();
        authBody.put("username", "admin");
        authBody.put("password", "password123");

        return authBody;
    }

    public String getAuthToken() {
        Map<String, String> body = generateBodyForAuthToken();

        Response response = RestAssured
                .given().baseUri("https://restful-booker.herokuapp.com/auth")
                .contentType(ContentType.JSON)
                .accept(ContentType.ANY)
                .body(body).log().uri().log().body()
                .when().post()
                .then().statusCode(200)
                .extract().response();

        return (String) response.path("token");
    }


    @Test
    public void getAuthTokenTest() {
        System.out.println(getAuthToken());
    }


    public int createBookingUsingPOJO() {
        // create and fill pojo
        GetBookingResponse postObj = new GetBookingResponse();
        postObj.setAdditionalneeds("Parking Area");
        postObj.setFirstname("Mustafa");
        postObj.setLastname("Yilmaz");

        Bookingdates dateObj = new Bookingdates();
        dateObj.setCheckin("01.05.2023");
        dateObj.setCheckout("2023.04.15");

        postObj.setBookingdates(dateObj);
        postObj.setTotalprice(2000);
        postObj.setDepositpaid(true);

        // make request and print response
        Response response = postBooking(postObj);

        String createdBodyString = response.body().asString();
        System.out.println("createdBodyString = " + createdBodyString);

        return (int )response.path("bookingid");
    }

    @Test
    public void useAuthAndPutMethod() {
        // create token
        String authToken = getAuthToken();

        // create a booking
        int bookingId = createBookingUsingPOJO();

        // update the booking
        GetBookingResponse updatedbody = new GetBookingResponse();
        updatedbody.setAdditionalneeds("Updated Parking Area");
        updatedbody.setFirstname("Updated Mustafa");
        updatedbody.setLastname("Updated Yilmaz");

        Bookingdates dateObj = new Bookingdates();
        dateObj.setCheckin("01.05.2023");
        dateObj.setCheckout("2023.04.15");

        updatedbody.setBookingdates(dateObj);
        updatedbody.setTotalprice(5000);
        updatedbody.setDepositpaid(true);

        Response putResponse = RestAssured.given().contentType(ContentType.JSON)
                .baseUri("https://restful-booker.herokuapp.com")
                .pathParam("id", bookingId)
                .header("Cookie", "token=" + authToken)
                .body(updatedbody)
                .when().put("booking/{id}")
                .then().statusCode(200)
                .extract().response();

        String updatedBodyString = putResponse.body().asString();

        System.out.println("updatedbody = " + updatedBodyString);

        // assert that booking is updated

        Response response = getbooking(bookingId);

        GetBookingResponse pojoObj = response.body().as(GetBookingResponse.class);

        assertEquals("Updated Mustafa", pojoObj.getFirstname());
        assertEquals(5000, pojoObj.getTotalprice());
        assertEquals("Updated Parking Area", pojoObj.getAdditionalneeds());

    }

}
