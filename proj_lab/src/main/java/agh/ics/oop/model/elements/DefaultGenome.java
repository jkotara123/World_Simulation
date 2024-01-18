package agh.ics.oop.model.elements;


public class DefaultGenome extends AbstractGenome{
    public DefaultGenome(int length){
        super(length);
    }
    public DefaultGenome(Genome genome1, Genome genome2, int energy1, int energy2,int minMutations, int maxMutations){
        super(genome1, genome2, energy1, energy2,minMutations,maxMutations);
    }
    @Override
    public void mutate(int minMutations, int maxMutations) {
        for(int gen : this.chooseToMutate(minMutations,maxMutations)){
            this.getGenome().set(gen, rd.nextInt(8));
        }
    }
}
