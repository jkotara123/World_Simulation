package agh.ics.oop.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Vector2dTest {
    @Test
    public void TestEquals(){
        Vector2d vector = new Vector2d(2,3);

        Vector2d equalVector = new Vector2d(2,3);
        Vector2d diffVector1 = new Vector2d(1,3);
        Vector2d diffVector2 = new Vector2d(2,2);
        int notVector1 = 4;
        String notVector2 = "string";

        assertEquals(vector,vector);
        assertEquals(vector, equalVector);
        assertNotEquals(vector, diffVector1);
        assertNotEquals(vector, diffVector2);
        assertNotEquals(vector, notVector1);
        assertNotEquals(vector, notVector2);
    }
    @Test
    public void TestToString(){
        Vector2d v1 = new Vector2d(3,5);
        Vector2d v2 = new Vector2d(62,-700);

        String v1String = "(3,5)";
        String v2String = "(62,-700)";

        assertEquals(v1String,v1.toString());
        assertEquals(v2String,v2.toString());
    }
    @Test
    public void TestPrecedes(){
        Vector2d v1 = new Vector2d(1,1);
        Vector2d v2 = new Vector2d(2,2);
        Vector2d v3 = new Vector2d(2,3);

        assertTrue(v1.precedes(v1));
        assertTrue(v2.precedes(v2));
        assertTrue(v3.precedes(v3));

        assertFalse(v2.precedes(v1));
        assertFalse(v3.precedes(v2));
        assertFalse(v3.precedes(v2));

        assertTrue(v1.precedes(v2));
        assertTrue(v1.precedes(v3));
        assertTrue(v2.precedes(v3));
    }
    @Test
    public void TestFollows(){
        Vector2d v1 = new Vector2d(1,1);
        Vector2d v2 = new Vector2d(2,2);
        Vector2d v3 = new Vector2d(2,3);

        assertTrue(v1.follows(v1));
        assertTrue(v2.follows(v2));
        assertTrue(v3.follows(v3));

        assertTrue(v2.follows(v1));
        assertTrue(v3.follows(v2));
        assertTrue(v3.follows(v2));

        assertFalse(v1.follows(v2));
        assertFalse(v1.follows(v3));
        assertFalse(v2.follows(v3));
    }
    @Test
    public void TestUpperRight(){
        Vector2d vec1 = new Vector2d(2,4);
        Vector2d vec2 = new Vector2d(4,2);
        Vector2d vec3 = new Vector2d(4,5);

        Vector2d vec1x2 = new Vector2d(4,4);
        Vector2d vec1x3 = new Vector2d(4,5);
        Vector2d vec2x3 = new Vector2d(4,5);

        assertEquals(vec1x2,vec1.upperRight(vec2));
        assertEquals(vec1x3,vec1.upperRight(vec3));
        assertEquals(vec2x3,vec2.upperRight(vec3));
    }
    @Test
    public void TestLowerLeft(){
        Vector2d vec1 = new Vector2d(2,4);
        Vector2d vec2 = new Vector2d(4,2);
        Vector2d vec3 = new Vector2d(4,5);

        Vector2d vec1x2 = new Vector2d(2,2);
        Vector2d vec1x3 = new Vector2d(2,4);
        Vector2d vec2x3 = new Vector2d(4,2);

        assertEquals(vec1x2,vec1.lowerLeft(vec2));
        assertEquals(vec1x3,vec1.lowerLeft(vec3));
        assertEquals(vec2x3,vec2.lowerLeft(vec3));
    }
    @Test
    public void TestAdd(){
        Vector2d v1 = new Vector2d(1,3);
        Vector2d v2 = new Vector2d(-1,5);
        Vector2d v3 = new Vector2d(15,-7);

        Vector2d vec1x2 = new Vector2d(0,8);
        Vector2d vec1x3 = new Vector2d(16,-4);
        Vector2d vec2x3 = new Vector2d(14,-2);
        Vector2d vec1x1 = new Vector2d(2,6);

        assertEquals(vec1x2,v1.add(v2));
        assertEquals(vec1x3,v1.add(v3));
        assertEquals(vec2x3,v2.add(v3));
        assertEquals(vec1x1,v1.add(v1));
    }
    @Test
    public void TestSubtract(){
        Vector2d v1 = new Vector2d(1,3);
        Vector2d v2 = new Vector2d(-1,5);
        Vector2d v3 = new Vector2d(15,-7);

        Vector2d vec1x2 = new Vector2d(2,-2);
        Vector2d vec1x3 = new Vector2d(-14,10);
        Vector2d vec2x3 = new Vector2d(-16,12);
        Vector2d vec1x1 = new Vector2d(0,0);

        assertEquals(vec1x2,v1.subtract(v2));
        assertEquals(vec1x3,v1.subtract(v3));
        assertEquals(vec2x3,v2.subtract(v3));
        assertEquals(vec1x1,v1.subtract(v1));
    }
    @Test
    public void TestOpposite(){
        Vector2d vec1 = new Vector2d(2,3);
        Vector2d vec2 = new Vector2d(0,-3);

        Vector2d oppVec1 = new Vector2d(-2,-3);
        Vector2d oppVec2 = new Vector2d(0,3);

        assertEquals(oppVec1,vec1.opposite());
        assertEquals(oppVec2,vec2.opposite());
    }
}
