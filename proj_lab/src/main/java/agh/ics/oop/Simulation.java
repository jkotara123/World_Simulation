package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.elements.*;
import agh.ics.oop.model.maps.*;
import agh.ics.oop.model.observers.ConsoleMapDisplay;
import agh.ics.oop.model.util.RandomPositionsGenerator;
import agh.ics.oop.presenter.SimulationPresenter;
import javafx.fxml.FXMLLoader;

import java.util.*;


public class Simulation implements Runnable{
    private final ArrayList<Animal> animalsAlive = new ArrayList<>(0);
    private final List<Animal> animalsDead = new ArrayList<>();
    private final Map<Genome,List<Animal>> genomeList = new HashMap<>();
    private final WorldMap map;
    private final SimulationParameters simulationParameters;
    private final RandomPositionsGenerator randomPositionsGenerator = new RandomPositionsGenerator();
    private boolean isRunning = false;

    public Simulation(SimulationParameters simulationParameters){
        this.simulationParameters = simulationParameters;
        map = simulationParameters.getMap();


        placeAnimals();

        ArrayList<Vector2d> grassPositions = randomPositionsGenerator.kPositionsNoRepetition
                (map.getMapBorders().allPositions(), simulationParameters.startingGrassAmount());

        for(Vector2d position: grassPositions) { //rozkladanie trawy na mapie
            Grass grass = new Grass(position);
            this.map.placeGrass(grass);
        }
    }

    public void placeAnimals(){
        ArrayList<Vector2d> animalPositions = randomPositionsGenerator.kPositionsWithRepetition
                (map.getMapBorders().allPositions(), simulationParameters.startingAnimalAmount());

        for(Vector2d position: animalPositions){
            Animal animal = new Animal(simulationParameters.getGenome(),
                    position,
                    simulationParameters.energyParameters().startingEnergy());
            this.map.placeAnimal(animal);
            animalsAlive.add(animal);
        }
    }
    public Animal getAnimal(int i) {
        assert i>=0 && i<animalsAlive.size();
        return animalsAlive.get(i);
    }

    public WorldMap getMap() {
        return map;
    }

    public void removeDeadAnimal(Animal animal) {
        this.animalsAlive.remove(animal);
        map.removeAnimal(animal);
        animalsDead.add(animal);
    }
    public void reproduce(Vector2d position){
        if(map.animalsAt(position).size()>=2){
            List<Animal> parents = map.kWinners(position,2);
            if(parents.get(1).getEnergy()>=simulationParameters.energyParameters().energyToFull()){
                Animal child = new Animal(parents.get(0),parents.get(1),simulationParameters.energyParameters().energyToReproduce(),simulationParameters.mutationVariant());
                map.placeAnimal(child);
                animalsAlive.add(child);
                parents.get(0).changeEnergy(-simulationParameters.energyParameters().energyToReproduce());
                parents.get(1).changeEnergy(-simulationParameters.energyParameters().energyToReproduce());
            }
        }
    }

    public Genome mostPopularGenome() {
        return null;
    }

    public int averageEnergy() {
        return animalsAlive.stream().mapToInt(Animal::getEnergy).sum()/animalsAlive.size();
    }


    public int averageLifeSpan() {
        return animalsDead.stream().mapToInt(Animal::getLifeSpan).sum()/animalsDead.size();
    }

    public int averageChildrenNumber() {
        return animalsAlive.stream()
                .mapToInt(animal -> animal.getChildren().size())
                .sum()/animalsAlive.size();
    }
    public void startRunning(){
        this.isRunning = true;
    }
    public void stopRunning(){
        this.isRunning = false;
    }
    @Override
    public void run() {
        startRunning();
        while(true) { // na razie daje tu losowa liczbe jako liczbe wykonan
            //usuniecie martwych zwierzakow
            animalsAlive.removeIf(animal -> animal.getEnergy() <= 0);

            //ruszanie sie zwierzakow
            animalsAlive.forEach(map::move);

            //jedzenie roslin
            animalsAlive.forEach(map::eatGrass);

                //rozmnazanie sie najedzonych zwierzakow
                map.getMapBorders().allPositions().forEach(this::reproduce); // do sprawdzenia

                //nowe rosliny
                map.growGrass(simulationParameters.dailyGrassGrowth());

                try {
                    if (isRunning) {
                        Thread.sleep(700); // to jest niepewne
                    }
                    else{
                        while (!isRunning){
                            Thread.sleep(100);
                        }
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
