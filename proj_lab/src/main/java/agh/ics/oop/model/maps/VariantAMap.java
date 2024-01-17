package agh.ics.oop.model.maps;

import agh.ics.oop.parameters.EnergyParameters;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Animal;

import java.util.Random;

public class VariantAMap extends AbstractWorldMap implements WorldMap {

    public VariantAMap(Boundary mapBorders, EnergyParameters energyParameters) {
        super(mapBorders, energyParameters);
    }
    @Override
    public Vector2d nextPosition(Animal animal) {
        Random rd = new Random();
        return new Vector2d(rd.nextInt(this.mapBorders.upperRight().x()), rd.nextInt(this.mapBorders.upperRight().y()));
    }
}
