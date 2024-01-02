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


    public TextField heightTextField;
    public TextField widthTextField;
    public Label errorMessage;
    public VBox configBottom;
    public HBox configTop;
    public VBox configCenter;
    public VBox simBottom;
    @FXML
    private GridPane mapGrid;
    private WorldMap map;
    private Simulation simulation;

    private void setMap(WorldMap map){
        this.map = map;
    }
    private void setSimulation(Simulation simulation){
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
            System.out.println(message);

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
    public void onSimulationStartClicked(){
        String[] widthString = widthTextField.getText().split(" ");
        int width = Integer.parseInt(widthString[0]);
        String[] heightString = heightTextField.getText().split(" ");
        int height = Integer.parseInt(heightString[0]);

        if(width<=0 || height<=0){
            errorMessage.setText("Nieprawidłowe dane");
        }
        else {
            EnergyParameters energyParameters = new EnergyParameters(10, 5, 1, 20, 15);
            SimulationParameters simulationParameters = new SimulationParameters(width, height,
                    1, 0, 0, 0,
                    5, 1, 3,
                    2, 0, 0, energyParameters);
            configCenter.setVisible(false);
            configTop.setVisible(false);
            configBottom.setVisible(false);
            simBottom.setVisible(true);
            mapGrid.setVisible(true);


            Simulation simulation = new Simulation(simulationParameters);

            this.setSimulation(simulation);
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
}
