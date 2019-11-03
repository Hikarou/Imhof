/**
 *  A class which represents a closed PolyLine
 *  
 *  @author:     Jose Ferro Pinto (233843)
 *  @author:     Dorian Laforest (234832)
 */

package ch.epfl.imhof.geometry;

import java.util.List;

public final class ClosedPolyLine extends PolyLine {
    /**
     * Constructor for ClosedPolyLine
     * @param points Represent the points of the ClosedPolyLine to construct
     */
    public ClosedPolyLine(List<Point> points) {
        super(points);
    }

    /**
     * {@inheritDoc}
     * @return true because a ClosedPolyLine is always closed
     */
    @Override
    public boolean isClosed() {
        return true;
    }

    /**
     * Computes the ClosedPolyLine's absolute area using the following method :
     * - To compute the signed area of a closed PolyLine we first take an arbitrary point P of the plan then add all the signed area
     *      of all the triangles formed by the point P and the two extremities of each PolyLine's vertices. 
     *      Then we take the absolute value of the signed area
     * - To compute the signed area of a triangle we compute the vector product of the three triangle's vertices. 
     * @return the area of the ClosedPolyLine
     */
    public double area(){
        double result = 0;
        for (int i = 0; i < super.points().size(); i++) {
            result += (generalizedPoint(i).x()*generalizedPoint(i+1).y()-generalizedPoint(i+1).x()*generalizedPoint(i).y());
        }
        return Math.abs(result/2);
    }

    /**
     * Tests if the point p is inside the PolyLine using the winding number method :
     * The winding number of a closed curve compared to a point is the number of counterclockwise round
     * made by the curve around the point. A point is outside the closed curve if and only if
     * the winding number is 0
     * @param p the point we want to test
     * @return true if p is inside the PolyLine, false otherwise
     */
    public boolean containsPoint(Point p) {
        int index = 0;
        double pY = p.y();
        //For each segment S = (Pi, Pi+1) of the closed PolyLine
        for(int i = 0; i< super.points().size(); i++) {
            Point p1 = super.points().get(i);
            Point p2 = generalizedPoint(i+1);
            if(p1.y() <= pY) {
                //If Pi+1.y <= P.y and P is on the left side of (Pi,Pi+1) : increment the index 
                if((p2.y() > pY) && (isLeft(p, p1, p2))) {
                    index++;
                }
            }
            //Else if P2.y <= P.y and P is on the left side of (Pi+1, Pi) : decrement the index
            else if((p2.y() <= pY) && (isLeft(p, p2, p1))) {
                index--;
            }
        }
        return (index != 0);
    }

    /**
     * Private method to know if a point P is on the left side of a segment (P1, P2)
     * We use the signed area of the triangle (P,P1,P2) to know where P is
     * If the signed area is negative then P is on the left side
     * @param p The point to test
     * @param p1 The first point to compare
     * @param p2 The second point to compare
     * @return True if P is on the left side of (P1, P2), false otherwise
     */
    private boolean isLeft(Point p, Point p1, Point p2){
        return (p1.x()-p.x())*(p2.y()-p.y())>(p2.x()-p.x())*(p1.y()-p.y());
    }

    /**
     * Returns a generalized index's vertex
     * @param The point's index to be generalized
     * @return The point with a generalized index
     */
    private Point generalizedPoint(int index){
        return super.points().get(Math.floorMod(index ,super.points().size()));
    }
}