package agh.ics.oop.model.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DefaultGenome extends AbstractGenome{
    public DefaultGenome(int length){
        super(length);
    }
    public DefaultGenome(Genome genome1, Genome genome2, int energy1, int energy2){
        super(genome1, genome2, energy1, energy2);
    }
    @Override
    public void mutate() {
        for(int gen : this.chooseToMutate()){
            this.getGenome().set(gen, rd.nextInt(8));
        }
    }
}
