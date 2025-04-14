package agh.ics.darwinworld.Model.Exceptions;

public class ReproduceEnergyRequiredException extends Exception {
    public ReproduceEnergyRequiredException() {
        super("Incorrect energy required to reproduce");
    }
}
