package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.maps.Boundary;
import agh.ics.oop.model.observers.MapChangeListener;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.maps.WorldMap;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class SimulationPresenter implements MapChangeListener {

    public VBox simBottom;
    @FXML
    public Button animalsWithBestGenome;
    @FXML
    public Button equatorGrass;
    @FXML
    private GridPane mapGrid;
    private WorldMap map;
    private Simulation simulation;
    @FXML
    private Label dayCounter;
    @FXML
    public Label simulationStatistics;
    private Animal followedAnimal = null;
    private static int CELLSIZE = 30;

    private static final DecimalFormat dfSharp = new DecimalFormat("#.##");

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
            this.showDayCounter();
            this.showStatistics();
            this.drawMap();
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
        mapGrid.getColumnConstraints().add(new ColumnConstraints(CELLSIZE));
        mapGrid.getRowConstraints().add(new RowConstraints(CELLSIZE));
        for(int i = 0; i < width; i++){
            mapGrid.getColumnConstraints().add(new ColumnConstraints(CELLSIZE));
            label = new Label(String.format("%2d",i+bounds.lowerLeft().x()));
            mapGrid.add(label,i+1, 0);
            GridPane.setHalignment(label, HPos.CENTER);
        }
        for(int i = 0; i < height; i++){
            mapGrid.getRowConstraints().add(new RowConstraints(CELLSIZE));
            label = new Label(String.format("%2d",height+bounds.lowerLeft().y()-i-1));
            mapGrid.add(label,0,i+1);
            GridPane.setHalignment(label, HPos.CENTER);
        }
    }
    private void showDayCounter(){
        dayCounter.setText("Day "+simulation.getDayCounter());
    }
    private void showStatistics(){
        simulationStatistics.setText("Average energy: "+dfSharp.format(simulation.averageEnergy())+'\n'+
                "Average children count: "+dfSharp.format(simulation.averageChildrenNumber())+"\n"+
                "Dead animals count: "+simulation.getDeadAnimalsCount()+"\n"+
                "Average lifespan: "+dfSharp.format(simulation.averageLifeSpan())+"\n"
                +"The most popular genome:\n"+simulation.mostPopularGenome());
    }
    private void putElements(int height, Boundary bounds){
        for(WorldElement elem : this.map.getElements()){
            int newX= elem.getPosition().x()-bounds.lowerLeft().x() + 1;
            int newY= height - (elem.getPosition().y()-bounds.lowerLeft().y());
            Label label = new Label(elem.toString());
            label.setPrefHeight(CELLSIZE);
            label.setPrefWidth(CELLSIZE);
            mapGrid.add(label,newX,newY);
            GridPane.setHalignment(label, HPos.CENTER);
        }
    }
    private Label getLabelOnPosition(int x,int y){
        for(Node node:mapGrid.getChildren()){
            if (GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == x && GridPane.getRowIndex(node)==y){
                return (Label) node;
            }
        }
        return null;
    }
    public void onAnimalsWithBestGenomeClicked(){
        List<Animal> animals = simulation.animalsWithMostPopularGenome();
        Boundary bounds = this.map.getMapBorders();
        int height = bounds.upperRight().y() - bounds.lowerLeft().y()+1;
        for(Animal animal : animals){
            int newX= animal.getPosition().x()-bounds.lowerLeft().x() + 1;
            int newY= height - (animal.getPosition().y()-bounds.lowerLeft().y());
            getLabelOnPosition(newX, newY).setBackground(new Background(new BackgroundFill(Color.CORNFLOWERBLUE,CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }
    public void onEquatorGrassClicked(){
        Boundary bounds = this.map.getMapBorders();
        int height = bounds.upperRight().y() - bounds.lowerLeft().y()+1;
        Boundary equator = map.getEquator();
        for(Vector2d position :  map.getEquator().allPositions()){
            int newX= position.x()-bounds.lowerLeft().x() + 1;
            int newY= height - (position.y()-bounds.lowerLeft().y());
            Label label = getLabelOnPosition(newX, newY);
            if (label != null)  label.setBackground(new Background(new BackgroundFill(Color.GREENYELLOW,CornerRadii.EMPTY, Insets.EMPTY)));
            else {
                label = new Label(" ");
                label.setPrefHeight(CELLSIZE);
                label.setPrefWidth(CELLSIZE);
                label.setBackground(new Background(new BackgroundFill(Color.GREENYELLOW,CornerRadii.EMPTY, Insets.EMPTY)));
                mapGrid.add(label,newX,newY);
                GridPane.setHalignment(label, HPos.CENTER);
            }
        }
    }

    public void onSimulationStopButtonClicked(){
        simulation.stopRunning();
        animalsWithBestGenome.setVisible(true);
        equatorGrass.setVisible(true);
    }
    public void onSimulationResumeButtonClicked(){
        simulation.startRunning();
        animalsWithBestGenome.setVisible(false);
        equatorGrass.setVisible(false);
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

