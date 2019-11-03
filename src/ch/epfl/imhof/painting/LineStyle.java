/**
 *  Represents the line style with 5 parameters :
 *      - width
 *      - cap
 *      - join
 *      - Color
 *      - dashing pattern
 *  
 *  @author:     José Ferro Pinto (233843)
 *  @author:     Dorian Laforest (234832)
 */
package ch.epfl.imhof.painting;

import java.util.Arrays;

public final class LineStyle {
    /**
     *  LineStyle.lineCap enumeration
     *  Represents the three line cap a line can have
     *  
     *  @author:     José Ferro Pinto (233843)
     *  @author:     Dorian Laforest (234832)
     */
    public static enum LineCap{BUTT, ROUND, SQUARE};
    /**
     *  LineStyle.lineJoin enumeration
     *  Represents the three line join a line can have
     *  
     *  @author:     José Ferro Pinto (233843)
     *  @author:     Dorian Laforest (234832)
     */
    public static enum LineJoin{MITER, ROUND, BEVEL};
    private final LineCap cap;
    private final LineJoin join;
    private final float width;
    private final float[] dashingPattern;
    private final Color color;

    /**
     * LineStyle Constructor
     * Constructs a new LineStyle with the given width, color, line cap, line join and
     * dashing pattern
     * @param width the line's width
     * @param color the line's color
     * @param lCap the line's cap
     * @param lJoin the line's join
     * @param dP the line's dashing pattern
     * @throw IllegalArgumentException if the width is negative or if one of the dashing
     * pattern array's element is negative or equal to 0
     */
    public LineStyle(float width, Color color, LineCap lCap, LineJoin lJoin, float[] dP) throws IllegalArgumentException {
        if(!(width >= 0)) {
            throw new IllegalArgumentException("invalid width value " + width);
        }
        if (dP==null) {
            throw new IllegalArgumentException("invalid dashing pattern");
        }
        float[] dashingPattern = new float[dP.length];
        for(int i = 0; i < dP.length; ++i) {
            if(!(dP[i] > 0)) {
                throw new IllegalArgumentException("invalid dashing pattern value" + dP[i]);
            }
            dashingPattern[i] = dP[i];
        }

        this.width = width;
        this.color = color;
        this.cap = lCap;
        this.join = lJoin;
        this.dashingPattern = dashingPattern;
    }

    /**
     * LineStyle Constructor
     * Constructs a new LineStyle with the given width and color
     * Using default value for the line cap (BUTT), line join (MITER)
     * and dashing pattern (empty array)
     * @param width the line's width
     * @param color the line's color
     */
    public LineStyle(float width, Color color) {
        this(width, color, LineCap.BUTT, LineJoin.MITER, new float[0]);
    }

    /**
     * Returns the line cap
     * @return the line cap
     */
    public LineCap lineCap() {
        return this.cap;
    }
    
    /**
     * Returns the line join
     * @return the line join
     */
    public LineJoin lineJoin() {
        return this.join;
    }

    /**
     * Returns the line's width
     * @return the line's width
     */
    public float width() {
        return this.width;
    }

    /**
     * Returns the line's dashing pattern
     * @return the line's dashing pattern
     */
    public float[] dashingPattern() {
        return Arrays.copyOf(this.dashingPattern, this.dashingPattern.length);
    }

    /**
     * Returns the line's color
     * @return the line's color
     */
    public Color color() {
        return this.color;
    }

    /**
     * Returns an identical LineStyle except for the line cap
     * @param lC the new line cap
     * @return a new LineStyle
     */
    public LineStyle withLineCap(LineCap lC) {
        return new LineStyle(width, color, lC, join, dashingPattern);
    }

    /**
     * Returns an identical LineStyle except for the line join
     * @param lJ the new line join
     * @return a new LineStyle
     */
    public LineStyle withLineJoin(LineJoin lJ) {
        return new LineStyle(width, color, cap, lJ, dashingPattern);
    }

    /**
     * Returns an identical LineStyle except for the width
     * @param width the new line's width
     * @return a new LineStyle
     */
    public LineStyle withWidth(float width) {
        return new LineStyle(width, color, cap, join, dashingPattern);
    }

    /**
     * Returns an identical LineStyle except for the dashing pattern
     * @param dP the new line's dashing pattern
     * @return a new LineStyle
     */
    public LineStyle withDashingPattern(float[] dP) {
        return new LineStyle(width, color, cap, join, dP);
    }
    
    /**
     * Returns an identical LineStyle except for the color
     * @param c the new line's color
     * @return a new LineStyle
     */
    public LineStyle withColor(Color c) {
        return new LineStyle(width, c, cap, join, dashingPattern);
    }
}