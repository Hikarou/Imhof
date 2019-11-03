package ch.epfl.imhof.painting;

import static org.junit.Assert.*;

import java.awt.BasicStroke;

import org.junit.*;

import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;

public class LineStyleTest {
    final static float WIDTH = (float) 0.4;
    final static Color COLOR = Color.GREEN;
    final static LineCap CAP = LineStyle.LineCap.BUTT;
    final static LineJoin JOIN = LineStyle.LineJoin.BEVEL;
    final static float[] DP = new float[0];
    final static LineStyle LINE = new LineStyle(WIDTH, COLOR, CAP, JOIN, DP);
    
    
    @Test
    public void immutableClass(){
        float width = (float) 0.4;
        Color color = Color.GREEN;
        LineStyle.LineCap cap = LineStyle.LineCap.BUTT;
        LineStyle.LineJoin join = LineStyle.LineJoin.BEVEL;
        float[] dP = new float[0];
        
        LineStyle line = new LineStyle(width, color, cap, join, dP);
        
        cap = LineStyle.LineCap.ROUND;
        join = LineStyle.LineJoin.MITER;
        width = (float) 0.5;
        dP=new float[]{(float) 0.4};
        color=Color.RED;

        assertEquals(0.4, line.width(),0.1 );
        assertEquals(Color.GREEN.r(), line.color().r(),0.1 );
        assertEquals(Color.GREEN.g(), line.color().g(),0.1 );
        assertEquals(Color.GREEN.b(), line.color().b(),0.1 );
        assertEquals(LineStyle.LineCap.BUTT, line.lineCap());
        assertEquals(LineStyle.LineJoin.BEVEL, line.lineJoin());
        assertEquals(0,line.dashingPattern().length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeWidth(){
        new LineStyle((float) -1,COLOR, CAP, JOIN,DP);
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroDashing(){
        new LineStyle(WIDTH,COLOR, CAP, JOIN,new float[]{0});
    }
    
    @Test
    public void withWidth(){
        float n = (float) 0.1;
        LineStyle l = LINE.withWidth(n);
        
        assertEquals(n, l.width(),0);
        assertEquals(COLOR.r(), l.color().r(),0);
        assertEquals(COLOR.g(), l.color().g(),0);
        assertEquals(COLOR.b(), l.color().b(),0);
        assertEquals(CAP, l.lineCap());
        assertEquals(JOIN, l.lineJoin());
        assertEquals(DP.length, l.dashingPattern().length);
    }
    
    @Test
    public void withColor(){
        Color n = Color.BLUE;
        LineStyle l = LINE.withColor(n);
        
        assertEquals(WIDTH, l.width(),0);
        assertEquals(n.r(), l.color().r(),0);
        assertEquals(n.g(), l.color().g(),0);
        assertEquals(n.b(), l.color().b(),0);
        assertEquals(CAP, l.lineCap());
        assertEquals(JOIN, l.lineJoin());
        assertEquals(DP.length, l.dashingPattern().length);
    }
    
    @Test
    public void withCap(){
        LineStyle.LineCap n = LineStyle.LineCap.ROUND;
        LineStyle l = LINE.withLineCap(n);
        
        assertEquals(WIDTH, l.width(),0);
        assertEquals(COLOR.r(), l.color().r(),0);
        assertEquals(COLOR.g(), l.color().g(),0);
        assertEquals(COLOR.b(), l.color().b(),0);
        assertEquals(n, l.lineCap());
        assertEquals(JOIN, l.lineJoin());
        assertEquals(DP.length, l.dashingPattern().length);
    }
    
    @Test
    public void withJoin(){
        LineStyle.LineJoin n = LineStyle.LineJoin.MITER;
        LineStyle l = LINE.withLineJoin(n);
        
        assertEquals(WIDTH, l.width(),0);
        assertEquals(COLOR.r(), l.color().r(),0);
        assertEquals(COLOR.g(), l.color().g(),0);
        assertEquals(COLOR.b(), l.color().b(),0);
        assertEquals(CAP, l.lineCap());
        assertEquals(n, l.lineJoin());
        assertEquals(DP.length, l.dashingPattern().length);
    }
    
    @Test
    public void withDashingPattern(){
        float[] n = new float[]{(float) 0.1};
        LineStyle l = LINE.withDashingPattern(n);
        
        assertEquals(WIDTH, l.width(),0);
        assertEquals(COLOR.r(), l.color().r(),0);
        assertEquals(COLOR.g(), l.color().g(),0);
        assertEquals(COLOR.b(), l.color().b(),0);
        assertEquals(CAP, l.lineCap());
        assertEquals(JOIN, l.lineJoin());
        assertEquals(n.length, l.dashingPattern().length);
    }
    
    @Test
    public void OrdinalReturns() {
        LineStyle l1 = new LineStyle(0.4f, Color.WHITE, LineCap.BUTT, LineJoin.BEVEL, new float[0]);
        BasicStroke s1 = new BasicStroke(0.4f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10f, null, 0);
        
        LineStyle l2 = new LineStyle(0.4f, Color.WHITE, LineCap.ROUND, LineJoin.BEVEL, new float[0]);
        BasicStroke s2 = new BasicStroke(0.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 10f, null, 0);
        
        LineStyle l3 = new LineStyle(0.4f, Color.WHITE, LineCap.SQUARE, LineJoin.BEVEL, new float[0]);
        BasicStroke s3 = new BasicStroke(0.4f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 10f, null, 0);
        
        LineStyle l4 = new LineStyle(0.4f, Color.WHITE, LineCap.BUTT, LineJoin.BEVEL, new float[0]);
        BasicStroke s4 = new BasicStroke(0.4f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10f, null, 0);
        
        LineStyle l5 = new LineStyle(0.4f, Color.WHITE, LineCap.BUTT, LineJoin.MITER, new float[0]);
        BasicStroke s5 = new BasicStroke(0.4f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, null, 0);
        
        LineStyle l6 = new LineStyle(0.4f, Color.WHITE, LineCap.BUTT, LineJoin.ROUND, new float[0]);
        BasicStroke s6 = new BasicStroke(0.4f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10f, null, 0);
        
        assertEquals(l1.lineCap().ordinal(), s1.getEndCap());
        //System.out.println("LineCap.BUTT = " + l1.lineCap().ordinal() + " BasicStroke.BUTT = " + s1.getEndCap());
        
        assertEquals(l2.lineCap().ordinal(), s2.getEndCap());
        //System.out.println("LineCap.ROUND = " + l2.lineCap().ordinal() + " BasicStroke.ROUND = " + s2.getEndCap());
        
        assertEquals(l3.lineCap().ordinal(), s3.getEndCap());
        //System.out.println("LineCap.SQUARE = " + l3.lineCap().ordinal() + " BasicStroke.SQUARE = " + s3.getEndCap());
        
        assertEquals(l4.lineCap().ordinal(), s4.getEndCap());
        //System.out.println("LineJoin.BEVEL = " + l4.lineJoin().ordinal() + " BasicStroke.BEVEL = " + s4.getLineJoin());
        
        assertEquals(l5.lineCap().ordinal(), s5.getEndCap());
        //System.out.println("LineJoin.MITER = " + l5.lineJoin().ordinal() + " BasicStroke.MITER = " + s5.getLineJoin());
        
        assertEquals(l6.lineCap().ordinal(), s6.getEndCap());
        //System.out.println("LineJoin.ROUND = " + l6.lineJoin().ordinal() + " BasicStroke.ROUND = " + s6.getLineJoin());
    }
}
