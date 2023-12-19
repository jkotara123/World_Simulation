package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.enums.MapDirection;
import agh.ics.oop.model.enums.MoveDirection;
import agh.ics.oop.model.util.OptionsParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulationTest {
    @Test
    public void TestRun(){
        String[] args1 = {"f","b","r","l","f","f","r","r","f","f","f","f","f","f","f","f"};
        ArrayList<MoveDirection> directions1 = OptionsParser.change(args1);
        ArrayList<Vector2d> positions1 = new ArrayList<>(List.of(new Vector2d(2,2), new Vector2d(3,4)));
        RectangularMap map1 = new RectangularMap(5,5,1);
        Simulation simulation1 = new Simulation(directions1,positions1,map1);

        String[] args2 = {"f","f","b","l","r","r","b","b","r"};
        ArrayList<MoveDirection> directions2 = OptionsParser.change(args2);
        ArrayList<Vector2d> positions2 = new ArrayList<>(List.of(new Vector2d(2,2), new Vector2d(3,4),new Vector2d(4,4)));
        RectangularMap map2 = new RectangularMap(5,5,2);
        Simulation simulation2 = new Simulation(directions2,positions2,map2);

        GrassField map3 = new GrassField(3,new Random(5),3);
        Simulation simulation3 = new Simulation(directions1,positions1,map3);

        GrassField map4 = new GrassField(3,new Random(2),4);
        Simulation simulation4 = new Simulation(directions2,positions2,map4);


        simulation1.run();
        Assertions.assertTrue(simulation1.getAnimal(0).isAt(new Vector2d(2,0)));
        Assertions.assertEquals(simulation1.getAnimal(0).getOrientation(), MapDirection.SOUTH);
        Assertions.assertTrue(simulation1.getAnimal(1).isAt(new Vector2d(3,4)));
        Assertions.assertEquals(simulation1.getAnimal(1).getOrientation(),MapDirection.NORTH);

        simulation2.run();
        Assertions.assertTrue(simulation2.getAnimal(0).isAt(new Vector2d(3,3)));
        Assertions.assertEquals(simulation2.getAnimal(0).getOrientation(),MapDirection.WEST);
        Assertions.assertTrue(simulation2.getAnimal(1).isAt(new Vector2d(2,4)));
        Assertions.assertEquals(simulation2.getAnimal(1).getOrientation(),MapDirection.EAST);
        Assertions.assertTrue(simulation2.getAnimal(2).isAt(new Vector2d(4,3)));
        Assertions.assertEquals(simulation2.getAnimal(2).getOrientation(),MapDirection.SOUTH);

        simulation3.run();
        Assertions.assertTrue(simulation3.getAnimal(0).isAt(new Vector2d(2,-1)));
        Assertions.assertEquals(simulation3.getAnimal(0).getOrientation(), MapDirection.SOUTH);
        Assertions.assertTrue(simulation3.getAnimal(1).isAt(new Vector2d(3,7)));
        Assertions.assertEquals(simulation3.getAnimal(1).getOrientation(),MapDirection.NORTH);
        Assertions.assertTrue(map3.objectAt(new Vector2d(4,5)) instanceof Grass);

        simulation4.run();
        Assertions.assertTrue(simulation4.getAnimal(0).isAt(new Vector2d(3,3)));
        Assertions.assertEquals(simulation4.getAnimal(0).getOrientation(),MapDirection.WEST);
        Assertions.assertTrue(simulation4.getAnimal(1).isAt(new Vector2d(2,5)));
        Assertions.assertEquals(simulation4.getAnimal(1).getOrientation(),MapDirection.EAST);
        Assertions.assertTrue(simulation4.getAnimal(2).isAt(new Vector2d(4,3)));
        Assertions.assertEquals(simulation4.getAnimal(2).getOrientation(),MapDirection.SOUTH);
    }
}
