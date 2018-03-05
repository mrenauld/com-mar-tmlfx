package com.mar.tmlfx.model.utils;

import com.mar.tmlfx.model.Track;

import javafx.collections.ObservableList;

public class TrackListUtils {

    /**
     * Randomizes in place the order.
     * 
     * @param trackList
     */
    public static void randomize(ObservableList<Track> trackList) {
        int n = trackList.size();
        for (int i = n - 1; i >= 1; --i) {
            int j = (int) (Math.random() * (i + 1));
            Track temp = trackList.get(i);
            trackList.set(i, trackList.get(j));
            trackList.set(j, temp);
        }
    }

}
