package agh.ics.oop.parameters;

public record EnergyParameters(int energyFromEating, int energyToReproduce, int energyToMove,
                               int startingEnergy, int energyToFull) {
    public EnergyParameters {
        if (energyFromEating<=0){
            throw new IllegalArgumentException("Energy gained from eating must be greater than 0");
        }
        if (energyToReproduce<=0){
            throw new IllegalArgumentException("Energy needed to reproduce must be greater than 0");
        }
        if (energyToMove<=0){
            throw new IllegalArgumentException("Energy needed to move must be greater than 0");
        }
        if (startingEnergy<=0){
            throw new IllegalArgumentException("Starting energy must be greater than 0");
        }
        if (energyToFull<energyToReproduce){
            throw new IllegalArgumentException("Energy needed for an animal to be considered full must be greater than energy it loses during reproduction");
        }
    }
}
