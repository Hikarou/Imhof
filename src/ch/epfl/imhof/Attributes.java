/**
 *  A class which represents a set of attributes and their associated values
 *  
 *  @author:     Jose Ferro Pinto (233843)
 *  @author:     Dorian Laforest (234832)
 */

package ch.epfl.imhof;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class Attributes {
    final private Map<String, String> attributes;

    /**
     * Constructor for Attributes
     * @param attributes Map of attributes for OpenStreetMap
     */
    public Attributes(Map<String, String> attributes) {
        this.attributes = Collections.unmodifiableMap(new HashMap<>(attributes));
    }

    /**
     * Check if attributes is empty
     * @return true if empty, false otherwise
     */
    public boolean isEmpty(){
        return attributes.isEmpty();
    }

    /**
     * Check if key exists in attributes
     * @param key String to search
     * @return true if it exists, false otherwise
     */
    public boolean contains(String key){
        return attributes.containsKey(key);
    }

    /**
     * Returns the value associated with the key
     * @param key String to search
     * @return the key's value, null otherwise
     */
    public String get(String key){
        return attributes.get(key);
    }

    /**
     * Returns the value associated with the key
     * @param key String to search
     * @param defaultValue String to return if not found
     * @return the key's value, defaultValue otherwise
     */
    public String get(String key, String defaultValue){
        return attributes.getOrDefault(key, defaultValue);
    }

    /**
     * Returns the value associated with the key
     * @param key String to search
     * @param defaultValue to return if not found
     * @return the key's value, defaultValue otherwise
     */
    public int get(String key, int defaultValue){
        try {
            return Integer.parseInt(attributes.get(key));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Returns a filtered version of attributes containing the key in the Set keysToKeep
     * @param keysToKeep Set of String to search
     * @return The filtered attributes list
     */
    public Attributes keepOnlyKeys(Set<String> keysToKeep){
        Attributes.Builder builder = new Attributes.Builder();
        for (String key : keysToKeep) {
            if (this.contains(key)) {
                builder.put(key, get(key));
            }
        }
        return builder.build();
    }

    /**
     *  Represents Attributes Builder class
     *  
     *  @author:     Jose Ferro Pinto (233843)
     *  @author:     Dorian Laforest (234832)
     */
    public final static class Builder{
        private Map<String, String> attributes = new HashMap<String, String>();

        /**
         * Adds the association in the attributes or replaces it if key already exists
         * @param key String key associated
         * @param value String value associated
         */
        public void put(String key, String value){
            attributes.put(key, value);
        }

        /**
         * Builds and returns a new Attributes
         * @return a new Attributes
         */
        public Attributes build(){
            return new Attributes(attributes);
        }
    }
}