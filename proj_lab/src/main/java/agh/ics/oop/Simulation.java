package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Genome;
import agh.ics.oop.model.elements.Grass;
import agh.ics.oop.model.exceptions.IllegalPositionException;
import agh.ics.oop.model.maps.DefaultMap;
import agh.ics.oop.model.maps.VariantAMap;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.model.maps.WorldMap2;
import agh.ics.oop.model.util.RandomPositionsGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Simulation implements Runnable{
    private final ArrayList<Animal> animalList = new ArrayList<>(0);
    private final WorldMap2 map;
    private final SimulationParameters simulationParameters;
    public Simulation(SimulationParameters simulationParameters){
        this.simulationParameters = simulationParameters;
        if (simulationParameters.mapVariant()==1){ //wybor wariantu mapy
            this.map = new VariantAMap(simulationParameters.mapBoundary(),simulationParameters.energyParameters());
        }
        else{
            this.map = new DefaultMap(simulationParameters.mapBoundary(),simulationParameters.energyParameters());
        }
        RandomPositionsGenerator randomPositionsGenerator = new RandomPositionsGenerator();
        ArrayList<Vector2d> animalPositions = randomPositionsGenerator.kPositionsWithRepetition
                (simulationParameters.mapBoundary().allPositions(), simulationParameters.startingAnimalAmount());


        for(Vector2d position: animalPositions) { //rozkladanie zwierzakow na mapie
            Animal animal = new Animal(new Genome(simulationParameters.genomeLength()),
                                        position,
                                        simulationParameters.energyParameters().startingEnergy());
            this.map.placeAnimal(animal);
            animalList.add(animal);
        }

        ArrayList<Vector2d> grassPositions = randomPositionsGenerator.kPositionsNoRepetition
                (simulationParameters.mapBoundary().allPositions(), simulationParameters.startingGrassAmount());

        for(Vector2d position: grassPositions) { //rozkladanie trawy na mapie
            Grass grass = new Grass(position);
            this.map.placeGrass(grass);
        }
    }
    public Animal getAnimal(int i) {
        assert i>=0 && i<animalList.size();
        return animalList.get(i);
    }
    @Override
    public void run(){
        //usuniecie martwych zwierzakow

        //ruszanie sie zwierzakow

        //jedzenie roslin

        //rozmnazanie sie najedzonych zwierzakow

        //nowe rosliny
        for(int i=0;i < 10;i++){ // na razie daje tu losowa liczbe jako liczbe wykonan
            for (Animal animal: animalList){
                this.map.move(animal);
            }
            try {
                Thread.sleep(700); // to jest niepewne
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
