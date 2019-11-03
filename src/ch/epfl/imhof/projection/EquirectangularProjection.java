/**
 *  Represents the equirectangular projection
 *  
 *  @author:     José Ferro Pinto (233843)
 *  @author:     Dorian Laforest (234832)
 */

package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

public final class EquirectangularProjection implements Projection{

    /**
     * {@inheritDoc}
     * using the Equirectangular projection
     */
    @Override
    public Point project(PointGeo point) {
        return new Point(point.longitude(), point.latitude());
    }

    /**
     * {@inheritDoc}
     * using the Equirectangular projection
     */
    @Override
    public PointGeo inverse(Point point) {
        return new PointGeo(point.x(), point.y());
    }
}