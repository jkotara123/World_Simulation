package agh.ics.oop.model.observers;

import agh.ics.oop.Simulation;

public interface MapChangeListener {
    void mapChanged(Simulation simulation, String message);
}
