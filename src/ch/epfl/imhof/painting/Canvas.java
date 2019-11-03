/**
 *  Canvas' interface which represents canvas
 *  
 *  @author:     Jose Ferro Pinto (233843)
 *  @author:     Dorian Laforest (234832)
 */

package ch.epfl.imhof.painting;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

public interface Canvas {
    /**
     * Draws a PolyLine on the canvas
     * @param polyLine The PolyLine to draw
     * @param lineStyle The LineStyle to draw with
     */
    public void drawPolyLine(PolyLine polyLine, LineStyle lineStyle);
    
    /**
     * Draws a Polygon on the canvas
     * @param polygon The Polygon to draw
     * @param color The color to draw with
     */
    public void drawPolygon(Polygon polygon, Color color);
}