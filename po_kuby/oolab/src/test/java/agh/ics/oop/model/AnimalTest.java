package agh.ics.oop.model;

import agh.ics.oop.model.enums.MapDirection;
import agh.ics.oop.model.enums.MoveDirection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AnimalTest {

    @Test
    public void TestToString(){
        RectangularMap map = new RectangularMap(0,5,1);
        Animal animalDefault = new Animal();

        Animal animal1 = new Animal(new Vector2d(2,5));
        animal1.move(MoveDirection.LEFT,map);

        Animal animal2 = new Animal(new Vector2d(1,0));
        animal2.move(MoveDirection.LEFT,map);
        animal2.move(MoveDirection.LEFT,map);

        String resDefault = "N";
        String res1 = "W";
        String res2 = "S";

        Assertions.assertEquals(resDefault,animalDefault.toString());
        Assertions.assertEquals(res1,animal1.toString());
        Assertions.assertEquals(res2,animal2.toString());
    }
    @Test
    public void TestIsAt(){
        Animal animalDefault = new Animal();
        Animal animal1 = new Animal(new Vector2d(2,5));
        Animal animal2 = new Animal(new Vector2d(1,0));

        Vector2d position1 = new Vector2d(2,5);
        Vector2d position2 = new Vector2d(1,0);

        Assertions.assertTrue(animalDefault.isAt(Animal.DEFAULT_POSITION));
        Assertions.assertTrue(animal1.isAt(position1));
        Assertions.assertTrue(animal2.isAt(position2));

        Assertions.assertFalse(animalDefault.isAt(position1));
        Assertions.assertFalse(animal1.isAt(position2));
        Assertions.assertFalse(animal2.isAt(Animal.DEFAULT_POSITION));
    }
@Test
    public void TestMove(){
        RectangularMap map1 = new RectangularMap(5,5,1);
        RectangularMap map2 = new RectangularMap(5,5,2);
        RectangularMap map3 = new RectangularMap(5,5,3);
        RectangularMap map4 = new RectangularMap(5,5,4);
        RectangularMap map5 = new RectangularMap(5,5,5);
        Animal animalTurn = new Animal();
        Animal animalCannotGoForward=new Animal(new Vector2d(3,4));
        Animal animalGoForward1=new Animal();
        Animal animalGoForward2=new Animal();
        animalGoForward2.move(MoveDirection.LEFT,map3);
        Animal animalGoBackward1=new Animal();
        Animal animalGoBackward2 = new Animal();
        animalGoBackward2.move(MoveDirection.RIGHT,map5);
        animalTurn.move(MoveDirection.RIGHT,map1);                                   //skrecanie w prawo
        Assertions.assertEquals(animalTurn.getOrientation(), MapDirection.EAST);
        animalTurn.move(MoveDirection.RIGHT,map1);
        Assertions.assertEquals(animalTurn.getOrientation(), MapDirection.SOUTH);
        animalTurn.move(MoveDirection.RIGHT,map1);
        Assertions.assertEquals(animalTurn.getOrientation(), MapDirection.WEST);
        animalTurn.move(MoveDirection.RIGHT,map1);
        Assertions.assertEquals(animalTurn.getOrientation(), MapDirection.NORTH);
        animalTurn.move(MoveDirection.LEFT,map1);                                     //skrecanie w lewo
        Assertions.assertEquals(animalTurn.getOrientation(), MapDirection.WEST);
        animalTurn.move(MoveDirection.LEFT,map1);
        Assertions.assertEquals(animalTurn.getOrientation(), MapDirection.SOUTH);
        animalTurn.move(MoveDirection.LEFT,map1);
        Assertions.assertEquals(animalTurn.getOrientation(), MapDirection.EAST);
        animalTurn.move(MoveDirection.LEFT,map1);
        Assertions.assertEquals(animalTurn.getOrientation(), MapDirection.NORTH);

        animalCannotGoForward.move(MoveDirection.FORWARD,map2);
        Assertions.assertTrue(animalCannotGoForward.isAt(new Vector2d(3,4)));

        animalGoForward1.move(MoveDirection.FORWARD,map3);
        Assertions.assertTrue(animalGoForward1.isAt(new Vector2d(2,3)));

        animalGoForward2.move(MoveDirection.FORWARD,map3);
        Assertions.assertTrue(animalGoForward2.isAt(new Vector2d(1,2)));

        animalGoBackward1.move(MoveDirection.BACKWARD,map4);
        Assertions.assertTrue(animalGoBackward1.isAt(new Vector2d(2,1)));

        animalGoBackward2.move(MoveDirection.BACKWARD,map5);
        Assertions.assertTrue(animalGoBackward2.isAt(new Vector2d(1,2)));
    }
}
