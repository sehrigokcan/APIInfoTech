package Homework01;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

public class Get03 {
//    3. https://www.boredapi.com    a gidin ve activityId yi query param
//    yoluyla kullanabilecegimiz bir api bulun. Postman de deneyip takiben
//    testini yazin. Gelen response un “Clean out your refrigerator”
//    icerdigini assert edin


    @Test
    public void name() {

        Response response= RestAssured.given().queryParam("key",9324336).get("http://www.boredapi.com/api/activity");
        response.prettyPrint();

        String body= response.body().asString();
        Assert.assertTrue(body.contains("Clean out your refrigerator"));

    }
}
