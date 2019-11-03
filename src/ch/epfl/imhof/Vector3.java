/**
 *  A class representing a Vector in 3-dimension
 *  
 *  @author:     Jose Ferro Pinto (233843)
 *  @author:     Dorian Laforest (234832)
 */
package ch.epfl.imhof;

public final class Vector3 {
    private final double x;
    private final double y;
    private final double z;
    
    /**
     * Constructor for Vector3
     * @param x the x component
     * @param y the y component
     * @param z the z component
     */
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * Returns the vector's norm
     * @return the vector's norm
     */
    public double norm() {
        return Math.sqrt(x*x+y*y+z*z);
    }
    
    /**
     * Returns a normalized vector
     * @return a normalized vector
     */
    public Vector3 normalized() {
        return new Vector3(x/norm(), y/norm(), z/norm());
    }
    
    /**
     * Returns the scalar product of this vector with the one
     * given in parameter
     * @param other the other vector
     * @return the scalar product of this vector and the one 
     * given in parameter
     */
    public double scalarProduct(Vector3 other) {
        return x*other.x + y*other.y + z*other.z;
    }
    
    /**
     * Returns the x value
     * @return the x value
     */
    public double x() {
        return x;
    }
    
    /**
     * Returns the y value
     * @return the y value
     */
    public double y() {
            return y;
    }
    
    /**
     * Returns the z value
     * @return the z value
     */
    public double z() {
     return z;   
    }
}