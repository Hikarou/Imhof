/**
 *  A concrete Canvas' implementation implementing also Java2D Library
 *  
 *  @author:     Jose Ferro Pinto (233843)
 *  @author:     Dorian Laforest (234832)
 */

package ch.epfl.imhof.painting;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.function.Function;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class Java2DCanvas implements Canvas {
    private final Function<Point, Point> frameChange;
    private final BufferedImage image;
    private final Graphics2D ctx;
    private final double RESOLUTION_FACTOR = 72d;
    
    /**
     * JAva2DCanvas' constructor using the change of reference
     * @param pBL Projected points at the bottom-left corner
     * @param pTR Projected points at the bottom-left corner
     * @param width Picture's width in pixels
     * @param height Picture's height in pixels
     * @param resolution Picture's resolution in dpi
     * @param background The background color
     * @throws IllegalArgumentException if pBL is higher or righter than pTR,
     * if width, width, resolution are negative or if background is null
     */
    public Java2DCanvas(Point pBL, Point pTR, int width, int height, int resolution, Color background) throws IllegalArgumentException{
        if (pBL.x() > pTR.x() || pBL.y() > pTR.y()){
            throw new IllegalArgumentException("The bottom left point has to be lower and lefter than the top right point");
        }
        if (width<=0){
            throw new IllegalArgumentException("Width must be positive");
        }
        if (height<=0){
            throw new IllegalArgumentException("Height must be positive");
        }
        if (resolution<=0){
            throw new IllegalArgumentException("Resolution must be positive");
        }
        if (background==null){
            throw new IllegalArgumentException("The background color must be defined");
        }
        
        double dilatation = resolution/RESOLUTION_FACTOR;
        this.frameChange = Point.alignedCoordinateChange(pBL, new Point(0, height/dilatation), pTR, new Point(width/dilatation, 0));
        this.image = new BufferedImage(width, height, TYPE_INT_RGB);
        this.ctx = image.createGraphics();
        ctx.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        ctx.scale(dilatation, dilatation);
        ctx.setColor(background.toAWTColor());
        ctx.fillRect(0, 0, width, height);
    }

    /**
     * Get the canvas' image
     * @return the canvas' image
     */
    public BufferedImage image() {
        return image;
    }
    
    @Override
    public void drawPolyLine(PolyLine polyLine, LineStyle lineStyle) {
        Path2D polyLinePath = generatePath(polyLine);
        
        float[] dashingPattern = lineStyle.dashingPattern().length == 0 ? null : lineStyle.dashingPattern();
        
        Stroke stroke = new BasicStroke(lineStyle.width(), lineStyle.lineCap().ordinal(), lineStyle.lineJoin().ordinal(), 10.0f, dashingPattern, 0f);
        
        ctx.setStroke(stroke);
        ctx.setColor(lineStyle.color().toAWTColor());
        ctx.draw(polyLinePath);
    }
    
    @Override
    public void drawPolygon(Polygon polygon, Color color) {
        Path2D shellPath = generatePath(polygon.shell());
        
        Area shell = new Area(shellPath);
        
        for(PolyLine polyLine : polygon.holes()) {
            Path2D hole = generatePath(polyLine);
            shell.subtract(new Area(hole));
        }
        ctx.setColor(color.toAWTColor());
        ctx.fill(shell);
    }
    /**
     * Generates the path for te given polyLine
     * @param polyLine The PolyLine to generate path with
     * @return the generated path
     */
    private Path2D generatePath(PolyLine polyLine) {
        Path2D path = new Path2D.Double();
        path.moveTo(frameChange.apply(polyLine.firstPoint()).x(), frameChange.apply(polyLine.firstPoint()).y());
        for(Point p : polyLine.points()) {
            if(p != polyLine.firstPoint()){
                path.lineTo(frameChange.apply(p).x(), frameChange.apply(p).y());
            }
        }
        
        if(polyLine.isClosed()){
            path.closePath();
        }

        return path;
    }
}