package agh.ics.darwinworld.View;

import javafx.scene.control.Label;

public class EmptyTileView extends Label {
    public EmptyTileView() {
        super();
        setStyle("-fx-background-color: white; -fx-padding: 10; -fx-min-width: 100");
        setText("*");
    }
}
