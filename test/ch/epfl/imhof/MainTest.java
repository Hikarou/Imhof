package ch.epfl.imhof;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

public class MainTest {

    @Ignore
    @Test
    public void testLausanneWithReference() throws IOException, SAXException {  
        //Lausanne
        String[] argsLausanne = {"./data/lausanne.osm.gz",
                                "./data/imhof-dems/N46E006.hgt",
                                "6.5594",
                                "46.5032",
                                "6.6508",
                                "46.5459",
                                "300",
                                "lausanneFinal.png"};
        Main.main(argsLausanne);
        File expectedLausanneFile = new File("./image_Schinz/Imhof_maps/lausanne.png");
        File actualLausanneFile = new File("./lausanneFinal.png");
        
        BufferedImage expectedLausanne = ImageIO.read(expectedLausanneFile);
        BufferedImage actualLausanne = ImageIO.read(actualLausanneFile);
        
        System.out.println("Lausanne :");
        BufferedImage lausanneDiff = diffImage(expectedLausanne, actualLausanne);
        ImageIO.write(lausanneDiff, "png", new File("./lausanneDiff.png"));
    }
    
    @Ignore
    @Test
    public void testBerneWithReference() throws IOException, SAXException {
        //Berne
        String[] argsBerne = {"./data/berne.osm.gz",
                              "./data/imhof-dems/N46E007.hgt",
                              "7.3912",
                              "46.9322",
                              "7.4841",
                              "46.9742",
                              "300",
                              "berneFinal.png"};
        Main.main(argsBerne);
        File expectedBerneFile = new File("./image_Schinz/Imhof_maps/berne.png");
        File actualBerneFile = new File("./berneFinal.png");
        
        BufferedImage expectedBerne = ImageIO.read(expectedBerneFile);
        BufferedImage actualBerne = ImageIO.read(actualBerneFile);
        
        System.out.println("Berne :");
        BufferedImage berneDiff = diffImage(expectedBerne, actualBerne);
        ImageIO.write(berneDiff, "png", new File("./berneDiff.png"));
    }
    
    @Ignore
    @Test
    public void testInterlakenWithReference() throws IOException, SAXException {
        //Interlaken
        String[] argsInterlaken = {"./data/interlaken.osm.gz", 
                                   "./data/imhof-dems/N46E007.hgt",
                                   "7.8122",
                                   "46.6645",
                                   "7.9049",
                                   "46.7061",
                                   "300",
                                   "interlakenFinal.png"};
        Main.main(argsInterlaken);
        File expectedInterlakenFile = new File("./image_Schinz/Imhof_maps/interlaken.png");
        File actualInterlakenFile = new File("./interlakenFinal.png");
        
        System.out.println("Interlaken :");
        BufferedImage expectedInterlaken = ImageIO.read(expectedInterlakenFile);
        BufferedImage actualInterlaken = ImageIO.read(actualInterlakenFile);
        
        BufferedImage interlakenDiff = diffImage(expectedInterlaken, actualInterlaken);
        ImageIO.write(interlakenDiff, "png", new File("./interlakenDiff.png"));
    }

    private static BufferedImage diffImage(BufferedImage iExpected, BufferedImage iActual) {
        BufferedImage result = new BufferedImage(iExpected.getWidth(), iExpected.getHeight(), TYPE_INT_RGB);
        int ct = 0;
        int tot = 0;
        for(int x = 0; x < iExpected.getWidth(); ++x) {
            for(int y = 0; y < iExpected.getHeight(); ++y) {
                tot++;
                if(!diffColor(iExpected.getRGB(x, y),iActual.getRGB(x, y))) {
                    /*
                    int eRGB = iExpected.getRGB(x, y);                    
                    int eR = (eRGB >> 16) & 0xFF;
                    int eG = (eRGB >> 8) & 0xFF;
                    int eB = (eRGB >> 0) & 0xFF;
                    int aRGB = iActual.getRGB(x, y);
                    int aR = (aRGB >> 16) & 0xFF;
                    int aG = (aRGB >> 8) & 0xFF;
                    int aB = (aRGB >> 0) & 0xFF;
                    System.out.println("x : " + x + " y : " + y + " color(r,g,b) expected : " + eR + "," + eG + "," + eB + " color(r,g,b) actual : " + aR + "," + aG + "," + aB);
                    //*/
                    ct++;
                    result.setRGB(x, y, new java.awt.Color(255, 0, 0).getRGB());
                }   
                else {
                    result.setRGB(x, y, new java.awt.Color(255, 255, 255).getRGB());
                }
            }
        }
        System.out.println("Pixels différents : " +/* iActual.getProperty(name) +*/ ct + "/" + tot);
        return result;
    }
    
    private static boolean diffColor(int eRGB, int aRGB) {
        int delta = 0;
        int eR = (eRGB >> 16) & 0xFF;
        int eG = (eRGB >> 8) & 0xFF;
        int eB = (eRGB >> 0) & 0xFF;
        int aR = (aRGB >> 16) & 0xFF;
        int aG = (aRGB >> 8) & 0xFF;
        int aB = (aRGB >> 0) & 0xFF;
        if((eR > (aR+delta)) || eR < ((aR-delta))
                || (eG > (aG+delta)) || eG < ((aG-delta))
                || (eB > (aB+delta)) || eB < ((aB-delta)))
            return false;
        return true;
    }
}