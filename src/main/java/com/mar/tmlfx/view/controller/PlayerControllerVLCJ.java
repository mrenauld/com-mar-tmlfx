package com.mar.tmlfx.view.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;

import com.mar.framework.core.logging.LogUtils;
import com.mar.iotools.string.FilepathUtils;
import com.mar.tmlfx.model.Track;
import com.mar.tmlfx.view.ViewUtils;
import com.mar.tmlfx.view.style.PlaylistCellFactory;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class PlayerControllerVLCJ extends AbstractPlayerController implements ActionListener, MediaPlayerEventListener {

    /** Number of increments of the progress bar. */
    private final int BAR_MAX = 2000;

    private MediaPlayerFactory factory;

    private EmbeddedMediaPlayer player;

    public PlayerControllerVLCJ() {
        factory = new MediaPlayerFactory(new String[] {});
    }

    @Override
    public void actionPerformed(ActionEvent pE) {
        // TODO Auto-generated method stub

    }

    @Override
    public void audioDeviceChanged(MediaPlayer pArg0, String pArg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void backward(MediaPlayer pArg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void buffering(MediaPlayer pArg0, float pArg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void chapterChanged(MediaPlayer pArg0, int pArg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void corked(MediaPlayer pArg0, boolean pArg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void elementaryStreamAdded(MediaPlayer pArg0, int pArg1, int pArg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void elementaryStreamDeleted(MediaPlayer pArg0, int pArg1, int pArg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void elementaryStreamSelected(MediaPlayer pArg0, int pArg1, int pArg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void endOfSubItems(MediaPlayer pArg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void error(MediaPlayer pArg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void finished(MediaPlayer pArg0) {
        goToNext();
    }

    @Override
    public void forward(MediaPlayer pArg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void lengthChanged(MediaPlayer pArg0, long pArg1) {
        Platform.runLater(() -> {
            labelTime.setText(ViewUtils.formatTimeVLCJ(0L, player.getLength()));
        });
    }

    @Override
    public void mediaChanged(MediaPlayer pArg0, libvlc_media_t pArg1, String pArg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mediaDurationChanged(MediaPlayer pArg0, long pArg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mediaFreed(MediaPlayer pArg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mediaMetaChanged(MediaPlayer pArg0, int pArg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mediaParsedChanged(MediaPlayer pArg0, int pArg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mediaStateChanged(MediaPlayer pArg0, int pArg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mediaSubItemAdded(MediaPlayer pArg0, libvlc_media_t pArg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mediaSubItemTreeAdded(MediaPlayer pArg0, libvlc_media_t pArg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void muted(MediaPlayer pArg0, boolean pArg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void newMedia(MediaPlayer pArg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void opening(MediaPlayer pArg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void pausableChanged(MediaPlayer pArg0, int pArg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void paused(MediaPlayer pArg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void play(Track pTrack) {
        stop();

        PlaylistCellFactory.setCurrent(pTrack.getPlayIdx());

        /*
         * To avoid "pre fill buffer" errors, release and re-create the
         * mediaPlayer.
         */
        if (player != null) {
            player.release();
        }
        player = factory.newEmbeddedMediaPlayer(null);
        player.addMediaPlayerEventListener(this);
        setVolume(sliderVolume.getValue());

        sliderPlayTime.setMin(0);
        sliderPlayTime.setMax(BAR_MAX);
        sliderPlayTime.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable pObs) {
                if (sliderPlayTime.isValueChanging()) {
                    // multiply duration by percentage calculated by slider
                    // position
                    float positionValue = (float) sliderPlayTime.getValue() / BAR_MAX;
                    player.setPosition(positionValue);
                }
            }
        });

        File file = new File(FilepathUtils.normalize(pTrack.getFilename()));
        URI uri = file.toURI();
        String asciiURI = uri.toASCIIString();
        asciiURI = asciiURI.replace("file:/", "file:///");

        player.playMedia(asciiURI);

        LogUtils.logInfo(this, "Start playing [" + pTrack.getArtist() + " - " + pTrack.getTitle() + "]");
        Platform.runLater(() -> {
            labelNowPlaying.setText(pTrack.getArtist() + " - " + pTrack.getTitle());
        });
    }

    @Override
    public void playing(MediaPlayer pArg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void positionChanged(MediaPlayer pArg0, float pArg1) {
        Platform.runLater(() -> {
            /* Update the position of the timer. */
            long time = player.getTime();
            long duration = player.getLength();
            int position = duration > 0 ? (int) Math.round(BAR_MAX * (double) time / duration) : 0;

            labelTime.setText(ViewUtils.formatTimeVLCJ(time, duration));
            sliderPlayTime.setValue(position);
        });
    }

    @Override
    public void quit() {
        super.quit();

        if (player != null) {
            player.stop();
            player.release();
        }
        factory.release();
    }

    @Override
    public void scrambledChanged(MediaPlayer pArg0, int pArg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void seekableChanged(MediaPlayer pArg0, int pArg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setVolume(double pVolume) {
        if (player != null) {
            player.setVolume((int) pVolume);
        } else {
            LogUtils.logDebug(this, "Impossible to set volume, player not yet initialized");
        }
    }

    @Override
    public void snapshotTaken(MediaPlayer pArg0, String pArg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void stop() {
        if (player != null && player.isPlaying()) {
            player.stop();
            LogUtils.logInfo(this, "Stop player");
        }
    }

    @Override
    public void stopped(MediaPlayer pArg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void subItemFinished(MediaPlayer pArg0, int pArg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void subItemPlayed(MediaPlayer pArg0, int pArg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void timeChanged(MediaPlayer pArg0, long pArg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void titleChanged(MediaPlayer pArg0, int pArg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void videoOutput(MediaPlayer pArg0, int pArg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void volumeChanged(MediaPlayer pArg0, float pArg1) {
        // TODO Auto-generated method stub

    }

    @FXML
    private void handleClick(MouseEvent pMouseEvent) {
        if (pMouseEvent.getSource().equals(buttonPlay)) {
            if (player == null) {
                goToNext();
            } else {
                if (!player.isPlaying()) {
                    player.play();
                    LogUtils.logInfo(this, "Player resume playing");
                } else {
                    player.pause();
                    LogUtils.logInfo(this, "Player in pause");
                }
            }
        } else if (pMouseEvent.getSource().equals(buttonStop)) {
            stop();
        } else if (pMouseEvent.getSource().equals(buttonPrev)) {
            goToPrev();
        } else if (pMouseEvent.getSource().equals(buttonNext)) {
            goToNext();
        }
    }

}
