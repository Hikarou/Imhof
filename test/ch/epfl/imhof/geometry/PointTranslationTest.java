package ch.epfl.imhof.geometry;


import java.util.function.Function;

import org.junit.Test;

public class PointTranslationTest {
    @Test (expected = IllegalArgumentException.class)
    public void test() {
        Function<Point, Point> blueToRed =
                Point.alignedCoordinateChange(new Point(1, -1),
                                              new Point(5, 4),
                                              new Point(1, 1),
                                              new Point(5, 0));
            blueToRed.apply(new Point(1, 1));
    }
}