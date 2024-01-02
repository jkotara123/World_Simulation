package agh.ics.oop.model.maps;

import agh.ics.oop.EnergyParameters;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Grass;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.enums.MapDirection;
import agh.ics.oop.model.observers.MapChangeListener;
import agh.ics.oop.model.util.RandomPositionsGenerator;

import java.util.*;

import static java.lang.Math.min;
public abstract class AbstractWorldMap implements WorldMap {
    private final List<MapChangeListener> observers = new ArrayList<>();

    protected final Map<Vector2d, List<Animal>> animalsAlive = new HashMap<>();
    protected final Map<Vector2d, Grass> grasses = new HashMap<>();

    protected final List<Vector2d> emptyPlacesOnEquator;
    protected final List<Vector2d> emptyPlacesNotOnEquator;

    protected final Boundary mapBorders;
    protected final Boundary equator;

    protected final EnergyParameters energyParameters;
    protected final RandomPositionsGenerator randomPositionsGenerator = new RandomPositionsGenerator();

    public AbstractWorldMap(Boundary mapBorders, EnergyParameters energyParameters) {
        this.mapBorders = mapBorders;
        this.energyParameters = energyParameters;

        int height = mapBorders.upperRight().y()-mapBorders.lowerLeft().y()+1;
        int lowEq = (height%2==0) ? height/2-1 - height/5-1 : height/2 - height/5-1;
        int highEq = height/2 + height/5-1;
        this.equator = new Boundary(new Vector2d(0,lowEq),new Vector2d(mapBorders.upperRight().x(),highEq));

        this.emptyPlacesOnEquator = this.equator.allPositions();
        List<Vector2d> belowEq = new Boundary(mapBorders.lowerLeft(),new Vector2d(equator.upperRight().x(), equator.lowerLeft().y()-1)).allPositions();
        List<Vector2d> aboveEq = new Boundary(new Vector2d(equator.lowerLeft().x(),equator.upperRight().y()+1),mapBorders.upperRight()).allPositions();
        emptyPlacesNotOnEquator = new ArrayList<>(belowEq);
        emptyPlacesNotOnEquator.addAll(aboveEq);

    }
  
    public Boundary getMapBorders(){
        return mapBorders;
    }
  
    public Map<Vector2d,Grass> getGrasses(){
        return grasses;
    }
  
      @Override
    public List<WorldElement> getElements() {
        List<WorldElement> elements = new ArrayList<>(grasses.values());
        for(List<Animal> animalList : animalsAlive.values()){
            elements.addAll(animalList);
        }
        return elements;
    }

    public EnergyParameters getEnergyParameters() {
        return energyParameters;
    }

    public void addObserver(MapChangeListener observer){
        observers.add(observer);
    }

    public void removeObserver(MapChangeListener observer){
        observers.remove(observer);
    }

    private void mapChanged(String message){
        for(MapChangeListener observer: observers){
            observer.mapChanged(this,message);
        }
    }
  
    @Override
    public void placeAnimal(Animal animal){
        animalsAlive.computeIfAbsent(animal.getPosition(),position->new ArrayList<>());
        animalsAlive.get(animal.getPosition()).add(animal);
    }
    @Override
    public void removeAnimal(Animal animal) {
        animalsAlive.get(animal.getPosition()).remove(animal);
    }
    @Override
    public void placeGrass(Grass grass) {
        grasses.put(grass.getPosition(),grass);
        if(grass.isOnEquator(equator)) emptyPlacesOnEquator.remove(grass.getPosition());
        else emptyPlacesNotOnEquator.remove(grass.getPosition());
    }
    @Override
    public void removeGrass(Grass grass){
        if(grass.isOnEquator(equator)) emptyPlacesOnEquator.add(grass.getPosition());
        else emptyPlacesNotOnEquator.add(grass.getPosition());
        grasses.remove(grass.getPosition());
    }

    @Override
    public void move(Animal animal) {
        Vector2d oldPosition = animal.getPosition();
        MapDirection oldOrientation = animal.getOrientation();

        this.removeAnimal(animal);
        animal.move(this);
        this.placeAnimal(animal);

        if (!animal.getPosition().equals(oldPosition)){
            mapChanged("Animal moved from " + oldPosition + " to " + animal.getPosition());
        }
        if (!animal.getOrientation().equals(oldOrientation)){
            mapChanged("Animal on position " + animal.getPosition() +
                    " changed orientation from " + oldOrientation + " to " + animal.getOrientation());
        }
    }

    @Override
    public List<Animal> animalsAt(Vector2d position) {
        animalsAlive.computeIfAbsent(position,k->new ArrayList<>());
        return animalsAlive.get(position);
    }
    @Override
    public List<Animal> kWinners(Vector2d position, int k){
        return animalsAt(position).stream()
                .sorted()
                .limit(k)
                .toList();
    }


    public void eatGrass(Animal animal) {
        if (grasses.containsKey(animal.getPosition())){
            kWinners(animal.getPosition(),1).get(0).changeEnergy(energyParameters.energyFromEating());
            removeGrass(grasses.get(animal.getPosition()));
        }
    }

    @Override
    public int countGrass() {
        return grasses.size();
    }

    @Override
    public List<Vector2d> emptyPositions() {
        List<Vector2d> places = new ArrayList<>(emptyPlacesOnEquator);
        places.addAll(emptyPlacesNotOnEquator);
        return places;
    }

    @Override
    public void growGrass(int grassAmount){
        int maxGrass = min(grassAmount,emptyPlacesNotOnEquator.size()+emptyPlacesOnEquator.size());
        Random rn = new Random();
        int newEquatorGrassAmount = 0;
        int newNoEquatorGrassAmount = 0;
        for(int i = 0;i<maxGrass;i++){
            if (rn.nextInt(5)<4){
                newEquatorGrassAmount+=1;
            }
            else newNoEquatorGrassAmount+=1;
        }
        if (emptyPlacesNotOnEquator.size()<newNoEquatorGrassAmount){
            newEquatorGrassAmount+=newNoEquatorGrassAmount-emptyPlacesNotOnEquator.size();
            newNoEquatorGrassAmount = emptyPlacesNotOnEquator.size();
        }
        else if (emptyPlacesOnEquator.size()<newEquatorGrassAmount){
            newNoEquatorGrassAmount += newEquatorGrassAmount-emptyPlacesOnEquator.size();
            newEquatorGrassAmount = emptyPlacesOnEquator.size();
        }
        for (Vector2d position: randomPositionsGenerator.kPositionsNoRepetition(emptyPlacesOnEquator,newEquatorGrassAmount)){
            placeGrass(new Grass(position));
        }
        for (Vector2d position: randomPositionsGenerator.kPositionsNoRepetition(emptyPlacesNotOnEquator,newNoEquatorGrassAmount)){
            placeGrass(new Grass(position));
        }
    }

    public boolean canMove(Animal animal){
        Vector2d newPosition = animal.getPosition().add(animal.getOrientation().toUnitVector());
        return (newPosition.precedes(mapBorders.upperRight()) && newPosition.follows(mapBorders.lowerLeft()));
    }
    public abstract Vector2d nextPosition(Animal animal);

    @Override
    public WorldElement objectAt(Vector2d position) {
        return this.animalsAlive.get(position).get(0);
    }
}
