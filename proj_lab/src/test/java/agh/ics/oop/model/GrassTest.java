package agh.ics.oop.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GrassTest {
    @Test
    public void TestToString(){
        Grass grass = new Grass(new Vector2d(2,2));
        Assertions.assertEquals("*",grass.toString());
    }
    @Test
    public void TestGetPosition(){
        Vector2d v1 = new Vector2d(2,3);
        Vector2d v2 = new Vector2d(-2,0);
        Grass g1 = new Grass(v1);
        Grass g2 = new Grass(v2);

        Assertions.assertEquals(v1,g1.getPosition());
        Assertions.assertEquals(v2,g2.getPosition());
        Assertions.assertNotEquals(v1,g2.getPosition());
        Assertions.assertNotEquals(v2,g1.getPosition());
    }
    @Test
    public void TestIsAt(){
        Vector2d v1 = new Vector2d(2,3);
        Vector2d v2 = new Vector2d(-2,0);
        Grass g1 = new Grass(v1);
        Grass g2 = new Grass(v2);
        Assertions.assertTrue(g1.isAt(v1));
        Assertions.assertTrue(g2.isAt(v2));
        Assertions.assertFalse(g1.isAt(v2));
        Assertions.assertFalse(g2.isAt(v1));
    }
}
