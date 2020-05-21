package ie.devine.examples.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@JsonDeserialize(builder = NamesModel.NamesModelBuilder.class)
public class NamesModel {
    @JsonProperty
    @Builder.Default String name = RandomStringUtils.random(8, true, false);
    @JsonProperty
    @Builder.Default List<String> positions = Arrays.asList(new String[]{"boss", "manager", "grunt"});
    @JsonProperty
    @Builder.Default String[] city = generateRandomCity();

    private static String[] generateRandomCity() {
        EnumeratedDistribution<String> cityDistribution =
                new EnumeratedDistribution<String>(Arrays.asList(
                                new Pair<String, Double>("Madrid", 3.0),
                                new Pair<String, Double>("London", 2.0),
                                new Pair<String, Double>("New York", 1.0),
                                new Pair<String, Double>("Boston", 5.0),
                                new Pair<String, Double>("Paris", 7.0),
                                new Pair<String, Double>("Rome", 4.0),
                                new Pair<String, Double>("Oslo", 6.0)
                        ));
        return Arrays.stream(cityDistribution.sample(1)).map(city -> (String)city).collect(Collectors.toList()).toArray(new String[0]);
    }
}

