package agh.ics.oop.model.observers;

import agh.ics.oop.model.maps.WorldMap;

public class ConsoleMapDisplay implements MapChangeListener {
    private int updateCounter=0;
    @Override
    public synchronized void mapChanged(WorldMap worldMap, String message) {
        System.out.println(message);
        System.out.print(worldMap);
        updateCounter++;
        System.out.println("Ilość zmian: " + updateCounter + "\n\n");
    }
}
