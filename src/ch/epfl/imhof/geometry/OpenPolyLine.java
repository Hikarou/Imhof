/**
 *  A class which represents an OpenPolyLine
 *  
 *  @author:     José Ferro Pinto (233843)
 *  @author:     Dorian Laforest (234832)
 */

package ch.epfl.imhof.geometry;

import java.util.List;

public final class OpenPolyLine extends PolyLine{

    /**
     * Constructor for OpenPolyLine
     * @param points Represent the points for this object
     */
    public OpenPolyLine(List<Point> points) {
        super(points);
    }

    /**
     * {@inheritDoc}
     * @return boolean false because OpenPolyLine is always open
     */
    @Override
    public boolean isClosed() {
        return false;
    }
}