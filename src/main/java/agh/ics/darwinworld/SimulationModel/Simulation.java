package agh.ics.darwinworld.SimulationModel;

import agh.ics.darwinworld.Util.Vector2d;
import agh.ics.darwinworld.WorldModel.WorldMap;

import java.util.List;

public class Simulation {
    private List<Vector2d> positions;
    private List<Animal> animals;
    private WorldMap worldMap;

    public Simulation(List<Vector2d> positions, WorldMap worldMap) {
        this.positions = positions;
        this.worldMap = worldMap;
    }
}
