package agh.ics.oop.model.elements;

import java.util.List;

public interface Genome {
    List<Integer> getGenome();
    int getCurrent();
    void nextIndex();
    List<Integer> chooseToMutate(int minMutations, int maxMutations); // to jest publiczne?
    void mutate(int minMutations, int maxMutations);

}
