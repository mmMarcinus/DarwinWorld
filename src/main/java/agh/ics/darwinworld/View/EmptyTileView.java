package agh.ics.darwinworld.View;


import javafx.scene.control.Label;

public class EmptyTileView extends Label {
    public EmptyTileView() {
        super();
        setStyle("-fx-background-color: #95bb65; -fx-padding: 10; -fx-pref-width: 80; -fx-pref-height: 80");
        setText("*");
    }
}
