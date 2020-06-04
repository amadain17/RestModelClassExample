package ie.devine.examples.tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.devine.examples.models.DataModel;
import ie.devine.examples.models.HeadersModel;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class RestDataJsonAT extends RestDataBase {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void generateDifferentRequestBodyEveryTime() {
        DataModel dataModel = DataModel.builder().build();
        Map<String, String> headersFromModel = objectMapper.convertValue(
                HeadersModel.builder().build(), new TypeReference<Map<String, String>>() {
                });

        Response response = given().spec(requestSpecification).basePath(BASE_PATH)
                .when().headers(headersFromModel).body(dataModel).post()
                .then().statusCode(SC_OK).extract().response();

        assertThat(response.getHeader(CONTENT_TYPE)).contains(APPLICATION_JSON.toString());
        String id = response.getBody().jsonPath().get(ID_PATH);
        assertThat(id).isEqualTo(dataModel.getId());
    }

    @Test
    public void generateMultipleDifferentUniqueRequests() {
        IntStream.range(1, 20).forEach(i -> {
            Map<String, String> headersFromModel = objectMapper.convertValue(
                    HeadersModel.builder().build(), new TypeReference<Map<String, String>>() {
                    });

            DataModel dataModel = DataModel.builder().build();
            Response response = given().spec(requestSpecification).basePath(BASE_PATH)
                    .when().headers(headersFromModel).body(dataModel).post()
                    .then().statusCode(SC_OK).extract().response();

            String id = response.getBody().jsonPath().get(ID_PATH);
            assertThat(id).isEqualTo(dataModel.getId());
        });
    }
}
