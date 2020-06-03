package ie.devine.examples.tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.devine.examples.models.DataModel;
import ie.devine.examples.models.HeadersModel;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;
import java.util.stream.IntStream;

import static ie.devine.examples.helpers.RestRequests.sendPostRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class RestDataJsonAT extends RestDataBase {

    @Test
    public void generateDifferentRequestBodyEveryTime() {
        DataModel dataFromModel = DataModel.builder().build();

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> headersFromModel = objectMapper.convertValue(HeadersModel.builder().build(), new TypeReference<Map<String, String>>() {
        });

        Response response = sendPostRequest(requestSpecification,
                dataFromModel, Collections.unmodifiableMap(headersFromModel));

        assertThat(response.getHeader(CONTENT_TYPE)).contains(APPLICATION_JSON.toString());
        String id = response.getBody().jsonPath().get(ID_PATH);
        assertThat(id).isEqualTo(dataFromModel.getId());
    }

    @Test
    public void generateMultipleDifferentUniqueRequests() {
        IntStream.range(1, 20).forEach(i -> {
            DataModel dataFromModel = DataModel.builder().build();
            Response response = sendPostRequest(
                    requestSpecification,
                    dataFromModel,
                    new ObjectMapper().convertValue(HeadersModel.builder().build(), new TypeReference<Map<String, String>>() {
                    }));
            String id = response.getBody().jsonPath().get(ID_PATH);
            assertThat(id).isEqualTo(dataFromModel.getId());
        });
    }
}
