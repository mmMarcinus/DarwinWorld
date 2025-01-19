package agh.ics.darwinworld.Presenter.MapStatistics;

public class MapStatistics {
    private int dayCount = 0;

    private int animalNumber = 0;

    private int plantNumber = 0;

    private int emptyTile = 0;

    private String firstPopularGenome = "";

    private int firstPopularGenomeCount = 0;

    private String secondPopularGenome = "";

    private int secondPopularGenomeCount = 0;

    private String thirdPopularGenome = "";

    private int thirdPopularGenomeCount = 0;

    private double averageEnergyLevel = 0.0;

    private double averageLifeLength = 0.0;

    private double averageKidsNumber = 0.0;

    public void setStatistics(int dayCount, int animalNumber, int plantNumber, int emptyTile, String firstPopularGenome, int firstPopularGenomeCount,
                                       String secondPopularGenome, int secondPopularGenomeCount, String thirdPopularGenome, int thirdPopularGenomeCount,
                                       double averageEnergyLevel, double averageLifeLength, double averageKidsNumber) {
        this.animalNumber = animalNumber;
        this.plantNumber = plantNumber;
        this.emptyTile = emptyTile;
        this.firstPopularGenome = firstPopularGenome;
        this.firstPopularGenomeCount = firstPopularGenomeCount;
        this.secondPopularGenome = secondPopularGenome;
        this.secondPopularGenomeCount = secondPopularGenomeCount;
        this.thirdPopularGenome = thirdPopularGenome;
        this.thirdPopularGenomeCount = thirdPopularGenomeCount;
        this.averageEnergyLevel = Math.round(averageEnergyLevel*100) / 100;
        this.averageLifeLength = Math.round(averageLifeLength*100) / 100;
        this.averageKidsNumber = Math.round(averageKidsNumber*100) / 100;
        this.dayCount = dayCount;
    }

    public int getDayCount() {
        return dayCount;
    }

    public int getAnimalNumber() {
        return animalNumber;
    }

    public int getPlantNumber() {
        return plantNumber;
    }

    public int getEmptyTile() {
        return emptyTile;
    }

    public String getFirstPopularGenome() {
        return firstPopularGenome;
    }

    public int getFirstPopularGenomeCount() {
        return firstPopularGenomeCount;
    }

    public String getSecondPopularGenome() {
        return secondPopularGenome;
    }

    public int getSecondPopularGenomeCount() {
        return secondPopularGenomeCount;
    }

    public String getThirdPopularGenome() {
        return thirdPopularGenome;
    }

    public int getThirdPopularGenomeCount() {
        return thirdPopularGenomeCount;
    }

    public double getAverageEnergyLevel() {
        return averageEnergyLevel;
    }

    public double getAverageLifeLength() {
        return averageLifeLength;
    }

    public double getAverageKidsNumber() {
        return averageKidsNumber;
    }
}
