package agh.ics.darwinworld.View;
import javafx.scene.control.Label;
public class AnimalView extends Label{
    public AnimalView(){
        super();
        setStyle("-fx-background-color: #572b0f; -fx-padding: 10; -fx-pref-width: 80; -fx-pref-height: 80");
        setText("Animal");
    }
}