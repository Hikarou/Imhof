/**
 *  A color represented by its red, green and blue components
 *  
 *  @author:     Jose Ferro Pinto (233843)
 *  @author:     Dorian Laforest (234832)
 */
package ch.epfl.imhof.painting;

public final class Color {
    private final double r;
    private final double g;
    private final double b;
    
    /**
     * Pure red color
     */
    public final static Color RED = new Color(1, 0, 0);
    /**
     * Pure green color
     */
    public final static Color GREEN = new Color(0, 1, 0);
    /**
     * Pure blue color
     */
    public final static Color BLUE = new Color(0, 0, 1);
    /**
     * Pure black color
     */
    public final static Color BLACK = new Color(0, 0, 0);
    /**
     * Pure white color
     */
    public final static Color WHITE = new Color(1, 1, 1);
    
    /**
     * Private Constructor for Color
     * Constructs a Color with the given red, green and blue components
     * @param r the red component must be in [0,1]
     * @param g the green component must be in [0,1]
     * @param b the blue component must be in [0,1]
     * @throw IllegalArgumentException if r, g or b are not in [0,1]
     */
    private Color(double r, double g, double b) throws IllegalArgumentException {
        if (! (0.0 <= r && r <= 1.0))
            throw new IllegalArgumentException("Invalid red component: " + r);
        if (! (0.0 <= g && g <= 1.0))
            throw new IllegalArgumentException("Invalid green component: " + g);
        if (! (0.0 <= b && b <= 1.0))
            throw new IllegalArgumentException("Invalid blue component: " + b);
        
        this.r= r;
        this.g = g;
        this.b = b;
    }
    
    /**
     * Returns a grey color with its three components equal to v
     * @param v the r, g and b component's value must be in [0,1]
     * @return a grey color
     * @throw IllegalArgumentException if r, g or b are not in [0,1]
     */
    public static Color gray(double v) throws IllegalArgumentException {
        return new Color(v, v, v);
    }
    
    /**
     * Returns a new color with the given red, green and blue values
     * @param r the red component must be in [0,1]
     * @param g the green component must be in [0,1]
     * @param b the blue component must be in [0,1]
     * @return a new color
     * @throw IllegalArgumentException if r, g or b are not in [0,1]
     */
    public static Color rgb(double r, double g, double b) throws IllegalArgumentException {
        return new Color(r, g, b);
    }
    
    /**
     * Returns a color with its rgb values packed in an integer
     * @param packedRGB the encoded color
     * @return a color
     */
    public static Color rgb(int packedRGB){
        return new Color((((packedRGB >> 16) & 0xFF) / 255d),
                (((packedRGB >>  8) & 0xFF) / 255d),
                (((packedRGB >>  0) & 0xFF) / 255d));
    }
    /**
     * Returns the red component
     * @return the red component
     */
    public double r() {
        return r;
    }
    
    /**
     * Returns the green component
     * @return the green component
     */
    public double g() {
        return g;
    }
    
    /**
     * Returns the blue component
     * @return the blue component
     */
    public double b() {
        return b;
    }
    
    /**
     * Multiply two color value-wise
     * @param that the other color to multiply with
     * @return the new color
     */
    public Color multiplyColor(Color that) {
        return new Color(this.r*that.r, this.g*that.g, this.b*that.b);
    }
    
    /**
     * Convert the color in an AWT Color
     * @return The color in AWT corresponding to the current Color
     */
    public java.awt.Color toAWTColor() {
        return new java.awt.Color((float)r, (float)g, (float)b);
    }
}