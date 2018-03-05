package com.mar.tmlfx.view.controller;

import java.util.List;

import com.mar.tmlfx.model.Track;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TagEditDialogController {

    public static final String MULTIPLE_VALUES = "<Multiple values>";

    @FXML
    private TextField textFieldArtist;

    @FXML
    private TextField textFieldTitle;

    @FXML
    private TextField textFieldAlbum;

    private Stage dialogStage;

    private List<Track> trackList;

    private boolean updateClicked = false;

    public boolean isUpdateClicked() {
        return updateClicked;
    }

    public void setStage(Stage pDialogStage) {
        dialogStage = pDialogStage;
    }

    public void setTrackList(List<Track> pTrackList) {
        trackList = pTrackList;

        if (pTrackList.size() == 1) {
            Track track = pTrackList.get(0);
            textFieldArtist.setText(track.getArtist());
            textFieldTitle.setText(track.getTitle());
            textFieldAlbum.setText(track.getAlbum());
        } else {
            if (isSameArtist()) {
                textFieldArtist.setText(pTrackList.get(0).getArtist());
            } else {
                textFieldArtist.setText(MULTIPLE_VALUES);
            }

            if (isSameTitle()) {
                textFieldTitle.setText(pTrackList.get(0).getTitle());
            } else {
                textFieldTitle.setText(MULTIPLE_VALUES);
            }

            if (isSameAlbum()) {
                textFieldAlbum.setText(pTrackList.get(0).getAlbum());
            } else {
                textFieldAlbum.setText(MULTIPLE_VALUES);
            }
        }
    }

    @FXML
    private void handleCancel() {
        updateClicked = false;
        dialogStage.close();
    }

    @FXML
    private void handleUpdate() {
        if (!MULTIPLE_VALUES.equals(textFieldArtist.getText())) {
            for (Track track : trackList) {
                track.setArtist(textFieldArtist.getText());
            }
        }

        if (!MULTIPLE_VALUES.equals(textFieldTitle.getText())) {
            for (Track track : trackList) {
                track.setTitle(textFieldTitle.getText());
            }
        }

        if (!MULTIPLE_VALUES.equals(textFieldAlbum.getText())) {
            for (Track track : trackList) {
                track.setAlbum(textFieldAlbum.getText());
            }
        }

        updateClicked = true;
        dialogStage.close();
    }

    private boolean isSameAlbum() {
        boolean same = true;
        String artist = trackList.get(0).getAlbum();
        for (int i = 1; i < trackList.size(); ++i) {
            if (!artist.equals(trackList.get(i).getAlbum())) {
                same = false;
                break;
            }
        }
        return same;
    }

    private boolean isSameArtist() {
        boolean same = true;
        String artist = trackList.get(0).getArtist();
        for (int i = 1; i < trackList.size(); ++i) {
            if (!artist.equals(trackList.get(i).getArtist())) {
                same = false;
                break;
            }
        }
        return same;
    }

    private boolean isSameTitle() {
        boolean same = true;
        String artist = trackList.get(0).getTitle();
        for (int i = 1; i < trackList.size(); ++i) {
            if (!artist.equals(trackList.get(i).getTitle())) {
                same = false;
                break;
            }
        }
        return same;
    }

}
