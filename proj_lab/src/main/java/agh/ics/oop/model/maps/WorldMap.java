package agh.ics.oop.model.maps;

import agh.ics.oop.EnergyParameters;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Grass;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.observers.MapChangeListener;

import java.util.List;
import java.util.Map;

public interface WorldMap extends MoveValidator {
    Boundary getMapBorders();
    Map<Vector2d,Grass> getGrasses();
    List<WorldElement> getElements(); //zwraca liste zwierzat i roslin (przydatne w rysowaniu bedzie (chyba) )
    EnergyParameters getEnergyParameters();

    void addObserver(MapChangeListener observer);
    void removeObserver(MapChangeListener observer);

    void placeGrass(Grass grass); // dodaje trawke na swoim miejscu
    void removeGrass(Grass grass); //usuwa trawke
    void placeAnimal(Animal animal); // dodaje zwierzaka na swoim miejscu
    void removeAnimal(Animal animal); //usuwa zwierzaka
    void move(Animal animal);   // rusza zwierze ogolnie

    List<Animal> animalsAt(Vector2d position); // zwraca liste zwierzakow na pozycji (moze byc kilka zwierzat)
    List<Animal> kWinners(Vector2d position, int k); // zwraca liste k-najsilniejszych zwierzakow
    void eatGrass(Grass grass); // wyznacza zwierzaka ktory zjada trawe, usuwa trawe,
                                     // zwierzak ma wlasna funkcje do zjadania
    int countGrass();    // liczba aktualnie zyjacych trawek
    List<Vector2d> emptyPositions();    // lista aktualnie pustych pozycji
    void growGrass(int grassAmount); //kladzie na mapie grassAmount traw

    Object objectAt(Vector2d currentPosition);
}
