package com.mar.tmlfx.view.controller;

import com.mar.algotools.mathematics.utils.MathOps;
import com.mar.framework.core.logging.LogUtils;
import com.mar.tmlfx.Settings;
import com.mar.tmlfx.model.TMLModel;
import com.mar.tmlfx.model.Track;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

public abstract class AbstractPlayerController {

    public static final double MAX_VOLUME = 100.0;

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

    @FXML
    protected Slider sliderVolume;

    @FXML
    public void initialize() {
        sliderVolume.setMin(0.0);
        sliderVolume.setMax(MAX_VOLUME);

        double volume = MathOps.clamp(Settings.getPlayerVolume(), 0.0, MAX_VOLUME);
        sliderVolume.setValue(volume);
        setVolume(volume);

        sliderVolume.valueChangingProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> pObservable, Object pOldValue, Object pNewValue) {
                setVolume(sliderVolume.getValue());
            }
        });
    }

    public abstract void play(Track pTrack);

    public void quit() {
        Settings.setPlayerVolume((int) sliderVolume.getValue());
    }

    public void setModel(TMLModel pModel) {
        model = pModel;
    }

    public abstract void setVolume(double pVolume);

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
        Track track = model.getPrevTrackToPlay();
        if (track != null) {
            play(track);
        } else {
            stop();
            LogUtils.logInfo(this, "No more track to play in this playlist");
        }
    }

}
