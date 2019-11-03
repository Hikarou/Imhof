/**
 *  Represents OSM's node class
 *  
 *  @author:     Jose Ferro Pinto (233843)
 *  @author:     Dorian Laforest (234832)
 */
package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.PointGeo;

public final class OSMNode extends OSMEntity {
    final private PointGeo position;
    /**
     * Constructor for OSMNode
     * @param id the unique ID from OSM
     * @param position the node's position
     * @param attributes the node's attributes
     */
    public OSMNode(long id, PointGeo position, Attributes attributes) {
        super(id, attributes);
        this.position = position;
    }

    /**
     * Returns the node's position
     * @return the node's position
     */
    public PointGeo position() {
        return this.position;
    }

    /**
     *  Represents OSMNode's Builder class
     *  
     *  @author:     Jose Ferro Pinto (233843)
     *  @author:     Dorian Laforest (234832)
     */
    public final static class Builder extends OSMEntity.Builder {
        private PointGeo position;

        /**
         * Constructor for OSMNode.Builder
         * @param id unique ID from OSM
         * @param position The node position
         */
        public Builder(long id, PointGeo position) {
            super(id);
            this.position = position;
        }

        /**
         * Builds and returns a new OSMNode 
         * @return a new OSMNode
         * @throws IllegalStateException if incomplete
         */
        public OSMNode build() throws IllegalStateException{
            if(isIncomplete()) {
                throw new IllegalStateException("Incomplete node");
            }

            return new OSMNode(super.id, position, super.attributes.build());
        }
    }
}