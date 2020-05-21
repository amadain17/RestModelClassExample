package ie.devine.examples;

import com.fasterxml.jackson.databind.ObjectMapper;
import ie.devine.examples.models.DataModel;
import ie.devine.examples.models.HeadersModel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;
import java.util.stream.IntStream;

import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class LombokDemoTest {
    private static final String TEST_URL = "https://postman-echo.com/post";

    @Test
    public void generateDifferentRequestBodyEveryTime() throws IOException {
        DataModel dataFromModel = DataModel.builder().build();

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> headersFromModel = objectMapper.convertValue(HeadersModel.builder().build(), Map.class);

        Response response = sendPostRequest(dataFromModel, headersFromModel);

        assertThat(response.getHeader("Content-Type")).contains(APPLICATION_JSON.toString());
        String id = response.getBody().jsonPath().get("data.id");
        assertThat(id).isEqualTo(dataFromModel.getId());

    }

    @Test
    public void generateMultipleDifferentUniqueRequests() throws IOException {
        IntStream.range(1, 20).forEach(i -> {
            DataModel dataFromModel = DataModel.builder().build();
            Response response = sendPostRequest(
                dataFromModel,
                new ObjectMapper().convertValue(HeadersModel.builder().build(), Map.class));
            String id = response.getBody().jsonPath().get("data.id");
            assertThat(id).isEqualTo(dataFromModel.getId());
        });
    }

    private Response sendPostRequest(DataModel requestBody, Map<String, String> headers) {
        return RestAssured.given().log().all()
                .when()
                .headers(headers)
                .body(requestBody)
                .post(TEST_URL)
                .then()
                .statusCode(SC_OK)
                .extract().response();
    }
}
