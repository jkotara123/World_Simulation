package agh.ics.oop.model.maps;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Grass;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.enums.MoveDirection;
import agh.ics.oop.model.exceptions.IllegalPositionException;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.util.RandomPositionsGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static java.lang.Math.sqrt;

public class GrassField extends AbstractWorldMap implements WorldMap {
    private final Map<Vector2d, Grass> grasses = new HashMap<>();
    private Boundary worldBounds;
    protected final Boundary grassBounds;

    public GrassField(int n, int mapID){
        this(n, new Random(), mapID);
    }
    public GrassField(int n,Random seed,int mapID){
        super(mapID);
        Vector2d worldUpperRight = new Vector2d(0, 0);
        Vector2d worldLowerLeft = new Vector2d((int)(sqrt(n * 10)),(int) (sqrt(n * 10)));

        RandomPositionsGenerator randomPositionsGenerator = new RandomPositionsGenerator(worldLowerLeft.x(),worldLowerLeft.y(),n,seed);
        for(Vector2d grassPosition : randomPositionsGenerator){
            grasses.put(grassPosition,new Grass(grassPosition));
            worldLowerLeft=worldLowerLeft.lowerLeft(grassPosition);
            worldUpperRight=worldUpperRight.upperRight(grassPosition);
        }
        this.grassBounds=new Boundary(worldLowerLeft,worldUpperRight);
        this.worldBounds=new Boundary(worldLowerLeft,worldUpperRight);
    }
    @Override
    public void place(Animal animal) throws IllegalPositionException {
        super.place(animal);
    }
    @Override
    public void move(Animal animal, MoveDirection direction){
        super.move(animal,direction);
    }
    public void updateCorners(){
        Vector2d worldLowerLeft = grassBounds.lowerLeft();
        Vector2d worldUpperRight = grassBounds.upperRight();
        for(Vector2d vector : animals.keySet()){
            worldLowerLeft=worldLowerLeft.lowerLeft(vector);
            worldUpperRight=worldUpperRight.upperRight(vector);
        }
        worldBounds=new Boundary(worldLowerLeft,worldUpperRight);
    }
    @Override
    public WorldElement objectAt(Vector2d position) {
        if (super.objectAt(position) == null){
            return grasses.get(position);
        }
        return super.objectAt(position);
    }
    @Override
    public ArrayList<WorldElement> getElements() {
        ArrayList<WorldElement> values = new ArrayList<>(super.getElements());
        values.addAll(grasses.values());
        return values;
    }
    @Override
    public Boundary getCurrentBounds() {
        updateCorners();
        return worldBounds;
    }
}
