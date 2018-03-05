package com.mar.tmlfx.view.controller;

import java.io.File;

import com.mar.framework.core.logging.LogUtils;
import com.mar.tmlfx.model.Track;
import com.mar.tmlfx.view.ViewUtils;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

public class PlayerController extends AbstractPlayerController {

    private MediaPlayer player;

    private Duration duration;

    public PlayerController() {

    }

    @Override
    public void play(Track pTrack) {
        stop();

        if (player != null) {
            player.dispose();
        }

        String uriString = new File(pTrack.getFilename()).toURI().toString();
        player = new MediaPlayer(new Media(uriString));
        setEndOfMediaAction();
        player.play();

        LogUtils.logInfo(this, "Start playing [" + pTrack.getArtist() + " - " + pTrack.getTitle() + "]");
        labelNowPlaying.setText(pTrack.getArtist() + " - " + pTrack.getTitle());
    }

    @Override
    public void stop() {
        if (player != null && !player.getStatus().equals(Status.STOPPED)) {
            player.stop();
            LogUtils.logInfo(this, "Stop player");
        }
    }

    @FXML
    private void handleClick(MouseEvent pMouseEvent) {
        if (pMouseEvent.getSource().equals(buttonPlay)) {
            if (player.getStatus().equals(Status.PAUSED)) {
                player.play();
                LogUtils.logInfo(this, "Player resume playing");
            } else {
                player.pause();
                LogUtils.logInfo(this, "Player in pause");
            }
        } else if (pMouseEvent.getSource().equals(buttonStop)) {
            stop();
        } else if (pMouseEvent.getSource().equals(buttonPrev)) {
            goToPrev();
        } else if (pMouseEvent.getSource().equals(buttonNext)) {
            goToNext();
        }
    }

    private void setEndOfMediaAction() {
        player.currentTimeProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable pObs) {
                updateValues();
            }
        });

        player.setOnReady(new Runnable() {
            @Override
            public void run() {
                duration = player.getMedia().getDuration();
                updateValues();
            }
        });

        player.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                goToNext();
            }
        });

        sliderPlayTime.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable pObs) {
                if (sliderPlayTime.isValueChanging()) {
                    // multiply duration by percentage calculated by slider
                    // position
                    player.seek(duration.multiply(sliderPlayTime.getValue() / 100.0));
                }
            }
        });
    }

    private void updateValues() {
        if (sliderPlayTime != null) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Duration currentTime = player.getCurrentTime();
                    // playTime.setText(formatTime(currentTime, duration));

                    labelTime.setText(ViewUtils.formatTimeFX(currentTime, duration));

                    sliderPlayTime.setDisable(duration.isUnknown());
                    if (!sliderPlayTime.isDisabled() && duration.greaterThan(Duration.ZERO)
                            && !sliderPlayTime.isValueChanging()) {
                        sliderPlayTime.setValue(currentTime.divide(duration).toMillis() * 100.0);
                    }
                    // if (!volumeSlider.isValueChanging()) {
                    // volumeSlider.setValue((int)Math.round(mp.getVolume()
                    // * 100));
                    // }
                }
            });
        }
    }

}
