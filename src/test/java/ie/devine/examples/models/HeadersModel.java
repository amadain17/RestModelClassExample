package ie.devine.examples.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.restassured.http.ContentType;
import lombok.Builder;
import lombok.Data;
import org.apache.tomcat.util.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static ie.devine.examples.helpers.Randomizers.generateRandomString;

@Builder
@Data
@JsonDeserialize(builder = HeadersModel.HeadersModelBuilder.class)
public class HeadersModel {

    @JsonProperty("Content-Type")
    @Builder.Default
    String contentType = ContentType.JSON.toString();

    @JsonProperty("X-Header")
    @Builder.Default
    String xheader = UUID.randomUUID().toString();

    @JsonProperty("Authorization")
    @Builder.Default
    String authorization = getBasicAuthorizationHeader();

    @JsonProperty("Accept")
    @Builder.Default
    String accept = ContentType.ANY.toString();

    private static String getBasicAuthorizationHeader() {
        String auth = generateRandomString() + ":" + generateRandomString();
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.ISO_8859_1));
        return "Basic " + new String(encodedAuth);
    }
}
