package tech.harmless.simplecupbuilder.utils.tuples;

import lombok.Getter;
import lombok.Setter;

public class Triplet<X, Y, Z> {

    @Setter
    @Getter
    private X x;
    @Setter
    @Getter
    private Y y;
    @Setter
    @Getter
    private Z z;

    public Triplet(X x, Y y, Z z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
