package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;


import java.text.DecimalFormat;
import javafx.scene.image.Image;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SimulationPresenter implements MapChangeListener {
    @FXML
    private Label animalStatistics;
    @FXML
    private Button animalsWithBestGenome;
    @FXML
    private Button equatorGrass;
    @FXML
    private Button stopButton;
    @FXML
    private Button runButton;
    @FXML
    private VBox animalStatisticsBox;
    @FXML
    private GridPane mapGrid;
    private WorldMap map;
    private Simulation simulation;

    private final ExecutorService executorService = Executors.newFixedThreadPool(4);
    @FXML
    private Label dayCounter;
    @FXML
    private Label simulationStatistics;
    private Animal followedAnimal = null;
    private static final int CELLSIZE = 45;



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
    public void mapChanged(Simulation simulation, String message) {
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
        simulationStatistics.setText("Alive animals count: "+simulation.getAliveAnimalsCount()+"\n"+
                "Grass count: "+simulation.getMap().countGrass()+"\n"+
                "Empty positions:  "+simulation.getMap().emptyPositions().size()+"\n"+
                "Average energy: "+dfSharp.format(simulation.getAverageEnergy())+'\n'+
                "Average children count: "+dfSharp.format(simulation.getAverageChildrenCount())+"\n"+
                "Dead animals count: "+simulation.getDeadAnimalsCount()+"\n"+
                "Average lifespan: "+dfSharp.format(simulation.getAverageLifespan())+"\n"
                +"The most popular genome:\n"+simulation.getMostPopularGenome());
        if(followedAnimal!=null){
            String deathDay = "living :)";
            if(followedAnimal.getEnergy()<=0) deathDay = String.valueOf(followedAnimal.getDeathDay());
            animalStatistics.setText("Lifespan: "+followedAnimal.getLifeSpan()+"\n"+
                    "Energy: "+followedAnimal.getEnergy()+"\n"+
                    "Genome: "+followedAnimal.getGenome()+"\n"+
                    "Genome part: "+followedAnimal.getGenome().getCurrent()+"\n"+
                    "Children count: "+followedAnimal.getChildren().size()+"\n"+
                    "Grass eaten: "+followedAnimal.getGrassEaten()+"\n"+
                    "Decdendants count: "+followedAnimal.countDescendants()+"\n"+
                    "Death day: "+deathDay);
        }
    }
    private void putElements(int height, Boundary bounds){
        for(WorldElement elem : this.map.getElements()){
            int newX= elem.getPosition().x()-bounds.lowerLeft().x() + 1;
            int newY= height - (elem.getPosition().y()-bounds.lowerLeft().y());
            Label label = new Label("");
            Image image = elem.toImage(simulation.getSimulationParameters().energyParameters(),CELLSIZE);
            ImageView img = new ImageView(image);

            if (elem.isAnAnimal()) {
                img.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                        startFollowing((Animal) elem);
                        label.setBackground(new Background(new BackgroundFill(Color.rgb(213,31,31),CornerRadii.EMPTY, Insets.EMPTY)));
                });
            }
            if (followedAnimal != null && followedAnimal.getPosition()==elem.getPosition()) {
                label.setBackground(new Background(new BackgroundFill(Color.rgb(213,31,31), CornerRadii.EMPTY, Insets.EMPTY)));
            }

            label.setPrefHeight(CELLSIZE);
            label.setPrefWidth(CELLSIZE);
            mapGrid.add(label,newX,newY);
            mapGrid.add(img,newX,newY);
            GridPane.setHalignment(img, HPos.CENTER);
            GridPane.setHalignment(label, HPos.CENTER);
        }
    }
    private Label getLabelOnPosition(int x,int y){
        for(Node node:mapGrid.getChildren()){
            if (GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == x && GridPane.getRowIndex(node)==y){
                if (node instanceof Label) return (Label) node;
            }
        }
        return null;
    }
    @FXML
    private void onAnimalsWithBestGenomeClicked(){
        List<Animal> animals = simulation.animalsWithMostPopularGenome();
        Boundary bounds = this.map.getMapBorders();
        int height = bounds.upperRight().y() - bounds.lowerLeft().y()+1;
        for(Animal animal : animals){
            int newX= animal.getPosition().x()-bounds.lowerLeft().x() + 1;
            int newY= height - (animal.getPosition().y()-bounds.lowerLeft().y());
            getLabelOnPosition(newX, newY).setBackground(new Background(new BackgroundFill(Color.CORNFLOWERBLUE,CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }
    @FXML
    private void onEquatorGrassClicked(){
        Boundary bounds = this.map.getMapBorders();
        int height = bounds.upperRight().y() - bounds.lowerLeft().y()+1;
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


    @FXML
    public void stopSimulation(){
        executorService.execute(simulation);
        simulation.stopRunning();
  
        animalsWithBestGenome.setVisible(true);
        equatorGrass.setVisible(true);
        runButton.setVisible(true);
        stopButton.setVisible(false);

    }
    public void runSimulation(){
        simulation.startRunning();
        executorService.submit(simulation);

        animalsWithBestGenome.setVisible(false);
        equatorGrass.setVisible(false);
        stopButton.setVisible(true);
        runButton.setVisible(false);
        }
    @FXML
    private void stopFollowing() {
        followedAnimal = null;
        animalStatisticsBox.setVisible(false);

    }
    private void startFollowing(Animal animal){
        if (!simulation.isRunning()){
            followedAnimal = animal;
            animalStatisticsBox.setVisible(true);
        }
        showStatistics();

    }
}

