package it.unibo.mvc.view;

import it.unibo.mvc.api.DrawNumberView;
import it.unibo.mvc.api.DrawNumberController;
import it.unibo.mvc.api.DrawResult;

public class DrawNumberStandardOutputView implements DrawNumberView{
    private DrawNumberController controller;

    DrawNumberStandardOutputView(){
    }

    @Override
    public void setController(DrawNumberController observer) {
        this.controller = observer;
    }

    @Override
    public void start() {
        System.out.println("loading UI"); //NOPMD
    }

    @Override
    public void result(DrawResult res) {
        System.err.println(res); //NOPMD
    }
}