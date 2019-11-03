/**
 *  Represents a reader for OSMMap. This class is not instantiable
 *  
 *  @author:     José Ferro Pinto (233843)
 *  @author:     Dorian Laforest (234832)
 */
package ch.epfl.imhof.osm;

import ch.epfl.imhof.PointGeo;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public final class OSMMapReader {
    /**
     * The private OSMMapReader constructor
     */
    private OSMMapReader() {   
    }

    /**
     * Static method to read an OSMFile in XML format
     * @param fileName The path of the XML file
     * @param unGZip A boolean to know if the file is compressed with GZip
     * @return a new OSMMap constructed with the XML file's informations
     * @throws SAXException An exception thrown if something went wrong with the XML reader
     * @throws IOException An exception thrown if something went wrong with the file (eg the filename is wrong, or the file doesn't exist)
     */
    public static OSMMap readOSMFile(String fileName, boolean unGZip) throws SAXException, IOException {
        InputStream i;
        //If the file is compressed with GZip, uncompressed it
        if(unGZip) {
            i = new GZIPInputStream(new BufferedInputStream(new FileInputStream(fileName)));
        }
        else {
            i = new BufferedInputStream(new FileInputStream(fileName));
        }
        OSMMap.Builder mapBuilder = new OSMMap.Builder();
        try {
            XMLReader r = XMLReaderFactory.createXMLReader();  
            /**
             * Anonymous subclass for the ContentHandler
             */
            r.setContentHandler(new DefaultHandler() {
                private OSMNode.Builder nodeBuilder;
                private OSMRelation.Builder relationBuilder;
                private OSMWay.Builder wayBuilder;
                private PointGeo position;
                private boolean isWay = false;
                private boolean isRelation = false;
                private boolean isNode = false;
                private OSMEntity entityForId;
                private double lat = 0d;
                private double lon = 0d;
                private long id = 0l;
                private long ref = 0l;
                private String v = "";
                private String k = "";
                private String type = "";
                private String role = "";
                private final static String NODE = "node";
                private final static String ND = "nd";
                private final static String WAY = "way";
                private final static String RELATION = "relation";
                private final static String TAG = "tag";
                private final static String MEMBER = "member";
                private final static String ID = "id";
                private final static String LATITUDE = "lat";
                private final static String LONGITUDE = "lon";
                private final static String REFERENCE = "ref";
                private final static String TYPE = "type";
                private final static String ROLE = "role";
                private final static String KEY = "k";
                private final static String VALUE = "v";

                /**
                 * A method called when an open XML tag is encountered
                 * This method initialize the various OSM Builder with the XML file informations 
                 * @param uri An unnecessary variable (for this project)
                 * @param lName An unnecessary variable (for this project)
                 * @param qName The XML tag name
                 * @param atts The XML attributes
                 * @throws SAXException An exception thrown if something went wrong with the XML reader
                 */
                @Override
                public void startElement(String uri, String lName, String qName, Attributes atts) throws SAXException {
                    switch(qName) {
                    case NODE:
                        isNode = true;
                        id = Long.parseLong(atts.getValue(ID));
                        lat = Double.parseDouble(atts.getValue(LATITUDE));
                        lon = Double.parseDouble(atts.getValue(LONGITUDE));
                        position = new PointGeo(Math.toRadians(lon), Math.toRadians(lat));
                        nodeBuilder = new OSMNode.Builder(id, position);
                        break;
                    case ND:
                        ref = Long.parseLong(atts.getValue(REFERENCE));
                        entityForId = mapBuilder.nodeForId(ref);
                        if(entityForId != null) {
                            wayBuilder.addNode((OSMNode)entityForId);
                        }
                        else {
                            wayBuilder.setIncomplete();
                        }
                        break;
                    case WAY:
                        isWay = true;
                        id = Long.parseLong(atts.getValue(ID));
                        wayBuilder = new OSMWay.Builder(id);
                        break;
                    case RELATION:
                        isRelation = true;
                        id = Long.parseLong(atts.getValue(ID));
                        relationBuilder = new OSMRelation.Builder(id);
                        break;
                    case TAG:
                        k = atts.getValue(KEY);
                        v = atts.getValue(VALUE);
                        if(isNode) {
                            nodeBuilder.setAttribute(k, v);
                        }
                        else if(isWay) {
                            wayBuilder.setAttribute(k, v);
                        }
                        else if(isRelation) {
                            relationBuilder.setAttribute(k, v);
                        }
                        break;
                        //if "member" appears, we have to specify with the type and build the right relation
                    case MEMBER:
                        type = atts.getValue(TYPE);
                        role = atts.getValue(ROLE);
                        id = Long.parseLong(atts.getValue(REFERENCE));
                        switch(type) {
                        case WAY:
                            entityForId = mapBuilder.wayForId(id);
                            if(entityForId != null) {
                                relationBuilder.addMember(OSMRelation.Member.Type.WAY, role, (OSMWay)entityForId);
                            }
                            else {
                                relationBuilder.setIncomplete();
                            }
                            break;
                        case NODE:
                            entityForId = mapBuilder.nodeForId(id);
                            if(entityForId != null) {
                                relationBuilder.addMember(OSMRelation.Member.Type.NODE, role, (OSMNode)entityForId);
                            }
                            else {
                                relationBuilder.setIncomplete();
                            }
                            break;
                        case RELATION:
                            entityForId = mapBuilder.relationForId(id);
                            if(entityForId != null) {
                                relationBuilder.addMember(OSMRelation.Member.Type.RELATION, role, (OSMRelation)entityForId);
                            }
                            else {
                                relationBuilder.setIncomplete();
                            }
                            break;
                        default:
                            throw new SAXException(type + "is not a valid member");
                        }
                        break;
                    default:
                        break;
                    }
                }

                /**
                 * A method called when a closed XML tag is encountered
                 * This method build the various OSMEntity and add them in the OSMMap
                 * @param uri An unnecessary variable (for this project)
                 * @param lName An unnecessary variable (for this project)
                 * @param qName The XML tag name
                 * @throws SAXException An exception thrown if something went wrong with the XML reader
                 */
                @Override
                public void endElement(String uri, String lName, String qName) throws SAXException {
                    switch(qName) {
                    case NODE:
                        isNode = false;
                        if(!nodeBuilder.isIncomplete()) {
                            mapBuilder.addNode(nodeBuilder.build());
                        }
                        break;
                    case WAY:
                        isWay = false;
                        if(!wayBuilder.isIncomplete()) {
                            mapBuilder.addWay(wayBuilder.build());
                        }
                        break;
                    case RELATION:
                        isRelation = false;
                        if(!relationBuilder.isIncomplete()) {
                            mapBuilder.addRelation(relationBuilder.build());
                        }
                        break;
                    default:
                        break;
                    }
                }
            });
            r.parse(new InputSource(i));
        }
        finally {
            i.close();
        }
        return mapBuilder.build();
    }
}