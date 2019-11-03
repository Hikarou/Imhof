/**
 *  A class which represents a polygon with holes
 *  
 *  @author:     José Ferro Pinto (233843)
 *  @author:     Dorian Laforest (234832)
 */

package ch.epfl.imhof.geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Polygon {
    final private ClosedPolyLine shell;
    final private List<ClosedPolyLine> holes;

    /**
     * General constructor for Polygons
     * @param shell represents the polygon's shell
     * @param holes represents the list with every holes
     */
    public Polygon(ClosedPolyLine shell, List<ClosedPolyLine> holes) {
        this.shell = shell;
        this.holes = Collections.unmodifiableList(new ArrayList<ClosedPolyLine>(holes));
    }

    /**
     * Constructor for Polygons without holes
     * @param shell represents the polygon's shell
     */
    public Polygon(ClosedPolyLine shell){
        this(shell, Collections.emptyList());
    }

    /**
     * Returns the shell
     * @return shell
     */
    public ClosedPolyLine shell(){
        return shell;
    }

    /**
     * Returns the list with every holes
     * @return the holes' list
     */
    public List<ClosedPolyLine> holes(){
        return holes;
    }
}