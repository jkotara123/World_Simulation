package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.enums.MoveDirection;
import agh.ics.oop.model.exceptions.IllegalPositionException;
import agh.ics.oop.model.interfaces.WorldMap;

import java.util.ArrayList;
import java.util.List;


public class Simulation implements Runnable{
    private final List<MoveDirection> moveList;
    private final ArrayList<Animal> animalList = new ArrayList<>(0);
    private final WorldMap map;
    public Simulation(List<MoveDirection> moveList, List<Vector2d> positionList, WorldMap map){
        this.moveList=moveList;
        this.map=map;
        for(Vector2d position : positionList) {
            Animal animal = new Animal(position);
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
        for(int i=0;i < moveList.size();i++){
            this.map.move(this.getAnimal(i%animalList.size()),moveList.get(i));
            try {
                Thread.sleep(700); // to jest niepewne
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
