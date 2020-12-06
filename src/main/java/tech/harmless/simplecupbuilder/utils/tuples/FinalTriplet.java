package tech.harmless.simplecupbuilder.utils.tuples;

import lombok.Getter;

public class FinalTriplet<X, Y, Z> {

    @Getter
    private final X x;
    @Getter
    private final Y y;
    @Getter
    private final Z z;

    public FinalTriplet(X x, Y y, Z z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
