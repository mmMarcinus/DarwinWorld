package agh.ics.darwinworld.Model.Exceptions;

public class MutationException extends Exception {
    public MutationException() {
        super("Incorrect mutation number");
    }
}
