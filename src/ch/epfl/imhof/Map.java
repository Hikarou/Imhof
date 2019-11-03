/**
 *  Represents a projected map composed with attributed geometric entities
 *  
 *  @author:     Jose Ferro Pinto (233843)
 *  @author:     Dorian Laforest (234832)
 */
package ch.epfl.imhof;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Map {
    private final List<Attributed<PolyLine>> polyLines;
    private final List<Attributed<Polygon>> polygons;

    /**
     * The map constructor
     * @param polyLines a list of attributed polyLines
     * @param polygons a list of attributed Polygon
     */
    public Map(List<Attributed<PolyLine>> polyLines, List<Attributed<Polygon>> polygons) {
        this.polyLines = Collections.unmodifiableList(new ArrayList<>(polyLines));
        this.polygons = Collections.unmodifiableList(new ArrayList<>(polygons));
    }

    /**
     * Returns the attributed PolyLine list
     * @return a list of the attributed PolyLines
     */
    public List<Attributed<PolyLine>> polyLines() {
        return this.polyLines;
    }

    /**
     * Returns the attributed Polygon
     * @return a list of the attributed Polygon
     */
    public List<Attributed<Polygon>> polygons() {
        return this.polygons;
    }

    /**
     *  Represents Map Builder class
     *  
     *  @author:     Jose Ferro Pinto (233843)
     *  @author:     Dorian Laforest (234832)
     */
    public static class Builder {
        private List<Attributed<PolyLine>> polyLines = new ArrayList<>();
        private List<Attributed<Polygon>> polygons = new ArrayList<>();

        /**
         * Adds an attributed PolyLine to the Map's builder
         * @param newPolyLine the attributed PolyLine to add
         */
        public void addPolyLine(Attributed<PolyLine> newPolyLine) {
            polyLines.add(newPolyLine);
        }

        /**
         * Adds an attributed Polygon to the Map's builder
         * @param newPolygon the attributed Polygon to add
         */
        public void addPolygon(Attributed<Polygon> newPolygon) {
            polygons.add(newPolygon);
        }

        /**
         * Builds and returns a new Map
         * @return the new constructed Map
         */
        public Map build() {
            return new Map(polyLines, polygons);
        }
    }
}