package Homework01;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;


import java.net.ResponseCache;

public class Get01 {
//
//    1. https://restcountries.com/    adresine gidin ve ulkeleri isimlerini
//    kullanarak arayabileceginiz ve bilgilerinin size bir json olarak verildigi
//    bir api bulun. Once Postman kullanarak bu api yi kullanmaya calisin.
//    Takiben Intellije de kucuk bir api testi yazin. Path param olarak
//“turkey” kullanin ve assertion olarak gelen response icerisinde
//“Ankara” olup olmadigini teyit edin.(contains)
//    Hint: Gelen response dan “body” yi su sekilde ayirabilirsiniz:
//    String body = response.body.asString();
//    Takiben Ankara kelimesini teyit etmek icin Javanin temel methodlarindan
//    birini kullanabilirsiniz


    @Test
    public void name() {

        Response response= RestAssured.given().contentType(ContentType.ANY).pathParam("countryname","turkey").get("https://restcountries.com/v3.1/name/{countryname}");
        response.prettyPrint();
        String body= response.body().asString();

        Assert.assertTrue(body.contains("Ankara"));
    }
}

