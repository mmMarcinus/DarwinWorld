package agh.ics.darwinworld.Model.WorldModel.Abstracts;

import agh.ics.darwinworld.Model.SimulationModel.Simulation;
import agh.ics.darwinworld.Presenter.MapStatistics.MapStatistics;

public interface MapChangeListener {
    void mapChanged(MapStatistics mapStatistics);
}
