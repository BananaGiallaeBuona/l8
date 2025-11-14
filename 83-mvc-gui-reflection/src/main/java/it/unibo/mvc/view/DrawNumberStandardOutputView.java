package it.unibo.mvc.view;

import it.unibo.mvc.api.DrawNumberView;
import it.unibo.mvc.api.DrawNumberController;
import it.unibo.mvc.api.DrawResult;

/**
 * Implements a view that prints to the standard output.
 */
public final class DrawNumberStandardOutputView implements DrawNumberView {
    private DrawNumberController controller;

    /**
     * Constructs a new DrawNumberStandardOutputView.
     */
    public DrawNumberStandardOutputView() {
        /*
         * I don't need to add nothing, but there is an error if constructor is empty
         */
    }

    @Override
    public void setController(final DrawNumberController observer) {
        this.controller = observer;
    }

    @Override
    public void start() {
        System.out.println("loading UI"); //NOPMD
    }

    @Override
    public void result(final DrawResult res) {
        System.err.println(res); //NOPMD
    }

    /**
     * Gets the controller associated with this view.
     *
     * @return the controller
     */
    public DrawNumberController getController() {
        return controller;
    }
}
