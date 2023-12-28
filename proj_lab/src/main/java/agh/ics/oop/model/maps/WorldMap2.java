package agh.ics.oop.model.maps;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Genome;
import agh.ics.oop.model.elements.Grass;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.exceptions.IllegalPositionException;

import java.util.List;

public interface WorldMap2 {

    void placeGrass(Grass grass); // dodaje trawke na swoim miejscu
    void removeGrass(Grass grass); //usuwa trawke
    void placeAnimal(Animal animal); // dodaje zwierzaka na swoim miejscu
    void removeAnimal(Animal animal); //usuwa zwierzaka
    boolean leavesMap(Animal animal); //mowi czy zwierzak chce wyjsc poza mape
    void move(Animal animal);   // rusza zwierze ogolnie
    void moveNormally(Animal animal); // rusza normalnie rusza zwierzaka
    void moveVariant(Animal animal);  // rusza zwierzaka przy wyjsciu poza mape w zaleznosci od wybranego wariantu
    List<Animal> animalsAt(Vector2d position); // zwraca liste zwierzakow na pozycji (moze byc kilka zwierzat)
    List<Animal> kWinners(Vector2d position, int k); // zwraca liste k-najsilniejszych zwierzakow
    boolean isGrassAt(Vector2d position); // czy jest trawa
    void eatGrass(Vector2d position); // wyznacza zwierzaka ktory zjada trawe, usuwa trawe,
                                     // zwierzak ma wlasna funkcje do zjadania
    void reproduce(Vector2d position);  // wyznacza dwa zwierzaki ktore sie rozmnazaja, dodaje nowego zwierzaka,
                                        // starym zmniejsza energie (funkcja w Genome - createNewGenome, dwa warianty)
    void removeDeadAnimal(Animal animal); // usuwa zwierzaka z listy zywych, dodaje na liste umarlych,
            // usuwa jego obecnosc z mapy, zwieksza sume lat niezywych i ilosc niezywych (ulatwia liczenie sredniej)

    List<WorldElement> getElements(); //zwraca liste zwierzat i roslin (przydatne w rysowaniu bedzie (chyba) )

    // WYMAGANE PRZEZ PROJEKT

    int countAnimals();  // liczba zwierzat (nwm czy tylko zywych, raczej tak)
    int countGrass();    // liczba aktualnie zyjacych trawek

    List<Vector2d> emptyPositions();    // lista aktualnie pustych pozycji

    Genome mostPopularGenome();     // najpopularniejszy genom

    int averageEnergy();    // aktualna srednia ilosc energii dla zywych zwierzakow

    int averageLifeSpan();  // srednia dlugosc zycia dla aktualnie niezywych zwierzakow

    int averageChildrenNumber();    // srednia ilosc dzieci dla zwierzaka dla zywych i umarlych

}
