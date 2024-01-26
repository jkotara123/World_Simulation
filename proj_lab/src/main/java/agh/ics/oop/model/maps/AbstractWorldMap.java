package agh.ics.oop.model.maps;

import agh.ics.oop.parameters.EnergyParameters;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Grass;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.util.RandomPositionsGenerator;

import java.util.*;

import static java.lang.Math.min;
import static java.lang.Math.round;

public abstract class AbstractWorldMap implements WorldMap {

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

        int height = mapBorders.upperRight().y() - mapBorders.lowerLeft().y() + 1;
        int lowEq = (height % 2 == 0) ? height / 2 - 1 - round((float) height / 10) + 1 : height / 2 - round((float) height / 10) + 1;
        int highEq = height / 2 + round((float) height / 10) - 1;
        this.equator = new Boundary(new Vector2d(0, lowEq), new Vector2d(mapBorders.upperRight().x(), highEq));

        this.emptyPlacesOnEquator = new ArrayList<>();
        emptyPlacesOnEquator.addAll(equator.allPositions());
        List<Vector2d> belowEq = new Boundary(mapBorders.lowerLeft(), new Vector2d(equator.upperRight().x(), equator.lowerLeft().y() - 1)).allPositions();
        List<Vector2d> aboveEq = new Boundary(new Vector2d(equator.lowerLeft().x(), equator.upperRight().y() + 1), mapBorders.upperRight()).allPositions();
        emptyPlacesNotOnEquator = new ArrayList<>(belowEq);
        emptyPlacesNotOnEquator.addAll(aboveEq);

    }

    public Boundary getMapBorders() {
        return mapBorders;
    }

    public Boundary getEquator() {
        return this.equator;
    }

    public Map<Vector2d, Grass> getGrasses() {
        return grasses; // dehermetyzacja
    }


    @Override
    public List<WorldElement> getElements() {
        List<WorldElement> elements = new ArrayList<>();
        for (Vector2d position : mapBorders.allPositions()) {
            if (!animalsAt(position).isEmpty()) elements.addAll(kWinners(position, 1));
            else if (grasses.containsKey(position)) elements.add(grasses.get(position));
        }
        return elements;
    }

    public EnergyParameters getEnergyParameters() {
        return energyParameters;
    }


    @Override
    public void placeAnimal(Animal animal) {
        animalsAlive.computeIfAbsent(animal.getPosition(), position -> new ArrayList<>());
        animalsAlive.get(animal.getPosition()).add(animal);
    }


    @Override
    public void removeAnimal(Animal animal) {
        animalsAlive.get(animal.getPosition()).remove(animal);
    }

    @Override
    public void placeGrass(Grass grass) {
        grasses.put(grass.getPosition(), grass);
        if (grass.isOnEquator(equator)) emptyPlacesOnEquator.remove(grass.getPosition());
        else emptyPlacesNotOnEquator.remove(grass.getPosition());
    }

    @Override
    public void removeGrass(Grass grass) {
        if (grass.isOnEquator(equator)) emptyPlacesOnEquator.add(grass.getPosition());
        else emptyPlacesNotOnEquator.add(grass.getPosition());
        grasses.remove(grass.getPosition());
    }

    @Override
    public void move(Animal animal) {

        this.removeAnimal(animal);
        animal.move(this);
        this.placeAnimal(animal);
    }

    @Override
    public List<Animal> animalsAt(Vector2d position) {
        animalsAlive.computeIfAbsent(position, k -> new ArrayList<>());
        return animalsAlive.get(position);
    }

    @Override
    public List<Animal> kWinners(Vector2d position, int k) {
        return animalsAt(position).stream()
                .sorted(Collections.reverseOrder())
                .limit(k)
                .toList();

    }


    public void eatGrass(Grass grass) {
        if (!animalsAt(grass.getPosition()).isEmpty()) {
            kWinners(grass.getPosition(), 1).get(0).eat(energyParameters.energyFromEating());
            removeGrass(grass);
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
    public void growGrass(int grassAmount) {
        int maxGrass = min(grassAmount, emptyPlacesNotOnEquator.size() + emptyPlacesOnEquator.size());
        Random rn = new Random(); // co wywołanie?
        int newEquatorGrassAmount = 0;
        int newNoEquatorGrassAmount = 0;
        for (int i = 0; i < maxGrass; i++) {
            if (rn.nextInt(5) < 4) {
                newEquatorGrassAmount += 1;
            } else newNoEquatorGrassAmount += 1;
        }
        if (emptyPlacesNotOnEquator.size() < newNoEquatorGrassAmount) {
            newEquatorGrassAmount += newNoEquatorGrassAmount - emptyPlacesNotOnEquator.size();
            newNoEquatorGrassAmount = emptyPlacesNotOnEquator.size();
        } else if (emptyPlacesOnEquator.size() < newEquatorGrassAmount) {
            newNoEquatorGrassAmount += newEquatorGrassAmount - emptyPlacesOnEquator.size();
            newEquatorGrassAmount = emptyPlacesOnEquator.size();
        }
        for (Vector2d position : randomPositionsGenerator.kPositionsNoRepetition(emptyPlacesOnEquator, newEquatorGrassAmount)) {
            placeGrass(new Grass(position));
        }
        for (Vector2d position : randomPositionsGenerator.kPositionsNoRepetition(emptyPlacesNotOnEquator, newNoEquatorGrassAmount)) {
            placeGrass(new Grass(position));
        }
    }

    public boolean canMove(Animal animal) {
        Vector2d newPosition = animal.getPosition().add(animal.getOrientation().toUnitVector());
        return (newPosition.precedes(mapBorders.upperRight()) && newPosition.follows(mapBorders.lowerLeft()));
    }

    public abstract Vector2d nextPosition(Animal animal);

    @Override
    public WorldElement objectAt(Vector2d position) {
        return this.animalsAlive.get(position).get(0);
    }
}
