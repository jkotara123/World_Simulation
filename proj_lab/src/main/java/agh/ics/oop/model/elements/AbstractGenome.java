package agh.ics.oop.model.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class AbstractGenome implements Genome
{
    protected List<Integer> genome = new ArrayList<>();
    private int index;
    protected Random rd = new Random();

    public AbstractGenome(List<Integer> genome, int index){
        this.genome = genome;
        this.index = index; // to ma być losowe
    }

    public AbstractGenome(List<Integer> genome){this(genome,0);}

    public AbstractGenome(int length){
        for(int i = 0;i<length;i++){
            this.genome.add(rd.nextInt(8));
        }
        this.index = rd.nextInt(length);
    }
    public AbstractGenome(Genome genome1, Genome genome2, int energy1, int energy2, int minMutations, int maxMutations){
        int n = genome1.getGenome().size();
        int biggerPart = n*energy1/(energy1+energy2);
        int smallerPart = n-biggerPart;
        ArrayList<Integer> newGenomeList = new ArrayList<>();
        System.out.println(genome1+", "+energy1+" + "+genome2+", "+energy2+" =");
        if (rd.nextInt(2)==1){
            for(int i=0; i<biggerPart; i++) newGenomeList.add(genome1.getGenome().get(i));
            for(int i=biggerPart; i<n; i++) newGenomeList.add(genome2.getGenome().get(i));
        }
        else{
            for(int i=0; i<smallerPart; i++) newGenomeList.add(genome2.getGenome().get(i));
            for(int i=smallerPart; i<n; i++) newGenomeList.add(genome1.getGenome().get(i));
        }
        System.out.println();
        this.genome=newGenomeList;
        this.index=rd.nextInt(n);
        System.out.println("Przed mutacja: "+this);
        this.mutate(minMutations,maxMutations);
        System.out.println("Po mutacji: "+this);
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
    public List<Integer> chooseToMutate(int minMutations, int maxMutations) {
        int n = this.getGenome().size();
        ArrayList<Integer> permutation = new ArrayList<>();
        for(int i=0;i<n;i++) permutation.add(i);
        Collections.shuffle(permutation);
        List<Integer> res = permutation.subList(0,rd.nextInt(minMutations,maxMutations+1));
        System.out.println(res);
        return res;
    }

    public abstract void mutate(int minMutations, int maxMutations);

    @Override
    public String toString() {
        return genome.toString();
    }
}
