package agh.ics.oop;


import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.DefaultGenome;
import agh.ics.oop.model.elements.Genome;
import agh.ics.oop.model.elements.Variant1Genome;
import agh.ics.oop.model.maps.Boundary;
import agh.ics.oop.model.maps.DefaultMap;
import agh.ics.oop.model.maps.VariantAMap;
import agh.ics.oop.model.maps.WorldMap;

public record SimulationParameters(int width, int height,
                                   int mapVariant, int mutationVariant,
                                   int startingGrassAmount, int startingAnimalAmount, int genomeLength, int dailyGrassGrowth,
                                   int minChildrenMutations, int maxChildrenMutations, EnergyParameters energyParameters) {

    public SimulationParameters {
        if (width<=0 || height<=0){
            throw new IllegalArgumentException("Wrong map dimensions");
        }
        if (startingAnimalAmount < 0){
            throw new IllegalArgumentException("Starting amount of animals must be greater than 0");
        }
        if (startingGrassAmount < 0){
            throw new IllegalArgumentException("Starting amount of grasses must be greater than 0");
        }
        if (genomeLength <=0){
            throw new IllegalArgumentException("Genome length must be greater than 0");
        }
        if (dailyGrassGrowth<0){
            throw new IllegalArgumentException("Daily grass growth amount must be greater or equal to 0");
        }
        if (minChildrenMutations < 0 || maxChildrenMutations < 0 || maxChildrenMutations > genomeLength || maxChildrenMutations < minChildrenMutations){
            throw new IllegalArgumentException("Wrong mutation configurations");
        }
    }

    public WorldMap getMap(){
        Boundary boundary = new Boundary(new Vector2d(0,0), new Vector2d(width()-1, height()-1 ));
        if (mapVariant==1){ //wybor wariantu mapy
            return new VariantAMap(boundary,energyParameters);
        }
        else{
            return new DefaultMap(boundary,energyParameters);
        }
    }

    public Genome getGenome(){
        if (mutationVariant==1){ //wybor wariantu mutacji
            return new Variant1Genome(genomeLength);
        }
        else{
            return new DefaultGenome(genomeLength);
        }
    }


}
