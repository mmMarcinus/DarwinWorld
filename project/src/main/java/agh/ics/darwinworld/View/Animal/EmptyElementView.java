package agh.ics.darwinworld.View.Animal;

import agh.ics.darwinworld.Model.AnimalModel.Animal;
import javafx.scene.layout.StackPane;

public class EmptyElementView extends StackPane {
    public EmptyElementView() {
        super();

        setPrefSize(999,999);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    }
}

