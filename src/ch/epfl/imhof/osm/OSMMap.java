/**
 *  A class which represents an OSM map
 *  
 *  @author:     Jose Ferro Pinto (233843)
 *  @author:     Dorian Laforest (234832)
 */
package ch.epfl.imhof.osm;

import java.util.*;

public final class OSMMap {
    private final List<OSMWay> ways;
    private final List<OSMRelation> relations;

    /**
     * The OSMMap constructor
     * @param ways the Collection of way to add
     * @param relations the Collection of relation to add
     */
    public OSMMap(Collection<OSMWay> ways, Collection<OSMRelation> relations){
        this.ways = Collections.unmodifiableList(new ArrayList<>(ways));
        this.relations = Collections.unmodifiableList(new ArrayList<>(relations));
    }

    /**
     * Returns the map's ways
     * @return The map's ways
     */
    public List<OSMWay> ways(){
        return ways;
    }

    /**
     * Returns the map's relation
     * @return The map's relation
     */
    public List<OSMRelation> relations(){
        return relations;
    }

    /**
     *  Represents OSMMap Builder class
     *  
     *  @author:     Jose Ferro Pinto (233843)
     *  @author:     Dorian Laforest (234832)
     */
    public final static class Builder{
        private Map<Long, OSMWay> waysMap = new HashMap<Long, OSMWay>();
        private Map<Long, OSMRelation> relationsMap = new HashMap<Long, OSMRelation>();
        private Map<Long, OSMNode> nodesMap = new HashMap<Long, OSMNode>();

        /**
         * Adds a new node to the nodes map
         * @param newNode The node to add
         */
        public void addNode(OSMNode newNode){
            nodesMap.put(newNode.id(), newNode);
        }

        /**
         * Returns the node with the given id or null if it does not exist
         * @param id the id to search
         * @return the node corresponding to id or null if does not exist
         */
        public OSMNode nodeForId(long id){
            return nodesMap.get(id);
        }

        /**
         * Adds a new way to the ways map
         * @param newWay The way to add
         */
        public void addWay(OSMWay newWay){
            waysMap.put(newWay.id(), newWay);
        }

        /**
         * Returns the way with the given id or null if it does not exist
         * @param id the id to search
         * @return the way corresponding to id or null if does not exist
         */
        public OSMWay wayForId(long id){
            return waysMap.get(id);
        }

        /**
         * Adds a new relation to the relations map
         * @param newRelation The relation to add
         */
        public void addRelation(OSMRelation newRelation){
            relationsMap.put(newRelation.id(),newRelation);
        }

        /**
         * Returns the relation with the given id or null if it doesn't exist
         * @param id the id to search 
         * @return the relation corresponding to id or null if does not exist
         */
        public OSMRelation relationForId(long id){
            return relationsMap.get(id);
        }

        /**
         * Builds and returns a new OSMMap
         * @return a new OSMMap with the added ways and relations until now
         */
        public OSMMap build(){
            return new OSMMap(waysMap.values(), relationsMap.values());
        }
    }
}