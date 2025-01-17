package agh.ics.darwinworld.View;
import javafx.scene.control.Label;
public class PlantView extends Label {
    public PlantView() {
        super();
        setStyle("-fx-background-color: #008f00; -fx-padding: 10; -fx-pref-width: 80; -fx-pref-height: 80");
        setText("Plant");
    }
}