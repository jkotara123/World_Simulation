package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Genome;
import agh.ics.oop.model.elements.Grass;
import agh.ics.oop.model.maps.*;
import agh.ics.oop.model.util.RandomPositionsGenerator;

import java.util.*;


public class Simulation implements Runnable{
    private final ArrayList<Animal> animalsAlive = new ArrayList<>(0);
    protected final List<Animal> animalsDead = new ArrayList<>();
    protected final Map<Genome,List<Animal>> genomeList = new HashMap<>();
    private final WorldMap map;
    private final SimulationParameters simulationParameters;
    public Simulation(SimulationParameters simulationParameters){
        Boundary boundary = new Boundary(new Vector2d(0,0), new Vector2d(simulationParameters.width()-1, simulationParameters.height()-1 ));
        this.simulationParameters = simulationParameters;
        if (simulationParameters.mapVariant()==1){ //wybor wariantu mapy
            this.map = new VariantAMap(boundary,simulationParameters.energyParameters());
        }
        else{
            this.map = new DefaultMap(boundary,simulationParameters.energyParameters());
        }
        RandomPositionsGenerator randomPositionsGenerator = new RandomPositionsGenerator();
        ArrayList<Vector2d> animalPositions = randomPositionsGenerator.kPositionsWithRepetition
                (boundary.allPositions(), simulationParameters.startingAnimalAmount());


        for(Vector2d position: animalPositions) { //rozkladanie zwierzakow na mapie
            Animal animal = new Animal(new Genome(simulationParameters.genomeLength()),
                                        position,
                                        simulationParameters.energyParameters().startingEnergy());
            this.map.placeAnimal(animal);
            animalsAlive.add(animal);
        }

        ArrayList<Vector2d> grassPositions = randomPositionsGenerator.kPositionsNoRepetition
                (boundary.allPositions(), simulationParameters.startingGrassAmount());

        for(Vector2d position: grassPositions) { //rozkladanie trawy na mapie
            Grass grass = new Grass(position);
            this.map.placeGrass(grass);
        }
    }
    public Animal getAnimal(int i) {
        assert i>=0 && i<animalsAlive.size();
        return animalsAlive.get(i);
    }

    public void removeDeadAnimal(Animal animal) {
        this.animalsAlive.remove(animal);
        map.removeAnimal(animal);
        animalsDead.add(animal);
    }

    public Genome mostPopularGenome() {
        return null;
    }

    public int averageEnergy() {
        int sum = 0;
        for(List<Animal> animalList : animalsAlive.values()){
            sum += animalList.stream().mapToInt(Animal::getEnergy).sum();
        }
        return sum/countAnimals();
    }


    public int averageLifeSpan() {
        return animalsDead.stream().mapToInt(Animal::getLifeSpan).sum()/animalsDead.size();
    }

    public int averageChildrenNumber() {
        return animalsAlive.values().stream()
                .flatMap(List::stream)
                .mapToInt(animal -> animal.getChildren().size())
                .sum()/animalsAlive.size();
    }
    @Override
    public void run(){
        //usuniecie martwych zwierzakow

        //ruszanie sie zwierzakow

        //jedzenie roslin

        //rozmnazanie sie najedzonych zwierzakow

        //nowe rosliny
        for(int i=0;i < 10;i++){ // na razie daje tu losowa liczbe jako liczbe wykonan
            for (Animal animal: animalsAlive){
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
