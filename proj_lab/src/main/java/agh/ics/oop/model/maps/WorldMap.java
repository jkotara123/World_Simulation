package agh.ics.oop.model.maps;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Grass;
import agh.ics.oop.model.elements.WorldElement;

import java.util.List;
import java.util.Map;

public interface WorldMap {

    void placeGrass(Grass grass); // dodaje trawke na swoim miejscu
    void removeGrass(Grass grass); //usuwa trawke
    void placeAnimal(Animal animal); // dodaje zwierzaka na swoim miejscu
    void removeAnimal(Animal animal); //usuwa zwierzaka
    void move(Animal animal);   // rusza zwierze ogolnie
    void moveVariant(Animal animal);  // rusza zwierzaka przy wyjsciu poza mape w zaleznosci od wybranego wariantu
    List<Animal> animalsAt(Vector2d position); // zwraca liste zwierzakow na pozycji (moze byc kilka zwierzat)
    List<Animal> kWinners(Vector2d position, int k); // zwraca liste k-najsilniejszych zwierzakow
    boolean isGrassAt(Vector2d position); // czy jest trawa
    void eatGrass(Grass grass); // wyznacza zwierzaka ktory zjada trawe, usuwa trawe,
                                     // zwierzak ma wlasna funkcje do zjadania
    void reproduce(Vector2d position);  // wyznacza dwa zwierzaki ktore sie rozmnazaja, dodaje nowego zwierzaka,
                                        // starym zmniejsza energie (funkcja w Genome - createNewGenome, dwa warianty)

    // WYMAGANE PRZEZ PROJEKT

    // DO SYMULACJI
    List<WorldElement> getElements(); //zwraca liste zwierzat i roslin (przydatne w rysowaniu bedzie (chyba) )
    int countGrass();    // liczba aktualnie zyjacych trawek

    List<Vector2d> emptyPositions();    // lista aktualnie pustych pozycji

    Boundary getMapBorders();
    void growGrass(int grassAmount); //kladzie na mapie grassAmount traw

    Map<Vector2d,Grass> getGrasses();


}
