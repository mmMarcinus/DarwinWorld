package agh.ics.darwinworld.View.EmptyTile;


import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class EmptyTileView extends StackPane {

        public EmptyTileView() {
            super();
            setStyle("-fx-background-color: #95bb65; -fx-background-image: url('/images/EmptyTile/EmptyTileImage.png'); -fx-background-size: 100% 100%; -fx-background-color: transparent;");
            setPrefWidth(999);
            setPrefHeight(999);
            setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        }
}
