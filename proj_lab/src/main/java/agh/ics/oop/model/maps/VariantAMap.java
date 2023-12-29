package agh.ics.oop.model.maps;

import agh.ics.oop.EnergyParameters;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Animal;

public class VariantAMap extends AbstractWorldMap implements WorldMap {

    public VariantAMap(Boundary mapBorders, EnergyParameters energyParameters) {
        super(mapBorders, energyParameters);
    }
    public boolean leavesMap(Animal animal) { // zmieniłam że ogolnie sprawdza czy bedzie na mapie a nie tylko na osi y
        Vector2d newPosition = animal.getPosition().add(animal.getOrientation().toUnitVector());
        return (newPosition.precedes(mapBorders.upperRight()) && newPosition.follows(mapBorders.lowerLeft()));
    }
    @Override
    public void moveVariant(Animal animal) {
        animal.move();
        if (!(animal.getPosition().precedes(mapBorders.upperRight()) && animal.getPosition().follows(mapBorders.lowerLeft()))){
            Vector2d newPosition = randomPositionsGenerator.kPositionsWithRepetition(mapBorders.allPositions(),1).get(0);
            animal.moveTo(newPosition);
        }

    }
}
