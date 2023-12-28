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
    protected final List<Animal> animalsDead = new ArrayList<>();
    protected final Map<Vector2d, Grass> grasses = new HashMap<>();
    protected final Map<Genome,List<Animal>> genomeList = new HashMap<>();
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
        genomeList.get(animal.getGenome()).add(animal);
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
    public boolean leavesMap(Animal animal) { // zmieniłam że ogolnie sprawdza czy bedzie na mapie a nie tylko na osi y
        Vector2d newPosition = animal.getPosition().add(animal.getOrientation().toUnitVector());
        return (newPosition.precedes(mapBorders.upperRight()) && newPosition.follows(mapBorders.lowerLeft()));
    }

    @Override
    public void move(Animal animal) {
//        Vector2d oldPosition = animal.getPosition();
//        Vector2d newPosition = animal.getPosition().add(animal.getOrientation().toUnitVector());
        this.removeAnimal(animal);
        this.moveVariant(animal);
        this.placeAnimal(animal);
//        if(leavesMap(animal)) this.moveVariant(animal);
//        else moveNormally(animal);
    }

    @Override
    public void moveNormally(Animal animal) {
//        animal.move(newPosition); //animal.move() powinien miec przekazywany wektor na ktory sie rusza zwierzak
//        this.placeAnimal(animal);
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
    public void eatGrass(Vector2d position) {
        if (isGrassAt(position) && !animalsAt(position).isEmpty()){
            Grass grass = grasses.get(position);
            kWinners(position,1).get(0).changeEnergy(energyParameters.energyFromEating());
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
    public void removeDeadAnimal(Animal animal) {
        animalsDead.add(animal);
        removeAnimal(animal);
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
    public int countAnimals() {
        return animalsAlive.values().stream().map(List::size).mapToInt(Integer::intValue).sum();
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
    public Genome mostPopularGenome() {
        return null;
    }

    @Override
    public int averageEnergy() {
        int sum = 0;
        for(List<Animal> animalList : animalsAlive.values()){
            sum += animalList.stream().mapToInt(Animal::getEnergy).sum();
        }
        return sum/countAnimals();
    }

    @Override
    public int averageLifeSpan() {
        return animalsDead.stream().mapToInt(Animal::getLifeSpan).sum()/animalsDead.size();
    }
    @Override
    public int averageChildrenNumber() {
        return animalsAlive.values().stream()
                .flatMap(List::stream)
                .mapToInt(animal -> animal.getChildren().size())
                .sum()/animalsAlive.size();
    }
}
