package tech.harmless.simplecupbuilder.utils.tuples;

import lombok.Getter;
import lombok.Setter;

public class Tuple<X, Y> {

    @Setter
    @Getter
    private X x;
    @Setter
    @Getter
    private Y y;

    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }
}
