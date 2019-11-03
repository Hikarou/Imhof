/**
 *  A class which represents a point in the plan in Cartesian coordinate system
 *  
 *	@author:     Jose Ferro Pinto (233843)
 *  @author:     Dorian Laforest (234832)
 */

package ch.epfl.imhof.geometry;

import java.util.function.Function;

public final class Point {
    private final double x;
    private final double y;

    /**
     * Constructor for Point
     * @param x  the x value in Cartesian coordinate system
     * @param y  the y value in Cartesian coordinate system
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the Point's x value
     * @return the x value in Cartesian coordinate system
     */
    public double x() {
        return this.x;
    }

    /**
     * Returns the Point's y value
     * @return the y value in Cartesian coordinate system
     */
    public double y() {
        return this.y;
    }
    
    /**
     * Static method that given two couple of point in different frame
     * calculates the corresponding change of frame
     * @param P1A the first point in the first frame
     * @param P1B the first point in the second frame
     * @param P2A the second point in the first frame
     * @param P2B the second point in the second frame
     * @return a new lambda expression Function to change frame
     * @throws IllegalArgumentException if two point are horizontally or vertically aligned
     */
    public static Function<Point, Point> alignedCoordinateChange(Point P1A, Point P1B, Point P2A, Point P2B) throws IllegalArgumentException {
        return x -> {
            if((P1A.x == P2A.x) || (P1A.y == P2A.y) || (P1B.x == P2B.x) || (P1B.y == P2B.y)) {
                throw new IllegalArgumentException("invalid point, two point cannot be horizontally or vertically aligned");
            }
            
            double xScaling = (P1B.x - P2B.x)/(P1A.x-P2A.x);
            double xTranslation = P1B.x - P1A.x*xScaling;
            double yScaling = (P1B.y - P2B.y)/(P1A.y-P2A.y);
            double yTranslation = P1B.y - P1A.y*yScaling;
            
            return new Point(x.x*xScaling + xTranslation, x.y*yScaling + yTranslation);
        };
    }
}