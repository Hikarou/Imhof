/**
 *  Represents a class for drawing a shaded colored relief
 *  
 *  @author:     José Ferro Pinto (233843)
 *  @author:     Dorian Laforest (234832)
 */
package ch.epfl.imhof.dem;

import ch.epfl.imhof.Vector3;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.projection.Projection;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.function.Function;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public final class ReliefShader {
    private final Projection proj;
    private final HGTDigitalElevationModel hgt;
    private final Vector3 lightSource;
    
    /**
     * Constructor for ReliefShader
     * @param proj the Projection type to use
     * @param hgt the HGTDigitalElevationModel 
     * @param lightSource a vector pointing in the direction of the light source
     */
    public ReliefShader(Projection proj, HGTDigitalElevationModel hgt, Vector3 lightSource) {
        this.proj = proj;
        this.hgt = hgt;
        this.lightSource = lightSource.normalized();
    }

    /**
     * Returns the shaded blurred relief
     * @param bL the relief bottom left Point
     * @param tR the relief top right Point
     * @param width the relief width
     * @param height the relief height
     * @param radius the blur radius
     * @return The shaded blurred (iff radius != 0) relief
     * @throws IllegalArgumentException if the width or the height are negative or null, if the radius is negative
     * and if the bottom left point is not lower and lefter than the top right point
     */
    public BufferedImage shadedRelief(Point bL, Point tR, int width, int height, double radius) throws IllegalArgumentException {
        if (radius < 0.0) {
            throw new IllegalArgumentException("Radius must be positive or 0");
        }
        if (width <= 0.0) {
            throw new IllegalArgumentException("Width must be positive");
        }
        if (height <= 0.0) {
            throw new IllegalArgumentException("Height must be positive");
        }        
        if ((bL.x() > tR.x()) || (bL.y() > tR.y())) {
            throw new IllegalArgumentException("The bottom left point must be lower and lefter than the top right point");
        }
        
        int n = (int)(2*Math.ceil(radius)+1);
        int stampRadius = (int) Math.floor(n/2d);
        Function<Point, Point> frameChange = Point.alignedCoordinateChange(new Point(stampRadius, height+stampRadius), bL, 
                                                                            new Point(width+stampRadius, stampRadius), tR);
        
        if(radius == 0.0) {
            return rawShadedRelief(width, height, frameChange);
        }
        
        BufferedImage relief = rawShadedRelief(width+2*stampRadius, height+2*stampRadius, frameChange);
        float[] kernel = getGaussianBlurKernel(radius);
        
        BufferedImage blurredRelief = blur(kernel, relief);
        blurredRelief = blurredRelief.getSubimage(stampRadius, stampRadius, width, height);
        return blurredRelief;
    }
    
    /**
     * Returns the raw shaded relief
     * @param width the relief width
     * @param height the relief height
     * @param frameChange the frame change function
     * @return the raw shaded relief
     */
    private BufferedImage rawShadedRelief(int width, int height, Function<Point, Point> frameChange) {
        BufferedImage relief = new BufferedImage(width, height, TYPE_INT_RGB);
        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                Point p = frameChange.apply(new Point(x, y));
                Vector3 n =  hgt.normalAt(proj.inverse(p));
                double cosTheta = n.normalized().scalarProduct(lightSource);
                double r = (cosTheta + 1)/2d;
                double g = r;
                double b = (0.7d*cosTheta + 1)/2d;
                Color c = Color.rgb(r, g, b);
                relief.setRGB(x, y, c.toAWTColor().getRGB());
            }
        }
        return relief;
    }
    
    /**
     * Returns the Gaussian blur kernel
     * @param radius the blur radius
     * @return an array containing the Gaussian blur kernel
     */
    private float[] getGaussianBlurKernel(double radius) {
        double sigma = radius/3d;
        double fract = 2*sigma*sigma;
        int n = (int)(2*Math.ceil(radius)+1);
        float[] gBRadius = new float[n];
        float sum = 0f;
        for(int i = 0 ; i< n; ++i) {
            int x = i-(n/2);
            float val = (float)Math.exp(-(x*x)/(fract));
            gBRadius[i] = val;
            sum += val;
        }
        for(int i = 0; i < n; ++i) {
            gBRadius[i] /= sum;
        }
        return gBRadius;
    }
    
    /**
     * Returns a blurred relief
     * @param kernel the Gaussian blur kernel
     * @param relief the relief to blur
     * @return a blurred relief
     */
    private BufferedImage blur(float[] kernel, BufferedImage relief) {
        Kernel kerV = new Kernel(1, kernel.length, kernel);
        ConvolveOp cOV = new ConvolveOp(kerV, ConvolveOp.EDGE_NO_OP, null);
        relief = cOV.filter(relief, null);
        
        Kernel kerH = new Kernel(kernel.length, 1, kernel);
        ConvolveOp cOH = new ConvolveOp(kerH, ConvolveOp.EDGE_NO_OP, null);
        relief = cOH.filter(relief, null);
        return relief;
    }
}