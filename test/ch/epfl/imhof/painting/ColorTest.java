package ch.epfl.imhof.painting;

import static org.junit.Assert.*;

import org.junit.*;

public class ColorTest {
    @Test(expected = IllegalArgumentException.class)
    public void negativeRed(){
        Color.rgb(-1,0,0);
    }

    @Test
    public void zeroComponents(){
        Color color = Color.rgb(0,0,0);
        assertEquals(0, color.r(),0);
        assertEquals(0, color.g(),0);
        assertEquals(0, color.b(),0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void tooMuchRed(){
        Color.rgb(4,0,0);
    }
    
    @Test
    public void multipliesTwoColors(){
        Color c = Color.rgb(0.5,0.6,0.7).multiplyColor(Color.rgb(0.1, 0.8, 1));

        assertEquals(0.05, c.r(), 0.1);
        assertEquals(0.48, c.g(), 0.1);
        assertEquals(0.7 , c.b(), 0.1);
    }
    
    @Test
    public void integerColor(){
        Color c = Color.rgb(16777215);

        assertEquals(1,c.r(),0);
        assertEquals(1,c.g(),0);
        assertEquals(1,c.b(),0);
    }
    
    @Test
    public void toAWT(){
        
        java.awt.Color awt = Color.GREEN.toAWTColor();
        java.awt.Color green = java.awt.Color.GREEN;

        assertEquals(green.getRed(), awt.getRed(),0);
        assertEquals(green.getGreen(), awt.getGreen(),0);
        assertEquals(green.getBlue(), awt.getBlue(),0);
    }
}
