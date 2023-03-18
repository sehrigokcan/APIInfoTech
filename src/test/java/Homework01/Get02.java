package Homework01;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

public class Get02 {
//    2. https://restful-booker.herokuapp.com    adresine gidin ve booking id
//    kullanarak booking cagirabileceginiz(GET) bir api bulun. Path param
//    olarak 175 kullanin. Once postmande deneyin sonra testini yazin.
//    Gelen response bodysinde Smith oldugunu assert edin.


    @Test
    public void name() {

        Response response= RestAssured.given().contentType(ContentType.ANY).pathParam("id",175).get("https://restful-booker.herokuapp.com/booking/{id}");
        response.prettyPrint();

        String body= response.body().asString();

        Assert.assertTrue(body.contains("Smith"));
    }
}
