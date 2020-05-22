package ie.devine.examples.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

import static ie.devine.examples.helpers.Randomizers.generateRandomCity;
import static ie.devine.examples.helpers.Randomizers.generateRandomString;

@Data
@Builder
@JsonDeserialize(builder = NamesModel.NamesModelBuilder.class)
public class NamesModel {
    @JsonProperty
    @Builder.Default
    String name = generateRandomString();
    @JsonProperty
    @Builder.Default
    List<String> positions = Arrays.asList("boss", "manager", "grunt");
    @JsonProperty
    @Builder.Default
    String city = generateRandomCity();

}
