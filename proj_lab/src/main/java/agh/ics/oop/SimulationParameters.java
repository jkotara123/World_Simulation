package agh.ics.oop;

import agh.ics.oop.model.maps.Boundary;

public record SimulationParameters(int width, int height,
                                   int mapVariant, int mutationVariant, int grassVariant, int behaviourVariant,
                                   int startingGrassAmount, int startingAnimalAmount, int genomeLength, int dailyGrassGrowth,
                                   int minChildrenMutations, int maxChildrenMutations, EnergyParameters energyParameters) {
}
