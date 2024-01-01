package agh.ics.oop.model.elements;

import java.util.List;

public interface Genome {
    public List<Integer> getGenome();
    public int getCurrent();
    public void nextIndex();
    public List<Integer> chooseToMutate();
    public abstract void mutate();

}
