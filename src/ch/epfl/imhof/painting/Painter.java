/**
 *  A functional interface representing a Painter
 *  
 *  @author:     Jose Ferro Pinto (233843)
 *  @author:     Dorian Laforest (234832)
 */

package ch.epfl.imhof.painting;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;

import java.util.function.Predicate;

public interface Painter {
    /**
     * Draws the map on the canvas
     * @param map The map to draw
     * @param canvas The canvas to draw on
     */
    public void drawMap(Map map, Canvas canvas);
    
    /**
     * Returns the Painter which draws the polygon's interior
     * @param color The color to draw with
     * @return The Painter
     */
    public static Painter polygon(Color color) {
        return (m, c) -> {
            for(Attributed<Polygon> p : m.polygons()) {
                c.drawPolygon(p.value(), color);
            }
        };
    }
    
    /**
     * Returns the Painter which draws all the lines with the LineStyle style
     * @param lS The line style to draw with
     * @return the Painter
     */
    public static Painter line(LineStyle lS){
        return(m, c) -> {
            for(Attributed<PolyLine> p : m.polyLines()) {
                c.drawPolyLine(p.value(), lS);
            }
        };
    }
    
    /**
     * Returns the Painter which draws all the lines with the style defined by the parameters
     * @param width The line's width
     * @param color The line's color
     * @param lCap The line's cap
     * @param lJoin The line's join
     * @param dashingPattern The line's dashing pattern
     * @return the Painter
     */
    public static Painter line(float width, Color color, LineCap lCap, LineJoin lJoin, float... dashingPattern) {
        return line(new LineStyle(width, color, lCap, lJoin, dashingPattern));
    }
    
    /**
     * Returns the Painter which draws all the line with the width, color and default style
     * @param width The line's width
     * @param color The line's color
     * @return the Painter
     */
    public static Painter line(float width, Color color) {
        return line(new LineStyle(width, color));
    }
    
    /**
     * Returns the Painter which draws all the polygons' holes and shells with the LineStyle
     * @param lS The line style to draw with
     * @return the Painter
     */
    public static Painter outline(LineStyle lS){
        return (m,c) -> {
            for(Attributed<Polygon> polygon : m.polygons()) {
                c.drawPolyLine(polygon.value().shell(), lS);
                for(PolyLine polyLine : polygon.value().holes()) {
                    c.drawPolyLine(polyLine, lS);
                }
            }
        };
    }
    
    /**
     * Returns the Painter which draws all the polygons' holes and shells with style defined by the parameters
     * @param width The line's width
     * @param color The line's color
     * @param lCap The line's cap
     * @param lJoin The line's join
     * @param dashingPattern The line's dashing pattern
     * @return the Painter
     */
    public static Painter outline(float width, Color color, LineCap lCap, LineJoin lJoin, float... dashingPattern) {
        return outline(new LineStyle(width, color, lCap, lJoin, dashingPattern));
    }

    /**
     * Returns the Painter which draws all the polygons' holes and shells with the width, color and default style
     * @param width The line's width
     * @param color The line's color
     * @return the Painter
     */
    public static Painter outline(float width, Color color) {
        return outline(new LineStyle(width, color));
    }
    
    /**
     * Returns a Painter which works only on the predicate
     * @param predicate The predicate to work with
     * @return The Painter
     */
    public default Painter when(Predicate<Attributed<?>> predicate) {
        return (m, c) -> {
            Map.Builder filteredMapBuilder = new Map.Builder();
            m.polygons().forEach(
                    x -> {
                        if(predicate.test(x)){
                            filteredMapBuilder.addPolygon(x);
                        };
                    });
            m.polyLines().forEach(
                    x -> {
                        if(predicate.test(x)){
                            filteredMapBuilder.addPolyLine(x);
                        };
                    });
            drawMap(filteredMapBuilder.build(), c);
        };
    }
    
    /**
     * Returns a Painter which draws this Painter above the other Painter
     * @param other The other Painter to draw above
     * @return The Painter
     */
    public default Painter above(Painter other) {
        return (m, c) -> {
            other.drawMap(m, c);
            drawMap(m, c);
        };
    }
    
    /**
     * Returns the Painter which layers all the layers
     * @return
     */
    public default Painter layered() {
        Painter painter = this.when(Filters.onLayer(-5));
        for (int i = -4; i <= 5 ; i++) {
            painter = this.when(Filters.onLayer(i)).above(painter);
        }
        return painter;
    }
}