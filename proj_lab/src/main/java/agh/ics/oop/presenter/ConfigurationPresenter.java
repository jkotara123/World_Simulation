package agh.ics.oop.presenter;

import agh.ics.oop.EnergyParameters;
import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationParameters;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class ConfigurationPresenter {
    @FXML
    private TextField heightTextField;
    @FXML
    private TextField widthTextField;
    @FXML
    private Label errorMessage;
    @FXML
    public TextField startingGrassAmountTextField;
    @FXML
    public TextField startingAnimalAmountTextField;
    @FXML
    public TextField genomeLengthTextField;
    @FXML
    public TextField dailyGrassGrowthTextField;
    @FXML
    public TextField minChildrenMutationsTextField;
    @FXML
    public TextField maxChildrenMutationsTextField;
    @FXML
    public TextField energyFromEatingTextField;
    @FXML
    public TextField energyToReproduceTextField;
    @FXML
    public TextField energyToMoveTextField;
    @FXML
    public TextField startingEnergyTextField;
    @FXML
    public TextField energyToFullTextField;
    @FXML
    public ComboBox<String> mapVariantComboBox;
    @FXML
    public ComboBox<String> mapMutationComboBox;
    @FXML
    public ComboBox<String> parameters_sets;
    @FXML
    public TextField nameField;
    @FXML
    public CheckBox configSave;

    private Simulation simulation;

    private List<List<String>> records;


    public void loadRecords() {
        records = getRecords();
        for(List<String> row: records){
            parameters_sets.getItems().add(row.get(0));
        }
    }

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
        presenter.setMap(simulation.getMap());
        simulation.addObserver(presenter);

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
        File file = new File("config_sets.csv");
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


    private void printRecords() throws FileNotFoundException {

        File file = new File("config_sets.csv");
        try (PrintWriter pw = new PrintWriter(file)) {
            for (List<String> row: records) {
                pw.println(String.join(",", row));
            }
        }
    }

    @FXML
    private void onConfirmButtonClicked() {
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

                for (List<String> row : records) {
                    if (Objects.equals(row.get(0), parameters.get(0))) {
                        throw new IllegalArgumentException("There is already a configuration with that name");
                    }
                }
                records.add(parameters);
            }
        }
        else {
            for (List<String> row : records) {
                if (Objects.equals(row.get(0), option)) {
                    parameters.addAll(row);
                    break;
                }
            }
        }
        try {
            printRecords();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
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
