package agh.ics.oop.model.util;
import agh.ics.oop.model.enums.MoveDirection;
import java.util.ArrayList;

public class OptionsParser {
    public static ArrayList<MoveDirection> change(String[] args) throws IllegalArgumentException{
        ArrayList<MoveDirection> res = new ArrayList<>(0);
        for(String argument : args){
            switch (argument) {
                case "f", "forward" -> res.add(MoveDirection.FORWARD);
                case "r", "right" -> res.add(MoveDirection.RIGHT);
                case "l", "left" -> res.add(MoveDirection.LEFT);
                case "b", "backward" -> res.add(MoveDirection.BACKWARD);
                default -> throw new IllegalArgumentException(argument + " is not legal move specification");
            }
        }
        return res;
    }
}
