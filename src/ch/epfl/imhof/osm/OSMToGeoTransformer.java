/**
 *  Represents the OSMToGeoTransformer class : a converter from OSM data to a map
 *  
 *  @author:     Jose Ferro Pinto (233843)
 *  @author:     Dorian Laforest (234832)
 */
package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.Graph;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.projection.Projection;

import java.util.*;

public final class OSMToGeoTransformer {
    private final Projection projectionType;
    private Map.Builder mapBuilder;
    private final static Set<String> SURFACE_ATTRIBUTES = new HashSet<>(Arrays.asList(
            "aeroway", "amenity", "building", "harbour", "historic",
            "landuse", "leisure", "man_made", "military", "natural",
            "office", "place", "power", "public_transport", "shop",
            "sport", "tourism", "water", "waterway", "wetland"));
    private final static Set<String> POLYGON_ATTRIBUTES = new HashSet<>(Arrays.asList(
            "building", "landuse", "layer", "leisure", "natural",
            "waterway"));
    private final static Set<String> POLYLINE_ATTRIBUTES = new HashSet<>(Arrays.asList(
            "bridge", "highway", "layer", "man_made", "railway",
            "tunnel", "waterway"));

    /**
     * Constructor for OSMTogeoTransformer
     * @param projection The projection to use in the transformation
     */
    public OSMToGeoTransformer(Projection projection) {
        projectionType = projection;
    }

    /**
     * Transforms an OSMMap to a Map
     * @param map the map to transform
     * @return the transformed map
     */
    public Map transform(OSMMap map) {
        mapBuilder = new Map.Builder();
        waysConverter(map.ways());
        relationsConverter(map.relations());
        return mapBuilder.build();
    }

    /**
     * Converts the ways in attributed PolyLines (open or closed) and Polygons
     * @param ways The way's list to convert
     */
    private void waysConverter(List<OSMWay> ways) {
        Attributes attPolygon;
        Attributes attPolyLine;
        PolyLine.Builder polyLineBuilder;
        Polygon polygon;
        PolyLine polyLine;
        for (OSMWay currentWay : ways) {
            //Reset PolyLine Builder
            polyLineBuilder = new PolyLine.Builder();
            attPolygon = currentWay.attributes().keepOnlyKeys(POLYGON_ATTRIBUTES);
            attPolyLine = currentWay.attributes().keepOnlyKeys(POLYLINE_ATTRIBUTES);
            //If it is a Polygon
            if(currentWay.isClosed() && wayDescribeSurface(currentWay)) {
                List<OSMNode> nonRepeatingNodes = currentWay.nonRepeatingNodes();
                //Create a Polygon without holes if the filtered attributes list is not empty
                if(!attPolygon.isEmpty()) {
                    for (OSMNode currentNode : nonRepeatingNodes) {
                        polyLineBuilder.addPoint(projectionType.project(currentNode.position()));
                    }
                    polygon = new Polygon(polyLineBuilder.buildClosed());
                    mapBuilder.addPolygon(new Attributed<Polygon>(polygon,attPolygon));
                }
            }
            //Else it is a PolyLine
            else {
                //If the filtered attributes is not empty
                if(!attPolyLine.isEmpty()) {
                    List<OSMNode> nonRepeatingNodes = currentWay.nonRepeatingNodes();
                    for(OSMNode currentNode : nonRepeatingNodes) {
                        polyLineBuilder.addPoint(projectionType.project(currentNode.position()));
                    }
                    if(currentWay.isClosed()) {
                        //Create a ClosedPolyLine
                        polyLine = polyLineBuilder.buildClosed();
                        mapBuilder.addPolyLine(new Attributed<PolyLine>(polyLine, attPolyLine));
                    }
                    else {
                        //Create an OpenPolyLine
                        polyLine = polyLineBuilder.buildOpen();
                        mapBuilder.addPolyLine(new Attributed<PolyLine>(polyLine, attPolyLine));
                    }
                }
            }
        }
    }

