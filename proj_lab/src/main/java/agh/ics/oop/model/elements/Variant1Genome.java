package agh.ics.oop.model.elements;

import java.util.Random;

public class Variant1Genome extends AbstractGenome{
    public Variant1Genome(int n){
        super(n);
    }
    public Variant1Genome(Genome genome1, Genome genome2, int energy1, int energy2, int minMutations, int maxMutations){
        super(genome1, genome2, energy1, energy2,minMutations,maxMutations);
    }
    @Override
    public void mutate(int minMutations, int maxMutations) {
        Random rd = new Random();
        for(int gen : this.chooseToMutate(minMutations,maxMutations)){
            if(rd.nextInt()%2 == 0) this.getGenome().set(gen,(this.getGenome().get(gen)+7)%8);
            else this.getGenome().set(gen,(this.getGenome().get(gen)+1)%8);
        }
    }
}
