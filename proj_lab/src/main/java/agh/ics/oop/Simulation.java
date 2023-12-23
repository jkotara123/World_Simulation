package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Genome;
import agh.ics.oop.model.exceptions.IllegalPositionException;
import agh.ics.oop.model.maps.WorldMap;

import java.util.ArrayList;
import java.util.List;


public class Simulation implements Runnable{
    private final ArrayList<Animal> animalList = new ArrayList<>(0);
    private final WorldMap map;
    public Simulation(List<Genome> genomeList, List<Vector2d> positionList, WorldMap map){
        this.map=map;
        for(int i = 0;i<genomeList.size();i++) {
            Animal animal = new Animal(genomeList.get(i),positionList.get(i));
            try{
                this.map.place(animal);
                animalList.add(animal);
            }
            catch (IllegalPositionException ex){
                System.out.println(ex.getMessage());
            }
        }
    }
    public Animal getAnimal(int i) {
        assert i>=0 && i<animalList.size();
        return animalList.get(i);
    }
    @Override
    public void run(){
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
