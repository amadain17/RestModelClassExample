package ie.devine.examples.helpers;

import ie.devine.examples.data.Cities;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Randomizers {
    public static String generateRandomCity() {
        List<Pair> weights = Arrays.stream(Cities.values())
                .map(city -> new Pair(city, city.getWeight()))
                .collect(Collectors.toList());

        EnumeratedDistribution distribution = new EnumeratedDistribution(weights);
        return ((Cities) distribution.sample()).getDisplayName();
    }

    public static String generateRandomString() {
        return RandomStringUtils.random(8, true, false);
    }
}
