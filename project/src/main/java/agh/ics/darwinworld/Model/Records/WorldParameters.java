package agh.ics.darwinworld.Model.Records;

public record WorldParameters(int height, int width, boolean polarMap, int startAnimalsNumber, int startEnergyLevel, int energyTakenEachDay,
                              int reproduceEnergyRequired, int genomesLength, int minMutation, int maxMutation, boolean changeGenome,
                              int startPlantNumber, int dayPlantNumber, int energyFromPlant, boolean exportStatistics) {
}
