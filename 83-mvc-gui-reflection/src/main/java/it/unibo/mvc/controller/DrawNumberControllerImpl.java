package it.unibo.mvc.controller;

import it.unibo.mvc.api.DrawNumber;
import it.unibo.mvc.api.DrawNumberController;
import it.unibo.mvc.api.DrawNumberView;
import java.util.HashSet;
import java.util.Objects;
import java.util.Collection;
//import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * This class implements the game controller. It orchestrates the game, exposes methods to its observers
 * (the boundaries), and sends results to them.
 * I've modified this class to do 
 *      "Extend the controller to support multiple views.
 *      To doing so, make sure that the controller has a collection of views (and not a single one),
 *      and that it notifies all of them (for instance with a `for` cicle) every time a new event should be displayed."
 */
public final class DrawNumberControllerImpl implements DrawNumberController {

    private final DrawNumber model;
    private Collection<DrawNumberView> views;

    /**
     * Builds a new game controller provided a game model.
     *
     * @param model the implementation of the game model
     */
    public DrawNumberControllerImpl(final DrawNumber model) {
        this.model = model;
        this.views = new HashSet<>();
    }

    @Override
    public void addView(final DrawNumberView view) {
        Objects.requireNonNull(view, "Cannot set a null view");
        this.views.add(view);
        view.setController(this);
        view.start();
    }

    @Override
    public void newAttempt(final int n) {
        final var attempt = model.attempt(n);
        if (views.isEmpty()) {
            throw new IllegalStateException("There is no view attached!");
        }
        for (final DrawNumberView v : views) {
            v.result(attempt);
        }
    }

    @Override
    public void resetGame() {
        this.model.reset();
    }

    /*@SuppressFBWarnings(
        value = "",
        justification = "This System.exit(0) is required for exercise"
    ) */
    @Override
    public void quit() {
        /*
         * A bit harsh. A good application should configure the graphics to exit by
         * natural termination when closing is hit. To do things more cleanly, attention
         * should be paid to alive threads, as the application would continue to persist
         * until the last thread terminates.
         */
        System.exit(0);
    }

}
