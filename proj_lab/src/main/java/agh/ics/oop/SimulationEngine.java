package agh.ics.oop;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {
    private final ArrayList<Simulation> simulationList;
    private final List<Thread> threads;
    ExecutorService executorService;
    public SimulationEngine(ArrayList<Simulation> simulationList, int n){
        this.executorService = Executors.newFixedThreadPool(n);
        this.simulationList=simulationList;
        this.threads = new ArrayList<>();
        for (Simulation simulation : simulationList) {
            threads.add(new Thread(simulation));
        }
    }
    public void runSync(){
        for( Simulation simulation : simulationList) {
            simulation.run();
        }
    }
    public void runAsync() throws InterruptedException {
        for (Thread thread : threads){
            thread.start();
        }
    }
    public void awaitSimulationEnds() throws InterruptedException {
        for (Thread thread : threads){
            thread.join();
        }
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.SECONDS);
    }
    public void runAsyncInThreadPool() throws InterruptedException {
        for (Simulation simulation : simulationList){
            executorService.submit(simulation);
        }
    }
}
