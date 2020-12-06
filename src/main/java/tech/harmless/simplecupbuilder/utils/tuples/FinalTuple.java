package tech.harmless.simplecupbuilder.utils.tuples;

import lombok.Getter;

public class FinalTuple<X, Y> {

    @Getter
    private final X x;
    @Getter
    private final Y y;

    public FinalTuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }
}