    /**
     * Converts the relations in attributed Polygons
     * @param relations The relation's list to convert
     */
    private void relationsConverter(List<OSMRelation> relations) {
        Attributes attMultiPolygon;
        List<Attributed<Polygon>> attributedPolygon = new ArrayList<>();
        for(OSMRelation currentRelation : relations) {
            attMultiPolygon = currentRelation.attributes().keepOnlyKeys(POLYGON_ATTRIBUTES);
            //If the relation describe a multiPolygon and the filtered attributes list is not empty
            if(!attMultiPolygon.isEmpty() && currentRelation.hasAttribute("type") && currentRelation.attributeValue("type").equals("multipolygon")) {
                List<Attributed<Polygon>> attributedPolygonToAdd = assemblePolygon(currentRelation, attMultiPolygon);
                if(!attributedPolygonToAdd.isEmpty()) {
                    attributedPolygon.addAll(attributedPolygonToAdd);
                } 
            }
        }
        //Add all the attributedPolygon in the Map's builder
        for(Attributed<Polygon> currentAttPolygon : attributedPolygon) {
            mapBuilder.addPolygon(currentAttPolygon);
        }
    }

    /**
     * Tests if the current way describes a surface
     * @param way the current way to test
     * @return True if the way describe a surface, false otherwise
     */    
    private boolean wayDescribeSurface(OSMWay way) {
        String key = way.attributeValue("area");
        //Returns true if the way has the attributes "area" and the value is "yes", "1" or "true" 
        //or if the way has one or more attributes in the SURFACE_ATTRIBUTES list
        return (key != null && (key.equals("yes") || key.equals("1") || key.equals("true"))) || (!way.attributes().keepOnlyKeys(SURFACE_ATTRIBUTES).isEmpty());
    }

    /**
     * Returns a list with all the rings (ClosedPolyLine) for the given relation and role
     * @param relation the relation to filter
     * @param role filtering by role
     * @return a filtered list of ClosedPolyLine or an empty list if the ring's computation fails
     */
    private List<ClosedPolyLine> ringsForRole(OSMRelation relation, String role) {
        //First step: Only have the members with the given role and the WAY type
        ArrayList<OSMRelation.Member> members = new ArrayList<OSMRelation.Member>(relation.members());
        ArrayList<OSMRelation.Member> clearedMembers = new ArrayList<OSMRelation.Member>();

        for (OSMRelation.Member member : members) {
            if (member.role()!=null&&member.role().equals(role)&&member.type().equals(OSMRelation.Member.Type.WAY)){
                clearedMembers.add(member);
            }
        }

        //Second step creating the PolyLines

        //Creates a Graph with all the nodes and their neighbors
        Graph.Builder<OSMNode> graphBuilder = new Graph.Builder<OSMNode>();
        for (OSMRelation.Member member : clearedMembers) {
            OSMNode addedNode = null;
            for (OSMNode node : ((OSMWay) member.member()).nodes()) {
                graphBuilder.addNode(node);
                if (addedNode != null) {
                    graphBuilder.addEdge(node, addedNode);
                }
                addedNode = node;
            }
        }

        Graph<OSMNode> graphed = graphBuilder.build();
        Set<OSMNode> nodes = graphed.nodes();
        Set<OSMNode> usedPoints = new HashSet<OSMNode>();
        List<ClosedPolyLine> rings = new ArrayList<ClosedPolyLine>();

        //Checking if all the nodes have exactly 2 neighbors
        for(OSMNode node : nodes) {
            if(graphed.neighborsOf(node).size() != 2) {
                return rings;
            }
        }

        //Creating all the ClosedPolyLines
        for (OSMNode node : nodes) {
            //If the node is not already used
            if(!usedPoints.contains(node)) {
                //Represents the list of the node's neighbors
                Set<OSMNode> neighbors = graphed.neighborsOf(node);
                int nodesAdded = 1;
                boolean change = false;
                PolyLine.Builder polyLine = new PolyLine.Builder();
                //Adding the first point in the PolyLine Builder
                polyLine.addPoint(projectionType.project(node.position()));
                //Adding the first point in the usedPoints list
                usedPoints.add(node);
                //Iterates on all the node's neighbors until it closes the PolyLine
                do {
                    change = false;
                    //For each node's neighbors
                    for(OSMNode neighbor : neighbors) {
                        //If the point is not already used
                        if(!usedPoints.contains(neighbor)) {
                            //Adds the point to the PolyLine
                            polyLine.addPoint(projectionType.project(neighbor.position()));
                            change = true;
                            //Changes the neighbors list to the a list of neighbors of the added point
                            neighbors = graphed.neighborsOf(neighbor);
                            //Adds the added point to the usedPoints list
                            usedPoints.add(neighbor);
                            nodesAdded++;
                            //Leave the for each loop
                            break;
                        }
                    }
                }while(change);
                //If the PolyLine has 2 or more points, add the PolyLine to the rings list
                if (nodesAdded>2) {
                    rings.add(polyLine.buildClosed());
                }
            }
        }
        return rings;
    }


