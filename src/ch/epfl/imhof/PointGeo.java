/**
 *  Represents a point on earth surface, in spherical coordinates
 *  
 *  @author:     José Ferro Pinto (233843)
 *  @author:     Dorian Laforest (234832)
 */

package ch.epfl.imhof;

public final class PointGeo {
    private final double longitude;
    private final double latitude;

    /**
     * Point's constructor
     * @param longitude Radiant coordinates
     * @param latitude Radiant coordinates
     * @throws IllegalArgumentException if longitude is not in : [-π; π]
     * @throws IllegalArgumentException if latitude is not in : [-π/2; π/2]
     */
    public PointGeo(double longitude, double latitude) throws IllegalArgumentException{
        if (Math.abs(longitude) > Math.PI || Math.abs(latitude) > Math.PI / 2) {
            throw new IllegalArgumentException("Out of bounds");
        }
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Returns the longitude in radiant
     * @return longitude in radiant
     */
    public double longitude() {
        return longitude;
    }

    /**
     * Returns the latitude in radiant
     * @return latitude in radiant
     */
    public double latitude() {
        return latitude;
    }
}