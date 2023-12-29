package agh.ics.oop.model.maps;

import agh.ics.oop.EnergyParameters;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Animal;

public class DefaultMap extends AbstractWorldMap implements WorldMap {
    public DefaultMap(Boundary mapBorders, EnergyParameters energyParameters) {
        super(mapBorders, energyParameters);
    }
    public boolean leavesMap(Animal animal) {
        Vector2d newPosition = animal.getPosition().add(animal.getOrientation().toUnitVector());
        return newPosition.y() > this.mapBorders.upperRight().y() && newPosition.y() < this.mapBorders.lowerLeft().y();
    }

    @Override
    public Vector2d nextPosition(Animal animal) {
        if(leavesMap(animal)){
            animal.turnAround();
            return animal.getPosition();
        }
        int width = mapBorders.upperRight().x()-mapBorders.lowerLeft().x()+1;
        Vector2d newPosition = animal.getPosition().add(animal.getOrientation().toUnitVector());
        return new Vector2d(newPosition.x()%width, newPosition.y());
    }
}
