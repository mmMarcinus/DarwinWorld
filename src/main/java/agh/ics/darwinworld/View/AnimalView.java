package agh.ics.darwinworld.View;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class AnimalView extends StackPane{
    public AnimalView() {
        super();
        setStyle("-fx-background-color: #95bb65; -fx-background-image: url('/images/Animal.png'); -fx-background-size: 100% 100%;");
        setPrefWidth(999);
        setPrefHeight(999);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    }
}