package ie.devine.examples;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.devine.examples.models.DataModel;
import ie.devine.examples.models.HeadersModel;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class LombokDemoTest {
    private static final String BASE_URL = "https://postman-echo.com";
    private static final String BASE_PATH = "post";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String ID_PATH = "data.id";
    private static RequestSpecification reqSpec;

    @BeforeAll
    public static void setUp() {
        reqSpec = new RequestSpecBuilder()
                .setRelaxedHTTPSValidation()
                .addFilters(Arrays.asList(new RequestLoggingFilter(), new ResponseLoggingFilter()))
                .setBaseUri(BASE_URL)
                .build();
    }

    @Test
    public void generateDifferentRequestBodyEveryTime() {
        DataModel dataFromModel = DataModel.builder().build();

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> headersFromModel = objectMapper.convertValue(HeadersModel.builder().build(), new TypeReference<Map<String, String>>() {
        });

        Response response = sendPostRequest(dataFromModel, Collections.unmodifiableMap(headersFromModel));

        assertThat(response.getHeader(CONTENT_TYPE)).contains(APPLICATION_JSON.toString());
        String id = response.getBody().jsonPath().get(ID_PATH);
        assertThat(id).isEqualTo(dataFromModel.getId());
    }

    @Test
    public void generateMultipleDifferentUniqueRequests() {
        IntStream.range(1, 20).forEach(i -> {
            DataModel dataFromModel = DataModel.builder().build();
            Response response = sendPostRequest(
                    dataFromModel,
                    new ObjectMapper().convertValue(HeadersModel.builder().build(), new TypeReference<Map<String, String>>() {
                    }));
            String id = response.getBody().jsonPath().get(ID_PATH);
            assertThat(id).isEqualTo(dataFromModel.getId());
        });
    }

    private Response sendPostRequest(DataModel requestBody, Map<String, String> headers) {
        return given()
                .spec(reqSpec)
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
