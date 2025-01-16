package agh.ics.darwinworld.View;

import javafx.scene.control.Label;

public class AnimalView extends Label{
    public AnimalView(){
        super();
        setStyle("-fx-background-color: brown; -fx-padding: 10; -fx-min-width: 100;");
        setText("Animal");
    }
}
