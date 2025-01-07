package agh.ics.darwinworld.WorldModel;

import java.util.List;

public class WorldMap {
    private int width;
    private int height;
    private int jungleTop;
    private int jungleBottom;
    //Lista Animali

    WorldMap(int width, int height) {
        this.width = width;
        this.height = height;

        this.jungleTop = (int) ( height /2 + height * 0.1);
        this.jungleBottom = (int) (height/2 - height * 0.1);
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getJungleTop() { return jungleTop; }
    public int getJungleBottom() { return jungleBottom; }
}
