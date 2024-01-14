package agh.ics.oop.presenter;

import agh.ics.oop.EnergyParameters;
import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.SimulationParameters;
import agh.ics.oop.model.maps.Boundary;
import agh.ics.oop.model.observers.MapChangeListener;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.maps.WorldMap;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;


public class SimulationPresenter implements MapChangeListener {

    public VBox simBottom;
    @FXML
    private GridPane mapGrid;
    private WorldMap map;
    private Simulation simulation;

    public void setMap(WorldMap map){
        this.map = map;
    }
    public void setSimulation(Simulation simulation){
        this.simulation = simulation;
    }
    private void drawMap(){
        Boundary bounds = this.map.getMapBorders();
        int width = bounds.upperRight().x() - bounds.lowerLeft().x()+1;
        int height = bounds.upperRight().y() - bounds.lowerLeft().y()+1;

        this.clearGrid();
        createGrid(width, height, bounds);
        putElements(height, bounds);

    }
    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(() -> {
            this.drawMap();
//            System.out.println(message);

        });
    }
    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0)); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }
    private void createGrid(int width, int height, Boundary bounds){
        Label label = new Label(" y\\x ");
        mapGrid.add(label,0,0);
        GridPane.setHalignment(label, HPos.CENTER);
        mapGrid.getColumnConstraints().add(new ColumnConstraints(30));
        mapGrid.getRowConstraints().add(new RowConstraints(30));
        for(int i = 0; i < width; i++){
            mapGrid.getColumnConstraints().add(new ColumnConstraints(30));
            label = new Label(String.format("%2d",i+bounds.lowerLeft().x()));
            mapGrid.add(label,i+1, 0);
            GridPane.setHalignment(label, HPos.CENTER);
        }
        for(int i = 0; i < height; i++){
            mapGrid.getRowConstraints().add(new RowConstraints(30));
            label = new Label(String.format("%2d",height+bounds.lowerLeft().y()-i-1));
            mapGrid.add(label,0,i+1);
            GridPane.setHalignment(label, HPos.CENTER);
        }
    }
    private void putElements(int height, Boundary bounds){
        for(WorldElement elem : this.map.getElements()){
            int newX= elem.getPosition().x()-bounds.lowerLeft().x() + 1;
            int newY= height - (elem.getPosition().y()-bounds.lowerLeft().y());
            Label label = new Label(elem.toString());
            mapGrid.add(label,newX,newY);
            GridPane.setHalignment(label, HPos.CENTER);
        }
    }

    public void onSimulationStopButtonClicked(){
        simulation.stopRunning();
    }
    public void onSimulationResumeButtonClicked(){
        simulation.startRunning();
    }
    public void runSimulation(){
            this.setMap(simulation.getMap());
            map.addObserver(this);

            try {
                SimulationEngine engine = new SimulationEngine(new ArrayList<>(List.of(simulation)), 4);
                engine.runAsync();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

