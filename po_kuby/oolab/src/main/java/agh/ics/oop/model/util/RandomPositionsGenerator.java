package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;

import java.util.*;

public class RandomPositionsGenerator implements Iterable<Vector2d> {
    private final ArrayList<Vector2d> points;

    public RandomPositionsGenerator(int maxWidth, int maxHeight, int grassCount){
        this(maxWidth,maxHeight,grassCount,new Random());
    }
    public RandomPositionsGenerator(int maxWidth, int maxHeight, int grassCount, Random rand){
        ArrayList<Vector2d> allPoints = new ArrayList<>((maxHeight+1)*(maxWidth+1));
        for(int i=0;i<=maxHeight;i++){
            for(int j=0;j<=maxWidth;j++){
                allPoints.add(new Vector2d(i,j));
            }
        }
        if(grassCount>allPoints.size()) grassCount = allPoints.size();
        points =new ArrayList<>(grassCount);
        Collections.shuffle(allPoints,rand);
        for(int i=0;i<grassCount;i++){
            points.add(allPoints.get(i));
        }
    }

    @Override
    public Iterator<Vector2d> iterator() {
        return this.points.iterator();
    }
}
