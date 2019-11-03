/**
 *  Represents a digital elevation model for the HGT file format
 *  
 *  @author:     Jose Ferro Pinto (233843)
 *  @author:     Dorian Laforest (234832)
 */

package ch.epfl.imhof.dem;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;

public class HGTDigitalElevationModel implements DigitalElevationModel {
    private final double latitude;
    private final double longitude;
    private ShortBuffer buffer;
    private final FileInputStream stream;
    private final double delta;
    private final int nbPointInLine;
    private final static char NORTH = 'N';
    private final static char SOUTH = 'S';
    private final static char WEST = 'W';
    private final static char EAST = 'E';
    private final static String FILE_EXTENSION = ".hgt";

    /**
     * HGTDigitalElevationModel's constructor
     * @param hgt The hgt file to read
     * @throws IllegalArgumentException if the file is not valid or cannot be read
     */
    public HGTDigitalElevationModel(File hgt) throws IllegalArgumentException {
        String fileName = hgt.getName();
        char latSgn = fileName.substring(0, 1).charAt(0);
        String latAbs = fileName.substring(1, 3);
        char lonSgn = fileName.substring(3, 4).charAt(0);
        String lonAbs = fileName.substring(4, 7);
        String fileExtension = fileName.substring(7);

        if (!(fileName.length() == 11)
                || !(latSgn == NORTH|| latSgn == SOUTH)
                || !(lonSgn == EAST|| lonSgn == WEST)) {
            throw new IllegalArgumentException("The file is not correctly named");
        }

        if(!fileExtension.equals(FILE_EXTENSION)) {
            throw new IllegalArgumentException("Wrong format file : " + fileExtension);
        }

        long fileLength = hgt.length();

        int nbPoint = (int)Math.sqrt(fileLength/2d);
        if(!(nbPoint*nbPoint*2 == fileLength)) {
            throw new IllegalArgumentException("Wrong file size : " + fileLength);
        }

        short lat = latSgn == NORTH ? (short)1 : (short)-1;
        short lon = lonSgn == EAST ? (short)1 : (short)-1;

        try {
            lat = (short) (lat*Short.parseShort(latAbs));
        }
        catch(NumberFormatException e) {
            throw new IllegalArgumentException("Invalid latitude in file name : " + latAbs);
        }

        try {
            lon = (short) (lon*Short.parseShort(lonAbs));
        }
        catch(NumberFormatException e) {
            throw new IllegalArgumentException("Invalid longitude in file name : " + lonAbs);
        }

        this.longitude = Math.toRadians(lon);
        this.latitude = Math.toRadians(lat);
        this.delta = Math.toRadians(1/(nbPoint-1d));
        this.nbPointInLine = nbPoint;
        FileInputStream stream = null;
        ShortBuffer buffer = null;
        try {
            stream = new FileInputStream(hgt);
            buffer = stream.getChannel()
                    .map(MapMode.READ_ONLY, 0, fileLength)
                    .asShortBuffer();
        }
        catch(FileNotFoundException e) {
            throw new IllegalArgumentException("File not found");
        }
        catch(Exception e) {
            throw new IllegalArgumentException("Invalid file contents");
        }
        this.stream = stream;
        this.buffer = buffer;
    }

    /* (non-Javadoc)
     * @see ch.epfl.imhof.dem.DigitalElevationModel#close()
     */
    @Override
    public void close() throws IOException {
        buffer = null;
        stream.close();
    }

    /* (non-Javadoc)
     * @see ch.epfl.imhof.dem.DigitalElevationModel#normalAt(ch.epfl.imhof.PointGeo)
     */
    @Override
    public Vector3 normalAt(PointGeo point) throws IllegalArgumentException {
        if(!(point.latitude() >= latitude && point.latitude() <= (latitude + Math.toRadians(1)))
                || !(point.longitude() >= longitude && point.longitude() <= (longitude + Math.toRadians(1)))) {
            throw new IllegalArgumentException("Invalid PointGeo");
        }
        double s = Earth.RADIUS*delta;
        short[] neighbors = neighborsOf(point);
        double x = 1/2d * s * (neighbors[0] - neighbors[1] + neighbors[2] - neighbors[3]);
        double y = 1/2d * s * (neighbors[0] + neighbors[1] - neighbors[2] - neighbors[3]);
        double z = s*s;
        return new Vector3(x, y, z);
    }

    /**
     * Returns the four neighbors of the point given in parameter in this order :
     * bottom-left, bottom-right, top-left and top-right 
     * @param point the point to find the neighbors of
     * @param delta the HGT file angular resolution
     * @return An array containing the four neighbors of the given point
     */
    private short[] neighborsOf(PointGeo point) {
        short[] neighbors = new short[4];
        int indexLatitude = (int)Math.floor((point.latitude()-latitude)/delta);
        if(indexLatitude == (nbPointInLine-1)) {
            indexLatitude--;
        }

        int indexLongitude = (int)Math.floor((point.longitude()-longitude)/delta);
        if(indexLongitude == (nbPointInLine-1)) {
            indexLongitude--;
        }

        int indexByte = (nbPointInLine-1-indexLatitude) * nbPointInLine + indexLongitude;
        neighbors[0] = buffer.get(indexByte);
        neighbors[1] = buffer.get(indexByte+1);
        int indexByteMinusOne = indexByte - nbPointInLine;
        neighbors[2] = buffer.get(indexByteMinusOne);
        neighbors[3] = buffer.get(indexByteMinusOne+1);
        return neighbors;
    }
}