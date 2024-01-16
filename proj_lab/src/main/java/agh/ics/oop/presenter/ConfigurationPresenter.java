package agh.ics.oop.presenter;

import agh.ics.oop.EnergyParameters;
import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationParameters;
import javafx.collections.ObservableArray;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
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
import java.util.stream.Collectors;


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
    public TextField nameField;
    public CheckBox configSave;

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

    private SimulationParameters setParameters(List<String> parameters){
        if (parameters.size()<16){
            throw new IllegalArgumentException("All of the parameters have to be chosen");
        }

        int width = Integer.parseInt(parameters.get(1));
        int height = Integer.parseInt(parameters.get(2));
        String mapVariant = parameters.get(3);
        String mutationVariant = parameters.get(4);
        int startingGrassAmount = Integer.parseInt(parameters.get(5));
        int startingAnimalAmount = Integer.parseInt(parameters.get(6));
        int genomeLength = Integer.parseInt(parameters.get(7));
        int dailyGrassGrowth = Integer.parseInt(parameters.get(8));
        int minChildrenMutations = Integer.parseInt(parameters.get(9));
        int maxChildrenMutations = Integer.parseInt(parameters.get(10));
        int energyFromEating = Integer.parseInt(parameters.get(11));
        int energyToReproduce = Integer.parseInt(parameters.get(12));
        int energyToMove = Integer.parseInt(parameters.get(13));
        int startingEnergy = Integer.parseInt(parameters.get(14));
        int energyToFull = Integer.parseInt(parameters.get(15));

        SimulationParameters simulationParameters;
        try {
            EnergyParameters energyParameters = new EnergyParameters(energyFromEating, energyToReproduce, energyToMove, startingEnergy, energyToFull);
            simulationParameters = new SimulationParameters(width, height,
                    mapVariant, mutationVariant,
                    startingGrassAmount, startingAnimalAmount, genomeLength,
                    dailyGrassGrowth, minChildrenMutations, maxChildrenMutations, energyParameters);
        } catch (IllegalArgumentException e) {
            errorMessage.setText(e.getMessage());
            throw new RuntimeException(e);
        }
        return simulationParameters;
    }
    private List<List<String>> getRecords(){
        List<List<String>> records = new ArrayList<>();
        File file = new File(getClass().getClassLoader().getResource("config_sets.csv").getPath());
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                records.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return records;
    }

    private void addRecord(List<String> parameters) throws FileNotFoundException {

        File file = new File(getClass().getClassLoader().getResource("config_sets.csv").getPath());
        try (PrintWriter pw = new PrintWriter(file)) {
            pw.println(String.join(",", parameters));
        }
    }

    public void onConfirmButtonClicked() {
        List<String> parameters = new ArrayList<>();
        String option = parameters_sets.getValue();
        if (Objects.equals(option, "My configuration")){

            parameters.add(nameField.getText());
            parameters.add(widthTextField.getText().split(" ")[0]);
            parameters.add(heightTextField.getText().split(" ")[0]);
            parameters.add(mapVariantComboBox.getValue());
            parameters.add(mapMutationComboBox.getValue());
            parameters.add(startingGrassAmountTextField.getText().split(" ")[0]);
            parameters.add(startingAnimalAmountTextField.getText().split(" ")[0]);
            parameters.add(genomeLengthTextField.getText().split(" ")[0]);
            parameters.add(dailyGrassGrowthTextField.getText().split(" ")[0]);
            parameters.add(minChildrenMutationsTextField.getText().split(" ")[0]);
            parameters.add(maxChildrenMutationsTextField.getText().split(" ")[0]);
            parameters.add(energyFromEatingTextField.getText().split(" ")[0]);
            parameters.add(energyToReproduceTextField.getText().split(" ")[0]);
            parameters.add(energyToMoveTextField.getText().split(" ")[0]);
            parameters.add(startingEnergyTextField.getText().split(" ")[0]);
            parameters.add(energyToFullTextField.getText().split(" ")[0]);
            System.out.println(parameters);
            if (configSave.isSelected()){
                List<List<String>> records = getRecords();
                for (List<String> row : records) {
                    if (Objects.equals(row.get(0), parameters.get(0))) {
                        throw new IllegalArgumentException("There is already a configuration with that name");
                    }
                }
                try {
                    addRecord(parameters);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        else {
            List<List<String>> records = getRecords();


            for (List<String> row : records) {
                if (Objects.equals(row.get(0), option)) {
                    parameters.addAll(row);
                    break;
                }
            }
        }

        SimulationParameters simulationParameters = setParameters(parameters);




        try {
            Simulation simulation = new Simulation(simulationParameters);
            this.setSimulation(simulation);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            startSimulation();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
