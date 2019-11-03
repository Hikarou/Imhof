/**
 *  Represents the CH1903 projection class
 *  
 *  @author:     Jose Ferro Pinto (233843)
 *  @author:     Dorian Laforest (234832)
 */

package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

public final class CH1903Projection implements Projection{

    /**
     * {@inheritDoc}
     *  using the CH1903 Projection
     */
    @Override
    public Point project(PointGeo point) {
        double lambda1 = (Math.toDegrees(point.longitude())*3600-26782.5)/10000;
        double phi1 = (Math.toDegrees(point.latitude())*3600-169028.66)/10000;
        double phi1Squared = phi1*phi1;
        double lambda1Squared = lambda1*lambda1;
        double x =  600072.37
                + 211455.93 * lambda1
                - 10938.51 * lambda1 * phi1
                - 0.36 * lambda1 * phi1Squared
                - 44.54 * lambda1Squared*lambda1;
        double y =  200147.07
                + 308807.95 * phi1
                + 3745.25 * lambda1Squared
                + 76.63 * phi1Squared
                - 194.56 * lambda1Squared * phi1
                + 119.79 * phi1Squared*phi1;
        return new Point(x,y);
    }

    /**
     * {@inheritDoc}
     *  using the CH1903 Projection
     */
    @Override
    public PointGeo inverse(Point point) {
        double x1 = (point.x()-600000)/1000000;
        double y1 = (point.y()-200000)/1000000;
        double x1Squared = x1*x1;
        double y1Squared = y1*y1;
        double lamba0 =  2.6779094
                + 4.728982 * x1
                + 0.791484 * x1 * y1
                + 0.1306 * x1 * y1Squared
                - 0.0436 * x1Squared*x1;
        double phi0 =  16.9023892
                + 3.238272 * y1
                - 0.270978 * x1Squared
                - 0.002528 * y1Squared
                - 0.0447 * x1Squared * y1
                - 0.0140 * y1Squared*y1;
        double lambda = Math.toRadians(lamba0*100/36);
        double phi = Math.toRadians(phi0*100/36);
        return new PointGeo(lambda, phi);
    }
}