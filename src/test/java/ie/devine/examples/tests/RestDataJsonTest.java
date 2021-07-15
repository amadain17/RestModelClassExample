package ie.devine.examples.tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.devine.examples.models.DataModel;
import ie.devine.examples.models.HeadersModel;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
public class RestDataJsonTest extends RestDataBase {

    private final ObjectMapper objectMapper = new ObjectMapper();
    int nThreads = 10;

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

    @Test
    public void multiThreadRequests() {
        ArrayList<String> responseList = new ArrayList<>();
        ExecutorService exec = Executors.newFixedThreadPool(nThreads);
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            exec.submit(() -> {
                String response = executeServiceCall(finalI);
                responseList.add(response);
            });
        } exec.shutdown();
        try {
            exec.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            log.error(e.toString());
        }
        log.info("Number of requests made " + responseList.size());
        for(String entry: responseList) {
            log.info("Entries: " + entry);
        }
    }

    private String executeServiceCall(int number) {
        DataModel dataModel = DataModel.builder().build();
        Map<String, String> headersFromModel = objectMapper.convertValue(
            HeadersModel.builder().build(), new TypeReference<Map<String, String>>() {
            });

        Response response = given()
            .spec(requestSpecification)
            .basePath(BASE_PATH)
            .when()
                .headers(headersFromModel)
                .body(dataModel)
                .post();
        return response.getStatusCode() == 200 ? "Success: " + dataModel.toString() : "Failure: " + response
            .getStatusCode() + " : " + dataModel.toString();
    }
}
