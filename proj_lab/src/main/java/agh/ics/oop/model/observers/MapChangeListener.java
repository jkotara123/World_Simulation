package agh.ics.oop.model.observers;

import agh.ics.oop.Simulation;
import agh.ics.oop.model.maps.WorldMap;

public interface MapChangeListener {
    void mapChanged(Simulation simulation, String message);
}
