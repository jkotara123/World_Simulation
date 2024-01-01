package agh.ics.oop.model.elements;

import java.util.Random;

public class Variant1Genome extends AbstractGenome{
    public Variant1Genome(int n){
        super(n);
    }
    public Variant1Genome(Genome genome1, Genome genome2, int energy1, int energy2){
        super(genome1, genome2, energy1, energy2);
    }
    @Override
    public void mutate() {
        Random rd = new Random();
        for(int gen : this.chooseToMutate()){
            if(rd.nextInt()%2 == 0) this.getGenome().set(gen,(this.getGenome().get(gen)+7)%8);
            else this.getGenome().set(gen,(this.getGenome().get(gen)+1)%8);
        }
    }
}
