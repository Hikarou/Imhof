/**
 * Represents a generic graph unoriented
 *
 * @author: José Ferro Pinto (233843)
 * @author: Dorian Laforest (234832)
 */

package ch.epfl.imhof;

import java.util.Map;
import java.util.*;

public final class Graph<N> {
    private final Map<N, Set<N>> neighbors;

    /**
     * Constructor for Graph
     * @param neighbors a map containing all the point associated with their neighbors' set
     */
    public Graph(Map<N, Set<N>> neighbors) {
        Map<N, Set<N>> newNeighbors = new HashMap<>();
        for (Map.Entry<N, Set<N>> neighbor : neighbors.entrySet()) {
            newNeighbors.put(neighbor.getKey(), Collections.unmodifiableSet(new HashSet<>(neighbor.getValue())));
        }
        this.neighbors = Collections.unmodifiableMap(newNeighbors);
    }

    /**
     * Returns the nodes' set
     * @return the nodes' set
     */
    public Set<N> nodes() {
        return neighbors.keySet();
    }

    /**
     * Returns the neighbors of the given node
     * @param node the node to extract neighbors of
     * @return a set of the node's neighbors
     * @throws IllegalArgumentException if the node does not exist
     */
    public Set<N> neighborsOf(N node) throws IllegalArgumentException {
        if (neighbors.containsKey(node)) {
            return Collections.unmodifiableSet(neighbors.get(node));
        }
        throw new IllegalArgumentException("The given node does not exist");
    }

    /**
     *  Represents Graph's Builder class
     *
     *  @author: José Ferro Pinto (233843)
     *  @author: Dorian Laforest (234832)
     */
    public static final class Builder<N> {
        private Map<N, Set<N>> neighbors = new HashMap<>();

        /**
         * Adds the node if it does not exist in the map
         * @param n the node to add
         */
        public void addNode(N n) {
            if (!neighbors.containsKey(n)) {
                neighbors.put(n, new HashSet<>());
            }
        }

        /**
         * Adds the node n2 to n1's neighbors and vice-versa
         * @param n1 first node to add
         * @param n2 second node to add
         * @throws IllegalArgumentException if n1 or n2 does not exist
         */
        public void addEdge(N n1, N n2) throws IllegalArgumentException {
            if (!neighbors.containsKey(n1) || !neighbors.containsKey(n2)) {
                throw new IllegalArgumentException("The given nodes does not exist");
            }
            neighbors.get(n1).add(n2);
            neighbors.get(n2).add(n1);
        }

        /**
         * Builds the Graph
         * @return a new Graph
         */
        public Graph<N> build() {
            return new Graph<N>(neighbors);
        }
    }
}