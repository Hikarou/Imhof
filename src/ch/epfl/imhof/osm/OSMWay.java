/**
 *  Represents OSM's way class
 *  
 *  @author:     José Ferro Pinto (233843)
 *  @author:     Dorian Laforest (234832)
 */
package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class OSMWay extends OSMEntity {
    final private List<OSMNode> nodes;

    /**
     * Constructor for OSMWay
     * @param id the unique ID from OSM
     * @param nodes the way's nodes
     * @param attributes the way's attributes
     * @throws IllegalArgumentException if there is less than 2 nodes in the way
     */
    public OSMWay(long id, List<OSMNode> nodes, Attributes attributes) throws IllegalArgumentException{
        super(id, attributes);
        if(nodes.size() < 2) {
            throw new IllegalArgumentException("Can not create a way with less than 2 nodes");
        }
        this.nodes = Collections.unmodifiableList(new ArrayList<>(nodes));
    }

    /**
     * Returns the number of nodes
     * @return number of nodes
     */
    public int nodesCount() {
        return nodes.size();
    }

    /**
     * Returns the list of nodes
     * @return the list of nodes
     */
    public List<OSMNode> nodes() {
        return nodes;
    }

    /**
     * Returns the list of nodes without the last node if it's a closed way
     * @return the list of nodes 
     */
    public List<OSMNode> nonRepeatingNodes() {
        if(isClosed()) {
            return Collections.unmodifiableList(nodes.subList(0, nodes.size()-1));
        }
        return nodes;
    }

    /**
     * Returns the first Node
     * @return the first node
     */
    public OSMNode firstNode() {
        return nodes.get(0);
    }

    /**
     * Returns the last Node
     * @return the last node
     */
    public OSMNode lastNode() {
        return nodes.get(nodesCount()-1);
    }

    /**
     * Is the node closed ?
     * @return true if the node is closed, false otherwise
     */
    public boolean isClosed() {
        return firstNode() == lastNode();
    }

    /**
     *  Represents OSMWay's Builder class
     *  
     *  @author:     José Ferro Pinto (233843)
     *  @author:     Dorian Laforest (234832)
     */
    public final static class Builder extends OSMEntity.Builder {
        private List<OSMNode> nodes = new ArrayList<>();

        /**
         * Constructor for OSMWay.Builder
         * @param id unique ID from OSM
         */
        public Builder(long id) {
            super(id);
        }

        /**
         * Adds a Node to the list of nodes
         * @param newNode the node to add
         */
        public void addNode(OSMNode newNode) {
            nodes.add(newNode);
        }

        /**
         * {@inheritDoc}
         * And is there at least 2 nodes?
         */
        @Override
        public boolean isIncomplete() {
            return super.isIncomplete() || (nodes.size() < 2);
        }

        /**
         * Builds and returns a new OSMWay 
         * @return a new OSMWay
         * @throws IllegalStateException if incomplete
         */
        public OSMWay build() throws IllegalStateException{
            if(isIncomplete()) {
                throw new IllegalStateException("Incomplete way");
            }
            return new OSMWay(super.id, nodes, super.attributes.build());
        }
    }
}