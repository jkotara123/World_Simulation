package agh.ics.oop.presenter;

import agh.ics.oop.EnergyParameters;
import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationParameters;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;


public class ConfigurationPresenter {
    public TextField heightTextField;
    public TextField widthTextField;
    public Label errorMessage;
    public TextField startingGrassAmountTextField;
    public TextField startingAnimalAmountTextField;
    public TextField genomeLengthTextField;
    public TextField dailyGrassGrowthTextField;
    public TextField minChildrenMutationsTextField;
    public TextField maxChildrenMutationsTextField;
    public TextField energyFromEatingTextField;
    public TextField energyToReproduceTextField;
    public TextField energyToMoveTextField;
    public TextField startingEnergyTextField;
    public TextField energyToFullTextField;

    private Simulation simulation;

    private void setSimulation(Simulation simulation){
        this.simulation = simulation;
    }

    private void startSimulation() throws IOException {
        Stage secondaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = loader.load();

        SimulationPresenter presenter = loader.getController();
        presenter.setSimulation(simulation);

        var scene = new Scene(viewRoot);
        secondaryStage.setScene(scene);
        secondaryStage.setTitle("Simulation app");
        secondaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        secondaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());

        secondaryStage.show();
        presenter.runSimulation();
    }

    public void onConfirmButtonClicked() {
        int width = Integer.parseInt(widthTextField.getText().split(" ")[0]);
        int height = Integer.parseInt(heightTextField.getText().split(" ")[0]);
        int startingGrassAmount = Integer.parseInt(startingGrassAmountTextField.getText().split(" ")[0]);
        int startingAnimalAmount = Integer.parseInt(startingAnimalAmountTextField.getText().split(" ")[0]);
        int genomeLength = Integer.parseInt(genomeLengthTextField.getText().split(" ")[0]);
        int dailyGrassGrowth = Integer.parseInt(dailyGrassGrowthTextField.getText().split(" ")[0]);
        int minChildrenMutations = Integer.parseInt(minChildrenMutationsTextField.getText().split(" ")[0]);
        int maxChildrenMutations = Integer.parseInt(maxChildrenMutationsTextField.getText().split(" ")[0]);
        int energyFromEating = Integer.parseInt(energyFromEatingTextField.getText().split(" ")[0]);
        int energyToReproduce = Integer.parseInt(energyToReproduceTextField.getText().split(" ")[0]);
        int energyToMove = Integer.parseInt(energyToMoveTextField.getText().split(" ")[0]);
        int startingEnergy = Integer.parseInt(startingEnergyTextField.getText().split(" ")[0]);
        int energyToFull = Integer.parseInt(energyToFullTextField.getText().split(" ")[0]);

        SimulationParameters simulationParameters;
        try {
            EnergyParameters energyParameters = new EnergyParameters(energyFromEating, energyToReproduce, energyToMove, startingEnergy, energyToFull);
            simulationParameters = new SimulationParameters(width, height,
                    1, 0,
                    startingGrassAmount, startingAnimalAmount, genomeLength,
                    dailyGrassGrowth, minChildrenMutations, maxChildrenMutations, energyParameters);
        }catch (IllegalArgumentException e){
            errorMessage.setText(e.getMessage());
            throw new RuntimeException(e);
        }

        try {
            Simulation simulation = new Simulation(simulationParameters);
            this.setSimulation(simulation);
        }catch(FileNotFoundException e){
            throw new RuntimeException(e);
        }
        try {
            startSimulation();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
