/**
 *  Represents the Interface for every projection
 *  
 *  @author:     Jose Ferro Pinto (233843)
 *  @author:     Dorian Laforest (234832)
 */

package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

public interface Projection {
    /**
     * Projects the given point from spherical coordinate system to Cartesian coordinate system
     * @param point The point in spherical coordinate system to project
     * @return The projected point in Cartesian coordinate system
     */
    public Point project(PointGeo point);

    /**
     Projects the given point from Cartesian coordinate system to spherical coordinate system
     * @param point The point in Cartesian coordinate system to project
     * @return The projected point in spherical coordinate system
     */
    public PointGeo inverse(Point point);
}