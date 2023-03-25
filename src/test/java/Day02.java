import io.cucumber.java.it.Ma;
import io.cucumber.java.sl.In;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Day02 {


    @Before
    public void setUp(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
    @Test
    public void makeBasicGetRequestAndAssert() {
        /**
         * base url = "https://restcountries.com/v3.1/name";
         * use GET method
         * use /turkey endpoint
         * assert status code is 200 and content-type is application/json
         * get response "date" from headers
         */

        RestAssured.baseURI="https://restcountries.com/v3.1/name";
        Response response= RestAssured.get("turkey");

        Assert.assertEquals(200,response.statusCode());
        Assert.assertEquals("application/json",response.contentType());

        boolean doesDateExist=response.getHeaders().hasHeaderWithName("Date");
        if(doesDateExist){
            String date= response.getHeader("Date");
            assertTrue(date.contains("2023"));
        }

    }

    @Test
    public void randomNumberTest() {
        /**
         * url = "http://numbersapi.com/random/math";
         * use GET method
         * assert status code is 200 and content-type is "text/plain; charset=utf-8"
         * assert values of headers' "date" and "pragma"
         */
        // request random number
        Response response= RestAssured.get("http://numbersapi.com/random/math");

        assertEquals(200,response.statusCode());
        assertEquals("text/plain; charset=utf-8",response.contentType());
        assertTrue(response.getHeader("date").contains("2023"));
        assertEquals("no-cache",response.getHeader("pragma"));


    }

    @Test
    public void apiLimitCounterDecreasesWhenARequestIsMade() {
        /**
         * url = "http://dummy.restapiexample.com/api/v1/employees";
         * use GET method
         * assert "X-RateLimit-Limit" is 60
         * assert "X-RateLimit-Remaining" updates in next request
         */

        // make call
        String url = "http://dummy.restapiexample.com/api/v1/employees";
        Response response = RestAssured.get(url);

        assertEquals("60",response.getHeader("X-RateLimit-Limit"));
        int initialLimit=Integer.parseInt(response.getHeader("X-RateLimit-Remaining"));

        //make second call and assert
        String updatedLimit= RestAssured.get(url).getHeader("X-RateLimit-Remaining");

        assertEquals((initialLimit-1), Integer.parseInt(updatedLimit));

        System.out.println("initialLimit = " + initialLimit);
        System.out.println("updatedLimit = " + updatedLimit);

    }

    @Test
    public void turkeyHasBordersWithEightCountries() {
        /**
         * base url = "https://restcountries.com/v3.1";
         * endpoint "/name/turkey" (use path param in two ways)
         * use GET method
         * assert status code is 200 and border countries are true -> "ARM", "AZE", "BGR", "GEO", "GRC", "IRN", "IRQ", "SYR"
         */
        String country = "turkey";

        // 1. using variable
//        Response response = RestAssured.get("https://restcountries.com/v3.1/name/" + country);

        // 2. using Restassured library
        Response response = RestAssured.given().pathParam("countryName", "turkey").get("https://restcountries.com/v3.1/name/{countryName}");

        //assert
        assertEquals(200,response.statusCode());
        String responseBody= response.asString();
        assertTrue(responseBody.contains("ARM"));
        assertTrue(responseBody.contains("AZE"));
        assertTrue(responseBody.contains("BGR"));
        assertTrue(responseBody.contains("GEO"));
        assertTrue(responseBody.contains("GRC"));
        assertTrue(responseBody.contains("IRN"));
        assertTrue(responseBody.contains("IRQ"));
        assertTrue(responseBody.contains("SYR"));


    }

    @Test
    public void useRestAssuredPathMethodToReadData() {
        /**
         base url: https://jsonplaceholder.typicode.com
         * endpoint /comments/90 method: GET
         * make several assertions
         */

        // make call
        RestAssured.baseURI="https://jsonplaceholder.typicode.com";

        Response response= RestAssured.given().pathParam("user_id",8).get("users/{user_id}");
        response.prettyPrint();

        assertEquals(200,response.statusCode());

        System.out.println("response.path(\"postId\") = " + response.path("postId"));
        System.out.println("response.path(\"id\") = " + response.path("id"));
        System.out.println("response.path(\"body\") = " + response.path("body"));

    }
    @Test
    public void usePathMethodForComplexJsonStructure(){
        /**
         * base url: https://jsonplaceholder.typicode.com
         * endpoint /users/8 method: GET
         * assert companyName: "Abernathy Group" and longtitude: "-120.7677"
         */

        // make call
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        Response response = RestAssured.given().pathParam("user_id", "8")
                .get("users/{user_id}");

        // print
        response.prettyPrint();

        // assert
        assertEquals("Abernathy Group", response.path("company.name"));
        assertEquals("-120.7677", response.path("address.geo.lng"));
    }
    @Test
    public void usePathMethodForArrayJson() {
        /**
         * base url: https://jsonplaceholder.typicode.com
         * endpoint /todos method: GET
         * assert _todo with title:"et porro tempora" and completed:true
         */

        // make call
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        Response response = RestAssured.get("todos");
        response.prettyPrint();
        // assert

        assertEquals(4,(int)response.path("[3].id"));
        assertEquals(1,(int) response.path("[3].userId"));
        assertEquals(true,(boolean) response.path("[3].completed"));


    }

    @Test
    public void useGherkinMethodsAndBuiltInAssertion() {
        /**
         * base url: https://restcountries.com/v3.1/
         * endpoint /turkey method: GET
         */
        RestAssured.given().pathParam("country","turkey").
                and().baseUri("https://restcountries.com/v3.1/name").
                when().contentType("application/json").
                and().get("name/{country}")
                .then().assertThat().statusCode(200).
                and().assertThat().contentType("application/json")
                .and().assertThat().header("Server","Apache/2.4.38 (Debian)");


    }

    @Test
    public void useMatchersLibraryForAssertions() {
        /**
         * base url: https://restcountries.com/v3.1/name
         * endpoint /turkey method: GET
         */

        RestAssured.given().pathParam("country","turkey")
                .and().baseUri("https://restcountries.com/v3.1")
                .when().contentType("application/json")
                .and().get("name/{country}")
                .then().assertThat().statusCode(Matchers.instanceOf(Integer.class))
                .and().assertThat().statusCode(Matchers.greaterThan(199))
                .and().assertThat().statusCode(Matchers.lessThan(300))
                .and().assertThat().statusCode(Matchers.anyOf(Matchers.equalTo(200), Matchers.equalTo(201)))
                .and().assertThat().contentType(Matchers.is("application/json"))
                .and().assertThat().contentType(Matchers.instanceOf(String.class))
                .and().assertThat().contentType(Matchers.notNullValue())
                .and().assertThat().contentType(Matchers.not(Matchers.is("text")))
                .and().assertThat().header("Server",Matchers.not(Matchers.emptyOrNullString()))
                .and().assertThat().header("Server",Matchers.containsString("Debian"));

    }


    @Test
    public void useRestAssuredLogging() {

//        RestAssured.given().pathParam("country", "turkey").log().uri()
//                .and().baseUri("https://restcountries.com/v3.1")
//                .when().contentType("application/json").log().body().log().method()
//                .and().get("name/{country}")
//                .then().log().ifError();

        RestAssured.given().pathParam("country", "siirt")
                .and().baseUri("https://restcountries.com/v3.1")
                .when().contentType("application/json")
                .and().get("name/{country}")
                .then().log().ifStatusCodeIsEqualTo(404);

    }
    @Test
    public void setupLoggingAtBefore(){

        RestAssured
                .given().pathParam("country", "yozgat")
                .and().baseUri("https://restcountries.com/v3.1")
                .when().contentType("application/json")
                .and().get("name/{country}")
                .then().assertThat().contentType("application/json")
                .and().assertThat().statusCode(200)
                .and().assertThat().header("Server", "Apache/2.4.38 (Debian)");
    }

    // Serialization(java-json) and Deserialization (json-java)
    //Serialization


    @Test
    public void deserializeToMap(){
        Response response = RestAssured.given().accept(ContentType.JSON)
                .when().get("https://randomuser.me/api")
                .then().assertThat().statusCode(200).extract().response();

        Map<String,Object> deserializedObject= response.body().as(Map.class);
        System.out.println("deserializedObject = " + deserializedObject);

    }

    @Test
    public void deserializeToList() {
        Response response = RestAssured.given().accept(ContentType.JSON)
                .when().get("https://restcountries.com/v3.1/name/turkey")
                .then().assertThat().statusCode(200).extract().response();

        List<String> borderCountries= response.path("[0].borders");
        System.out.println("borderCountries = " + borderCountries);
        System.out.println("borderCountries.get(1) = " + borderCountries.get(1));

    }

    }

