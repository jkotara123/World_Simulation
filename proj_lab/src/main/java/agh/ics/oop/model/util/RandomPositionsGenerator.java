package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.maps.Boundary;

import java.util.*;

public class RandomPositionsGenerator{

    private final Random rand;
    public RandomPositionsGenerator(){
        this(new Random());
    }
    public RandomPositionsGenerator(Random rand){
        this.rand = rand;
    }

    public ArrayList<Vector2d> kPositionsNoRepetition(List<Vector2d> positions, int positionAmount){
        if(positionAmount>positions.size()) positionAmount = positions.size();
        ArrayList<Vector2d> points = new ArrayList<>(positionAmount);
        Collections.shuffle(positions,rand);
        for(int i=0;i<positionAmount;i++){
            points.add(positions.get(i));
        }
        return points;
    }

    public ArrayList<Vector2d> kPositionsWithRepetition(List<Vector2d> positions, int positionAmount){
        ArrayList<Vector2d> points = new ArrayList<>(positionAmount);
        for(int i=0;i<positionAmount;i++){
            points.add(positions.get(rand.nextInt(positions.size())));
        }
        return points;
    }

}
