package agh.ics.darwinworld.Records;

public record WorldParameters(int width, int height, boolean polarMap, int startAnimalsNumber, int startEnergyLevel, int energyTakenEachDay,
                              int reproduceEnergyRequired, int genomesLength, int minMutation, int maxMutation, boolean changeGenome,
                              int startPlantNumber, int dayPlantNumber, int energyFromPlant) {
}
