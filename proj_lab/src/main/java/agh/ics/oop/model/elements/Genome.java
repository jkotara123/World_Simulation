package agh.ics.oop.model.elements;

import java.util.List;

public class Genome {
    private final List<Integer> genome;
    private int index;

    public Genome(List<Integer> genome, int index){
        this.genome = genome;
        this.index = index; // to ma być losowe
    }

    public Genome(List<Integer> genome){this(genome,0);}

    public List<Integer> getGenome() {
        return genome;
    }

    public int getIndex() {
        return index;
    }

    public int getCurrent(){
        return genome.get(index);
    }

    public void nextIndex(){
        this.index = (this.index+1)%genome.size();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
