/**
 *  A generic class which represents a T's entity with attributes
 *  
 *  @author:     José Ferro Pinto (233843)
 *  @author:     Dorian Laforest (234832)
 */

package ch.epfl.imhof;

public final class Attributed<T> {
    private final T value;
    private final Attributes attributes;

    /**
     * Constructor for Attributed
     * @param value A T's entity
     * @param attributes The attributes to attach to the entity
     */
    public Attributed(T value, Attributes attributes) {
        this.value = value;
        this.attributes = attributes;
    }

    /**
     * Returns the value
     * @return the value
     */
    public T value() {
        return this.value;
    }

    /**
     * Returns the attributes
     * @return the attributes
     */
    public Attributes attributes() {
        return attributes;
    }
    /**
     * Checks if the given key exists in attributes
     * @param attributeName String to search
     * @return true if it exists, false otherwise
     */
    public boolean hasAttribute(String attributeName) {
        return attributes.contains(attributeName);
    }

    /**
     * Returns the value associated with the key
     * @param attributeName String to search
     * @return the key's value, null otherwise
     */
    public String attributeValue(String attributeName) {
        return attributes.get(attributeName);
    }

    /**
     * Returns the value associated with the key
     * @param attributeName String to search
     * @param defaultValue String to return if not found
     * @return the key's value, defaultValue otherwise
     */
    public String attributeValue(String attributeName, String defaultValue) {
        return attributes.get(attributeName, defaultValue);
    }

    /**
     * Returns the value associated with the key
     * @param attributeName String to search
     * @param defaultValue value to return if not found
     * @return the key's value, defaultValue otherwise
     */
    public int attributeValue(String attributeName, int defaultValue) {
        return attributes.get(attributeName, defaultValue);
    }
}