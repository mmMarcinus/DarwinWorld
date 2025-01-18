package agh.ics.darwinworld.View;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class AnimalView extends StackPane{
    public AnimalView() {
        super();
        setStyle("-fx-background-color: #572b0f;");
        setPrefWidth(999);
        setPrefHeight(999);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        getChildren().add(new Label("Animal"));
    }
}