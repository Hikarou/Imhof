/**
     *  Utility class giving some filters for attributed element
     *  
     *  @author:     José Ferro Pinto (233843)
     *  @author:     Dorian Laforest (234832)
     */
package ch.epfl.imhof.painting;

import ch.epfl.imhof.Attributed;

import java.util.function.Predicate;

public final class Filters {
    
    /**
     * Private constructor so the class can not be instantiate
     */
    private Filters() {
    }
    
    /**
     * Returns a predicate that is true only if the value for which it is applied 
     * has an attribute with the name given, regardless of its value.
     * @param name the attribute's name
     * @return The predicate
     */
    public static Predicate<Attributed<?>> tagged(String name) {
        return x -> x.hasAttribute(name);
    }
    
    /**
     * Returns a predicate that is true only if the value for which it is applied
     * has an attribute with the given name and moreover the value for this attribute is 
     * one of the given ones
     * @param name the attribute's name
     * @param value the atribute's value
     * @return The predicate
     */
    public static Predicate<Attributed<?>> tagged(String attributed, String ... nameValues){
        return x -> {
            if(nameValues.length <1){
                throw new IllegalArgumentException("invalid parameters number");
            }
            if(!x.hasAttribute(attributed)){
                return false;
            }
            for(String val : nameValues) {
                if(x.attributeValue(attributed).equals(val))
                    return true;
            }
            return false;
        };
    }
    
    /**
     * Returns a predicate that is only true when applied
     * to an attributed entity belonging to this layer.
     * @param nbLayer the layer's number
     * @return The predicate
     */
    public static Predicate<Attributed<?>> onLayer(int nbLayer) {
        return x -> {
            return x.hasAttribute("layer") ? Integer.parseInt(x.attributeValue("layer")) == nbLayer : 0 == nbLayer;
        };
    }
}