    /**
     * Given a relation, extracts the inner and outer rings using the RingForRoles method
     * and assembles the Polygons. Returns an attributed Polygon with the given attributes attached to it
     * or an empty list if the outer rings computation has failed
     * @param relation the current multiPolygon to assemble
     * @param attributes The multipolygon's attributes
     * @return A list of the multipolygon's attributed Polygon
     */
    private List<Attributed<Polygon>> assemblePolygon(OSMRelation relation, Attributes attributes) {
        List<Attributed<Polygon>> attributedPolygon = new ArrayList<>();
        //A comparator for ClosedPolyLine : compare the area
        Comparator<ClosedPolyLine> c = (ClosedPolyLine cp1, ClosedPolyLine cp2) -> 
            Double.valueOf(cp1.area()).compareTo(Double.valueOf(cp2.area()));

        //A list with the outer rings
        List<ClosedPolyLine> outerPolyLines = ringsForRole(relation, "outer");
        //A list with the inner rings
        List<ClosedPolyLine> innerPolyLines = ringsForRole(relation, "inner");

        //Tests if ringsForRole has worked for the outer rings <=> the outer rings list is not empty
        if(!outerPolyLines.isEmpty()) {
            //Sorts the outer rings according to their areas in ascending order
            Collections.sort(outerPolyLines, c);
            //Tests if ringsForRole has worked for the inner rings <=> the inner rings list is not empty
            if(!innerPolyLines.isEmpty()) {
                //Sorts the inner rings according to their areas in ascending order
                Collections.sort(innerPolyLines, c);
                //A set containing the already used inner rings
                Set<ClosedPolyLine> usedInnerPolyLine = new HashSet<>();
                List<ClosedPolyLine> currentHoles = new ArrayList<>();

                for(ClosedPolyLine currentOuterPolyLine : outerPolyLines) {
                    currentHoles.clear();

                    for(ClosedPolyLine currentInnerPolyLine : innerPolyLines) {
                        //If the inner ring is not already used and is contained in the current outer ring
                        if(!usedInnerPolyLine.contains(currentInnerPolyLine) && currentOuterPolyLine.containsPoint(currentInnerPolyLine.firstPoint())) {
                            usedInnerPolyLine.add(currentInnerPolyLine);
                            currentHoles.add(currentInnerPolyLine);
                        }
                    }
                    //Creates an attributed Polygon with holes
                    if(!currentHoles.isEmpty()) {
                        attributedPolygon.add(new Attributed<Polygon>(new Polygon(currentOuterPolyLine, currentHoles), attributes));
                    }
                    //Creates an attributed Polygon without holes
                    else {
                        attributedPolygon.add(new Attributed<Polygon>(new Polygon(currentOuterPolyLine), attributes));
                    }
                }
            }
            //Creates an attributed Polygon without holes
            else {
                for(ClosedPolyLine currentOuterPolyLine : outerPolyLines) {
                    attributedPolygon.add(new Attributed<Polygon>(new Polygon(currentOuterPolyLine), attributes));
                }
            }
        }
        return attributedPolygon;
    }
}