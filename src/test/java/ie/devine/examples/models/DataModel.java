package ie.devine.examples.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

import static java.util.UUID.randomUUID;

@Data
@Builder
@JsonDeserialize(builder = DataModel.DataModelBuilder.class)
public class DataModel {

    @JsonProperty
    @Builder.Default String id = randomUUID().toString();
    @JsonProperty
    @Builder.Default String position = randomUUID().toString();
    @JsonProperty
    @Builder.Default List<NamesModel> names = Arrays.asList(NamesModel.builder().build(), NamesModel.builder().build());

}
