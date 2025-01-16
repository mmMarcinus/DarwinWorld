package agh.ics.darwinworld.ApplicationModel;

import agh.ics.darwinworld.View.AnimalView;
import agh.ics.darwinworld.View.PlantView;
import agh.ics.darwinworld.WorldModel.Abstracts.MapChangeListener;
import agh.ics.darwinworld.WorldModel.NormalWorldMap;
import agh.ics.darwinworld.WorldModel.Abstracts.WorldMap;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class SimulationPresenter implements MapChangeListener {

    private NormalWorldMap normalWorldMap;

    @FXML
    private GridPane mapGrid;
    @FXML
    private GridPane worldGrid;

    public void setWorldMap(NormalWorldMap normalWorldMap) {
        this.normalWorldMap = normalWorldMap;
    }

    @FXML
    public void initialize(){
        normalWorldMap = new NormalWorldMap(10,10);
        setWorldMap(normalWorldMap);
        drawMap();
    }


    public void drawMap(){
        mapGrid.getChildren().clear();
//        mapGrid.add(new AnimalView(),0,0);
//        mapGrid.add(new AnimalView(),1,0);
//        mapGrid.add(new AnimalView(),0,1);
//        mapGrid.add(new PlantView(), 10, 10);
//        mapGrid.add(new PlantView(), 1, 1);
//        mapGrid.add(new AnimalView(),3,0);

        //tutaj bede roibl
//        //tutaj po kolei rysuje wszytkie rzeczy na mapie
//        mapGrid.getChildren().clear(); // Usuwanie starych elementów z siatki
//
//
//        for (int i = 0; i < normalWorldMap.getWidth(); i++) {
//            mapGrid.getColumnConstraints().add(new ColumnConstraints(10)); // szerokość komórki
//        }
//        for (int j = 0; j < normalWorldMap.getHeight(); j++) {
//            mapGrid.getRowConstraints().add(new RowConstraints(10)); // wysokość komórki
//        }
//
//        for (int i = 0; i < normalWorldMap.getWidth(); i++) {
//            for (int j = 0; j < normalWorldMap.getHeight(); j++) {
//                Label cell = new Label(); // Pusta komórka
//                cell.setStyle("-fx-border-color: black; -fx-background-color: white;"); // Styl komórek
//                cell.setPrefSize(10, 10); // Rozmiar komórki
//                mapGrid.add(cell, i, j); // Dodanie komórki na odpowiednie miejsce w siatce
//            }
//        }
//        mapGrid.setGridLinesVisible(true);
    }

    @Override
    public void mapChanged(WorldMap worldMap) {
        //tutaj renderuje mape na nowo
    }

}
