package it.unibo.mvc.view;

import it.unibo.mvc.api.DrawNumberView;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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

    @SuppressFBWarnings(
        value = "",
        justification = "This System.exit(0) is required for exercise"
    )
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
    @SuppressFBWarnings(
        value = "EI",
        justification = "Clients need to retrieve the controller in this implementation."
    )
    public DrawNumberController getController() {
        return controller;
    }
}
