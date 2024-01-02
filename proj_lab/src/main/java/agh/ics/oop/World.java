package agh.ics.oop;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Genome;
import agh.ics.oop.model.observers.ConsoleMapDisplay;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class World {
    public static void main(String[] args){
        try {
            EnergyParameters energyParameters = new EnergyParameters(10,5,1,20,15);
            SimulationParameters simulationParameters = new SimulationParameters(10,10,
                    0,0,
                    10,3,3,
                    4,0,0, energyParameters);

            Simulation simulation1 = new Simulation(simulationParameters);
            SimulationEngine simulationEngine = new SimulationEngine(new ArrayList<>(List.of(simulation1)),4);

            simulationEngine.runAsyncInThreadPool();
            simulationEngine.awaitSimulationEnds();
            System.out.print("System zakonczyl dzialanie");
        }
        catch (IllegalArgumentException | InterruptedException ex){
            System.out.println(ex.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
