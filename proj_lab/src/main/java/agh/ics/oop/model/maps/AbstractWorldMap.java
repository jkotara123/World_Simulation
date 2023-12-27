package agh.ics.oop.model.maps;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.enums.MapDirection;
import agh.ics.oop.model.exceptions.IllegalPositionException;
import agh.ics.oop.model.observers.MapChangeListener;
import agh.ics.oop.model.elements.WorldElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractWorldMap implements WorldMap {
    protected final Map<Vector2d, Animal> animals;
    protected MapVisualizer map;
    protected final ArrayList<MapChangeListener> observers;
    protected final int mapID;
    public AbstractWorldMap(int mapID){
        this.animals=new HashMap<>();
        this.map = new MapVisualizer(this);
        this.observers = new ArrayList<>();
        this.mapID=mapID;
    }

    public int getMapID(){
        return this.mapID;
    }
    public void addObserver(MapChangeListener observer){
        observers.add(observer);
    }
    public void removeObserver(MapChangeListener observer){
        observers.remove(observer);
    }
    private void emitMessage(String message){
        for(MapChangeListener observer : observers){
            observer.mapChanged(this,message);
        }
    }
    public void move(Animal animal){
        if(objectAt(animal.getPosition())==animal){
            Vector2d oldPosition = animal.getPosition();
            MapDirection oldOrientation = animal.getOrientation();
            this.animals.remove(animal.getPosition());
            animal.move(this);
            this.animals.put(animal.getPosition(),animal);

            emitMessage("Animal on position " + animal.getPosition() +
                        " changed orientation from " + oldOrientation + " to " + animal.getOrientation());

            if (!animal.getPosition().equals(oldPosition)){
                emitMessage("Animal moved from " + oldPosition + " to " + animal.getPosition());
            }
        }
    }
    public void place(Animal animal) throws IllegalPositionException{
        if(canMoveTo(animal.getPosition())){
            animals.put(animal.getPosition(),animal);
            emitMessage("Zwierzak dodany na pozycje "+animal.getPosition());
        }
        else{
            throw new IllegalPositionException(animal.getPosition());
        }
    }
    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position);
    }
    public WorldElement objectAt(Vector2d position) {
        return animals.get(position);
    }
    public boolean canMoveTo(Vector2d position) {
        return !isOccupied(position);
    }
    @Override
    public ArrayList<WorldElement> getElements() {
        return new ArrayList<>(animals.values());
    }
    @Override
    public abstract Boundary getCurrentBounds();
    @Override
    public String toString() {
        Boundary bounds = getCurrentBounds();
        return map.draw(bounds.lowerLeft(), bounds.upperRight());
    }
}