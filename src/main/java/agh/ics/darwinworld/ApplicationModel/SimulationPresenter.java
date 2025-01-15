package agh.ics.darwinworld.ApplicationModel;

import agh.ics.darwinworld.WorldModel.Abstracts.MapChangeListener;
import agh.ics.darwinworld.WorldModel.NormalWorldMap;
import agh.ics.darwinworld.WorldModel.Abstracts.WorldMap;

public class SimulationPresenter implements MapChangeListener {
    NormalWorldMap normalWorldMap;

    public void setWorldMap(NormalWorldMap map) {
        this.normalWorldMap = map;
    }

    public void drawMap(){
        //tutaj po kolei rysuje wszytkie rzeczy na mapie
    }

    @Override
    public void mapChanged(WorldMap worldMap) {
        //tutaj renderuje mape na nowo
    }

}
