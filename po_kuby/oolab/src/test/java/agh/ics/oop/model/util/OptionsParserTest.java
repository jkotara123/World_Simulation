package agh.ics.oop.model.util;
import static org.junit.jupiter.api.Assertions.*;
import agh.ics.oop.model.enums.MoveDirection;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;


public class OptionsParserTest {
    @Test
    public void TestOptionsParser(){
        String[] args1 = {"f","k","b","r","a","l"};
        String[] args2 = {"r","f","l","b","rr","ll"};
        String[] args3 = {"f","b","r","l","forward"};
        String[] args4 = {"r","f","l","b"};

        Throwable exception1 = assertThrows(IllegalArgumentException.class, () -> OptionsParser.change(args1));
        Throwable exception2 = assertThrows(IllegalArgumentException.class, () -> OptionsParser.change(args2));

        assertEquals("k is not legal move specification",exception1.getMessage());
        assertEquals("rr is not legal move specification",exception2.getMessage());

        ArrayList<MoveDirection> resExpected3 = new ArrayList<MoveDirection>(Arrays.asList(MoveDirection.FORWARD,MoveDirection.BACKWARD,MoveDirection.RIGHT,MoveDirection.LEFT,MoveDirection.FORWARD));
        ArrayList<MoveDirection> resExpected4 = new ArrayList<MoveDirection>(Arrays.asList(MoveDirection.RIGHT,MoveDirection.FORWARD,MoveDirection.LEFT,MoveDirection.BACKWARD));

        assertEquals(resExpected3, OptionsParser.change(args3));
        assertEquals(resExpected4,OptionsParser.change(args4));
    }
}
