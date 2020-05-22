package ie.devine.examples.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Cities {
    LONDON("London", 2.0),
    MADRID("Madrid", 2.2),
    NEW_YORK("New York", 1.0),
    BOSTON("Boston", 2.7),
    PARIS("Paris", 3.5),
    ROME("Rome", 2.9),
    OSLO("Oslo", 3.1);

    private final String displayName;
    private final Double weight;
}
