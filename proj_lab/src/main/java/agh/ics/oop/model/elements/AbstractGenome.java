package agh.ics.oop.model.elements;

import java.util.*;

public abstract class AbstractGenome implements Genome {
    protected List<Integer> genome = new ArrayList<>();
    private int index;
    protected Random rd = new Random(); // static


    public AbstractGenome(int length) {
        for (int i = 0; i < length; i++) {
            this.genome.add(rd.nextInt(8));
        }
        this.index = rd.nextInt(length);
    }

    public AbstractGenome(Genome genome1, Genome genome2, int energy1, int energy2, int minMutations, int maxMutations) {
        int n = genome1.getGenome().size();
        int biggerPart = n * energy1 / (energy1 + energy2);
        int smallerPart = n - biggerPart;
        ArrayList<Integer> newGenomeList = new ArrayList<>();
        if (rd.nextInt(2) == 1) {
            for (int i = 0; i < biggerPart; i++) newGenomeList.add(genome1.getGenome().get(i));
            for (int i = biggerPart; i < n; i++) newGenomeList.add(genome2.getGenome().get(i));
        } else {
            for (int i = 0; i < smallerPart; i++) newGenomeList.add(genome2.getGenome().get(i));
            for (int i = smallerPart; i < n; i++) newGenomeList.add(genome1.getGenome().get(i));
        }
        System.out.println();
        this.genome = newGenomeList;
        this.index = rd.nextInt(n);
        this.mutate(minMutations, maxMutations); // poziomy abstrakcji
    }

    public List<Integer> getGenome() {
        return genome; // dehermetyzacja
    }


    public int getCurrent() {
        return genome.get(index);
    }

    public void nextIndex() {
        this.index = (this.index + 1) % genome.size();
    }

    @Override
    public List<Integer> chooseToMutate(int minMutations, int maxMutations) {
        int n = this.getGenome().size();
        ArrayList<Integer> permutation = new ArrayList<>();
        for (int i = 0; i < n; i++) permutation.add(i);
        Collections.shuffle(permutation);
        return permutation.subList(0, rd.nextInt(minMutations, maxMutations + 1));
    }

    public abstract void mutate(int minMutations, int maxMutations);

    @Override
    public String toString() {
        return genome.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Genome)) return false;
        return this.genome.equals(((Genome) other).getGenome());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.genome);
    }
}
