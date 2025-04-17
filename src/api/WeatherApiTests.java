package api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;

public class WeatherApiTests {

    private final String API_KEY = "valid-api-key";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "http://localhost:8080"; // mock server
    }

    @Test
    public void testValidCityWeather() {
        RestAssured
            .given()
                .queryParam("city", "London")
                .header("x-api-key", API_KEY)
            .when()
                .get("/getWeather")
            .then()
                .statusCode(200)
                .body("city", equalTo("London"))
                .body("temperature", notNullValue())
                .body("humidity", notNullValue());
    }

    @Test
    public void testInvalidCityReturns404() {
        RestAssured
            .given()
                .queryParam("city", "FakeCity")
                .header("x-api-key", API_KEY)
            .when()
                .get("/getWeather")
            .then()
                .statusCode(404)
                .body("error", equalTo("City not found"));
    }

    @Test
    public void testMissingApiKeyReturns401() {
        RestAssured
            .given()
                .queryParam("city", "London")
            .when()
                .get("/getWeather")
            .then()
                .statusCode(401)
                .body("error", equalTo("Unauthorized"));
    }

    @Test
    public void testJsonResponseStructure() {
        Response response = RestAssured
            .given()
                .queryParam("city", "London")
                .header("x-api-key", API_KEY)
            .when()
                .get("/getWeather");

        response
            .then()
            .statusCode(200)
            .body("$", hasKey("city"))
            .body("$", hasKey("temperature"))
            .body("$", hasKey("humidity"))
            .body("$", hasKey("description"));
    }
}