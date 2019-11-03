/**
 *  An abstract class which represents any PolyLine
 *  
 *  @author:     Jose Ferro Pinto (233843)
 *  @author:     Dorian Laforest (234832)
 */

package ch.epfl.imhof.geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class PolyLine {
    private final List<Point> polyLine;

    /**
     * Constructor for PolyLine
     * @param points Represent a list of the points for this object
     * @throws IllegalArgumentException If the point's list is incorrect
     */
    public PolyLine(List<Point> points) throws IllegalArgumentException{
        if (points == null || points.isEmpty()) {
            throw new IllegalArgumentException("Incorrect point's list");
        }

        polyLine = Collections.unmodifiableList(new ArrayList<Point>(points));
    }

    /**
     * Returns the PolyLine's points list
     * @return the PolyLine's point list
     */
    public List<Point> points(){
        return polyLine;
    }

    /**
     * Returns the first PolyLine's point
     * @return the first Point
     */
    public Point firstPoint(){
        return polyLine.get(0);
    }

    /**
     * Tests if the PolyLine is closed
     * @return True if the PolyLine is closed, false otherwise
     */
    public abstract boolean isClosed();

    /**
     *  Represents PolyLine Builder class
     *  
     *  @author:     Jose Ferro Pinto (233843)
     *  @author:     Dorian Laforest (234832)
     */
    public final static class Builder {
        private List<Point> points = new ArrayList<>();

        /**
         * Adds a point at the end of the current list of points
         * @param newPoint Represents the point to add
         */
        public void addPoint(Point newPoint) {
            points.add(newPoint);
        }

        /**
         * Builds and returns a new OpenPolyLine
         * @return a new OpenPolyLine with the added points until now
         */
        public OpenPolyLine buildOpen() {
            return new OpenPolyLine(points);
        }

        /**
         * Builds and returns a new ClosedPolyLine
         * @return a new ClosedPolyLine with the added points until now
         */
        public ClosedPolyLine buildClosed() {
            return new ClosedPolyLine(points);
        }
    }
}