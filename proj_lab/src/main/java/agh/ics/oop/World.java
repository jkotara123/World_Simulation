package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.ConsoleMapDisplay;
import agh.ics.oop.model.util.OptionsParser;
import agh.ics.oop.presenter.SimulationPresenter;

import java.util.ArrayList;
import java.util.List;

public class World {
    public static void main(String[] args){
        try {
            ConsoleMapDisplay observer = new ConsoleMapDisplay();
            SimulationPresenter presenter = new SimulationPresenter();
            String[] args1 = {"f","b","r","l","f","f","r","r","backward","f","left","left","f","f","f","f"};
            ArrayList<Vector2d> positions1 = new ArrayList<>(List.of(new Vector2d(7, 8), new Vector2d(3, 4),new Vector2d(2,8)));
            GrassField map1 = new GrassField(7,1);
            map1.addObserver(observer);
            Simulation simulation1 = new Simulation(OptionsParser.change(args1), positions1, map1);

            SimulationEngine simulationEngine = new SimulationEngine(new ArrayList<>(List.of(simulation1)),4);

            simulationEngine.runAsyncInThreadPool();
            simulationEngine.awaitSimulationEnds();
            System.out.print("System zakonczyl dzialanie");
        }
        catch (IllegalArgumentException | InterruptedException ex){
            System.out.println(ex.getMessage());
        }
    }
}
