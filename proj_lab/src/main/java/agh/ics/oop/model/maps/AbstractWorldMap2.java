package agh.ics.oop.model.maps;

import agh.ics.oop.EnergyParameters;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Genome;
import agh.ics.oop.model.elements.Grass;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.enums.MapDirection;
import agh.ics.oop.model.exceptions.IllegalPositionException;
import agh.ics.oop.model.util.RandomPositionsGenerator;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractWorldMap2 implements WorldMap2 {

    protected final Map<Vector2d, List<Animal>> animalsAlive = new HashMap<>();

    protected final Map<Vector2d, Grass> grasses = new HashMap<>();
    protected final Set<Vector2d> emptyPlacesOnEquator = new HashSet<>();
    protected final Set<Vector2d> emptyPlacesNotOnEquator = new HashSet<>();
    protected final Boundary mapBorders;
    protected final Boundary equator;
    protected final EnergyParameters energyParameters;
    protected final RandomPositionsGenerator randomPositionsGenerator = new RandomPositionsGenerator();

    public AbstractWorldMap2(Boundary mapBorders, EnergyParameters energyParameters) {
        this.mapBorders = mapBorders;
        this.equator = mapBorders; //TRZEBA ZROBIC
        this.energyParameters = energyParameters;
    }

    @Override
    public void placeAnimal(Animal animal){
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
        this.removeAnimal(animal);
        this.moveVariant(animal);
        this.placeAnimal(animal);
    }


    public abstract void moveVariant(Animal animal);

    @Override
    public List<Animal> animalsAt(Vector2d position) {
        return animalsAlive.get(position);
    }
    @Override
    public List<Animal> kWinners(Vector2d position, int k){
        return animalsAt(position).stream()
                .sorted()
                .limit(k)
                .toList();
    }

    @Override
    public boolean isGrassAt(Vector2d position) {
        return grasses.get(position) != null;
    }

    @Override
    public void eatGrass(Grass grass) {
        if (!animalsAt(grass.getPosition()).isEmpty()){
            kWinners(grass.getPosition(),1).get(0).changeEnergy(energyParameters.energyFromEating());
            removeGrass(grass);
        }
    }

    @Override
    public void reproduce(Vector2d position) {
        if(animalsAt(position).size()>=2){
            List<Animal> parents = kWinners(position,2);
            if(parents.get(1).getEnergy()>=energyParameters.energyToFull()){
                // to wciaz trzeba dokodzic
                parents.get(0).changeEnergy(-energyParameters.energyToReproduce());
                parents.get(1).changeEnergy(-energyParameters.energyToReproduce());
            }
        }
    }

    @Override
    public List<WorldElement> getElements() {
        List<WorldElement> elements = new ArrayList<>(grasses.values());
        for(List<Animal> animalList : animalsAlive.values()){
            elements.addAll(animalList);
        }
        return elements;
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

}
