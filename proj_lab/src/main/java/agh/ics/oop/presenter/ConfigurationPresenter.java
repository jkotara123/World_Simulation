package agh.ics.oop.presenter;

import agh.ics.oop.EnergyParameters;
import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationParameters;
import javafx.collections.ObservableArray;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


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
    public ComboBox<String> mapVariantComboBox;
    public ComboBox<String> mapMutationComboBox;
    public HBox configTop;
    public ComboBox<String> parameters_sets;
    public VBox configCenter;
    public VBox parameters_sets_box;
    public VBox configBottom;

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
        String[] Parameters;

        String mapVariant = mapVariantComboBox.getValue();
        String mutationVariant = mapMutationComboBox.getValue();
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
                    mapVariant, mutationVariant,
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

    public void onConfirmConfigButton() {
        String option = parameters_sets.getValue();
        if (Objects.equals(option, "My configuration")){
            configCenter.setVisible(true);
            configBottom.setVisible(true);
            parameters_sets_box.setVisible(false);
        }
        else {

            List<List<String>> records = new ArrayList<>();
            try (InputStream inputStream = getClass().getResourceAsStream("/config_sets.csv");
                 BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    records.add(Arrays.asList(values));
                    System.out.println("wo");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            for (List<String> row: records){
                System.out.println(row.get(0));
                if(Objects.equals(row.get(0), option)){

                    int width = Integer.parseInt(row.get(1));
                    int height = Integer.parseInt(row.get(2));
                    String mapVariant = row.get(3);
                    String mutationVariant = row.get(4);
                    int startingGrassAmount = Integer.parseInt(row.get(5));
                    int startingAnimalAmount = Integer.parseInt(row.get(6));
                    int genomeLength = Integer.parseInt(row.get(7));
                    int dailyGrassGrowth = Integer.parseInt(row.get(8));
                    int minChildrenMutations = Integer.parseInt(row.get(9));
                    int maxChildrenMutations = Integer.parseInt(row.get(10));
                    int energyFromEating = Integer.parseInt(row.get(11));
                    int energyToReproduce = Integer.parseInt(row.get(12));
                    int energyToMove = Integer.parseInt(row.get(13));
                    int startingEnergy = Integer.parseInt(row.get(14));
                    int energyToFull = Integer.parseInt(row.get(15));

                    SimulationParameters simulationParameters;

                    try {
                        EnergyParameters energyParameters = new EnergyParameters(energyFromEating, energyToReproduce, energyToMove, startingEnergy, energyToFull);
                        simulationParameters = new SimulationParameters(width, height,
                                mapVariant, mutationVariant,
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
        }



    }
}
