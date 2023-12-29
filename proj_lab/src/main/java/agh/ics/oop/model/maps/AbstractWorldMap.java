package agh.ics.oop.model.maps;

import agh.ics.oop.EnergyParameters;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Grass;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.util.RandomPositionsGenerator;

import java.util.*;

import static java.lang.Math.min;

public abstract class AbstractWorldMap implements WorldMap {

    protected final Map<Vector2d, List<Animal>> animalsAlive = new HashMap<>();

    protected final Map<Vector2d, Grass> grasses = new HashMap<>();
    protected final List<Vector2d> emptyPlacesOnEquator = new ArrayList<>();
    protected final List<Vector2d> emptyPlacesNotOnEquator = new ArrayList<>();
    protected final Boundary mapBorders;
    protected final Boundary equator;
    protected final EnergyParameters energyParameters;
    protected final RandomPositionsGenerator randomPositionsGenerator = new RandomPositionsGenerator();

    public AbstractWorldMap(Boundary mapBorders, EnergyParameters energyParameters) {
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
    public Boundary getMapBorders(){
        return mapBorders;
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
    public Map<Vector2d,Grass> getGrasses(){
        return grasses;
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

}
