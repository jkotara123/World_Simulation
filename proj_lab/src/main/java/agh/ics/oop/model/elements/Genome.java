package agh.ics.oop.model.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Genome {
    private List<Integer> genome = new ArrayList<>();
    private int index;


    public Genome(List<Integer> genome, int index){
        this.genome = genome;
        this.index = index; // to ma być losowe
    }

    public Genome(List<Integer> genome){this(genome,0);}

    public Genome(int length){
        Random rn = new Random();
        for(int i = 0;i<length;i++){
            this.genome.add(rn.nextInt()%8);
        }
        this.index = rn.nextInt()%length;
    }

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
