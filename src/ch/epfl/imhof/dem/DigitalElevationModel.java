/**
 *  represents a digital elevation model interface
 *  
 *  @author:     Jose Ferro Pinto (233843)
 *  @author:     Dorian Laforest (234832)
 */

package ch.epfl.imhof.dem;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;

import java.io.IOException;

public interface DigitalElevationModel extends AutoCloseable{
    @Override
    /**
     * Close the file
     * @throws IOException if the file cannot be closed
     */
    public void close() throws IOException;
    
    /**
     * Returns a vector normal to the earth at this point
     * @param point The point to use in the computation
     * @return The vector normal to the earth at this point
     * @throws IllegalArgumentException if the point is not in the file range
     */
    public Vector3 normalAt(PointGeo point) throws IllegalArgumentException;
}