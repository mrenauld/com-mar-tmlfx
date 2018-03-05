package com.mar.tmlfx.model;

import java.util.List;

import com.mar.algotools.combinatorics.Combinatorics;
import com.mar.framework.core.logging.LogUtils;
import com.mar.tmlfx.model.utils.TrackListUtils;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Playlist extends TrackList {

    public static final int NO_CURRENT_TRACK = -1;

    private StringProperty title = new SimpleStringProperty();

    private StringProperty filename = new SimpleStringProperty();

    private int current;

    private boolean randomOrder = true;

    private boolean repeat = true;

    public Playlist() {
        super();
        current = NO_CURRENT_TRACK;
    }

    @Override
    public void addTrack(Track pTrack) {
        super.addTrack(pTrack);

        if (!randomOrder) {
            pTrack.setPlayIdx(size());
        } else {
            randomizeOrder();
        }
    }

    @Override
    public void addTrackList(List<Track> pTrackList) {
        super.addTrackList(pTrackList);

        if (!randomOrder) {
            for (int i = 0; i < pTrackList.size(); ++i) {
                pTrackList.get(i).setPlayIdx(size() - pTrackList.size());
            }
        } else {
            randomizeOrder();
        }
    }

    public StringProperty filenameProperty() {
        return filename;
    }

    public Track getCurrentTrack() {
        if (current != NO_CURRENT_TRACK) {
            return trackList.get(current);
        } else {
            return null;
        }
    }

    public String getFilename() {
        return filename.get();
    }

    public String getTitle() {
        return title.get();
    }

    public void goToFirst() {
        current = findTrackPositionWithPlayIdx(0);
    }

    public void goToLast() {
        current = findTrackPositionWithPlayIdx(size() - 1);
    }

    public void goToNext() {
        if (current != NO_CURRENT_TRACK) {
            int currentPlayIdx = trackList.get(current).getPlayIdx();
            if (currentPlayIdx < trackList.size() - 1) {
                current = findTrackPositionWithPlayIdx(currentPlayIdx + 1);
            } else {
                if (!repeat) {
                    current = NO_CURRENT_TRACK;
                } else {
                    goToFirst();
                }
            }
        }
    }

    public void goToPrev() {
        if (current != NO_CURRENT_TRACK) {
            int currentPlayIdx = trackList.get(current).getPlayIdx();
            if (currentPlayIdx > 0) {
                current = findTrackPositionWithPlayIdx(currentPlayIdx - 1);
            } else {
                if (!repeat) {
                    current = NO_CURRENT_TRACK;
                } else {
                    goToLast();
                }
            }
        }
    }

    public void goToTrack(Track pTrack) {
        boolean found = false;
        for (int i = 0; i < trackList.size(); ++i) {
            Track track = trackList.get(i);
            if (track.getKey().equals(pTrack.getKey())) {
                current = i;
                found = true;
                break;
            }
        }

        if (!found) {
            LogUtils.logWarning(this, "The track with key [" + pTrack.getKey() + "] was not found in the track list");
        }
    }

    public boolean isNoCurrentTrack() {
        return current == NO_CURRENT_TRACK;
    }

    public void randomizePlayOrder() {
        TrackListUtils.randomize(trackList);
    }

    public void setFilename(String pPath) {
        filename.set(pPath);
    }

    public void setTitle(String pTitle) {
        title.set(pTitle);
    }

    public StringProperty titleProperty() {
        return title;
    }

    private int findTrackPositionWithPlayIdx(int pPlayIdx) {
        int pos = NO_CURRENT_TRACK;
        for (int i = 0; i < trackList.size(); ++i) {
            if (trackList.get(i).getPlayIdx() == pPlayIdx) {
                pos = i;
                break;
            }
        }
        return pos;
    }

    private void randomizeOrder() {
        int[] randPerm = Combinatorics.getRandPerm(size());
        for (int i = 0; i < trackList.size(); ++i) {
            Track track = trackList.get(i);
            track.setPlayIdx(randPerm[i]);
        }
    }

}
