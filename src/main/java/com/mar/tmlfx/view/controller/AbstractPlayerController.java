package com.mar.tmlfx.view.controller;

import com.mar.framework.core.logging.LogUtils;
import com.mar.tmlfx.model.TMLModel;
import com.mar.tmlfx.model.Track;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

public abstract class AbstractPlayerController {

    protected TMLModel model;

    @FXML
    protected Label labelNowPlaying;

    @FXML
    protected Label labelTime;

    @FXML
    protected Button buttonPlay;

    @FXML
    protected Button buttonStop;

    @FXML
    protected Button buttonPrev;

    @FXML
    protected Button buttonNext;

    @FXML
    protected Slider sliderPlayTime;

    public abstract void play(Track pTrack);

    public void quit() {

    }

    public void setModel(TMLModel pModel) {
        model = pModel;
    }

    public abstract void stop();

    protected void goToNext() {
        Track track = model.getNextTrackToPlay();
        if (track != null) {
            play(track);
        } else {
            stop();
            LogUtils.logInfo(this, "No more track to play in this playlist");
        }
    }

    protected void goToPrev() {
        Track track = model.getNextTrackToPlay();
        if (track != null) {
            play(track);
        } else {
            stop();
            LogUtils.logInfo(this, "No more track to play in this playlist");
        }
    }

}
