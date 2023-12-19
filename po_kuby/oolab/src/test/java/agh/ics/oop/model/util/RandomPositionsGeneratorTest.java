package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.util.RandomPositionsGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RandomPositionsGeneratorTest {
    @Test
    public void TestConstructor(){
        RandomPositionsGenerator points = new RandomPositionsGenerator(5,5,3);
        int counter=0;
        for(Vector2d point : points){
            counter++;
        }
        Assertions.assertEquals(3,counter);


        RandomPositionsGenerator points2 = new RandomPositionsGenerator(2,4,20);
        int counter2=0;
        for(Vector2d point : points2){
            counter2++;
        }
        Assertions.assertEquals(15,counter2);
    }
}
