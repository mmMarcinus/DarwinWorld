package agh.ics.darwinworld.Presenter.MapStatistics;

import java.io.PrintWriter;
import java.util.UUID;

public class StatisticsToFile {

    public static void setHeader(PrintWriter writer){
        writer.println("Map ID, Day number, Animals number, Plants number, Empty Tile, Most Popular Genome, Most Popular Genome Appearance, " +
                       "Second Most Popular Genome, Second Most Popular Genome Appearance, Third Most Popular Genome, Third Popular Genome Appearance, " +
                       "Average Energy Level, Average Life Length, Average Kids Number");

    }

    public static void fillStatisticsDay(PrintWriter writer, UUID mapID, MapStatistics mapStatistics) {
        writer.print(mapID + ", " + mapStatistics.getDayCount() + ", " + mapStatistics.getAnimalNumber() + ", " + mapStatistics.getPlantNumber() + ", " +
                       mapStatistics.getEmptyTile() + ", ");
        if (mapStatistics.getFirstPopularGenomeCount() != 0) {
            writer.print(mapStatistics.getFirstPopularGenome() + ", " + mapStatistics.getFirstPopularGenomeCount() + ", ");
        }
        else{
            writer.print("most popular genome not found, 0, ");
        }
        if (mapStatistics.getSecondPopularGenomeCount() != 0) {
            writer.print(mapStatistics.getSecondPopularGenome() + ", " + mapStatistics.getSecondPopularGenomeCount() + ", ");
        }
        else{
            writer.print("second most popular genome not found, 0, ");
        }
        if (mapStatistics.getThirdPopularGenomeCount() != 0) {
            writer.print(mapStatistics.getThirdPopularGenome() + ", " + mapStatistics.getThirdPopularGenomeCount() + ", ");
        }
        else{
            writer.print("third most popular genome not found, 0, ");
        }
        writer.println(mapStatistics.getAverageEnergyLevel() + ", " + mapStatistics.getAverageLifeLength() + ", " + mapStatistics.getAverageKidsNumber());
    }
}
