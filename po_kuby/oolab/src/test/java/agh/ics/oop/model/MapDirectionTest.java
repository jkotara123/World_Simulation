package agh.ics.oop.model;
import org.junit.jupiter.api.Test;
import static agh.ics.oop.model.enums.MapDirection.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapDirectionTest {
    @Test
    public void TestNext(){
        assertEquals(EAST, NORTH.next() );
        assertEquals(SOUTH, EAST.next() );
        assertEquals(WEST, SOUTH.next() );
        assertEquals(NORTH, WEST.next() );
    }
    @Test
    public void TestPrevious(){
        assertEquals(EAST, SOUTH.previous() );
        assertEquals(SOUTH, WEST.previous() );
        assertEquals(WEST, NORTH.previous() );
        assertEquals(NORTH, EAST.previous() );
    }
}
