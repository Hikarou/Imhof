/**
 *  Represents OSM's mother class
 *  
 *  @author:     José Ferro Pinto (233843)
 *  @author:     Dorian Laforest (234832)
 */

package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;

public abstract class OSMEntity {
    final private long id;
    final private Attributes attributes;

    /**
     * Constructor for OSMEntity
     * @param id unique ID from OSM
     * @param attributes attributes contained by the entity
     */
    public OSMEntity(long id, Attributes attributes){
        this.id = id;
        this.attributes = attributes;
    }

    /**
     * Returns the ID
     * @return the ID
     */
    public long id(){
        return id;
    }

    /**
     * Returns all the attributes
     * @return the attributes
     */
    public Attributes attributes(){
        return attributes;
    }

    /**
     * Checks if key exists in attributes
     * @param key the string to search
     * @return true if it exists, false otherwise
     */
    public boolean hasAttribute(String key){
        return attributes.contains(key);
    }

    /**
     * Returns the value associated with the key
     * @param key the string to search
     * @return the key's value, null otherwise
     */
    public String attributeValue(String key){
        return attributes.get(key);
    }

    /**
     *  Represents OSM's Builder mother class
     *  
     *  @author:     José Ferro Pinto (233843)
     *  @author:     Dorian Laforest (234832)
     */
    public abstract static class Builder{
        protected long id;
        private boolean isIncomplete;
        protected Attributes.Builder attributes;

        /**
         * The OSMEntity Builder
         * @param id unique ID from OSM
         */
        public Builder(long id){
            this.id = id;
            isIncomplete = false;
            attributes = new Attributes.Builder();
        }

        /**
         * Add the association in the attribute or replaces if key already exists
         * @param key the key associated
         * @param value the value associated
         */
        public void setAttribute(String key, String value){
            attributes.put(key, value);
        }

        /**
         * Declares the Entity incomplete
         */
        public void setIncomplete(){
            isIncomplete = true;
        }

        /**
         * Is the entity incomplete ?
         * @return true if incomplete, false otherwise
         */
        public boolean isIncomplete(){
            return isIncomplete;
        }
    }
}