import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.assertEquals;

public class Day01 {

    @Before
    public void setup(){


    }
    @Test
    public void getRandomActivityAndPrint() {

    Response response= given().contentType(ContentType.ANY).get("https://www.boredapi.com/api/activity");
    response.prettyPrint();
    }

    @Test
    public void number9008639BringsMemorizeActivity() {
        Response response= given().contentType(ContentType.ANY).get("https://www.boredapi.com/api/activity?key=9008639");
        String expectedResponce= "{\"activity\":\"Memorize a favorite quote or poem\",\"type\":\"education\",\"participants\":1,\"price\":0,\"link\":\"\",\"key\":\"9008639\",\"accessibility\":0.8}";
        assertEquals(expectedResponce,response.body().print());

    }

    @Test
    public void number9008639BringsMemorizeActivity1() {
        Response response = given().contentType(ContentType.ANY).queryParam("key", 9008639).get("https://www.boredapi.com/api/activity");

        String expectedResponse = "{\"activity\":\"Memorize a favorite quote or poem\",\"type\":\"education\",\"participants\":1,\"price\":0,\"link\":\"\",\"key\":\"9008639\",\"accessibility\":0.8}";
        assertEquals(expectedResponse, response.body().print());
    }

    @Test
    public void number3BringsAstonCox() {
        Response response= given().contentType(ContentType.ANY).pathParam("my_id",3).get("https://dummy.restapiexample.com/api/v1/employe/{my_id}");
        String expected = "{\"status\":\"success\",\"data\":{\"id\":3,\"employee_name\":\"Ashton Cox\",\"employee_salary\":86000,\"employee_age\":66,\"profile_image\":\"\"},\"message\":\"Successfully! Record has been fetched.\"}";

        assertEquals(200,response.statusCode());
        assertEquals(expected,response.body().print());
    }
}
