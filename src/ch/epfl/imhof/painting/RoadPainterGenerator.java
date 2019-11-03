/**
 *  A road painter's generator
 *  
 *  @author:     Jose Ferro Pinto (233843)
 *  @author:     Dorian Laforest (234832)
 */
package ch.epfl.imhof.painting;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;

import java.util.function.Predicate;

public class RoadPainterGenerator {
    
    /**
     * Private constructor so the class can not be instantiate
     */
    private RoadPainterGenerator(){
    };
    
    /**
     * Generates the painter for the corresponding road specification
     * @param RS Roads' specification
     * @return The Painter
     */
    public static Painter painterForRoads(RoadSpec... RS){
        Painter bridgeI=RS[0].internalBridge();
        Painter bridgeC=RS[0].externalBridge();
        Painter roadI=RS[0].internalRoad();
        Painter roadC=RS[0].externalRoad();
        Painter tunnel=RS[0].tunnel();
        
        for (int i = 1; i < RS.length; i++) {
            RoadSpec roadSpec=RS[i];
            bridgeI=bridgeI.above(roadSpec.internalBridge());
            bridgeC=bridgeC.above(roadSpec.externalBridge());
            roadI=roadI.above(roadSpec.internalRoad());
            roadC=roadC.above(roadSpec.externalRoad());
            tunnel=tunnel.above(roadSpec.tunnel());
        }
        return bridgeI.above(bridgeC).above(roadI).above(roadC).above(tunnel);
    }

    /**
     * Represents the roads' specification
     *  
     *  @author:     Jose Ferro Pinto (233843)
     *  @author:     Dorian Laforest (234832)
     */
    public static class RoadSpec{
        private final static Predicate<Attributed<?>> isBridge = Filters.tagged("bridge");
        private final static Predicate<Attributed<?>> isTunnel = Filters.tagged("tunnel");
        private final Predicate<Attributed<?>> predicate;
        private final float wI;
        private final Color cI;
        private final float wC;
        private final Color cC;
        
        /**
         * Creates the road specification
         * @param predicate The predicates which it only draws on
         * @param wI The width of the interior line
         * @param cI The color of the interior line
         * @param wC The width of the exterior line
         * @param cC The color of the exterior line
         */
        public RoadSpec(Predicate<Attributed<?>> predicate, float wI, Color cI, float wC, Color cC){
            this.predicate = predicate;
            this.wI = wI;
            this.cI = cI;
            this.wC = wC;
            this.cC = cC;
        }
        
        /**
         * Returns the internal bridge painter
         * @return the internal bridge painter
         */
        public Painter internalBridge(){
            return Painter.line(internalBridgeLS()).when(predicate.and(isBridge));
        }
        
        /**
         * Returns the external bridge painter
         * @return the external bridge painter
         */
        public Painter externalBridge(){
            return Painter.line(externalBridgeLS()).when(predicate.and(isBridge));
        }
        
        /**
         * Returns the internal road painter
         * @return the internal road painter
         */
        public Painter internalRoad(){
            return Painter.line(internalRoadLS()).when(predicate.and(isBridge.negate()).and(isTunnel.negate()));
        }
        
        /**
         * Returns the external road painter
         * @return the external road painter
         */
        public Painter externalRoad(){
            return Painter.line(externalRoadLS()).when(predicate.and(isBridge.negate()).and(isTunnel.negate()));
        }
        
        /**
         * Returns the tunnel painter
         * @return the tunnel painter
         */
        public Painter tunnel(){
            return Painter.line(tunnelLS()).when(predicate.and(isTunnel));
        }
        
        /**
         * Returns the internal bridge line style
         * @return the internal bridge line style
         */
        private LineStyle internalBridgeLS(){
            return new LineStyle(wI, cI, LineCap.ROUND, LineJoin.ROUND, new float[0]);
        }

        /**
         * Returns the external bridge line style
         * @return the external bridge line style
         */
        private LineStyle externalBridgeLS(){
            return new LineStyle(wI+2*wC, cC, LineCap.BUTT, LineJoin.ROUND, new float[0]);
        }

        /**
         * Returns the internal road line style
         * @return the internal road line style
         */
        private LineStyle internalRoadLS(){
            return internalBridgeLS();
        }

        /**
         * Returns the external road line style
         * @return the external road line style
         */
        private LineStyle externalRoadLS(){
            return externalBridgeLS().withLineCap(LineCap.ROUND);
        }

        /**
         * Returns the tunnel line style
         * @return the tunnel line style
         */
        private LineStyle tunnelLS(){
            return new LineStyle(wI/2, cC, LineCap.BUTT, LineJoin.ROUND, new float[]{2*wI, 2*wI});
        }
    }
}