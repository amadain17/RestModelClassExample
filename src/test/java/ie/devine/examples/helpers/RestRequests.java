package ie.devine.examples.helpers;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static ie.devine.examples.tests.RestDataBase.BASE_PATH;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

public class RestRequests {
    public static Response sendPostRequest(RequestSpecification requestSpecification, Object requestBody, Map<String, String> headers) {
        return given()
                .spec(requestSpecification)
                .basePath(BASE_PATH)
                .when()
                .headers(headers)
                .body(requestBody)
                .post()
                .then()
                .statusCode(SC_OK)
                .extract().response();
    }
}
