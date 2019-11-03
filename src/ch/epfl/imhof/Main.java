/**
 *  Represents the Imhof main class
 *  
 *  @author:     José Ferro Pinto (233843)
 *  @author:     Dorian Laforest (234832)
 */
package ch.epfl.imhof;

import ch.epfl.imhof.dem.Earth;
import ch.epfl.imhof.dem.HGTDigitalElevationModel;
import ch.epfl.imhof.dem.ReliefShader;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMap;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.Java2DCanvas;
import ch.epfl.imhof.painting.Painter;
import ch.epfl.imhof.projection.CH1903Projection;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public final class Main {
    private final static double DPI_TO_DPM_FACTOR = 0.0254d;
    private final static Vector3 LIGHT_SOURCE = new Vector3(-1.0, 1.0, 1.0);
    private final static double BLUR_RADIUS_IN_METER = 0.0017d;
    private final static double MAP_SCALE = 25000d;
    private final static Color BACKGROUND_COLOR = Color.WHITE;
    
    /**
     * Main method of Imhof project
     * Generate a map from the given arguments
     * @param args the arguments :
     * - the OSM file path compressed with gzip
     * - the HGT file path
     * - the bottom left point longitude in degree
     * - the bottom left point latitude in degree
     * - the top right point longitude in degree
     * - the top right point latitude in degree
     * - the image resolution in DPI
     * - the image file path to generate
     * @throws IOException if something went wrong with the IO side
     * @throws SAXException if something went wrong with the XMLReader side
     */
    public static void main(String[] args) throws SAXException, IOException {
        String osmFilePath = args[0];
        File hgtFile= new File(args[1]);
        File output = new File(args[7]);
        int indexBeginExtension = args[7].indexOf('.') + 1;
        String imageExtension = args[7].substring(indexBeginExtension);
        
        double longitudeBottomLeft = Math.toRadians(Double.parseDouble(args[2]));
        double latitudeBottomLeft = Math.toRadians(Double.parseDouble(args[3]));
        double longitudeTopRight = Math.toRadians(Double.parseDouble(args[4]));
        double latitudeTopRight = Math.toRadians(Double.parseDouble(args[5]));
        
        int resolutionDPI = Integer.parseInt(args[6]);
        double resolutionDPM = resolutionDPI/DPI_TO_DPM_FACTOR;
        
        try (HGTDigitalElevationModel hgt = new HGTDigitalElevationModel(hgtFile)) {
            CH1903Projection proj = new CH1903Projection();
            ReliefShader rS = new ReliefShader(proj, hgt, LIGHT_SOURCE);
        
            PointGeo bottomLeftPointGeo = new PointGeo(longitudeBottomLeft, latitudeBottomLeft);
            PointGeo topRightPointGeo = new PointGeo(longitudeTopRight, latitudeTopRight);
            Point bottomLeftPoint = proj.project(bottomLeftPointGeo);
            Point topRightPoint = proj.project(topRightPointGeo);
        
            double radius = BLUR_RADIUS_IN_METER*resolutionDPM;
            int height = (int)Math.round(resolutionDPM/MAP_SCALE*(latitudeTopRight - latitudeBottomLeft)*Earth.RADIUS);
            int width = (int)Math.round(height*(topRightPoint.x()-bottomLeftPoint.x())/(topRightPoint.y() - bottomLeftPoint.y()));
        
            BufferedImage relief = rS.shadedRelief(bottomLeftPoint, topRightPoint, width, height, radius);

            OSMMap osmMap = OSMMapReader.readOSMFile(osmFilePath, true);
            OSMToGeoTransformer osmMapTransformer = new OSMToGeoTransformer(new CH1903Projection());
            Map map = osmMapTransformer.transform(osmMap);
            Java2DCanvas canvas = new Java2DCanvas(bottomLeftPoint, topRightPoint, width, height, resolutionDPI, BACKGROUND_COLOR);

            Painter painter = SwissPainter.painter();
            painter.drawMap(map, canvas);
            BufferedImage image = canvas.image();
        
            BufferedImage imageWithRelief = new BufferedImage(width, height, TYPE_INT_RGB);
            for(int x = 0; x < width; ++x) {
                for(int y = 0; y < height; ++y) {
                    Color colMap = Color.rgb(image.getRGB(x, y));
                    Color colRelief = Color.rgb(relief.getRGB(x, y));
                    Color finalCol = colMap.multiplyColor(colRelief);
                    imageWithRelief.setRGB(x, y, finalCol.toAWTColor().getRGB());
                }
            }
            ImageIO.write(imageWithRelief, imageExtension, output);
        }
    }
}