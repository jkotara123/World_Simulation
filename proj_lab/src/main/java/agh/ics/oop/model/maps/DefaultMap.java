package agh.ics.oop.model.maps;

import agh.ics.oop.EnergyParameters;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Animal;

public class DefaultMap extends AbstractWorldMap implements WorldMap {
    public DefaultMap(Boundary mapBorders, EnergyParameters energyParameters) {
        super(mapBorders, energyParameters);
    }
    public boolean leavesMap(Animal animal) { // zmieniłam że ogolnie sprawdza czy bedzie na mapie a nie tylko na osi y
        Vector2d newPosition = animal.getPosition().add(animal.getOrientation().toUnitVector());
        return true;
    }
    @Override
    public void moveVariant(Animal animal) {
//        if super.leavesMap(animal){
//            animal.turnOpposite();
//        }
        animal.move();
        animal.changeEnergy(-energyParameters.energyToMove());
        if (animal.getPosition().y()>mapBorders.upperRight().y() || animal.getPosition().y()<mapBorders.lowerLeft().y()){

            animal.moveTo(animal.getPosition().add((animal.getOrientation().toUnitVector())));
        }
        else if (animal.getPosition().x()>mapBorders.upperRight().x()){
            animal.moveTo(new Vector2d(mapBorders.lowerLeft().x(),animal.getPosition().y()));
        }
        else if (animal.getPosition().x()<mapBorders.lowerLeft().x()){
            animal.moveTo(new Vector2d(mapBorders.upperRight().x(),animal.getPosition().y()));
        }
    }
}
