package ie.devine.examples.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.restassured.http.ContentType;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Builder
@Data
@JsonDeserialize(builder = HeadersModel.HeadersModelBuilder.class)
public class HeadersModel {
    @JsonProperty("Content-Type")
    @Builder.Default String contentType = ContentType.JSON.toString();
    @JsonProperty("X-Header")
    @Builder.Default String xheader = UUID.randomUUID().toString();
    @JsonProperty("Authorization")
    @Builder.Default String authorization = getBasicAuthorizationHeader();

    private static String getBasicAuthorizationHeader() {
        String user = RandomStringUtils.random(8, true, false);
        String password = RandomStringUtils.random(8, true, true);
        String auth = user + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(StandardCharsets.ISO_8859_1));
        return "Basic " + new String(encodedAuth);
    }
}
