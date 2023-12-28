package agh.ics.oop.model.maps;

import agh.ics.oop.EnergyParameters;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Animal;

public class VariantAMap extends AbstractWorldMap2 implements WorldMap2 {

    public VariantAMap(Boundary mapBorders, EnergyParameters energyParameters) {
        super(mapBorders, energyParameters);
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
